FROM maven:3.9-eclipse-temurin-17-alpine AS build
WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

COPY --from=build /app/target/*.jar app.jar

ENV SERVER_PORT=8080

ENV SPRING_DATASOURCE_URL="jdbc:h2:mem:test"
ENV SPRING_DATASOURCE_DRIVERCLASSNAME="org.h2.Driver"
ENV SPRING_DATASOURCE_USERNAME="dsegovia"
ENV SPRING_DATASOURCE_PASSWORD="dsegovia"

ENV SPRING_H2_CONSOLE_ENABLED="true"
ENV SPRING_H2_CONSOLE_PATH="/h2-console"
ENV SPRING_H2_CONSOLE_SETTINGS_WEB_ALLOW_OTHERS="true"

ENV SPRING_SQL_INIT_MODE="always"
ENV SPRING_SQL_INIT_SCHEMA_LOCATIONS="classpath:script.sql"

ENV SPRING_JPA_DATABASE_PLATFORM="org.hibernate.dialect.H2Dialect"
ENV SPRING_JPA_HIBERNATE_DDL_AUTO="none"
ENV SPRING_JPA_DEFER_DATASOURCE_INITIALIZATION="true"

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]