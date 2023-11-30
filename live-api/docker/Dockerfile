FROM openjdk:17-jdk-alpine
EXPOSE 8080
ADD live-api-docker-exec.jar app.jar
ENV JAVA_OPTS=""
ENTRYPOINT java ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom -jar app.jar