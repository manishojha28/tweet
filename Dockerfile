FROM openjdk:8-jdk-alpine
ADD target/auth.jar auth.jar
ENTRYPOINT ["java","-jar", "/auth.jar"]