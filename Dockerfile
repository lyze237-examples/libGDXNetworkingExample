FROM adoptopenjdk:8-jdk-hotspot AS builder

COPY . /app
WORKDIR /app

RUN chmod u+x ./gradlew
RUN ./gradlew headless:installDist --no-daemon

FROM adoptopenjdk:8-jre-hotspot

EXPOSE 9999

RUN mkdir /app

COPY --from=builder /app/headless/build/install/headless /app/
RUN chmod u+x /app/bin/headless

ENTRYPOINT [ "/app/bin/headless" ]






