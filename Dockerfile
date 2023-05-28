FROM eclipse-temurin:11-jdk as BUILD
LABEL authors="Matheus"

COPY . /docker-building
WORKDIR /docker-building
RUN ./gradlew build shadowJar

FROM eclipse-temurin:11-jre

VOLUME /scraper/assets
COPY --from=BUILD /docker-building/build/libs/quake-log-scraper-1.0.0-all.jar /scraper/scraper.jar
WORKDIR /scraper

RUN echo "alias scraper='java -jar /scraper/scraper.jar'" >> ~/.bashrc
