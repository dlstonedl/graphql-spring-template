FROM openjdk:8
RUN mkdir app
WORKDIR /app
COPY /build/libs/graphql-spring-template-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]

