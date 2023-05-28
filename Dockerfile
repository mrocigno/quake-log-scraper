FROM eclipse-temurin:11-jdk as BUILD
LABEL authors="Matheus"

COPY . /docker-building
WORKDIR /docker-building
RUN ./gradlew build