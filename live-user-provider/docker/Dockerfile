FROM openjdk:17-jdk-alpine
EXPOSE 9090
ADD live-user-provider-docker-exec.jar app.jar
ADD /arthas-bin.zip /opt/
# 对外注册到 nacos 的 ip 和端口
ENV DUBBO_IP_TO_REGISTRY="10.21.24.220"
ENV DUBBO_PORT_TO_REGISTRY="9090"
ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/app.jar"]