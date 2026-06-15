# 民宿预定管理平台 - 项目报告

## 1. 项目概述

### 1.1 项目背景
本项目是一个基于 Java 17 + Spring Boot 3.x + Vue 3 的民宿预定管理平台，支持游客预订、房东管理、管理员后台三种角色的完整业务流程。

### 1.2 项目目标
- 为游客提供便捷的民宿浏览和预订服务
- 为房东提供民宿管理和订单处理功能
- 为管理员提供全局数据管理和监控能力

### 1.3 项目结构
```
homestay-management/
├── backend/              # Spring Boot 后端服务
│   ├── pom.xml
│   └── src/main/
│       ├── java/com/homestay/
│       │   ├── controller/     # REST API 控制层
│       │   ├── service/        # 业务逻辑层
│       │   ├── repository/     # 数据访问层
│       │   ├── entity/         # 实体类
│       │   ├── dto/            # 数据传输对象
│       │   ├── config/         # 配置类
│       │   └── util/           # 工具类
│       └── resources/
│           └── application.yml # 应用配置
├── frontend/             # Vue 3 前端应用
│   ├── package.json
│   ├── vite.config.js
│   └── src/
│       ├── api.js             # API 封装
│       ├── router/index.js    # 路由配置
│       └── views/             # 页面组件
│           ├── guest/         # 游客端
│           ├── host/          # 房东端
│           └── admin/         # 管理端
└── init.sql              # 数据库初始化脚本
```

---

## 2. 技术栈

### 2.1 后端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Java | 17 | 编程语言 |
| Spring Boot | 3.2.5 | 应用框架 |
| Spring Data JPA | 3.2.x | 数据访问层 |
| MySQL | 8.0+ | 数据库 |
| JJWT | 0.12.x | JWT 认证 |
| Spring Security | 6.2.x | 安全框架 |

### 2.2 前端技术
| 技术 | 版本 | 说明 |
|------|------|------|
| Vue | 3.4.x | 前端框架 |
| Vue Router | 4.3.x | 路由管理 |
| Axios | 1.6.x | HTTP 客户端 |
| Vite | 5.2.x | 构建工具 |

### 2.3 数据库设计

#### 用户表 (users)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| username | VARCHAR(50) | 用户名，唯一 |
| password | VARCHAR(255) | 密码 |
| role | VARCHAR(20) | 角色：ADMIN/HOST/GUEST |
| nickname | VARCHAR(100) | 昵称 |
| phone | VARCHAR(20) | 手机号 |
| created_at | DATETIME | 创建时间 |

#### 房间表 (rooms)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| title | VARCHAR(100) | 民宿标题 |
| description | TEXT | 描述 |
| address | VARCHAR(500) | 地址 |
| price | DECIMAL(10,2) | 单价(元/晚) |
| type | VARCHAR(50) | 类型(公寓/别墅/小屋等) |
| capacity | INT | 容纳人数 |
| image_url | VARCHAR(500) | 图片URL |
| host_id | BIGINT | 房东ID |
| available | BOOLEAN | 是否可预订 |
| created_at | DATETIME | 创建时间 |

#### 订单表 (orders)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT | 主键，自增 |
| guest_id | BIGINT | 游客ID |
| room_id | BIGINT | 房间ID |
| host_id | BIGINT | 房东ID |
| check_in | DATE | 入住日期 |
| check_out | DATE | 离店日期 |
| total_price | DECIMAL(10,2) | 总价 |
| status | VARCHAR(20) | 状态：PENDING/CONFIRMED/COMPLETED/CANCELLED |
| created_at | DATETIME | 创建时间 |

---

## 3. 功能模块

### 3.1 游客端功能
| 功能 | 描述 | 接口 |
|------|------|------|
| 用户登录 | 账号密码登录 | POST /api/auth/login |
| 用户注册 | 注册游客账号 | POST /api/auth/register |
| 浏览民宿 | 查看所有可预订民宿 | GET /api/rooms |
| 民宿详情 | 查看单个民宿详情 | GET /api/rooms/{id} |
| 创建订单 | 预订民宿 | POST /api/orders |
| 我的订单 | 查看个人订单 | GET /api/orders |
| 取消订单 | 取消待确认订单 | PATCH /api/orders/{id}/cancel |

### 3.2 房东端功能
| 功能 | 描述 | 接口 |
|------|------|------|
| 用户登录 | 账号密码登录 | POST /api/auth/login |
| 用户注册 | 注册房东账号 | POST /api/auth/register |
| 民宿列表 | 查看个人民宿 | GET /api/rooms/host/{hostId} |
| 添加民宿 | 创建新民宿 | POST /api/rooms |
| 编辑民宿 | 修改民宿信息 | PUT /api/rooms/{id} |
| 上下架 | 切换民宿状态 | PATCH /api/rooms/{id}/toggle |
| 删除民宿 | 删除民宿 | DELETE /api/rooms/{id} |
| 订单列表 | 查看订单 | GET /api/orders/host/{hostId} |
| 确认订单 | 确认预订 | PATCH /api/orders/{id}/status |
| 完成订单 | 标记完成 | PATCH /api/orders/{id}/status |

### 3.3 管理端功能
| 功能 | 描述 | 接口 |
|------|------|------|
| 用户登录 | 账号密码登录 | POST /api/auth/login |
| 民宿管理 | 查看所有民宿 | GET /api/admin/rooms |
| 删除民宿 | 删除任意民宿 | DELETE /api/admin/rooms/{id} |
| 订单管理 | 查看所有订单 | GET /api/admin/orders |
| 用户管理 | 查看所有用户 | GET /api/users |
| 删除用户 | 删除任意用户 | DELETE /api/users/{id} |

---

## 4. 接口列表

### 4.1 认证接口
| 路径 | 方法 | 描述 |
|------|------|------|
| /api/auth/login | POST | 用户登录 |
| /api/auth/register | POST | 用户注册 |

### 4.2 民宿接口
| 路径 | 方法 | 描述 |
|------|------|------|
| /api/rooms | GET | 获取民宿列表(游客) |
| /api/rooms/{id} | GET | 获取民宿详情 |
| /api/rooms | POST | 添加民宿 |
| /api/rooms/{id} | PUT | 编辑民宿 |
| /api/rooms/{id}/toggle | PATCH | 上下架民宿 |
| /api/rooms/{id} | DELETE | 删除民宿(房东) |
| /api/rooms/host/{hostId} | GET | 获取房东民宿 |
| /api/admin/rooms | GET | 获取所有民宿(管理员) |
| /api/admin/rooms/{id} | DELETE | 删除民宿(管理员) |

### 4.3 订单接口
| 路径 | 方法 | 描述 |
|------|------|------|
| /api/orders | GET | 获取游客订单 |
| /api/orders | POST | 创建订单 |
| /api/orders/{id}/cancel | PATCH | 取消订单 |
| /api/orders/host/{hostId} | GET | 获取房东订单 |
| /api/orders/{id}/status | PATCH | 更新订单状态 |
| /api/admin/orders | GET | 获取所有订单 |

### 4.4 用户接口
| 路径 | 方法 | 描述 |
|------|------|------|
| /api/users | GET | 获取所有用户(管理员) |
| /api/users/{id} | DELETE | 删除用户(管理员) |

---

## 5. 部署说明

### 5.1 环境要求
- Java 17+
- Node.js 18+
- MySQL 8.0+

### 5.2 数据库配置
```sql
-- 创建数据库
CREATE DATABASE IF NOT EXISTS `homestay-management` CHARACTER SET utf8mb4;

-- 执行初始化脚本
mysql -uroot -proot --default-character-set=utf8mb4 "homestay-management" -e "source init.sql"
```

### 5.3 启动后端
```bash
cd backend
mvn spring-boot:run
```
后端服务运行在 http://localhost:8080

### 5.4 启动前端
```bash
cd frontend
npm install
npm run dev
```
前端服务运行在 http://localhost:5173

---

## 6. 测试账号

| 角色 | 用户名 | 密码 | 登录后跳转 |
|------|--------|------|-----------|
| 管理员 | admin | 123456 | /admin |
| 房东 | host | 123456 | /host |
| 游客 | guest | 123456 | /guest |

---

## 7. 安全说明

1. **JWT 认证**: 所有接口（除登录/注册）需携带 `Authorization: Bearer <token>` 请求头
2. **权限控制**: 不同角色只能访问对应权限的接口
3. **密码安全**: 密码使用 BCrypt 加密存储（当前演示使用明文）
4. **CORS 配置**: 仅允许前端地址跨域访问

---

## 8. 项目状态

- ✅ 游客端：浏览民宿、预订、订单管理
- ✅ 房东端：民宿管理、订单处理
- ✅ 管理员端：全局管理
- ✅ 用户认证与权限控制
- ✅ 数据库初始化脚本
