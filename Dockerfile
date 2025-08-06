FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

EXPOSE 8080

COPY target/expo-management.jar expo-management.jar

ENTRYPOINT ["java", "-jar", "expo-management.jar"]

