# Stage 1: Cache Gradle dependencies
FROM gradle:8.13-jdk21-corretto AS cache
RUN mkdir -p /home/gradle/cache_home
ENV GRADLE_USER_HOME=/home/gradle/cache_home
COPY build.gradle.kts gradle.properties settings.gradle.kts /home/gradle/app/
COPY gradle /home/gradle/app/gradle
WORKDIR /home/gradle/app
RUN gradle dependencies --no-daemon

# Stage 2: Build Application
FROM gradle:8.13-jdk21-corretto AS build
COPY --from=cache /home/gradle/cache_home /home/gradle/.gradle
COPY --chown=gradle:gradle . /home/gradle/src
WORKDIR /home/gradle/src
RUN gradle clean bootJar --no-daemon -D https.protocols=TLSv1.2

# Stage 3: Create the Runtime Image
FROM amazoncorretto:21 AS runtime
EXPOSE 8089
RUN mkdir /app
COPY --from=build /home/gradle/src/build/libs/*.jar /app/auth_service.jar
ENTRYPOINT ["java","-jar","/app/auth_service.jar"]