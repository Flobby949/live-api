FROM openjdk:17-jdk-alpine
EXPOSE 9090
ADD live-user-provider-docker-exec.jar app.jar
ADD /arthas-bin.zip /opts/
# 对外注册到 nacos 的 ip 和端口
ENV DUBBO_IP_TO_REGISTRY="47.102.140.119"
ENV DUBBO_PORT_TO_REGISTRY="9090"
ENV JAVA_OPTS=""
ENTRYPOINT java ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom  -jar app.jar
