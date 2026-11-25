#!/bin/sh
# Checks the release boundary against the tags that already exist.
set -eu
root=$(CDPATH= cd -- "$(dirname "$0")/.." && pwd)
cd "$root"

expect() {
	tag=$1
	want=$2
	got=$(sh ops/release-changes.sh "$tag")
	if [ "$got" != "$want" ]; then
		echo "FAIL $tag" >&2
		printf 'got:\n%s\nwant:\n%s\n' "$got" "$want" >&2
		exit 1
	fi
	echo "ok $tag"
}

expect v1.0.0 "$(printf 'previous=none\nbackend=true\nfrontend=true')"
expect v1.1.0 "$(printf 'previous=v1.0.0\nbackend=true\nfrontend=true')"
expect v1.2.0 "$(printf 'previous=v1.1.0\nbackend=false\nfrontend=true')"
