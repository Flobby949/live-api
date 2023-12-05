FROM openjdk:17-jdk-alpine
EXPOSE 8000
ADD live-gateway-docker-exec.jar app.jar
ADD /arthas-bin.zip /opt/
ENV JAVA_OPTS=""
ENTRYPOINT java ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom -jar app.jar
