FROM eclipse-temurin:24-jre-noble
ARG JAR_FILE=target/service_auth-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} auth_service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/auth_service.jar"]