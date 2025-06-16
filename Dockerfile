FROM openjdk:19-alpine
RUN apk add --no-cache maven
WORKDIR /evote
COPY ./ .
RUN mvn clean package
ENTRYPOINT [ "java", "-jar", "target/evoting-0.0.1-SNAPSHOT.jar" ]
