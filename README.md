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