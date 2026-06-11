# Etapa 1: compilar el proyecto
FROM maven:3.9-eclipse-temurin-24 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Etapa 2: imagen final ligera (solo el JRE para ejecutar)
FROM eclipse-temurin:24-jre-noble
COPY --from=build /app/target/*.jar auth_service.jar
EXPOSE 8080
ENTRYPOINT ["java","-jar","/auth_service.jar"]