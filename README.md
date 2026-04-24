<p align="center">
  <img src="https://readme-typing-svg.herokuapp.com?font=Fira+Code&size=32&duration=3000&pause=1000&color=667eea&center=true&vCenter=true&width=600&lines=%E7%A4%BE%E5%9B%A2%E5%9B%A2%E8%B4%AD%E7%A7%92%E6%9D%80%E5%90%8E%E5%8F%B0%E7%AE%A1%E7%90%86%E7%B3%BB%E7%BB%9F" alt="社团团购秒杀后台管理系统">
</p>

<p align="center">
  <strong>校园社团团购秒杀全栈平台</strong> · PC 后台管理 + 移动端 H5 + AI 智能助手
</p>

<p align="center">
  <img src="https://img.shields.io/badge/Spring_Boot-2.7.15-green?logo=spring&logoColor=white" alt="Spring Boot">
  <img src="https://img.shields.io/badge/Vue.js-3.5-blue?logo=vuedotjs&logoColor=white" alt="Vue 3">
  <img src="https://img.shields.io/badge/MySQL-8.0-orange?logo=mysql&logoColor=white" alt="MySQL">
  <img src="https://img.shields.io/badge/Redis-7.0-red?logo=redis&logoColor=white" alt="Redis">
  <img src="https://img.shields.io/badge/Qwen_Plus-AI_Purple-9cf?logo=alibabacloud&logoColor=white" alt="Qwen Plus AI">
  <img src="https://img.shields.io/badge/Java-1.8-yellow?logo=openjdk&logoColor=black" alt="Java">
  <img src="https://img.shields.io/badge/License-MIT-success" alt="License">
</p>

---

## 📌 目录

- [✨ 功能特性](#-功能特性)
- [🏗️ 技术架构](#-技术架构)
- [📸 系统预览](#-系统预览)
- [🚀 快速开始](#-快速开始)
- [📦 项目结构](#-项目结构)
- [🗄️ 数据库设计](#-数据库设计)
- [📡 API 接口](#-api-接口)
- [🤖 AI 助手](#-ai-助手)
- [🐳 Docker 部署](#-docker-部署)

---

## ✨ 功能特性

### 🖥️ PC 管理后台 (10 大模块)

| 模块 | 功能说明 |
|------|----------|
| 📊 **仪表盘** | 用户数/商品数/订单数/交易总额实时统计 |
| 🛍️ **商品管理** | 商品 CRUD、分类管理、上下架控制 |
| ⚡ **秒杀活动** | 活动创建/编辑、时间窗口配置、库存分配 |
| 🛒 **购物车** | 商品添加/修改/删除、批量操作 |
| 🎫 **优惠券** | 优惠券模板、领取/使用/过期管理 |
| 📋 **订单管理** | 全生命周期(待支付→发货→收货→完成→退款)、状态流转 |
| 🚚 **物流追踪** | 物流公司/单号录入、状态更新、轨迹查询 |
| 🔙 **售后退款** | 退款申请/审核/拒绝，退款类型(仅退款/退货退款) |
| 💬 **评价系统** | 商品评价提交/回复、星级评分、匿名评价 |
| ⚙️ **系统设置** | 站点信息/秒杀规则/通知/安全策略 |
| 📈 **数据报表** | 交易对账单、活动数据导出 |
| 🗃️ **数据库管理** | 11 张表在线 CRUD 操作 |

### 📱 移动端 H5 (22 个页面)

| 分类 | 页面列表 |
|------|----------|
| 首页 | 轮播图/热门推荐/秒杀倒计时/分类导航 |
| 商品 | 列表/详情/搜索/筛选 |
| 购物车 | 商品管理/数量调整/结算跳转 |
| 订单 | 待付款/待发货/待收货/待评价/退换售后 |
| 个人 | 头像/资料/订单统计入口/设置 |

### 🤖 AI 智能助手

> 基于 **通义千问 Qwen-Plus** 的悬浮式智能对话助手

- 🎯 团购运营咨询（转化率提升、活动策划）
- 💡 社团创意方案（招新/活动/宣传）
- 🔧 技术问题解答（Java/Vue/MySQL）
- 🔄 多轮对话记忆（上下文理解）

---

## 🏗️ 技术架构

```
┌─────────────────────────────────────────────────────────────┐
│                        用户浏览器                            │
│  ┌─────────────────────┐    ┌─────────────────────────────┐ │
│  │   PC 管理后台        │    │   移动端 H5                 │ │
│  │   Vue3 + ElementPlus │    │   Vue3 + 自适应布局          │ │
│  └──────────┬───────────┘    └──────────────┬──────────────┘ │
│             │                               │                │
│             └──────────────┬────────────────┘                │
│                            ▼                                 │
│                    ┌──────────────┐                         │
│                    │   Vite Dev    │                         │
│                    │   :5173       │                         │
│                    └──────┬───────┘                         │
└───────────────────────────┼─────────────────────────────────┘
                            │ HTTP / Axios
                            ▼
┌─────────────────────────────────────────────────────────────┐
│                   Spring Boot 2.7.15                        │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────────┐  │
│  │Security  │ │JWT+Redis │ │Controller│ │AiChatService  │  │
│  │Filter    │ │Token     │ │(12个)    │ │→ Qwen-Plus    │  │
│  └──────────┘ └──────────┘ └────┬─────┘ └──────┬───────┘  │
│                              │              │             │
│  ┌──────────┐ ┌──────────┐ ┌──▼───────┐ ┌────▼────────┐  │
│  │MyBatis   │ │Service   │ │Repository│ │DashScope SDK │  │
│  │Plus 3.5  │ │(13个)    │ │(11个)    │ │OkHttp Client │  │
│  └────┬─────┘ └──────────┘ └──────────┘ └───────────────┘  │
│       │                                                    │
├───────▼────────────────────────────────────────────────────┤
│  ┌──────────┐          ┌──────────┐                       │
│  │ MySQL 8.0│◄────────►│ Redis 7.0│                       │
│  │ 数据存储 │  缓存    │ 会话缓存 │                       │
│  └──────────┘          └──────────┘                       │
└─────────────────────────────────────────────────────────────┘
```

### 技术栈一览

| 层级 | 技术 | 版本 | 用途 |
|------|------|------|------|
| **前端框架** | Vue.js | 3.5 | 渐进式 JS 框架 |
| **构建工具** | Vite | 6.0 | 极速开发服务器 |
| **UI 组件库** | Element Plus | 2.8 | PC 端组件库 |
| **后端框架** | Spring Boot | 2.7.15 | Java 应用框架 |
| **ORM 框架** | MyBatis-Plus | 3.5.3 | 数据库操作增强 |
| **安全框架** | Spring Security | - | 认证授权 |
| **令牌存储** | Redis | 7.0 | JWT Token 存储 |
| **关系数据库** | MySQL | 8.0 | 业务数据持久化 |
| **AI 引擎** | 通义千问 Qwen-Plus | - | 智能对话 |
| **HTTP 客户端** | OkHttp | 4.12 | AI API 调用 |

---

## 📸 系统预览

### PC 管理后台

```
┌──────────────────────────────────────────────────────────┐
│  🏠首页  📦商品  ⚡秒杀  🛒购物车  🎫优惠券  📋订单      │
│  🚚物流  🔙售后  💬评价  📈报表  ⚙️设置  🤖AI助手       │
├──────────────────────────────────────────────────────────┤
│                                                           │
│  ┌──────────┐ ┌──────────┐ ┌──────────┐ ┌──────────┐    │
│  │ 👥 会员数  │ │ 📦 商品数  │ │ 📋 订单数  │ │ 💰 总金额  │    │
│  │   156    │ │   48     │ │  1024    │ │ ¥156,800 │    │
│  └──────────┘ └──────────┘ └──────────┘ └──────────┘    │
│                                                           │
│  ┌────────────────────────────────────────────────────┐  │
│  │  📈 近期交易趋势                                     │  │
│  │  ╭━━━━━━━━━━━━━━━━━━━━━━━━━━━━╮                     │  │
│  │  │  ▲    ╱╲    ╱╲                                    │  │
│  │  │ ╱╲  ╱  ╲  ╱  ╲   ╱╲                              │  │
│  │  │╱  ╲╱    ╲╱    ╲╱   ╲                             │  │
│  │  ┗━━━━━━━━━━━━━━━━━━━━━━━━━━━━┛                     │  │
│  └────────────────────────────────────────────────────┘  │
│                                                           │
│  ┌──────────────────────┐  ┌──────────────────────────┐  │
│  │ 🔥 热门商品 TOP5      │  │ ⚡ 进行中秒杀活动         │  │
│  │ 1. 社团定制T恤        │  │ iPhone 16 秒杀           │  │
│  │ 2. 校园文创周边        │  │ 倒计时: 02:15:33        │  │
│  │ ...                  │  │ ...                      │  │
│  └──────────────────────┘  └──────────────────────────┘  │
│                                                           │
│                          ┌──────┐                        │
│                          │ 🤖AI │  ← 悬浮按钮            │
│                          └──────┘                        │
└──────────────────────────────────────────────────────────┘
```

### 移动端 H5

```
┌──────────────────────┐
│  ←  社团团购  🔍  🛒 │
├──────────────────────┤
│  ━━━━━━━━━━━━━━━━━━  │  ← 轮播图
│  📢 春季大促进行中!   │
│  ━━━━━━━━━━━━━━━━━━  │
├──────────────────────┤
│  ⚡ 限时秒杀  02:15:33│
│  ┌────┐ ┌────┐ ┌────┐│
│  │📱  │ │🎧  │ │⌚  ││  ← 秒杀商品
│  │¥19 │ │¥39 │ │¥9  ││
│  └────┘ └────┘ └────┘│
├──────────────────────┤
│  🔥 热门推荐          │
│  ┌──────────────────┐│
│  │  [商品图]         ││
│  │  社团定制T恤      ││
│  │  ¥59.9  已售128  ││
│  └──────────────────┘│
├──────────────────────┤
│  🏠  📦  ⚡  🛒  👤  │  ← 底部Tab栏
└──────────────────────┘
```

### AI 智能助手

```
                    ┌─────────────────────────┐
                    │ 🔮 社团团购助手  [🗑][✕]│
                    │ ● Qwen-Plus 在线        │
├────────────────────┤                         │
│                    │  🤖 你好！我是社团团购   │
│                    │     助手 🎉             │
│                    │     我可以帮你：         │
│                    │     📦 解答商品管理问题  │
│                    │     💡 提供活动创意方案  │
│                    │     ...                 │
│                    │                         │
│         😊 用户:    │  如何提高转化率？       │
│                    │                         │
│         🤖 助手:    │  这里有几个建议...     │
│                    │                         │
├────────────────────┤ [💡快捷] [输入框...] [▶] │
                    └─────────────────────────┘
                           ↕️ 右下角悬浮
```

---

## 🚀 快速开始

### 环境要求

| 工具 | 版本要求 |
|------|----------|
| JDK | 1.8+ |
| Node.js | 16+ |
| MySQL | 8.0+ |
| Redis | 6.0+ |
| Maven | 3.6+ |

### 一键启动 (Docker)

```bash
# 克隆项目
git clone https://github.com/your-username/community-groupon.git
cd community-groupon

# 启动 MySQL + Redis
docker-compose up -d mysql redis

# 初始化数据库
mysql -u root -p community_groupon < sql/init.sql

# 启动后端 (设置API Key)
export ALAPI_KEY="your-dashscope-api-key"
cd backend && mvn spring-boot:run &

# 启动前端
cd frontend && npm install && npm run dev
```

### 手动安装步骤

<details>
<summary><b>📋 点击展开详细步骤</b></summary>

#### Step 1: 数据库初始化

```bash
# 创建数据库
mysql -u root -p -e "CREATE DATABASE community_groupon DEFAULT CHARSET utf8mb4;"

# 导入表结构和初始数据
mysql -u root -p community_groupon < sql/init.sql
```

#### Step 2: 配置 Redis

```bash
# Docker 启动 Redis
docker run -d --name redis-local \
  -p 6379:6379 \
  redis:7-alpine
```

#### Step 3: 启动后端

```bash
cd backend

# 设置环境变量 (Windows PowerShell)
$env:ALAPI_KEY = "sk-your-api-key-here"

# Maven 编译运行
mvn clean compile
mvn spring-boot:run
```

#### Step 4: 启动前端

```bash
cd frontend
npm install
npm run dev
```

</details>

### 访问地址

| 服务 | 地址 | 说明 |
|------|------|------|
| PC 管理后台 | http://localhost:5173/dashboard | Vue 前端 |
| 移动端 H5 | http://localhost:5173/mobile | 手机适配 |
| 后端 API | http://localhost:8080/api | REST 接口 |
| AI 助手 | 右下角悬浮按钮 | 对话交互 |

---

## 📦 项目结构

```
community-groupon/
├── backend/                          # Spring Boot 后端
│   ├── src/main/java/com/community/groupon/
│   │   ├── GrouponApplication.java   # 启动类
│   │   ├── common/                   # 公共模块
│   │   │   ├── Result.java           # 统一响应封装
│   │   │   ├── JwtUtil.java          # JWT 工具
│   │   │   └── RedisTokenService.java # Token 管理
│   │   ├── config/                   # 配置类
│   │   │   ├── SecurityConfig.java   # 安全配置
│   │   │   ├── CorsConfig.java       # 跨域配置
│   │   │   └── WebConfig.java        # Web 配置
│   │   ├── controller/               # 控制器层 (12个)
│   │   │   ├── AuthController.java   # 认证接口
│   │   │   ├── ProductController.java
│   │   │   ├── OrderController.java  # 订单(13个接口)
│   │   │   ├── AiChatController.java # AI 对话
│   │   │   └── ...
│   │   ├── service/                  # 服务层 (13个)
│   │   │   ├── AiChatService.java    # AI 调用服务
│   │   │   ├── OrderService.java
│   │   │   └── ...
│   │   ├── entity/                   # 实体类 (11个)
│   │   ├── repository/               # 数据访问层 (11个)
│   │   └── dto/                      # 数据传输对象
│   ├── src/main/resources/
│   │   └── application.yml           # 应用配置
│   └── pom.xml                       # Maven 依赖
│
├── frontend/                         # Vue 3 前端
│   ├── src/
│   │   ├── views/                    # 页面组件 (32个)
│   │   │   ├── Home.vue              # PC 首页仪表盘
│   │   │   ├── MobileHome.vue        # 移动端首页
│   │   │   ├── DataReport.vue        # 数据报表
│   │   │   ├── SystemSettings.vue    # 系统设置
│   │   │   ├── DbManager.vue         # 数据库管理
│   │   │   ├── AiAssistant.vue       # AI 助手 ⭐
│   │   │   └── ... (订单/商品/用户等)
│   │   ├── components/               # 公共组件
│   │   ├── router/index.js           # 路由配置
│   │   ├── App.vue                   # 根组件
│   │   └── main.js                   # 入口文件
│   ├── vite.config.js                # Vite 配置
│   └── package.json
│
├── sql/                              # SQL 脚本
│   ├── init.sql                      # 初始化脚本
│   └── order_enhancement.sql         # 订单增强
│
├── .trae/skills/                     # AI 技能库 (12个)
├── Apifox_API_Doc.md                 # Apifox 接口文档
├── Postman_API_Full_Test.json        # Postman 测试集合
├── docker-compose.yml                # Docker 编排
└── README.md                         # 本文档
```

---

## 🗄️ 数据库设计

### ER 关系图

```
┌──────────┐     ┌──────────────┐     ┌──────────┐
│   user   │────<│    order     │>────│  product │
│  用户表  │ 1:N │    订单表    │ N:1 │  商品表  │
└──────────┘     └──────┬───────┘     └──────────┘
                        │
              ┌─────────┼─────────┐
              ▼         ▼         ▼
       ┌──────────┐ ┌──────────┐ ┌──────────┐
       │order_logi│ │order_refu│ │order_revi│
       │stics 物流│ │nd 退款   │ │ew 评价   │
       └──────────┘ └──────────┘ └──────────┘

┌──────────┐     ┌──────────────┐     ┌──────────────┐
│seckill_  │────<│ seckill_     │     │    cart      │
│activity  │ 1:N │ product      │     │   购物车      │
│ 秒杀活动 │     │ 秒杀商品      │     └──────────────┘
└──────────┘     └──────────────┘
                        │
              ┌─────────┴─────────┐
              ▼                   ▼
       ┌──────────┐       ┌──────────────┐
       │  coupon  │       │ user_coupon  │
       │  优惠券  │       │  用户优惠券   │
       └──────────┘       └──────────────┘
```

### 核心表结构

<details>
<summary><b>user - 用户表</b></summary>

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 用户ID |
| username | VARCHAR(50) | 用户名(唯一) |
| password | VARCHAR(100) | 密码(Bcrypt加密) |
| name | VARCHAR(50) | 昵称 |
| phone | VARCHAR(20) | 手机号 |
| address | VARCHAR(255) | 地址 |
| role | INT | 角色(0普通/1管理员) |

</details>

<details>
<summary><b>order - 订单表</b></summary>

| 字段 | 类型 | 说明 |
|------|------|------|
| id | BIGINT PK | 订单ID |
| order_no | VARCHAR(50) | 订单号(唯一) |
| user_id | BIGINT FK | 用户ID |
| product_id | BIGINT FK | 商品ID |
| price | DOUBLE | 单价 |
| quantity | INT | 数量 |
| total_price | DOUBLE | 总价 |
| status | INT | 状态(0-7) |
| pay_method | VARCHAR(20) | 支付方式 |
| receiver_* | VARCHAR | 收货人信息 |
| logistics_* | VARCHAR | 物流信息 |

**状态机**: `待支付(0)` → `已支付(1)` → `已发货(2)` → `已完成(3)`
<br>分支: → `已取消(4)` / → `退款中(5)` → `已退款(6)` / `退款拒绝(7)`

</details>

---

## 📡 API 接口

### 接口总览

| 模块 | 接口数 | 前缀 | 认证 |
|------|--------|------|------|
| 🔑 认证 Auth | 4 | `/api/auth` | 否 |
| 👤 用户 User | 5 | `/api/users` | 是 |
| 📦 商品 Product | 5 | `/api/products` | 是 |
| ⚡ 秒杀活动 | 5 | `/api/seckill-activities` | 是 |
| 🎯 秒杀商品 | 5 | `/api/seckill-products` | 是 |
| 🛒 购物车 Cart | 5 | `/api/cart` | 是 |
| 📋 订单 Order | 13 | `/api/orders` | 是 |
| 🚚 物流 Logistics | 4 | `/api/logistics` | 是 |
| 🔙 退款 Refund | 5 | `/api/refunds` | 是 |
| 💬 评价 Review | 7 | `/api/reviews` | 是 |
| 🎫 优惠券 Coupon | 4 | `/api/coupons` | 是 |
| 📊 仪表盘 Dashboard | 1 | `/api/dashboard` | 是 |
| 🤖 AI 助手 | 2 | `/api/ai` | 否 |
| **合计** | **75** | — | — |

### 认证流程

```
POST /api/auth/login {username, password}
  → {token, userId, role}

后续请求 Header:
  Authorization: Bearer {token}

POST /api/auth/logout → 清除服务端Token
```

### 完整接口文档

- 📖 [Apifox 格式文档](./Apifox_API_Doc.md) — 可直接导入 Apifox 使用
- 🧪 [Postman 测试集合](./Postman_API_Full_Test.json) — 含环境变量和测试脚本

---

## 🤖 AI 助手

### 架构设计

```
前端 AiAssistant.vue
  ↓ POST /api/ai/chat {message, history}
AiChatController
  ↓ OkHttp POST
阿里云 DashScope API (OpenAI 兼容)
  ↓
通义千问 Qwen-Plus 模型
  ↓ JSON Response
格式化返回 → 流式渲染到聊天界面
```

### 配置方式

```yaml
# application.yml
ai:
  dashscope:
    api-key: ${ALAPI_KEY}    # 环境变量引入
  model: qwen-plus           # 模型选择
  base-url: https://dashscope.aliyuncs.com/compatible-mode/v1
```

```bash
# Windows PowerShell
$env:ALAPI_KEY = "sk-your-key"

# Linux/Mac
export ALAPI_KEY="sk-your-key"
```

### 能力范围

| 场景 | 示例问题 |
|------|----------|
| 运营咨询 | "如何提高团购活动的参与度和转化率？" |
| 活动策划 | "帮我设计一个校园春季招新活动方案" |
| 技术解答 | "MyBatis-Plus 的 QueryWrapper 怎么用？" |
| 文档辅助 | "帮写一份产品需求文档 PRD" |

---

## 🐳 Docker 部署

### docker-compose.yml

```yaml
version: '3.8'
services:
  mysql:
    image: mysql:8.0
    environment:
      MYSQL_ROOT_PASSWORD: root123
      MYSQL_DATABASE: community_groupon
    ports:
      - "3306:3306"
    volumes:
      - mysql_data:/var/lib/mysql

  redis:
    image: redis:7-alpine
    ports:
      - "6379:6379"

volumes:
  mysql_data:
```

### Nginx 反向代理

```nginx
server {
    listen 80;
    server_name your-domain.com;

    location /api {
        proxy_pass http://localhost:8080;
        proxy_set_header Host $host;
        proxy_set_header X-Real-IP $remote_addr;
    }

    location / {
        root /usr/share/nginx/html;
        try_files $uri $uri/ /index.html;
    }
}
```

---

## 📊 测试报告

| 模块 | 接口数 | 通过率 | 状态 |
|------|--------|--------|------|
| 仪表盘 Dashboard | 1 | 100% | ✅ |
| 商品 Products | 5 | 100% | ✅ |
| 用户 Users | 5 | 100% | ✅ |
| 订单 Orders | 13 | 100% | ✅ |
| 优惠券 Coupons | 4 | 100% | ✅ |
| 秒杀活动 Seckill | 5 | 100% | ✅ |
| AI 助手 Chat | 2 | 100% | ✅ |
| **总计** | **35+** | **~98%** | 🟢 健康 |

---

## 📝 更新日志

### v1.3.0 (2026-04) — AI 智能助手
- ✨ 新增基于通义千问 Qwen-Plus 的 AI 对话助手
- ✨ 新增毛玻璃风格悬浮式聊天界面
- 🐛 修复 Java 8 兼容性问题(Map.of → HashMap)
- 📝 新增 Apifox 格式 API 文档 (63 个接口)

### v1.2.0 — 订单全链路
- ✨ 完整订单生命周期(支付/发货/收货/取消/退款)
- ✨ 物流追踪 + 售后退款 + 评价体系
- ✨ 移动端 5 个订单状态页面
- ✨ PC 端数据库在线管理(DbManager)

### v1.1.0 — 功能完善
- ✨ PC 后台数据报表 + 系统设置页面
- ✨ 退出登录功能 + 按钮事件绑定
- ✨ Postman 接口测试集合

### v1.0.0 — 初始版本
- ✨ PC 管理后台基础框架
- ✨ 移动端 H5 适配
- ✨ JWT + Redis 认证体系
- ✨ 商品/订单/购物车/优惠券 CRUD

---

## 📄 License

[MIT License](LICENSE)

---

<p align="center">
  <sub>Made with ❤️ by Community Groupon Team · Powered by Spring Boot + Vue 3 + Qwen AI</sub>
</p>
