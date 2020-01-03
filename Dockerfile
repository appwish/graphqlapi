FROM openjdk:11.0.5-jdk as builder
ENV APP_HOME=/usr/app/
WORKDIR $APP_HOME

COPY build.gradle settings.gradle gradlew $APP_HOME
COPY gradle $APP_HOME/gradle

# downloads and caches dependencies in Docker image layer
# so that they don't have to be downloaded each time
RUN ./gradlew build || return 0

COPY . .
RUN ./gradlew test && ./gradlew shadowJar

FROM azul/zulu-openjdk-alpine:11-jre

COPY --from=builder /usr/app/build/libs/graphqlapi-1.0.0-SNAPSHOT-fat.jar graphqlapi.jar

EXPOSE 8000
ENTRYPOINT ["java", "-jar", "graphqlapi.jar"]
