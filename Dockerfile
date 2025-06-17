FROM temurin:17
EXPOSE 8080
ADD target/expo-management.jar expo-management.jar
ENTRYPOINT [ "java", "-jar", "target/expo-management.jar" ]
