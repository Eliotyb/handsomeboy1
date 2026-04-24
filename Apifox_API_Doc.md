# 社团团购秒杀后台管理系统 - API 接口文档

> **Base URL**: `http://localhost:8080`
> **认证方式**: Bearer Token (JWT + Redis)
> **Content-Type**: `application/json`

---

## 目录

- [一、认证模块 (Auth)](#一认证模块-auth)
- [二、用户模块 (User)](#二用户模块-user)
- [三、商品模块 (Product)](#三商品模块-product)
- [四、秒杀活动模块 (SeckillActivity)](#四秒杀活动模块-seckillactivity)
- [五、秒杀商品模块 (SeckillProduct)](#五秒杀商品模块-seckillproduct)
- [六、购物车模块 (Cart)](#六购物车模块-cart)
- [七、订单模块 (Order)](#七订单模块-order)
- [八、物流模块 (Logistics)](#八物流模块-logistics)
- [九、售后退款模块 (Refund)](#九售后退款模块-refund)
- [十、评价模块 (Review)](#十评价模块-review)
- [十一、优惠券模块 (Coupon)](#十一优惠券模块-coupon)
- [十二、仪表盘模块 (Dashboard)](#十二仪表盘模块-dashboard)

---

## 全局响应结构

所有接口统一返回格式：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {}
}
```

| 字段 | 类型 | 说明 |
|------|------|------|
| code | integer | 状态码，200=成功，500=失败 |
| message | string | 提示信息 |
| data | object/array/null | 返回数据 |

**请求头（需认证接口必填）**：

| Header名 | 值 | 说明 |
|----------|-----|------|
| Authorization | Bearer {token} | JWT Token |

---

## 一、认证模块 (Auth)

### 1.1 用户登录

**接口地址**：`POST /api/auth/login`
**Content-Type**：`application/json`
**是否需要认证**：否

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名 |
| password | string | 是 | 密码 |

**请求示例**：

```json
{
  "username": "zhangsan",
  "password": "123456"
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| code | integer | 状态码 |
| message | string | 提示信息 |
| data.token | string | JWT Token（后续请求放入 Authorization Header） |
| data.userId | long | 用户ID |
| data.username | string | 用户名 |
| data.name | string | 昵称 |
| data.role | integer | 角色(0=普通用户,1=管理员) |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 2,
    "username": "zhangsan",
    "name": "张三",
    "role": 0
  }
}
```

---

### 1.2 管理员登录

**接口地址**：`POST /api/auth/admin/login`
**Content-Type**：`application/json`
**是否需要认证**：否

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 管理员用户名 |
| password | string | 是 | 密码 |

**请求示例**：

```json
{
  "username": "admin",
  "password": "admin"
}
```

**响应参数**：同 1.1 用户登录

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 1,
    "username": "admin",
    "name": "管理员",
    "role": 1
  }
}
```

---

### 1.3 用户注册

**接口地址**：`POST /api/auth/register`
**Content-Type**：`application/json`
**是否需要认证**：否

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名(唯一) |
| password | string | 是 | 密码 |
| name | string | 否 | 昵称 |
| phone | string | 否 | 手机号 |
| address | string | 否 | 地址 |

**请求示例**：

```json
{
  "username": "newuser",
  "password": "123456",
  "name": "新用户",
  "phone": "13800138000"
}
```

**响应参数**：同 1.1 登录（注册成功后自动返回 token）

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "token": "eyJhbGciOiJIUzI1NiJ9...",
    "userId": 10,
    "username": "newuser",
    "name": "新用户",
    "role": 0
  }
}
```

---

### 1.4 退出登录

**接口地址**：`POST /api/auth/logout`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求头**：

| Header名 | 值 | 说明 |
|----------|-----|------|
| Authorization | Bearer {token} | JWT Token |

**请求参数**：无

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 二、用户模块 (User)

### 2.1 获取用户列表

**接口地址**：`GET /api/users`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 用户ID |
| data[].username | string | 用户名 |
| data[].name | string | 昵称 |
| data[].phone | string | 手机号 |
| data[].address | string | 地址 |
| data[].role | integer | 角色(0=普通用户,1=管理员) |
| data[].createTime | datetime | 创建时间 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "username": "admin",
      "name": "管理员",
      "phone": "13800000001",
      "address": "",
      "role": 1,
      "createTime": "2025-01-01 00:00:00",
      "updateTime": "2025-01-01 00:00:00"
    },
    {
      "id": 2,
      "username": "zhangsan",
      "name": "张三",
      "phone": "13800000002",
      "role": 0,
      "createTime": "2025-01-01 00:00:00",
      "updateTime": "2025-01-01 00:00:00"
    }
  ]
}
```

---

### 2.2 获取用户详情

**接口地址**：`GET /api/users/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long(long) | 是 | 用户ID |

**响应参数**：同 2.1 单个对象

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 2,
    "username": "zhangsan",
    "name": "张三",
    "phone": "13800000002",
    "address": "北京市海淀区",
    "role": 0,
    "createTime": "2025-01-01 00:00:00",
    "updateTime": "2025-01-01 00:00:00"
  }
}
```

---

### 2.3 创建用户

**接口地址**：`POST /api/users`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 是 | 用户名(唯一) |
| password | string | 是 | 密码（自动加密） |
| name | string | 否 | 昵称 |
| phone | string | 否 | 手机号 |
| address | string | 否 | 地址 |
| role | integer | 否 | 角色(默认0) |

**请求示例**：

```json
{
  "username": "lisi",
  "password": "123456",
  "name": "李四",
  "phone": "13900139000",
  "address": "上海市浦东新区",
  "role": 0
}
```

**响应参数**：同 2.2

---

### 2.4 更新用户

**接口地址**：`PUT /api/users/{id}`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| username | string | 否 | 用户名 |
| password | string | 否 | 密码（留空不修改） |
| name | string | 否 | 昵称 |
| phone | string | 否 | 手机号 |
| address | string | 否 | 地址 |
| role | integer | 否 | 角色 |

**请求示例**：

```json
{
  "name": "李四(已更新)",
  "phone": "13900139001"
}
```

**响应参数**：同 2.2

---

### 2.5 删除用户

**接口地址**：`DELETE /api/users/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 用户ID |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 三、商品模块 (Product)

### 3.1 获取商品列表

**接口地址**：`GET /api/products`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 商品ID |
| data[].name | string | 商品名称 |
| data[].description | string | 商品描述 |
| data[].price | double | 原价 |
| data[].stock | integer | 库存数量 |
| data[].image | string | 图片URL |
| data[].status | integer | 状态(0=下架,1=上架) |
| data[].category | string | 分类名称 |
| data[].createTime | datetime | 创建时间 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "社团定制T恤",
      " description ": "纯棉舒适,多色可选",
      "price": 59.9,
      "stock": 100,
      "image": "/images/tshirt.jpg",
      "status": 1,
      "category": "服装配饰",
      "createTime": "2025-01-15 10:00:00",
      "updateTime": "2025-01-15 10:00:00"
    }
  ]
}
```

---

### 3.2 获取商品详情

**接口地址**：`GET /api/products/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 商品ID |

**响应参数**：同 3.1 单个对象

---

### 3.3 新增商品

**接口地址**：`POST /api/products`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 是 | 商品名称 |
| description | string | 否 | 商品描述 |
| price | double | 是 | 价格 |
| stock | integer | 是 | 库存 |
| image | string | 否 | 图片URL |
| status | integer | 否 | 状态(默认1上架) |
| category | string | 否 | 分类 |

**请求示例**：

```json
{
  "name": "社团徽章套装",
  "description": "精美金属徽章，限量发售",
  "price": 29.9,
  "stock": 50,
  "image": "/images/badge.jpg",
  "status": 1,
  "category": "文创周边"
}
```

**响应参数**：同 3.2

---

### 3.4 更新商品

**接口地址**：`PUT /api/products/{id}`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 商品ID |

**请求参数**：同 3.3（全部可选）

**请求示例**：

```json
{
  "name": "社团徽章套装(升级版)",
  "price": 39.9,
  "stock": 30
}
```

**响应参数**：同 3.2

---

### 3.5 删除商品

**接口地址**：`DELETE /api/products/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 商品ID |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 四、秒杀活动模块 (SeckillActivity)

### 4.1 获取活动列表

**接口地址**：`GET /api/seckill-activities`
**Content-Type**：无
**是否需要认证**：是

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 活动ID |
| data[].name | string | 活动名称 |
| data[].startTime | datetime | 开始时间 |
| data[].endTime | datetime | 结束时间 |
| data[].status | integer | 状态 |
| data[].totalStock | integer | 总库存（关联计算） |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "春季开学季秒杀",
      "startTime": "2025-03-01 00:00:00",
      "endTime": "2025-03-07 23:59:59",
      "status": 1,
      "totalStock": 200
    }
  ]
}
```

---

### 4.2 获取活动详情

**接口地址**：`GET /api/seckill-activities/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 活动ID |

**响应参数**：SeckillActivity 完整字段

---

### 4.3 创建活动

**接口地址**：`POST /api/seckill-activities`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| name | string | 是 | 活动名称 |
| startTime | datetime | 是 | 开始时间 |
| endTime | datetime | 是 | 结束时间 |
| status | integer | 否 | 状态(默认1) |

**请求示例**：

```json
{
  "name": "五一劳动节特惠",
  "startTime": "2025-05-01 00:00:00",
  "endTime": "2025-05-03 23:59:59",
  "status": 1
}
```

---

### 4.4 更新活动

**接口地址**：`PUT /api/seckill-activities/{id}`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 活动ID |

**请求参数**：同 4.3

---

### 4.5 删除活动

**接口地址**：`DELETE /api/seckill-activities/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 活动ID |

---

## 五、秒杀商品模块 (SeckillProduct)

### 5.1 获取秒杀商品列表

**接口地址**：`GET /api/seckill-products`
**Content-Type**：无
**是否需要认证**：是

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 秒杀商品ID |
| data[].activityId | long | 关联活动ID |
| data[].productId | long | 关联商品ID |
| data[].seckillPrice | double | 秒杀价格 |
| data[].seckillStock | integer | 秒杀库存 |
| data[].limitPerUser | integer | 每人限购数 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "activityId": 1,
      "productId": 1,
      "seckillPrice": 19.9,
      "seckillStock": 100,
      "limitPerUser": 2
    }
  ]
}
```

---

### 5.2 获取秒杀商品详情

**接口地址**：`GET /api/seckill-products/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 秒杀商品ID |

---

### 5.3 添加秒杀商品

**接口地址**：`POST /api/seckill-products`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| activityId | long | 是 | 活动ID |
| productId | long | 是 | 商品ID |
| seckillPrice | double | 是 | 秒杀价格 |
| seckillStock | integer | 是 | 秒杀库存 |
| limitPerUser | integer | 否 | 每人限购(默认1) |

**请求示例**：

```json
{
  "activityId": 1,
  "productId": 5,
  "seckillPrice": 9.9,
  "seckillStock": 50,
  "limitPerUser": 1
}
```

---

### 5.4 更新秒杀商品

**接口地址**：`PUT /api/seckill-products/{id}`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 秒杀商品ID |

**请求参数**：同 5.3

---

### 5.5 删除秒杀商品

**接口地址**：`DELETE /api/seckill-products/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 秒杀商品ID |

---

## 六、购物车模块 (Cart)

### 6.1 获取购物车

**接口地址**：`GET /api/cart`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 购物车项ID |
| data[].productId | long | 商品ID |
| data[].productName | string | 商品名称 |
| data[].productPrice | double | 商品价格 |
| data[].quantity | integer | 数量 |
| data[].productImage | string | 商品图片 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "productId": 1,
      "productName": "社团定制T恤",
      "productPrice": 59.9,
      "quantity": 2,
      "productImage": "/images/tshirt.jpg"
    }
  ]
}
```

---

### 6.2 加入购物车

**接口地址**：`POST /api/cart`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | long(integer) | 是 | 商品ID |
| quantity | integer | 是 | 数量 |

**请求示例**：

```json
{
  "productId": 1,
  "quantity": 2
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.id | long | 购物车项ID |
| data.userId | long | 用户ID |
| data.productId | long | 商品ID |
| data.quantity | integer | 数量 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 5,
    "userId": 2,
    "productId": 1,
    "quantity": 2
  }
}
```

---

### 6.3 修改购物车数量

**接口地址**：`PUT /api/cart/{id}`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 购物车项ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| quantity | integer | 是 | 新的数量 |

**请求示例**：

```json
{
  "quantity": 5
}
```

---

### 6.4 删除购物车项

**接口地址**：`DELETE /api/cart/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 购物车项ID |

---

### 6.5 清空购物车

**接口地址**：`DELETE /api/cart/clear`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 七、订单模块 (Order)

### 7.1 从购物车创建订单

**接口地址**：`POST /api/orders/create`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| receiverName | string | 是 | 收货人姓名 |
| receiverPhone | string | 是 | 收货人电话 |
| receiverAddress | string | 是 | 收货地址 |
| payMethod | string | 否 | 支付方式(wechat/alipay/balance) |

**请求示例**：

```json
{
  "receiverName": "张三",
  "receiverPhone": "13800000002",
  "receiverAddress": "北京市海淀区XX路XX号",
  "payMethod": "wechat"
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.orderNo | string | 订单号 |
| data.totalAmount | double | 总金额 |
| data.totalCount | integer | 总件数 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "orderNo": "ORD-20260414143000123-2",
    "totalAmount": 119.8,
    "totalCount": 2
  }
}
```

---

### 7.2 秒杀下单

**接口地址**：`POST /api/orders/seckill`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | long(integer) | 是 | 商品ID |
| price | double | 是 | 秒杀价格 |
| quantity | integer | 否 | 数量(默认1) |
| receiverName | string | 否 | 收货人姓名 |
| receiverPhone | string | 否 | 收货人电话 |
| receiverAddress | string | 否 | 收货地址 |
| payMethod | string | 否 | 支付方式 |

**请求示例**：

```json
{
  "productId": 1,
  "price": 19.9,
  "quantity": 1,
  "receiverName": "张三",
  "receiverPhone": "13800000002",
  "receiverAddress": "北京市海淀区XX路XX号",
  "payMethod": "alipay"
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.orderNo | string | 秒杀订单号(前缀SEC-) |
| data.totalAmount | double | 总金额 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "orderNo": "SEC-20260414143000234-2",
    "totalAmount": 19.9
  }
}
```

---

### 7.3 获取所有订单

**接口地址**：`GET /api/orders`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 订单ID |
| data[].orderNo | string | 订单号 |
| data[].userId | long | 用户ID |
| data[].productId | long | 商品ID |
| data[].price | double | 单价 |
| data[].quantity | integer | 数量 |
| data[].totalPrice | double | 总价 |
| data[].status | integer | 状态 |
| data[].payMethod | string | 支付方式 |
| data[].receiverName | string | 收货人 |
| data[].receiverPhone | string | 收货电话 |
| data[].receiverAddress | string | 收货地址 |
| data[].logisticsCompany | string | 物流公司 |
| data[].logisticsNo | string | 物流单号 |
| data[].createTime | datetime | 创建时间 |
| data[].payTime | datetime | 支付时间 |
| data[].shipTime | datetime | 发货时间 |
| data[].receiveTime | datetime | 收货时间 |
| data[].cancelReason | string | 取消原因 |

**订单状态说明**：

| status值 | 含义 |
|----------|------|
| 0 | 待支付 |
| 1 | 已支付/待发货 |
| 2 | 已发货/待收货 |
| 3 | 已完成 |
| 4 | 已取消 |
| 5 | 退款中 |
| 6 | 已退款 |
| 7 | 退款拒绝 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "orderNo": "ORD-20260414000001000-2",
      "userId": 2,
      "productId": 1,
      "price": 59.9,
      "quantity": 2,
      "totalPrice": 119.8,
      "status": 1,
      "payMethod": "wechat",
      "receiverName": "张三",
      "receiverPhone": "13800000002",
      "receiverAddress": "北京市海淀区XX路XX号",
      "logisticsCompany": null,
      "logisticsNo": null,
      "createTime": "2026-04-14 14:30:00",
      "payTime": "2026-04-14 14:31:00",
      "shipTime": null,
      "receiveTime": null,
      "cancelReason": null
    }
  ]
}
```

---

### 7.4 获取订单详情

**接口地址**：`GET /api/orders/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 订单ID |

**响应参数**：同 7.3 单个对象

---

### 7.5 获取我的订单

**接口地址**：`GET /api/orders/user/my-orders`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无（从Token中获取用户ID）

**响应参数**：同 7.3

---

### 7.6 按状态查询订单

**接口地址**：`GET /api/orders/user/status/{status}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | integer | 是 | 订单状态(0-7) |

**响应参数**：同 7.3

---

### 7.7 确认付款

**接口地址**：`PUT /api/orders/{id}/pay`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 订单ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| payMethod | string | 否 | 支付方式(wechat/alipay/balance) |

**请求示例**：

```json
{
  "payMethod": "alipay"
}
```

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

> ⚠️ 注意：仅 status=0（待支付）的订单可执行此操作，执行后 status 变为 1

---

### 7.8 发货

**接口地址**：`PUT /api/orders/{id}/ship`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 订单ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| logisticsCompany | string | 是 | 物流公司(如:顺丰快递/中通快递/圆通快递) |
| logisticsNo | string | 是 | 物流单号 |

**请求示例**：

```json
{
  "logisticsCompany": "顺丰速运",
  "logisticsNo": "SF1234567890123"
}
```

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

> ⚠️ 注意：仅 status=1（待发货）的订单可执行此操作，执行后 status 变为 2

---

### 7.9 确认收货

**接口地址**：`PUT /api/orders/{id}/receive`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 订单ID |

**请求参数**：无

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

> ⚠️ 注意：仅 status=2（待收货）的订单可执行此操作，执行后 status 变为 3

---

### 7.10 取消订单

**接口地址**：`PUT /api/orders/{id}/cancel`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 订单ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| reason | string | 否 | 取消原因(默认"用户取消") |

**请求示例**：

```json
{
  "reason": "不想要了"
}
```

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

> ⚠️ 注意：仅 status≤1 的订单可取消，执行后 status 变为 4

---

### 7.11 更新订单

**接口地址**：`PUT /api/orders/{id}`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 订单ID |

**请求参数**：Order 对象全字段（按需传参）

**请求示例**：

```json
{
  "receiverName": "李四",
  "receiverPhone": "13900139000",
  "receiverAddress": "上海市浦东新区XX路XX号"
}
```

**响应参数**：同 7.4

---

### 7.12 删除订单

**接口地址**：`DELETE /api/orders/{id}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 订单ID |

---

### 7.13 订单统计

**接口地址**：`GET /api/orders/stats`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无（从Token获取当前用户）

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.pendingPayment | integer | 待付款数量 |
| data.pendingShipment | integer | 待发货数量 |
| data.pendingReceive | integer | 待收货数量 |
| data.pendingReview | integer | 待评价数量 |
| data.refund | integer | 退款中/已退款数量 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "pendingPayment": 2,
    "pendingShipment": 1,
    "pendingReceive": 3,
    "pendingReview": 5,
    "refund": 1
  }
}
```

---

## 八、物流模块 (Logistics)

### 8.1 获取订单物流记录

**接口地址**：`GET /api/logistics/order/{orderId}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | long | 是 | 订单ID |

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 物流记录ID |
| data[].orderId | long | 订单ID |
| data[].logisticsCompany | string | 物流公司 |
| data[].logisticsNo | string | 物流单号 |
| data[].status | integer | 物流状态 |
| data[].currentLocation | string | 当前位置 |
| data[].remark | string | 备注 |
| data[].createTime | datetime | 创建时间 |

**物流状态说明**：

| status值 | 含义 |
|----------|------|
| 0 | 待揽收 |
| 1 | 已揽收 |
| 2 | 运输中 |
| 3 | 派送中 |
| 4 | 已签收 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "orderId": 1,
      "logisticsCompany": "顺丰速运",
      "logisticsNo": "SF1234567890123",
      "status": 2,
      "currentLocation": "北京转运中心",
      "remark": "包裹运输中",
      "createTime": "2026-04-15 09:00:00"
    }
  ]
}
```

---

### 8.2 获取最新物流状态

**接口地址**：`GET /api/logistics/order/{orderId}/latest`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | long | 是 | 订单ID |

**响应参数**：同 8.1 单条记录

---

### 8.3 创建物流记录

**接口地址**：`POST /api/logistics`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | long | 是 | 订单ID |
| logisticsCompany | string | 是 | 物流公司 |
| logisticsNo | string | 是 | 物流单号 |
| status | integer | 否 | 状态(默认0) |
| currentLocation | string | 否 | 当前位置 |
| remark | string | 否 | 备注 |

**请求示例**：

```json
{
  "orderId": 1,
  "logisticsCompany": "顺丰速运",
  "logisticsNo": "SF1234567890123",
  "status": 0,
  "currentLocation": "商家仓库",
  "remark": "等待揽收"
}
```

**响应参数**：同 8.1 单条记录

---

### 8.4 更新物流状态

**接口地址**：`PUT /api/logistics/{id}/status`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 物流记录ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | integer | 是 | 新的状态(0-4) |
| currentLocation | string | 否 | 当前位置更新 |

**请求示例**：

```json
{
  "status": 2,
  "currentLocation": "北京转运中心"
}
```

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 九、售后退款模块 (Refund)

### 9.1 申请退款

**接口地址**：`POST /api/refunds/apply`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | long(integer) | 是 | 订单ID |
| refundType | integer | 否 | 退款类型(1=仅退款,2=退货退款,默认1) |
| reason | string | 否 | 申请原因 |
| description | string | 否 | 详细描述 |
| amount | double | 是 | 退款金额 |

**请求示例**：

```json
{
  "orderId": 1,
  "refundType": 1,
  "reason": "商品有质量问题",
  "description": "收到后发现T恤有破洞",
  "amount": 59.9
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.id | long | 退款单ID |
| data.refundNo | string | 退款单号(前缀REF-) |
| data.orderId | long | 订单ID |
| data.refundType | integer | 退款类型 |
| data.reason | string | 原因 |
| data.refundAmount | double | 退款金额 |
| data.status | integer | 状态(0=待审核) |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "refundNo": "REF-20260415100000-2",
    "orderId": 1,
    "refundType": 1,
    "reason": "商品有质量问题",
    "description": "收到后发现T恤有破洞",
    "refundAmount": 59.9,
    "status": 0
  }
}
```

> ℹ️ 说明：申请退款成功后，对应订单 status 自动变为 5（退款中）

---

### 9.2 获取我的退款记录

**接口地址**：`GET /api/refunds/my`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无（从Token获取用户ID）

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 退款单ID |
| data[].refundNo | string | 退款单号 |
| data[].orderId | long | 订单ID |
| data[].refundType | integer | 退款类型(1仅退款/2退货退款) |
| data[].reason | string | 原因 |
| data[].refundAmount | double | 退款金额 |
| data[].status | integer | 状态 |
| data[].rejectReason | string | 拒绝原因 |
| data[].createTime | datetime | 申请时间 |

**退款状态说明**：

| status值 | 含义 |
|----------|------|
| 0 | 待审核 |
| 1 | 待寄回 |
| 2 | 处理中 |
| 3 | 成功(已退款) |
| 4 | 拒绝 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "refundNo": "REF-20260415100000-2",
      "orderId": 1,
      "refundType": 1,
      "reason": "商品有质量问题",
      "refundAmount": 59.9,
      "status": 0,
      "rejectReason": null,
      "createTime": "2026-04-15 10:00:00"
    }
  ]
}
```

---

### 9.3 获取全部退款列表（管理端）

**接口地址**：`GET /api/refunds/list`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：同 9.2

---

### 9.4 按状态筛选退款

**接口地址**：`GET /api/refunds/list/status/{status}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| status | integer | 是 | 退款状态(0-4) |

**响应参数**：同 9.2

---

### 9.5 审核退款

**接口地址**：`PUT /api/refunds/{id}/audit`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 退款单ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| auditUserId | long(integer) | 是 | 审核人用户ID |
| status | integer | 是 | 审核结果(3=通过/4=拒绝) |
| remark | string | 否 | 审核备注 |
| rejectReason | string | 否 | 拒绝原因(status=4时填写) |

**请求示例 - 通过**：

```json
{
  "auditUserId": 1,
  "status": 3,
  "remark": "审核通过，同意退款"
}
```

**请求示例 - 拒绝**：

```json
{
  "auditUserId": 1,
  "status": 4,
  "remark": "不符合退款条件",
  "rejectReason": "商品不影响使用，不支持退款"
}
```

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

> ℹ️ 说明：status=3（通过）时自动完成退款；status=4（拒绝）时记录拒绝原因

---

## 十、评价模块 (Review)

### 10.1 提交评价

**接口地址**：`POST /api/reviews`
**Content-Type**：`application/json`
**是否需要认证**：是

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | long(integer) | 是 | 订单ID |
| productId | long(integer) | 是 | 商品ID |
| rating | integer | 是 | 评分(1-5星) |
| content | string | 否 | 评价内容 |
| isAnonymous | integer | 否 | 是否匿名(0=否,1=是,默认0) |

**请求示例**：

```json
{
  "orderId": 1,
  "productId": 1,
  "rating": 5,
  "content": "质量非常好，穿着很舒服！推荐购买~",
  "isAnonymous": 0
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.id | long | 评价ID |
| data.orderId | long | 订单ID |
| data.productId | long | 商品ID |
| data.userId | long | 用户ID |
| data.rating | integer | 评分 |
| data.content | string | 内容 |
| data.isAnonymous | integer | 是否匿名 |
| data.status | integer | 状态(1=正常) |
| data.createTime | datetime | 创建时间 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "id": 1,
    "orderId": 1,
    "productId": 1,
    "userId": 2,
    "rating": 5,
    "content": "质量非常好，穿着很舒服！推荐购买~",
    "isAnonymous": 0,
    "status": 1,
    "createTime": "2026-04-16 11:00:00"
  }
}
```

---

### 10.2 获取商品评价列表

**接口地址**：`GET /api/reviews/product/{productId}`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | long | 是 | 商品ID |

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 评价ID |
| data[].orderId | long | 订单ID |
| data[].productId | long | 商品ID |
| data[].userId | long | 用户ID |
| data[].rating | integer | 评分(1-5) |
| data[].content | string | 评价内容 |
| data[].images | string | 评价图片 |
| data[].isAnonymous | integer | 是否匿名 |
| data[].replyContent | string | 商家回复 |
| data[].replyTime | datetime | 回复时间 |
| data[].createTime | datetime | 评价时间 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "orderId": 1,
      "productId": 1,
      "userId": 2,
      "rating": 5,
      "content": "质量非常好，穿着很舒服！",
      "images": null,
      "isAnonymous": 0,
      "replyContent": "感谢您的支持！",
      "replyTime": "2026-04-16 12:00:00",
      "createTime": "2026-04-16 11:00:00"
    }
  ]
}
```

---

### 10.3 获取商品平均评分

**接口地址**：`GET /api/reviews/product/{productId}/avg-rating`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| productId | long | 是 | 商品ID |

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.avgRating | double | 平均评分(1-5) |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "avgRating": 4.67
  }
}
```

---

### 10.4 获取我的评价

**接口地址**：`GET /api/reviews/my`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无（从Token获取用户ID）

**响应参数**：同 10.2

---

### 10.5 获取待评价订单

**接口地址**：`GET /api/reviews/pending/my`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无（从Token获取用户ID）

**响应参数**：同 10.2（仅返回已完成但未评价的订单）

---

### 10.6 获取全部评价列表（管理端）

**接口地址**：`GET /api/reviews/list`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：同 10.2

---

### 10.7 商家回复评价

**接口地址**：`PUT /api/reviews/{id}/reply`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| id | long | 是 | 评价ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| replyContent | string | 是 | 回复内容 |

**请求示例**：

```json
{
  "replyContent": "感谢您的认可！我们会继续努力提供优质产品和服务~"
}
```

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": null
}
```

---

## 十一、优惠券模块 (Coupon)

### 11.1 获取我的优惠券

**接口地址**：`GET /api/coupons/user`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无（从Token获取用户ID）

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data[].id | long | 优惠券ID |
| data[].name | string | 优惠券名称 |
| data[].type | integer | 类型 |
| data[].value | double | 面额/折扣 |
| data[].minAmount | double | 最低消费 |
| data[].startTime | datetime | 有效期开始 |
| data[].endTime | datetime | 有效期结束 |
| data[].status | integer | 状态(0=未使用,1=已使用,2=已过期) |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": [
    {
      "id": 1,
      "name": "新人专享券",
      "type": 1,
      "value": 10.0,
      "minAmount": 50.0,
      "startTime": "2025-01-01 00:00:00",
      "endTime": "2025-12-31 23:59:59",
      "status": 0
    }
  ]
}
```

---

### 11.2 获取可领取优惠券列表

**接口地址**：`GET /api/coupons/available`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：同 11.1（未领取的可用优惠券模板）

---

### 11.3 领取优惠券

**接口地址**：`POST /api/coupons/{couponId}/receive`
**Content-Type**：无
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| couponId | long | 是 | 优惠券ID |

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.message | string | 结果提示 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "message": "领取成功"
  }
}
```

---

### 11.4 使用优惠券

**接口地址**：`POST /api/coupons/{couponId}/use`
**Content-Type**：`application/json`
**是否需要认证**：是

**路径参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| couponId | long | 是 | 优惠券ID |

**请求参数**：

| 参数名 | 类型 | 必填 | 说明 |
|--------|------|------|------|
| orderId | long(integer) | 是 | 关联订单ID |

**请求示例**：

```json
{
  "orderId": 1
}
```

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.message | string | 结果提示 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "message": "使用成功"
  }
}
```

---

## 十二、仪表盘模块 (Dashboard)

### 12.1 获取仪表盘统计数据

**接口地址**：`GET /api/dashboard/stats`
**Content-Type**：无
**是否需要认证**：是

**请求参数**：无

**响应参数**：

| 参数名 | 类型 | 说明 |
|------|------|------|
| data.userCount | long | 用户总数 |
| data.productCount | long | 商品总数 |
| data.seckillActivityCount | long | 秒杀活动总数 |
| data.orderCount | long | 订单总数 |
| data.totalAmount | string | 交易总金额 |

**响应示例**：

```json
{
  "code": 200,
  "message": "操作成功",
  "data": {
    "userCount": 156,
    "productCount": 48,
    "seckillActivityCount": 8,
    "orderCount": 1024,
    "totalAmount": "156800"
  }
}
```

---

## 附录

### A. 错误码说明

| code | message | 说明 |
|------|---------|------|
| 200 | 操作成功 | 请求处理成功 |
| 500 | xxx错误 | 业务逻辑错误或系统异常 |

### B. 认证流程

```
1. 调用 POST /api/auth/login 获取 token
2. 将 token 存入本地存储(localStorage/sessionStorage)
3. 后续请求在 Header 中携带: Authorization: Bearer {token}
4. 调用 POST /api/auth/logout 清除服务端 token
```

### C. 导入 Apifox 方法

1. 打开 Apifox 项目
2. 点击顶部菜单「项目设置」→「导入数据」
3. 选择「Markdown」格式
4. 上传本文件 (`Apifox_API_Doc.md`)
5. 点击「确认导入」即可自动生成所有接口

### D. 接口汇总表

| 模块 | 接口数 | 路径前缀 |
|------|--------|----------|
| 认证 Auth | 4 | `/api/auth` |
| 用户 User | 5 | `/api/users` |
| 商品 Product | 5 | `/api/products` |
| 秒杀活动 SeckillActivity | 5 | `/api/seckill-activities` |
| 秒杀商品 SeckillProduct | 5 | `/api/seckill-products` |
| 购物车 Cart | 5 | `/api/cart` |
| 订单 Order | 13 | `/api/orders` |
| 物流 Logistics | 4 | `/api/logistics` |
| 售后退款 Refund | 5 | `/api/refunds` |
| 评价 Review | 7 | `/api/reviews` |
| 优惠券 Coupon | 4 | `/api/coupons` |
| 仪表盘 Dashboard | 1 | `/api/dashboard` |
| **合计** | **63** | — |
