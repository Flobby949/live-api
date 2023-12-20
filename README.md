# Flobby 的直播项目

> JDK 17
> MySQL 8

## 项目结构

## 系统部署

### 端口分配

- api -> 随机端口
- bank-api -> 随机端口
- gateway -> 8000
- im-core-server -> dubbo:9010 netty:8888 ws:8877
- user-provider -> 9090
- account -> 139.196.144.39:9091
- msg-provider -> 139.196.144.39:9092
- gift-provider -> 9093
- bank-provider -> 9094
- id-generator -> 139.196.144.39:9095
- im-provider -> 139.196.144.39:9096
- im-router-provider -> 139.196.144.39:9097
- living-provider -> 139.196.144.39:9098

### 依赖情况

#### 独立部署

- account
- id-generator
- im-provider
- living-provider
- bank-provider

#### 依赖部署

- api ->
- bank-api -> bank-provider
- gateway -> account
- im-router -> im-core-server -> im-provider
- msg-provider -> im-router , living-provider
- user-provider -> account , id-generator
- gift-provider -> im-router , bank-provider