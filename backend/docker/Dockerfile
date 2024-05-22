FROM docker.io/library/eclipse-temurin:17-jre-alpine

LABEL org.opencontainers.image.source=https://github.com/SaimonWoidig/weather-app
LABEL org.opencontainers.image.description="Weather-app backend"
LABEL org.opencontainers.image.licenses=MIT
LABEL org.opencontainers.image.authors="Šimon Woidig <woidig.simon@gmail.com>"

RUN adduser -D -h /usr/local/weather-app -u 1000 weather

USER weather
WORKDIR /usr/local/weather-app
RUN mkdir -p /usr/local/weather-app/config /usr/local/weather-app/data

COPY --chown=weather --chmod=744 ./build/libs/weather-app-backend.jar /usr/local/weather-app/weather-app-backend.jar
COPY --chown=weather --chmod=744 ./docker/application.yaml /usr/local/weather-app/config/application.yaml
COPY --chown=weather --chmod=744 ./docker/entrypoint.sh /usr/local/weather-app/entrypoint.sh

EXPOSE 8080/tcp

ENTRYPOINT ["/bin/sh", "/usr/local/weather-app/entrypoint.sh"]
