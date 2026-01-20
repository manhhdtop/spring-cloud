# docker build -f config/Dockerfile -t cloud-config:latest .

FROM gradle:9.4.1-jdk25-noble AS build
USER root

WORKDIR /home/gradle/project
COPY . .

RUN gradle :config:bootJar -x test --no-daemon \
    && find /home/gradle/project/config/build/libs -maxdepth 1 -name '*.jar' ! -name '*-plain.jar' -exec cp {} /tmp/boot.jar \;

FROM eclipse-temurin:25-jre AS runtime
WORKDIR /app

RUN groupadd --system spring && useradd --system --gid spring spring

COPY --from=build --chown=spring:spring /tmp/boot.jar /app/app.jar

USER spring:spring

ENTRYPOINT ["java", "-jar", "/app/app.jar"]
