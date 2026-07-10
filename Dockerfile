FROM sbtscala/scala-sbt:graalvm-community-25.0.2_1.12.13_3.8.4 AS build

WORKDIR /build
COPY project ./project
COPY build.sbt .
RUN sbt update

COPY app ./app
COPY auth ./auth
COPY game ./game
COPY infrastructure ./infrastructure
RUN sbt app/assembly

FROM eclipse-temurin:25-alpine as jre
WORKDIR /usr/oselka
COPY --from=build /build/app/target/scala-3.8.4/*.jar ./oselka.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "oselka.jar"]
