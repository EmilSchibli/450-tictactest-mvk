#!/bin/sh
# Starts the released jar in ttyd and a tiny HTTP version endpoint.
# ttyd itself is installed on the machine. This script only wires the release.
set -eu
cd "$(dirname "$0")"

jar=${TICTACTEST_JAR:-}
if [ -z "$jar" ]; then
	jar=$(ls -1 tictactest-*.jar | head -n 1)
fi
version=${jar##*/}
version=${version#tictactest-}
version=${version%.jar}
case "$version" in
	*.*.*) ;;
	*)
		echo "jar name must be tictactest-X.Y.Z.jar, got $jar" >&2
		exit 1
		;;
esac

export TICTACTEST_VERSION="$version"
bind=${TICTACTEST_BIND:-127.0.0.1}
ttyd_port=${TICTACTEST_TTYD_PORT:-7681}
version_port=${TICTACTEST_VERSION_PORT:-7682}
export TICTACTEST_BIND="$bind"
export TICTACTEST_VERSION_PORT="$version_port"

python3 "$(dirname "$0")/version-server.py" &
version_pid=$!
trap 'kill "$version_pid"' EXIT INT TERM

echo "version $version on http://$bind:$version_port/version"
echo "terminal on http://$bind:$ttyd_port"
exec ttyd -W -i "$bind" -p "$ttyd_port" java -Xmx64m -jar "$jar"
