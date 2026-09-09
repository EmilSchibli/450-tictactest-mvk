FROM azul/zulu-openjdk:25

RUN apt-get update \
	&& apt-get install -y --no-install-recommends \
		bash \
		ca-certificates \
		curl \
		git \
		wget \
		sudo \
	&& rm -rf /var/lib/apt/lists/*

# Cursor install script uses macOS `base64 -D`; GNU base64 only has `-d`
RUN mv /usr/bin/base64 /usr/bin/base64.real \
	&& printf '%s\n' \
		'#!/bin/sh' \
		'if [ "$1" = "-D" ]; then shift; exec /usr/bin/base64.real -d "$@"; fi' \
		'exec /usr/bin/base64.real "$@"' \
		> /usr/bin/base64 \
	&& chmod +x /usr/bin/base64

ARG USERNAME=dev
ARG USER_UID=1000
ARG USER_GID=1000

RUN groupadd --gid ${USER_GID} ${USERNAME} \
	&& useradd --uid ${USER_UID} --gid ${USER_GID} -m -s /bin/bash ${USERNAME} \
	&& echo "${USERNAME} ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/${USERNAME} \
	&& mkdir -p /home/${USERNAME}/.gradle \
	&& chown -R ${USER_UID}:${USER_GID} /home/${USERNAME}

ENV GRADLE_USER_HOME=/home/${USERNAME}/.gradle \
	LANG=C.UTF-8
