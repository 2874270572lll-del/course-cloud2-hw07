# Course Cloud 2 (HW07)

本项目是基于 Docker Compose + Nacos 实现的微服务课程系统，主要完成 HW07 文档要求的「多实例启动与负载均衡验证」功能。


## 项目结构
```
course-cloud2/
├── catalog-service/       # 课程目录服务（多实例部署）
├── enrollment-service/    # 选课服务
├── init-scripts/          # 数据库初始化脚本（Nacos 认证表）
├── docker-compose.yml     # 服务编排配置
└── README.md              # 项目说明文档
```


## 核心功能
1. **Nacos 服务发现**：服务自动注册到 Nacos，支持服务间调用
2. **多实例负载均衡**：`catalog-service` 启动 3 个实例，请求自动分摊
3. **容器化部署**：Docker Compose 一键启动所有依赖服务


## 部署步骤

### 1. 环境准备
- 安装 [Docker](https://www.docker.com/) 和 [Docker Compose](https://docs.docker.com/compose/)
- 安装 [Git](https://git-scm.com/)


### 2. 拉取代码
```bash
git clone git@github.com:2874270572lll-del/course-cloud2-hw07-.git
cd course-cloud2-hw07-
```


### 3. 启动服务
一键启动所有服务（含 3 个 `catalog-service` 实例）：
```bash
docker compose up -d --scale catalog-service=3
```


### 4. 验证服务状态

#### （1）Nacos 控制台验证
访问 `http://localhost:8848/nacos`（账号/密码：`nacos/nacos`），「服务列表」中可见：
- `catalog-service`：3 个健康实例
- `enrollment-service`：1 个健康实例


#### （2）负载均衡验证
执行以下命令，查看请求分配到不同实例（输出 3 个不同主机名）：
```bash
for i in {1..10}; do
  echo -n "第 $i 次: "
  curl -s http://localhost:8082/api/test/call-catalog | jq -r '."catalog-service-response".hostname'
  sleep 0.5
done
```


## 版本标签
本项目 HW07 提交版本已标记为：
```bash
git tag v07  # 本地标签
git push origin v07  # 推送至远程
```


## 技术栈
- 服务发现：Nacos 2.2.3
- 容器编排：Docker Compose
- 框架：Spring Boot 3.3.4
- 数据库：MySQL 8.4
```
