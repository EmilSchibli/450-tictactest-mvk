#!/bin/sh
# Puts one published release on the existing instance and website.
# Does not run tofu apply and does not create an instance.
set -eu
version=${1:?usage: deploy-release.sh X.Y.Z}
root=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
infra="$root/infra"

bucket=$(tofu -chdir="$infra" output -raw artifact_bucket)
frontend=$(tofu -chdir="$infra" output -raw frontend_bucket)
instance=$(tofu -chdir="$infra" output -raw instance_id)
region=$(tofu -chdir="$infra" output -raw aws_region)
version_url=$(tofu -chdir="$infra" output -raw version_url)
frontend_url=$(tofu -chdir="$infra" output -raw frontend_url)

work=$(mktemp -d)
trap 'rm -rf "$work"' EXIT
base="https://github.com/EmilSchibli/450-tictactest-mvk/releases/download/v$version"
curl -fsSL -o "$work/tictactest-$version.jar" "$base/tictactest-$version.jar"
curl -fsSL -o "$work/webconsole.zip" "$base/tictactest-webconsole-$version.zip"
curl -fsSL -o "$work/frontend.zip" "$base/tictactest-frontend-$version.zip"

aws s3 cp "$work/tictactest-$version.jar" "s3://$bucket/releases/$version/tictactest-$version.jar" --region "$region"
aws s3 cp "$work/webconsole.zip" "s3://$bucket/releases/$version/tictactest-webconsole-$version.zip" --region "$region"
printf '%s\n' "$version" | aws s3 cp - "s3://$bucket/current" --region "$region"

aws ssm send-command \
	--instance-ids "$instance" \
	--document-name AWS-RunShellScript \
	--comment "restart tictactest $version" \
	--parameters '{"commands":["systemctl restart tictactest"]}' \
	--region "$region"

unzip -q "$work/frontend.zip" -d "$work/front"
aws s3 cp "s3://$frontend/config.js" "$work/front/frontend/config.js" --region "$region"
aws s3 sync "$work/front/frontend" "s3://$frontend/" --delete --region "$region"

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

front=$(curl -fsS "$frontend_url/version.txt" | tr -d '[:space:]')
if [ "$front" != "$version" ]; then
	echo "frontend is '$front', expected $version" >&2
	exit 1
fi
echo "running backend $got"
echo "frontend $frontend_url"
