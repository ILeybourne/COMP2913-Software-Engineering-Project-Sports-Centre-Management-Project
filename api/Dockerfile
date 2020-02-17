########################################### BUILD ENVIRONEMNT
ARG BASE_BUILD_IMAGE=11-jdk

FROM openjdk:${BASE_BUILD_IMAGE} as build

ARG DEPENDENCY=/app

# Bash environment for workspace
ENV BUILD_LOCATION=${DEPENDENCY}

WORKDIR ${DEPENDENCY}

# TODO: Mount the Maven Cache to home directory for speed. Same for apt/cache
COPY mvnw .
COPY .mvn .mvn
COPY pom.xml .
COPY src src

# Intall dependencies but don't package JAR
RUN ./mvnw install -DskipTests

ENV LOCATION=$BUILD_LOCATION/target/dependency

RUN mkdir -p $LOCATION && (cd $LOCATION; jar -xf ../*.jar)

# We can attah a debugger if we need via environemnt variables
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/$ARTIFACT_NAME" ]



########################################### PRODUCTION ENVIRONMENT


FROM openjdk:11-jdk-alpine AS production
COPY --from=build /app /app
ENTRYPOINT [ "sh", "-c", "java $JAVA_OPTS -Djava.security.egd=file:/dev/./urandom -jar /app/app.jar" ]
# Set classpath and Class to run
#ENTRYPOINT ["java","--classpath","app:app/lib/*","uk.ac.leeds.comp2913.api.ApiApplication"]

#COPY --from=workspace /app/api.jar /app




########################################### UNIT TEST ENVIRONMENT STAGE FOR CI/CD
FROM build AS test

ENV TEST=1
ENV SPRING_PROFILE=test

RUN ./mvnw test
ENTRYPOINT ["./mvnw", "test"]
