---
name: "dev-builder"
description: "全栈开发辅助技能，快速编写代码、调试BUG、代码审查、重构优化、架构设计。当用户需要写新功能代码、修复Bug、代码review、性能优化、技术选型、项目脚手架搭建等开发任务时调用此技能。"
---

# ⚡ Dev Builder - 全栈开发加速器

## 技能概述

一站式全栈开发辅助工具，覆盖前端/后端/数据库/DevOps全链路开发需求，让编码效率提升 10 倍。

## 核心能力地图

### 🚀 快速开发 (Code Generation)

#### 前端开发
| 任务 | 能力 |
|------|------|
| **页面开发** | Vue/React/Angular 组件生成，含样式/响应式/交互 |
| **状态管理** | Redux/Pinia/MobX store 设计与实现 |
| **路由配置** | SPA 路由设计，含守卫/懒加载/嵌套路由 |
| **API 对接** | Axios 封装/请求拦截/错误处理/缓存策略 |
| **表单处理** | 动态表单/校验规则/联动逻辑/提交处理 |
| **图表可视化** | ECharts/D3.js/Chart.js 图表配置生成 |
| **动画效果** | CSS Animation/Framer Motion/GSAP 动效代码 |

#### 后端开发
| 任务 | 能力 |
|------|------|
| **CRUD 接口** | Controller/Service/Repository 全链路生成 |
| **数据库操作** | SQL/ORM 查询构建，含索引优化建议 |
| **认证授权** | JWT/OAuth2/Session 认证体系实现 |
| **中间件** | 日志/限流/鉴权/CORS 中间件编写 |
| **定时任务** | Quartz/Schedule/Bull 任务调度代码 |
| **消息队列** | Kafka/RabbitMQ/Redis 生产消费代码 |
| **文件上传** | 本传/OSS/分片上传/缩略图生成 |

#### 数据库
| 任务 | 能力 |
|------|------|
| **建表语句** | MySQL/PostgreSQL/MongoDB DDL 生成 |
| **数据迁移** | Flyway/Liquibase/Flyway 版本化迁移脚本 |
| **查询优化** | EXPLAIN 分析/慢SQL优化/索引建议 |
| **种子数据** | 测试数据批量插入脚本 |
| **ER图生成** | Mermaid/PlantUML 格式的实体关系图 |

---

### 🐛 BUG 修复 (Debugging)

#### 系统化排错流程
```
Step 1: 复现确认
  → 收集错误日志/堆栈/复现步骤/环境信息
  
Step 2: 定位根因
  → 代码静态分析 + 日志追踪 + 断点排查
  → 区分: 语法错误/逻辑错误/并发问题/环境问题/第三方依赖

Step 3: 制定修复方案
  → 最小改动原则 (只修bug不引入新功能)
  → 向后兼容考虑
  → 回归影响范围评估

Step 4: 编写修复代码
  → 修复本体 + 补充防御性编程
  → 添加单元测试覆盖该bug场景

Step 5: 验证确认
  → 本地验证通过 → 提供验证步骤给用户
```

#### 常见 Bug 模式库
| 类型 | 典型案例 | 修复策略 |
|------|----------|----------|
| **NPE空指针** | 未判空调用对象方法 | Optional/前置判空/默认值 |
| **异步竞态** | 并发更新同一数据 | 乐观锁/分布式锁/事务隔离 |
| **内存泄漏** | 事件监听未移除/闭包引用 | 及时清理/WeakRef/生命周期绑定 |
| **跨域问题** | 前后端端口不同 | CORS配置/Nginx代理 |
| **时区问题** | 时间存储显示不一致 | 统一UTC存储/前端本地化显示 |
| **编码乱码** | 中文显示问号 | 统一UTF-8/设置charset |
| **依赖冲突** | npm/maven 版本不兼容 | lock file/resolution strategy |

---

### 🔍 代码审查 (Code Review)

#### Review 检查清单

##### 正确性 (Correctness)
- [ ] 逻辑是否正确，边界条件是否覆盖
- [ ] 是否有空指针/越界/类型错误风险
- [ ] 异常处理是否完善，不会吞掉异常
- [ ] 并发安全性（如有共享状态）

##### 性能 (Performance)
- [ ] 是否有不必要的循环/递归/深拷贝
- [ ] 数据库查询是否有 N+1 问题
- [ ] 大数据处理是否分页/流式处理
- [ ] 缓存使用是否合理

##### 安全性 (Security)
- [ ] SQL注入防护（参数化查询）
- [ ] XSS防护（输入过滤/输出编码）
- [ ] 敏感数据是否脱敏/加密存储
- [ ] 权限校验是否完备
- [ ] 依赖包是否存在已知漏洞

##### 可维护性 (Maintainability)
- [ ] 命名是否语义清晰
- [ ] 函数/方法长度适中（<50行）
- [ ] 是否有必要的注释（复杂逻辑）
- [ ] 是否遵循项目既有风格
- [ ] 魔法数字是否提取为常量

---

### 🏗️ 架构设计 (Architecture)

#### 项目脚手架生成
```
支持的技术栈组合:
├── Frontend: Vue3 + Vite + TS + Pinia + ElementPlus
├── Backend:  SpringBoot + MyBatisPlus + Redis + JWT
├── Mobile:   ReactNative / Flutter / UniApp
├── DB:       MySQL 8.0 + Redis 7.0
└── DevOps:   Docker + Nginx + CI/CD Pipeline

输出: 完整项目目录结构 + 配置文件 + README
```

#### API 设计规范
```
RESTful API 设计原则:
├── URL 名词复数: /api/users /api/orders
├── HTTP 方法语义: GET(查)/POST(增)/PUT(改)/DELETE(删)
├── 状态码规范: 200(成功)/400(参数错)/401(未认证)/403(无权)/500(服务错)
├── 分页约定: ?page=1&size=20&sort=createdAt,desc
├── 响应格式: { code, message, data }
└── 版本控制: /api/v1/...
```

## 使用示例

### 示例1：快速生成 CRUD
```
用户: 帮我写一个完整的文章管理模块
→ 自动生成:
  backend:
    - Article.java (Entity)
    - ArticleMapper.java (Repository)
    - ArticleService.java (Service层)
    - ArticleController.java (REST API)
  frontend:
    - ArticleList.vue (列表页+搜索+分页)
    - ArticleForm.vue (新增/编辑表单)
    - articleApi.js (API封装)
    - router配置
  database:
    - CREATE TABLE article (...);
    - 索引建议
```

### 示例2：Debug 排查
```
用户: 我的接口返回500错误，这是日志
→ 分析过程:
  1. 定位异常堆栈: NullPointerException at OrderService.java:45
  2. 追踪代码: order.getUser().getName() → user 为 null
  3. 根因: 外键查询未加 LEFT JOIN，订单无用户时NPE
  4. 修复: 
     - 短期: Optional.ofNullable(user).map(User::getName).orElse("未知")
     - 长期: SQL改为LEFT JOIN + 代码判空
  5. 补充: 单元测试覆盖 user=null 场景
```

### 示例3：代码优化
```
用户: 这段代码太慢了帮我优化
→ 分析:
  原始: 循环内每次查数据库 → O(n)次查询
  优化: 批量查询 + 内存匹配 → O(1)+O(1)
  提升: 从 5s → 50ms (100倍提升)
  附带: 添加Redis缓存层 + 缓存失效策略
```

## 开发最佳实践

- ✅ 先理解需求再写代码，不确定就先问清楚
- ✅ 写代码前先看项目既有风格，保持一致
- ✅ 每个功能点配套单元测试
- ✅ Commit message 遵循 Conventional Commits 规范
- ✅ 敏感信息(API Key/密码)绝不硬编码
- ✅ 大改动先沟通方案，小改动直接上手
- ❌ 不随意修改不相关代码
- ❌ 不引入项目未使用的依赖
- ❌ 不写注释解释显而易见的代码
