FROM eclipse-temurin:11-jdk as BUILD
LABEL authors="Matheus"

COPY . /docker-building
WORKDIR /docker-building
RUN ./gradlew shadowJar

FROM eclipse-temurin:11-jre

COPY /assets/qgames.log /bin/qgames.log
COPY --from=BUILD /docker-building/build/libs/quake-log-scrapper-1.0-SNAPSHOT-all.jar /bin/scrapper.jar
WORKDIR /bin

ENTRYPOINT ["java", "-jar", "scrapper.jar"]