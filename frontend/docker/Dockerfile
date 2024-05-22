FROM docker.io/library/node:20-alpine

LABEL org.opencontainers.image.source=https://github.com/SaimonWoidig/weather-app
LABEL org.opencontainers.image.description="Weather-app frontend"
LABEL org.opencontainers.image.licenses=MIT
LABEL org.opencontainers.image.authors="Šimon Woidig <woidig.simon@gmail.com>"

RUN adduser -D -h /usr/local/weather-app -u 1001 weather

USER weather
WORKDIR /usr/local/weather-app
RUN mkdir -p /usr/local/weather-app/config /usr/local/weather-app/data

COPY --chown=weather --chmod=744 ./.output /usr/local/weather-app/app
COPY --chown=weather --chmod=744 ./docker/entrypoint.sh /usr/local/weather-app/entrypoint.sh

EXPOSE 3000/tcp

ENV BACKEND_API_URL=""

ENTRYPOINT ["/bin/sh", "/usr/local/weather-app/entrypoint.sh"]
