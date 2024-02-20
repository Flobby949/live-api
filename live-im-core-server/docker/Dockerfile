FROM openjdk:17-jdk-alpine
EXPOSE 9010 8888 8877
ADD live-im-core-server-docker-exec.jar app.jar
ADD /arthas-bin.zip /opts/
# 对外注册到 nacos 的 ip 和端口
ENV DUBBO_IP_TO_REGISTRY="106.14.23.235"
ENV DUBBO_PORT_TO_REGISTRY="9010"
ENV JAVA_OPTS="-DDUBBO_IP_TO_REGISTRY=106.14.23.235 -DDUBBO_PORT_TO_REGISTRY=9010"
ENTRYPOINT java ${JAVA_OPTS}  -Djava.security.egd=file:/dev/./urandom  -jar app.jar
