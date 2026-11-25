#!/bin/sh
# Puts one published release on the existing instance and website.
# Does not run tofu apply and does not create an instance.
# The jar is downloaded from the GitHub Release. Nothing is rebuilt here.
set -eu
version=${1:?usage: deploy-release.sh X.Y.Z [both|backend|frontend]}
part=${2:-both}
case "$part" in
	both|backend|frontend) ;;
	*)
		echo "part must be both, backend or frontend" >&2
		exit 1
		;;
esac
case "$version" in
	v*) version=${version#v} ;;
esac
if ! printf '%s\n' "$version" | grep -Eq '^[0-9]+\.[0-9]+\.[0-9]+$'; then
	echo "version must be X.Y.Z" >&2
	exit 1
fi

root=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
infra="$root/infra"

if [ -n "${ARTIFACT_BUCKET:-}" ]; then
	bucket=$ARTIFACT_BUCKET
	frontend_bucket=${FRONTEND_BUCKET:?FRONTEND_BUCKET}
	instance=${INSTANCE_ID:?INSTANCE_ID}
	region=${AWS_REGION:?AWS_REGION}
	version_url=${VERSION_URL:?VERSION_URL}
	frontend_url=${FRONTEND_URL:?FRONTEND_URL}
else
	bucket=$(tofu -chdir="$infra" output -raw artifact_bucket)
	frontend_bucket=$(tofu -chdir="$infra" output -raw frontend_bucket)
	instance=$(tofu -chdir="$infra" output -raw instance_id)
	region=$(tofu -chdir="$infra" output -raw aws_region)
	version_url=$(tofu -chdir="$infra" output -raw version_url)
	frontend_url=$(tofu -chdir="$infra" output -raw frontend_url)
fi

aws sts get-caller-identity --region "$region" >/dev/null

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
base="https://github.com/EmilSchibli/450-tictactest-mvk/releases/download/v$version"

deploy_backend() {
	curl -fsSL -o "$work/tictactest-$version.jar" "$base/tictactest-$version.jar"
	curl -fsSL -o "$work/webconsole.zip" "$base/tictactest-webconsole-$version.zip"
	aws s3 cp "$work/tictactest-$version.jar" "s3://$bucket/releases/$version/tictactest-$version.jar" --region "$region"
	aws s3 cp "$work/webconsole.zip" "s3://$bucket/releases/$version/tictactest-webconsole-$version.zip" --region "$region"
	printf '%s\n' "$version" | aws s3 cp - "s3://$bucket/current" --region "$region"
	cmd_id=$(aws ssm send-command \
		--instance-ids "$instance" \
		--document-name AWS-RunShellScript \
		--comment "restart tictactest $version" \
		--timeout-seconds 180 \
		--parameters '{"commands":["systemctl restart tictactest"]}' \
		--region "$region" \
		--query 'Command.CommandId' \
		--output text | tr -d '\r')
	if ! aws ssm wait command-executed \
		--command-id "$cmd_id" \
		--instance-id "$instance" \
		--region "$region"; then
		echo "restart $cmd_id failed" >&2
		aws ssm get-command-invocation \
			--command-id "$cmd_id" \
			--instance-id "$instance" \
			--region "$region" \
			--query '[Status,StandardOutputContent,StandardErrorContent]' \
			--output text >&2 || true
		exit 1
	fi
	i=0
	got=""
	while [ "$i" -lt 30 ]; do
		got=$(curl -fsS "$version_url" | tr -d '[:space:]' || true)
		if [ "$got" = "$version" ]; then
			break
		fi
		i=$((i + 1))
		sleep 2
	done
	if [ "$got" != "$version" ]; then
		echo "backend is '$got', expected $version" >&2
		exit 1
	fi
	echo "running backend $got"
}

deploy_frontend() {
	curl -fsSL -o "$work/frontend.zip" "$base/tictactest-frontend-$version.zip"
	unzip -q "$work/frontend.zip" -d "$work/front"
	aws s3 cp "s3://$frontend_bucket/config.js" "$work/front/frontend/config.js" --region "$region"
	# An older release has older timestamps. Same-sized files would otherwise stay.
	aws s3 sync "$work/front/frontend" "s3://$frontend_bucket/" --delete --exact-timestamps --region "$region"
	front=$(curl -fsS "$frontend_url/version.txt" | tr -d '[:space:]')
	if [ "$front" != "$version" ]; then
		echo "frontend is '$front', expected $version" >&2
		exit 1
	fi
	echo "frontend $frontend_url"
}

if [ "$part" = both ] || [ "$part" = backend ]; then
	deploy_backend
fi
if [ "$part" = both ] || [ "$part" = frontend ]; then
	deploy_frontend
fi
