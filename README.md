# 校园二手交易平台

> 基于 Spring Boot + Vue 的校园二手物品交易平台

## 项目简介

本项目是一个面向校园的二手物品交易平台，采用前后端分离架构。后端使用 Spring Boot + MyBatis，前端使用 Vue 2 + Vue Router + Vuex。

## 技术栈

### 后端
| 技术 | 说明 |
|------|------|
| Spring Boot 3.x | 后端框架 |
| MyBatis | ORM持久层框架 |
| MySQL 8.x | 数据库 |
| Druid | 数据库连接池 |
| SpringDoc OpenAPI 3 | API文档生成 |

### 前端
| 技术 | 说明 |
|------|------|
| Vue 2 | 前端框架 |
| Vue Router | 路由管理 |
| Vuex | 状态管理 |
| Axios | HTTP请求 |
| Vue CLI | 开发工具 |

## 功能模块

### 用户模块
- 用户注册、登录
- 登录互顶（多设备登录踢出）
- 个人信息修改
- 管理员登录

### 商品模块
- 商品浏览、搜索
- 商品详情查看
- 商品发布、编辑、删除
- 商品图片管理（多图上传、删除）
- 商品分类

### 订单模块
- 创建订单
- 订单管理
- 订单状态跟踪

### 聊天模块
- 商品专属聊天（卖家与买家）
- 消息列表
- 消息已读未读

### 管理模块
- 用户管理（查看、封禁）
- 商品管理
- 订单管理

## 项目结构

```
springboot_wxx/
├── src/                      # 后端代码
│   └── main/java/com/cc/springboot_wxx/
│       ├── entity/           # 实体类
│       ├── mapper/           # MyBatis Mapper
│       ├── service/          # 业务逻辑
│       ├── controller/       # REST接口
│       ├── config/           # 配置类
│       └── util/             # 工具类
├── web/my-project/           # 前端代码
│   ├── src/
│   │   ├── views/            # 页面组件
│   │   │   ├── Login.vue     # 登录
│   │   │   ├── Register.vue  # 注册
│   │   │   ├── Index.vue     # 首页
│   │   │   ├── Detail.vue    # 商品详情
│   │   │   ├── Publish.vue   # 发布商品
│   │   │   ├── My.vue        # 个人中心
│   │   │   ├── Admin.vue     # 管理后台
│   │   │   └── AdminLogin.vue # 管理员登录
│   │   ├── store/            # Vuex状态管理
│   │   └── router/           # 路由配置
│   ├── public/               # 静态资源
│   └── dist/                 # 打包后的文件
├── uploads/                  # 上传文件目录
├── pom.xml                   # Maven配置
└── README.md
```

## 快速开始

### 环境要求
- JDK 17+
- Maven 3.8+
- Node.js 14+
- MySQL 8.0+

### 1. 配置数据库

```sql
CREATE DATABASE campus_flea CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
```

修改后端配置文件 `src/main/resources/application.properties`：

```properties
spring.datasource.url=jdbc:mysql://localhost:3306/campus_flea
spring.datasource.username=root
spring.datasource.password=你的密码
```

### 2. 启动后端

```bash
cd springboot_wxx
mvn spring-boot:run
```

后端启动成功访问：http://localhost:25080

### 3. 启动前端

```bash
cd web/my-project
npm install
npm run serve
```

前端启动成功访问：http://localhost:8080

### 4. 部署（可选）

前端打包后放入后端 `uploads` 目录，或单独部署：

```bash
cd web/my-project
npm run build
```

将 `dist` 目录内容部署到 Web 服务器。

## API 接口

启动后端后访问：
- Swagger UI：http://localhost:25080/swagger-ui.html
- OpenAPI JSON：http://localhost:25080/v3/api-docs

### 主要接口

| 模块 | 接口 | 说明 |
|------|------|------|
| 用户 | POST /user/register | 用户注册 |
| 用户 | POST /user/login | 用户登录 |
| 商品 | GET /goods | 商品列表 |
| 商品 | POST /goods | 发布商品 |
| 订单 | POST /order | 创建订单 |
| 消息 | POST /messages | 发送消息 |
| 消息 | GET /messages/chats | 聊天列表 |

## 配置说明

| 配置项 | 默认值 | 说明 |
|--------|--------|------|
| server.port | 25080 | 后端服务端口 |
| upload.path | D:/work/.../uploads | 文件上传目录 |
| spring.datasource | - | 数据库连接配置 |

## 数据库表

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

## 开发规范

- 后端使用 MyBatis 注解方式
- 枚举类型直接映射，不使用 `.name()`
- 使用 snake_case 命名数据库字段
- 前端采用 Vue 2 组件化开发
- RESTful 接口风格

## 注意事项

1. 前端请求后端接口需要携带 Cookie（Session 认证）
2. 文件上传大小限制 10MB
3. 登录互顶：新登录会踢掉之前的设备
4. 聊天按商品区分，一个商品一个聊天会话

## 许可证

MIT License