FROM gradle:latest AS build

RUN gradle --version && java -version

WORKDIR /usr/src/app

# Only copy dependency-related files
COPY build.gradle settings.gradle /usr/src/app/

# Only download dependencies
# Eat the expected build failure since no source code has been copied yet
RUN gradle clean build --no-daemon > /dev/null 2>&1 || true

# Copy source files
COPY src/ /usr/src/app/src/

# Do the actual build
RUN gradle bootJar

FROM openjdk:latest

WORKDIR /usr/src/app

# Copy the built jar
COPY --from=build /usr/src/app/build/libs/authentication-front-end.jar app.jar

CMD java -jar app.jar