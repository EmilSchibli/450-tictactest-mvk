#!/bin/sh
# Says which parts of one release differ from the previous release tag.
# A normal commit is not the boundary. The previous tag is.
set -eu
root=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
cd "$root"

tag=${1:?usage: release-changes.sh vX.Y.Z}
git rev-parse --verify --quiet "$tag^{commit}" >/dev/null

previous=$(git describe --tags --abbrev=0 "$tag^" 2>/dev/null || true)
backend=false
frontend=false

if [ -z "$previous" ]; then
	backend=true
	frontend=true
else
	files=$(git diff --name-only "$previous" "$tag")
	while IFS= read -r path; do
		[ -n "$path" ] || continue
		case "$path" in
			src/main/*|build.gradle|settings.gradle|gradle.properties|gradle/*|gradlew|gradlew.bat|ops/start-webconsole.sh|ops/version-server.py)
				backend=true
				;;
			frontend/*)
				frontend=true
				;;
		esac
	done <<EOF
$files
EOF
fi

if [ -z "$previous" ]; then
	previous=none
fi
printf 'previous=%s\n' "$previous"
printf 'backend=%s\n' "$backend"
printf 'frontend=%s\n' "$frontend"
