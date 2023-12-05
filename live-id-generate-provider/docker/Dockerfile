FROM openjdk:17-jdk-alpine
EXPOSE 9098
ADD live-id-generate-provider-docker-exec.jar app.jar
ADD /arthas-bin.zip /opt/
# 对外注册到 nacos 的 ip 和端口
ENV DUBBO_IP_TO_REGISTRY="101.132.144.1"
ENV DUBBO_PORT_TO_REGISTRY="8090"
ENV JAVA_OPTS=""
ENTRYPOINT java ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom -jar app.jar
