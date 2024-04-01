# Flobby 的直播项目

> JDK 17
> MySQL 8

## 项目结构

## 系统部署

### 端口分配

- api -> 随机端口
- bank-api -> 随机端口
- gateway -> 8000
- im-core-server -> dubbo:9010 netty:8888 ws:8887
- user-provider -> 9090
- account -> 9091
- msg-provider -> 9092
- gift-provider -> 9093
- bank-provider -> 9094
- id-generator -> 9095
- im-provider -> 9096
- im-router-provider -> 9097
- living-provider -> 9098

### 依赖情况

#### 独立部署

- account
- id-generator
- im-provider
- bank-provider

#### 依赖部署

- api ->
- bank-api -> bank-provider
- gateway -> account
- im-router -> im-core-server -> im-provider
- msg-provider -> living-provider -> im-router
- user-provider -> account , id-generator
- gift-provider -> im-router , bank-provider

### 命令

```shell
# 下载镜像
docker pull lavaclone/live-living-provider-docker:1.0.0
docker pull lavaclone/live-im-router-provider-docker:1.0.0
docker pull lavaclone/live-bank-provider-docker:1.0.0
docker pull lavaclone/live-gift-provider-docker:1.0.0
docker pull lavaclone/live-im-provider-docker:1.0.0
docker pull lavaclone/live-im-core-server-docker:1.0.0
docker pull lavaclone/live-msg-provider-docker:1.0.0
docker pull lavaclone/live-account-provider-docker:1.0.0
docker pull lavaclone/live-id-generate-provider-docker:1.0.0
docker pull lavaclone/live-gateway-provider-docker:1.0.0
docker pull lavaclone/live-user-provider-docker:1.0.0
docker pull lavaclone/live-api-provider-docker:1.0.0

# 运行容器
docker run -d --name account -p 9091:9091 lavaclone/live-account-provider-docker:1.0.0
docker run -d --name id-generate -p 9095:9095 lavaclone/live-id-generate-provider-docker:1.0.0
docker run -d --name im-provider -p 9096:9096 lavaclone/live-im-provider-docker:1.0.0
docker run -d --name bank -p 9094:9094 lavaclone/live-bank-provider-docker:1.0.0

docker run -d --name im-core -p 8888:8888 -p 8887:8887 -p 9010:9010 lavaclone/live-im-core-server-docker:1.0.0
docker run -d --name im-router -p 9097:9097 lavaclone/live-im-router-provider-docker:1.0.0
docker run -d --name living -p 9098:9098 lavaclone/live-living-provider-docker:1.0.0
docker run -d --name msg -p 9092:9092 lavaclone/live-msg-provider-docker:1.0.0
docker run -d --name gift -p 9093:9093 lavaclone/live-gift-provider-docker:1.0.0
docker run -d --name user -p 9090:9090 lavaclone/live-user-provider-docker:1.0.0

# api业务需要单独设置端口号
#docker run -d --name bank-api -p 9099:9099 lavaclone/live-bank-api-provider-docker:1.0.0
#docker run -d --name gateway -p 8000:8000 lavaclone/live-gateway-provider-docker:1.0.0
#docker run -d --name api -p 8080:8080 lavaclone/live-api-provider-docker:1.0.0

```