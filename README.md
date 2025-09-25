<h1 align="center">Sagittarius</h1>
<p align="center"><img src="images/background.png"></p>


**Sagittarius(射手座)** 象征着探索者、哲学家与自由的追寻者。其符号是一支射向远方的箭，完美契合了个人博客的核心精神：
- **思想如箭，精准洞见：** 博客文章应像射手射出的箭，旨在传递清晰、有力、直达核心的观点与知识。
- **追逐自由，分享无界：** 射手座对自由的热爱，象征着我们在技术世界里对开源、共享和无障碍交流的追求。本平台旨在为每一个思想提供自由发布的舞台。
- **探索未知，阔步前行：** 射手座的探索精神，代表了开发者不断学习新技术、构建新项目的旅程。这个博客系统本身，就是一次运用众多前沿技术的探索与实践。

## 项目介绍
Sagittarius是一个基于Spring Cloud的微服务架构博客系统，提供完整的博客内容管理、用户认证、搜索、任务调度等功能。系统采用模块化设计，各服务之间通过REST API和消息队列进行通信，具有高可用性、可扩展性和灵活性的特点。

## 技术栈

### 核心框架
- Spring Boot 3.2.x
- Spring Cloud 2023.x
- Spring Cloud Alibaba

### 服务治理
- Nacos 3.0.x (服务注册与发现、配置中心)
- Spring Cloud Gateway (API网关)
- Spring Cloud LoadBalancer (负载均衡)

### 数据存储
- MySQL 8.0 (关系型数据库)
- Redis 7.0 (缓存、分布式锁)
- Elasticsearch 8.x (全文搜索引擎)
- MinIO (对象存储)

### 消息队列
- RabbitMQ (异步通信、消息队列)

### 认证授权
- Spring Security
- JWT (JSON Web Token)

### 任务调度
- Spring Task

### 监控与日志
- Spring Boot Admin (监控)
- SLF4J + Logback (日志框架)

### 构建工具
- Gradle 9.0
- Kotlin DSL

## 模块结构

项目采用模块化微服务架构，包含以下12个核心模块：

| 模块名称 | 模块说明 | 端口 | 主要功能 |
|---------|---------|------|---------|
| sagittarius-gateway | API网关 | 8800 | 请求路由、负载均衡、限流、认证前置 |
| sagittarius-auth | 认证授权服务 | 8801 | 用户认证、授权管理、JWT签发验证 |
| sagittarius-user | 用户服务 | 8802 | 用户信息管理、个人设置、权限控制 |
| sagittarius-task | 任务调度服务 | 8803 | 定时任务管理、执行调度、日志记录 |
| sagittarius-category | 分类服务 | 8804 | 内容分类、标签管理、目录结构 |
| sagittarius-search | 搜索服务 | 8805 | 全文检索、搜索建议、结果排序 |
| sagittarius-notification | 通知服务 | 8806 | 站内通知、邮件发送、消息推送 |
| sagittarius-report | 报表服务 | 8807 | 数据统计、报表生成、趋势分析 |
| sagittarius-file | 文件服务 | 8808 | 文件上传、下载、存储、管理 |
| sagittarius-monitor | 监控服务 | 8809 | 系统监控、健康检查、性能分析 |
| sagittarius-mq | 消息队列服务 | 8810 | 消息的发布、订阅、路由等功能 |
| sagittarius-common | 公共模块 | - | 共享工具类、常量、配置、实体类 |

## 快速开始

### 环境要求
- JDK 17+ 
- Gradle 9.0+ 
- Docker (用于运行依赖服务)
- MySQL 8.0
- Redis 7.0
- Nacos 2.3.x
- RabbitMQ 3.12+
- Elasticsearch 8.x
- MinIO

### 前置条件
1. 确保所有依赖的中间件服务已启动
2. 创建相应的数据库和配置Nacos

### 构建项目

在项目根目录执行以下命令：

```bash
./gradlew build
```

### 运行服务

可以单独运行各个模块，也可以使用Docker Compose运行整个系统。以下是单独运行某个模块的示例：

```bash
# 运行网关服务
cd sagittarius-gateway
./gradlew bootRun

# 运行认证服务
cd ../sagittarius-auth
./gradlew bootRun

# 其他模块类似
```

### 访问系统

所有服务通过网关统一访问，默认地址：http://localhost:8800

## 配置说明

各模块的配置主要集中在`application.yml`文件中，包括：
- 服务基本信息（名称、端口等）
- Nacos注册与配置中心配置
- 数据库连接配置
- Redis配置
- 消息队列配置
- 其他第三方服务配置

## 开发指南

### 代码风格
- 遵循Google Java Style Guide
- 使用Lombok简化代码
- 使用JavaDoc注释公共接口和类

### 提交规范
- 遵循Conventional Commits规范
- 提交信息包含类型、范围和简短描述

## 许可证

本项目采用 [Apache License 2.0](LICENSE) 许可证。
