FROM azul/zulu-openjdk-alpine:25.0.4.1-25.36

RUN apk add --no-cache \
		bash \
		ca-certificates \
		coreutils \
		curl \
		git \
		libgcc \
		libstdc++ \
		sudo \
		tar \
		zstd

# Cursor install script uses macOS `base64 -D`; GNU base64 only has `-d`
RUN printf '%s\n' \
		'#!/bin/sh' \
		'if [ "$1" = "-D" ]; then shift; exec /bin/base64 -d "$@"; fi' \
		'exec /bin/base64 "$@"' \
		> /usr/local/bin/base64 \
	&& chmod +x /usr/local/bin/base64

ARG GRADLE_VERSION=9.7.0
ARG GRADLE_SHA256=84fbba45c7f4c64abc77460e1c00f541e9f960e3c7ed2538f1ede19eacd873ae

RUN curl -fsSLo /tmp/gradle.zip "https://services.gradle.org/distributions/gradle-${GRADLE_VERSION}-bin.zip" \
	&& echo "${GRADLE_SHA256}  /tmp/gradle.zip" | sha256sum -c - \
	&& unzip -q /tmp/gradle.zip -d /opt \
	&& ln -s /opt/gradle-${GRADLE_VERSION}/bin/gradle /usr/local/bin/gradle \
	&& rm /tmp/gradle.zip

ARG USERNAME=dev
ARG USER_UID=1000
ARG USER_GID=1000

RUN addgroup -g ${USER_GID} ${USERNAME} \
	&& adduser -u ${USER_UID} -G ${USERNAME} -s /bin/bash -D ${USERNAME} \
	&& echo "${USERNAME} ALL=(ALL) NOPASSWD:ALL" > /etc/sudoers.d/${USERNAME} \
	&& mkdir -p /home/${USERNAME}/.gradle \
	&& chown -R ${USER_UID}:${USER_GID} /home/${USERNAME}

ENV GRADLE_USER_HOME=/home/${USERNAME}/.gradle \
	LANG=C.UTF-8
