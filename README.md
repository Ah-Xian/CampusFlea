# 校园二手交易平台

> 基于 Spring Boot + MyBatis + MySQL 的校园二手交易平台

## 项目简介

本项目是一个面向校园的二手物品交易平台，支持用户注册登录、商品发布与浏览、订单管理、即时聊天等功能。

## 技术栈

| 技术 | 说明 |
|------|------|
| Spring Boot 3.x | 后端框架 |
| MyBatis | ORM持久层框架 |
| MySQL 8.x | 数据库 |
| Druid | 数据库连接池 |
| SpringDoc OpenAPI 3 | API文档生成 |
| Thymeleaf | 模板引擎（预留） |

## 功能特性

### 用户模块
- 用户注册、登录、登出
- 登录互顶（多设备登录踢出）
- 用户信息修改
- 管理员用户管理

### 商品模块
- 商品发布、编辑、删除
- 多图上传
- 商品分类
- 商品搜索
- 商品状态管理（待审核/在售/已售/下架）

### 订单模块
- 创建订单
- 订单列表查询
- 订单状态管理

### 聊天模块
- 商品专属聊天（卖家与买家）
- 消息列表
- 会话已读未读

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- MySQL 8.0+

### 配置数据库

```sql
CREATE DATABASE campus_flea CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改 `src/main/resources/application.properties` 中的数据库配置：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/campus_flea
spring.datasource.username=root
spring.datasource.password=你的密码
```

### 启动项目

```bash
mvn spring-boot:run
```

访问地址：http://localhost:25080

### API文档

启动后访问：
- Swagger UI：http://localhost:25080/swagger-ui.html
- OpenAPI JSON：http://localhost:25080/v3/api-docs

## 项目结构

```
springboot_wxx/
├── src/main/java/com/cc/springboot_wxx/
│   ├── entity/          # 实体类
│   │   ├── User.java
│   │   ├── Goods.java
│   │   ├── Order.java
│   │   ├── ProductChat.java
│   │   └── ChatMessage.java
│   ├── mapper/          # MyBatis Mapper
│   ├── service/         # 业务逻辑层
│   ├── controller/      # REST接口层
│   ├── config/          # 配置类
│   └── util/            # 工具类
├── src/main/resources/
│   ├── application.properties
│   └── mapper/          # MyBatis XML（预留）
├── uploads/             # 上传文件目录
├── pom.xml
└── README.md
```

## 数据库表结构

| 表名 | 说明 |
|------|------|
| users | 用户表 |
| goods | 商品表 |
| goods_images | 商品图片表 |
| goods_categories | 商品分类表 |
| orders | 订单表 |
| user_online | 在线设备表 |
| product_chats | 商品聊天会话表 |
| chat_messages | 聊天消息表 |

## 接口示例

### 用户登录
```
POST /user/login
Content-Type: application/x-www-form-urlencoded

account=xxx&password=xxx
```

### 发布商品
```
POST /goods
Authorization: Session

title=商品标题&price=100&categoryId=1&content=商品描述
```

### 发送聊天消息
```
POST /messages?sellerId=2&goodsId=1&content=你好
```

## 配置说明

### 文件上传
- 上传目录：`upload.path` 配置
- 文件访问：`http://localhost:25080/uploads/文件名`
- 最大文件大小：10MB

### 服务端口
- 默认端口：25080

## 开发规范

- 使用 MyBatis 注解方式（不推荐 XML）
- 枚举类型直接映射，不使用 `.name()`
- 使用 snake_case 命名数据库字段
- 代码添加中文注释
- RESTful 接口风格

## 许可证

MIT License