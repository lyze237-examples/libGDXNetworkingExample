FROM adoptopenjdk:8-jdk-hotspot AS builder

COPY . /app
WORKDIR /app

RUN gradle headless:jar --no-daemon

FROM adoptopenjdk:8-jre-hotspot

EXPOSE 9999

RUN mkdir /app

COPY --from=builder /app/headless/build/libs/headless*.jar /app/headless.jar

ENTRYPOINT ["java", "-jar", "/app/headless.jar"]






