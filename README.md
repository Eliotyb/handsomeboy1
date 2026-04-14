# 🚀 社团团购秒杀后台管理系统

> **Pulse Velocity** - 一个面向校园社团的团购秒杀全栈管理系统，支持 PC 端后台管理与移动端用户操作。

---

## 📖 目录

- [项目简介](#项目简介)
- [技术架构](#技术架构)
- [功能模块](#功能模块)
- [系统截图](#系统截图)
- [快速开始](#快速开始)
- [数据库设计](#数据库设计)
- [API 接口文档](#api-接口文档)
- [项目结构](#项目结构)
- [部署说明](#部署说明)

---

## 项目简介

本项目是一个**校园社团团购秒杀平台**，采用前后端分离架构，包含：

| 端 | 说明 | 访问地址 |
|---|------|----------|
| **PC 后台管理** | 商品管理、秒杀活动、订单处理、数据报表、系统设置 | `/dashboard` |
| **手机端 (H5)** | 首页、分类浏览、购物车、秒杀下单、个人中心、订单追踪、售后评价 | `/mobile` |

### 核心业务流程

```
用户注册/登录 → 浏览商品 → 加入购物车 → 秒杀抢购(限时限量)
→ 下单支付 → 待发货 → 物流跟踪 → 确认收货 → 评价/售后退款
```

---

## 技术架构

### 整体架构图

```
┌─────────────────────────────────────────────────────┐
│                    用户浏览器                        │
│  ┌──────────────┐    ┌──────────────────────────┐   │
│  │  PC端管理后台  │    │     手机端 H5 页面        │   │
│  │  Vue3 + EP    │    │  Vue3 + Element Plus     │   │
│  └──────┬───────┘    └──────────┬───────────────┘   │
│         │                       │                   │
│         └───────────┬───────────┘                   │
│                     │ Vite Dev Server :5173          │
└─────────────────────┼───────────────────────────────┘
                      │ HTTP / Axios
┌─────────────────────┼───────────────────────────────┐
│              Spring Boot :8080                       │
│  ┌─────────────────────────────────────────────┐   │
│  │              Controller 层                    │   │
│  │ Auth | User | Product | Seckill | Cart       │   │
│  │ Order | Logistics | Refund | Review | Coupon │   │
│  └────────────────────┬────────────────────────┘   │
│                       │                            │
│  ┌────────────────────▼────────────────────────┐   │
│  │           Service 层 + Repository 层          │   │
│  │      MyBatis Plus ORM + Redis Token          │   │
│  └────────────────────┬────────────────────────┘   │
│                       │                            │
│  ┌────────────────────▼────────────────────────┐   │
│  │            MySQL 8.0 (Docker)                │   │
│  │  community_groupon: user/product/order/...   │   │
│  └─────────────────────────────────────────────┘   │
│                       │                            │
│  ┌────────────────────▼────────────────────────┐   │
│  │            Redis (Docker)                    │   │
│  │        JWT Token 存储 & 验证                  │   │
│  └─────────────────────────────────────────────┘   │
└───────────────────────────────────────────────────┘
```

### 技术栈详情

| 层级 | 技术 | 版本 | 用途 |
|------|------|------|------|
| **前端框架** | Vue.js | 3.5.13 | 渐进式 JavaScript 框架 |
| **构建工具** | Vite | 6.0.5 | 极速开发服务器与构建 |
| **UI 组件库** | Element Plus | 2.8.4 | 企业级 Vue 3 组件库 |
| **路由管理** | Vue Router | 4.4.5 | SPA 路由 |
| **HTTP 客户端** | Axios | 1.7.9 | API 请求 |
| **后端框架** | Spring Boot | 2.7.15 | Java 全栈框架 |
| **ORM 框架** | MyBatis Plus | 3.5.3 | 数据持久化 |
| **数据库** | MySQL | 8.0 (Docker) | 关系型数据库 |
| **缓存** | Redis | (Docker) | Token 存储 / 缓存 |
| **认证方式** | JWT + Redis | - | 无状态 Token 鉴权 |

---

## 功能模块

### 一、PC 后台管理系统

| 模块 | 功能描述 | 对应页面 |
|------|---------|---------|
| 🏠 **仪表盘** | 数据概览、活动列表、快捷操作、推广卡片 | `Home.vue` |
| 👤 **商品管理** | 商品 CRUD、搜索筛选 | `ProductList.vue` |
| ⚡ **秒杀活动** | 活动 CRUD、时间设置 | `SeckillActivityList.vue` |
| 🎯 **秒杀商品** | 设置秒杀价格/库存 | `SeckillDetail.vue` |
| 📋 **订单管理** | 全流程操控：付款→发货→收货→取消→删除，批量操作 | `OrderList.vue` |
| 👥 **用户管理** | 用户列表/查看/编辑/删除 | `UserList.vue` |
| 📊 **数据对账单** | 成交汇总、明细表格、导出报表 | `DataReport.vue` |
| ⚙️ **系统设置** | 基本配置/秒杀规则/通知/安全/关于 | `SystemSettings.vue` |
| 🗄️ **数据库管理** | 11张表在线 CRUD、CSV 导出 | `DbManager.vue` |

### 二、手机端 (H5)

| 模块 | 功能描述 | 对应页面 |
|------|---------|---------|
| 📱 **首页** | 轮播Banner、分类入口、热门推荐 | `MobileHome.vue` |
| 📂 **分类浏览** | 分类导航、商品网格 | `MobileCategory.vue` |
| 🛒 **购物车** | 商品列表、数量修改、结算 | `MobileCart.vue` |
| 💳 **支付成功** | 支付结果展示 | `MobilePaySuccess.vue` |
| ⏱ **秒杀详情** | 倒计时、立即购买 | `SeckillDetail.vue` |
| ✅ **秒杀成功** | 下单确认 | `SeckillSuccess.vue` |
| 👤 **个人中心** | 头像/昵称/角色、订单统计角标、菜单导航 | `MobileProfile.vue` |
| 💰 **待付款** | 倒计时条、取消/立即支付 | `MobileOrderPending.vue` |
| 📦 **待发货** | 已支付状态、提醒发货 | `MobileOrderShipping.vue` |
| 🚚 **待收货** | 物流动态、配送信息、确认收货 | `MobileOrderReceived.vue` |
| ⭐ **待评价** | 评分弹窗(1-5星)、匿名评价 | `MobileOrderReview.vue` |
| ↩️ **退换货** | Tab切换(全部/审核/处理中/完成) | `MobileAfterSale.vue` |
| 📍 **地址管理** | 收货地址 CRUD、默认地址 | `MobileAddress.vue` |
| 🎫 **优惠券** | 可用/已用/已过期 Tab | `MobileCoupon.vue` |
| ❤️ **我的收藏** | 收藏商品网格 | `MobileFavorites.vue` |
| 🕐 **浏览历史** | 按日期分组的历史记录 | `MobileHistory.vue` |
| 💬 **客服服务** | FAQ 折叠面板、联系方式 | `MobileService.vue` |
| ❓ **帮助中心** | 分类帮助、热门话题、反馈表单 | `MobileHelp.vue` |
| ℹ️ **关于我们** | App 信息、抖音链接 | `MobileAbout.vue` |
| 🔑 **登录/注册** | 表单验证、JWT 登录 | `Login.vue` / `MobileLogin.vue` / `MobileRegister.vue` |

---

## 快速开始

### 环境要求

- **JDK** 1.8+
- **Node.js** 16+
- **Docker** (用于运行 MySQL 和 Redis)
- **Maven** 3.6+

### 1. 启动数据库服务

```bash
# 启动 MySQL (端口 3306, 密码: 123456, 数据库: community_groupon)
docker run -d --name mysql-local -p 3306:3306 \
  -e MYSQL_ROOT_PASSWORD=123456 \
  -e MYSQL_DATABASE=community_groupon \
  mysql:8.0

# 启动 Redis (端口 6379)
docker run -d --name redis-local -p 6379:6379 redis:latest
```

### 2. 初始化数据库

```bash
# 按顺序执行 SQL 脚本
mysql -uroot -p123456 community_groupon < sql/create_tables.sql
mysql -uroot -p123456 community_groupon < sql/sample_data.sql
mysql -uroot -p123456 community_groupon < sql/seckill_event_data.sql
mysql -uroot -p123456 community_groupon < sql/full_products_with_category.sql
mysql -uroot -p123456 community_groupon < sql/create_coupon_table.sql
mysql -uroot -p123456 community_groupon < sql/order_enhancement.sql
```

### 3. 启动后端

```bash
cd backend
mvn spring-boot:run
# 服务启动在 http://localhost:8080
```

### 4. 启动前端

```bash
cd frontend
npm install   # 首次运行需要安装依赖
npm run dev    # 开发服务器启动在 http://localhost:5173
```

### 5. 访问系统

| 入口 | 地址 | 默认账号 |
|------|------|---------|
| PC 后台登录 | http://localhost:5173/ | admin / admin |
| PC 仪表盘 | http://localhost:5173/dashboard | 登录后访问 |
| 手机端首页 | http://localhost:5173/mobile | 同上 |
| 手机端登录 | http://localhost:5173/mobile/login | zhangsan / 123456 |

---

## 数据库设计

### ER 关系概览

```
┌──────────┐     ┌──────────────┐     ┌─────────────┐
│   user   │────<│     order    │────>│   product   │
│  用户表   │ 1:N │    订单表    │ N:1 │    商品表    │
└──────────┘     └──────┬───────┘     └──────┬──────┘
                        │                     │
                 ┌──────┴──────┐     ┌────────┴────────┐
                 │order_logistics│     │seckill_activity │
                 │   物流信息表   │     │    秒杀活动表     │
                 └──────────────┘     └────────┬────────┘
                                            │
                 ┌──────────────┐     ┌────────┴────────┐
                 │ order_refund  │     │ seckill_product │
                 │   售后退款表   │     │    秒杀商品表    │
                 └──────────────┘     └─────────────────┘

┌────────────┐  ┌─────────────┐  ┌──────────────┐
│    cart    │  │ user_coupon │  │  order_review │
│   购物车   │  │  用户优惠券  │  │    评价表     │
└────────────┘  └─────────────┘  └──────────────┘

┌────────────┐
│   coupon  │
│  优惠券模板 │
└────────────┘
```

### 核心表结构

#### user (用户表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| username | VARCHAR(50) | 用户名(唯一) |
| password | VARCHAR(100) | 加密密码 |
| name | VARCHAR(50) | 昵称 |
| phone | VARCHAR(20) | 手机号 |
| address | VARCHAR(255) | 地址 |
| role | VARCHAR(20) | 角色(admin/user) |

#### `order` (订单表 - 增强版)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_no | VARCHAR(50) | 订单号(唯一) |
| user_id | BIGINT | 用户ID |
| product_id | BIGINT | 商品ID |
| price | DOUBLE | 单价 |
| quantity | INT | 数量 |
| total_price | DOUBLE | 总价 |
| pay_method | VARCHAR(20) | 支付方式(wechat/alipay/balance) |
| status | INT | 状态(0待付/1待发/2待收/3完成/4取消/5退款中/6已退款) |
| receiver_name | VARCHAR(50) | 收货人 |
| receiver_phone | VARCHAR(20) | 收货电话 |
| receiver_address | VARCHAR(255) | 收货地址 |
| logistics_company | VARCHAR(50) | 物流公司 |
| logistics_no | VARCHAR(100) | 物流单号 |
| pay_time / ship_time / receive_time / finish_time | DATETIME | 时间线 |
| cancel_reason | VARCHAR(255) | 取消原因 |

#### order_logistics (物流信息表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_id | BIGINT | 关联订单 |
| logistics_company | VARCHAR(50) | 物流公司 |
| logistics_no | VARCHAR(100) | 物流单号 |
| status | INT | 状态(0待揽收/1已揽收/2运输中/3派送中/4已签收) |
| current_location | VARCHAR(255) | 当前位置 |

#### order_refund (售后退款表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| refund_no | VARCHAR(50) | 退款单号(唯一) |
| order_id | BIGINT | 关联订单 |
| refund_type | INT | 类型(1仅退款/2退货退款) |
| reason | VARCHAR(200) | 申请原因 |
| refund_amount | DOUBLE | 退款金额 |
| status | INT | 状态(0待审核/1待寄回/2处理中/3成功/4拒绝) |

#### order_review (评价表)
| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 主键 |
| order_id | BIGINT | 关联订单 |
| product_id | BIGINT | 商品ID |
| rating | INT | 评分(1-5星) |
| content | TEXT | 评价内容 |
| is_anonymous | TINYINT | 是否匿名 |
| reply_content | TEXT | 商家回复 |

---

## API 接口文档

### 认证相关

| 方法 | 路径 | 说明 | 认证 |
|------|------|------|------|
| POST | `/api/auth/login` | 用户登录 | ❌ |
| POST | `/api/auth/admin/login` | 管理员登录 | ❌ |
| POST | `/api/auth/register` | 注册 | ❌ |
| POST | `/api/auth/logout` | 退出登录 | ✅ |

### 用户管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/users` | 用户列表 |
| GET | `/api/users/{id}` | 用户详情 |
| POST | `/api/users` | 创建用户 |
| PUT | `/api/users/{id}` | 更新用户 |
| DELETE | `/api/users/{id}` | 删除用户 |

### 商品管理

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/products` | 商品列表 |
| GET | `/api/products/{id}` | 商品详情 |
| POST | `/api/products` | 新增商品 |
| PUT | `/api/products/{id}` | 更新商品 |
| DELETE | `/api/products/{id}` | 删除商品 |

### 秒杀活动

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/seckill-activities` | 活动列表 |
| POST | `/api/seckill-activities` | 创建活动 |
| PUT | `/api/seckill-activities/{id}` | 更新活动 |
| DELETE | `/api/seckill-activities/{id}` | 删除活动 |

### 秒杀商品

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/seckill-products` | 秒杀商品列表 |
| POST | `/api/seckill-products` | 添加秒杀商品 |
| PUT | `/api/seckill-products/{id}` | 更新 |
| DELETE | `/api/seckill-products/{id}` | 删除 |

### 购物车

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/cart` | 查看购物车 |
| POST | `/api/cart` | 加入购物车 `{productId, quantity}` |
| PUT | `/api/cart/{id}` | 修改数量 `{quantity}` |
| DELETE | `/api/cart/{id}` | 删除项 |
| DELETE | `/api/cart/clear` | 清空购物车 |

### 订单核心 (13个接口)

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/orders` | 所有订单 |
| GET | `/api/orders/user/my-orders` | 我的订单 |
| GET | `/api/orders/{id}` | 订单详情 |
| POST | `/api/orders/create` | 从购物车创建订单 |
| POST | `/api/orders/seckill` | 秒杀下单 |
| **PUT** | `/api/orders/{id}/pay` | **确认付款** |
| **PUT** | `/api/orders/{id}/ship` | **发货**(填物流公司+单号) |
| **PUT** | `/api/orders/{id}/receive` | **确认收货** |
| **PUT** | `/api/orders/{id}/cancel` | **取消订单** |
| GET | `/api/orders/user/status/{status}` | 按状态查询 |
| GET | `/api/orders/stats` | 订单统计 |
| PUT | `/api/orders/{id}` | 更新订单(全字段) |
| DELETE | `/api/orders/{id}` | 删除订单 |

**status 状态码**: `0`=待支付 `1`=已支付/待发货 `2`=已发货/待收货 `3`=已完成 `4`=已取消 `5`=退款中 `6`=已退款

### 物流模块

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/logistics/order/{orderId}` | 物流记录列表 |
| GET | `/api/logistics/order/{orderId}/latest` | 最新物流状态 |
| POST | `/api/logistics` | 创建物流记录 |
| PUT | `/api/logistics/{id}/status` | 更新物流状态 |

### 售后退款

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/refunds/apply` | 申请退款 |
| GET | `/api/refunds/my` | 我的退款记录 |
| GET | `/api/refunds/list` | 全部退款(管理端) |
| GET | `/api/refunds/list/status/{status}` | 按状态筛选 |
| PUT | `/api/refunds/{id}/audit` | 审核(通过/拒绝) |

### 评价模块

| 方法 | 路径 | 说明 |
|------|------|------|
| POST | `/api/reviews` | 提交评价 |
| GET | `/api/reviews/product/{productId}` | 商品评价列表 |
| GET | `/api/reviews/product/{productId}/avg-rating` | 平均评分 |
| GET | `/api/reviews/my` | 我的评价 |
| GET | `/api/reviews/pending/my` | 待评价订单 |
| PUT | `/api/reviews/{id}/reply` | 商家回复 |

### 优惠券

| 方法 | 路径 | 说明 |
|------|------|------|
| GET | `/api/coupons/user` | 我的优惠券 |
| GET | `/api/coupons/available` | 可领取列表 |
| POST | `/api/coupons/{id}/receive` | 领取优惠券 |
| POST | `/api/coupons/{id}/use` | 使用优惠券 |

> **完整 Postman 测试集合文件**: [`Postman_API_Full_Test.json`](./Postman_API_Full_Test.json)
> 包含 11 个模块共 **55+ 个接口**，每个接口预填了请求参数示例。

---

## 项目结构

```
d:\社团\
├── backend/                          # Spring Boot 后端
│   ├── pom.xml                       # Maven 配置
│   └── src/main/java/com/community/groupon/
│       ├── common/
│       │   ├── Result.java            # 统一响应封装
│       │   └── JwtTokenUtil.java      # JWT 工具类
│       ├── config/
│       │   ├── CorsConfig.java        # 跨域配置
│       │   └── SecurityConfig.java    # 安全配置
│       ├── controller/               # 控制器层 (11个)
│       │   ├── AuthController.java   # 认证: 登录/注册/登出
│       │   ├── UserController.java   # 用户 CRUD
│       │   ├── ProductController.java# 商品管理
│       │   ├── SeckillActivityController.java # 秒杀活动
│       │   ├── SeckillProductController.java  # 秒杀商品
│       │   ├── CartController.java    # 购物车
│       │   ├── OrderController.java   # 订单(含状态变更)
│       │   ├── CouponController.java  # 优惠券
│       │   ├── LogisticsController.java# 物流
│       │   ├── RefundController.java  # 售后退款
│       │   └── ReviewController.java  # 评价
│       ├── dto/
│       │   └── CartItemDTO.java       # 购物车项 DTO
│       ├── entity/                   # 实体类 (11个)
│       │   ├── User.java             # 用户
│       │   ├── Product.java          # 商品
│       │   ├── SeckillActivity.java  # 秒杀活动
│       │   ├── SeckillProduct.java   # 秒杀商品
│       │   ├── Order.java            # 订单(增强版)
│       │   ├── Cart.java             # 购物车
│       │   ├── Coupon.java           # 优惠券模板
│       │   ├── UserCoupon.java       # 用户优惠券
│       │   ├── OrderLogistics.java   # 物流信息
│       │   ├── OrderRefund.java      # 售后退款
│       │   └── OrderReview.java      # 评价
│       ├── repository/               # MyBatis Plus Mapper (11个)
│       ├── service/                  # 业务逻辑层
│       │   ├── RedisTokenService.java# Redis Token 管理
│       │   ├── OrderService.java    # 订单服务
│       │   ├── CartService.java      # 购物车服务
│       │   ├── CouponService.java   # 优惠券服务
│       │   ├── OrderLogisticsService.java
│       │   ├── OrderRefundService.java
│       │   └── OrderReviewService.java
│       └── resources/
│           ├── application.yml      # 应用配置
│           └── application-dev.yml  # 开发环境配置
│
├── frontend/                         # Vue 3 前端
│   ├── package.json                 # NPM 依赖
│   ├── vite.config.js               # Vite 配置
│   ├── index.html                  # 入口 HTML
│   └── src/
│       ├── main.js                  # 入口 JS
│       ├── App.vue                  # 根组件
│       ├── router/
│       │   └── index.js             # 路由配置 (30+ 路由)
│       ├── views/                   # 页面组件 (32个)
│       │   ├── Login.vue            # PC 登录页
│       │   ├── Home.vue             # PC 仪表盘
│       │   ├── ProductList.vue      # PC 商品管理
│       │   ├── SeckillActivityList.vue # PC 秒杀活动
│       │   ├── SeckillDetail.vue    # PC 秒杀详情
│       │   ├── SeckillSuccess.vue   # PC 秒杀成功
│       │   ├── OrderList.vue        # PC 订单管理(增强)
│       │   ├── UserList.vue         # PC 用户管理
│       │   ├── DataReport.vue       # PC 数据对账单
│       │   ├── SystemSettings.vue   # PC 系统设置
│       │   ├── DbManager.vue        # PC 数据库管理
│       │   │
│       │   ├── Mobile*.vue          # 手机端页面 (22个)
│       │   │   ├── MobileHome.vue    # 手机首页
│       │   │   ├── MobileLogin.vue   # 手机登录
│       │   │   ├── MobileRegister.vue
│       │   │   ├── MobileCategory.vue
│       │   │   ├── MobileCart.vue
│       │   │   ├── MobileProfile.vue # 个人中心(入口)
│       │   │   ├── MobileOrderPending.vue   # 待付款
│       │   │   ├── MobileOrderShipping.vue  # 待发货
│       │   │   ├── MobileOrderReceived.vue  # 待收货
│       │   │   ├── MobileOrderReview.vue    # 待评价
│       │   │   ├── MobileAfterSale.vue       # 退换货
│       │   │   ├── MobileAddress.vue         # 地址管理
│       │   │   ├── MobileCoupon.vue          # 优惠券
│       │   │   ├── MobileFavorites.vue       # 收藏
│       │   │   ├── MobileHistory.vue         # 历史
│       │   │   ├── MobileService.vue         # 客服
│       │   │   ├── MobileHelp.vue            # 帮助
│       │   │   └── MobileAbout.vue           # 关于
│       │   └── ...
│       └── assets/                  # 静态资源
│
├── sql/                              # 数据库脚本 (12个)
│   ├── create_tables.sql            # 基础表结构
│   ├── sample_data.sql              # 示例数据
│   ├── seckill_event_data.sql       # 秒杀活动数据
│   ├── full_products_with_category.sql # 商品+分类
│   ├── create_coupon_table.sql      # 优惠券表
│   ├── order_enhancement.sql        # 订单增强+物流+售后+评价
│   └── ...                         # 其他修补脚本
│
└── Postman_API_Full_Test.json        # Postman 接口测试集合
```

---

## 部署说明

### 生产环境部署

#### 1. 打包后端 JAR

```bash
cd backend
mvn clean package -DskipTests
# 生成 target/groupon-seckill-1.0-SNAPSHOT.jar
```

#### 2. 构建前端静态资源

```bash
cd frontend
npm run build
# 生成 dist/ 目录
```

#### 3. Docker Compose 一键部署（推荐）

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: your_prod_password
      MYSQL_DATABASE: community_groupon
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql
      - ./sql:/docker-entrypoint-initdb.d

  redis:
    image: redis:latest
    ports:
      - "6379:6379"

  backend:
    image: openjdk:8-jre
    working_dir: /app
    volumes:
      - ./backend/target/groupon-seckill-1.0-SNAPSHOT.jar:/app/app.jar
    command: java -jar app.jar --spring.profiles.active=prod
    ports:
      - "8080:8080"
    depends_on:
      - mysql
      - redis
    environment:
      SPRING_DATASOURCE_URL: jdbc:mysql://mysql:3306/community_groupon
      SPRING_REDIS_HOST: redis

  nginx:
    image: nginx:alpine
    ports:
      - "80:80"
    volumes:
      - ./frontend/dist:/usr/share/nginx/html
      - ./nginx.conf:/etc/nginx/conf.d/default.conf
    depends_on:
      - backend

volumes:
  mysql_data:
```

#### 4. Nginx 反向代理配置

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }

    location /api/ {
        proxy_pass http://backend:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
        proxy_set_header X-Forwarded-For $proxy_add_x_forwarded_for;
    }
}
```

---

## 开发团队

| 角色 | 职责 |
|------|------|
| **后端开发** | Spring Boot + MyBatis Plus + Redis Token |
| **前端开发** | Vue 3 + Element Plus + Vite |
| **数据库设计** | MySQL 8.0 表结构与索引优化 |
| **UI 设计** | PC 管理后台 + 移动端 H5 双端适配 |

---

## 许可证

MIT License

---

> 💡 **提示**: 本项目为学习实践项目，展示了完整的电商秒杀系统从 0 到 1 的搭建过程。如需用于生产环境，建议增加限流熔断、分布式锁、消息队列等高可用组件。
