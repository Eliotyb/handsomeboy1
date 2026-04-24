---
name: "ui-prompt-generator"
description: "一键生成高质量UI设计提示词(Prompt)的技能。当用户需要用AI绘图工具(Midjourney/Stable Diffusion/DALL-E等)生成UI设计稿、界面截图、原型图时调用此技能。"
---

# 🎨 UI Prompt Generator - UI设计提示词生成器

## 技能概述

将用户的UI设计需求转化为专业的AI绘图提示词，适配主流AI绘图工具，直接产出高质量的界面设计效果图。

## 支持的AI绘图工具

| 工具 | 提示词风格 | 特点 |
|------|-----------|------|
| **Midjourney v6** | 自然语言+参数 | 艺术感强，适合概念设计 |
| **DALL-E 3** | 自然语言 | 理解力强，文字渲染好 |
| **Stable Diffusion** | 标签式(Tag-based) | 精细控制，本地运行 |
| **Figma AI** | 设计语言 | 直接生成可编辑文件 |
| **即时设计AI** | 中文描述 | 国内工具，中文友好 |

## 提示词公式

### 基础公式
```
[平台类型] + [页面类型] + [设计风格] + [配色方案] + 
[布局特征] + [核心组件] + [细节修饰] + [质量参数]
```

### 示例拆解
```
Mobile app dashboard screen, 
minimalist design, 
gradient purple-to-blue color scheme,
card-based layout with rounded corners,
featuring: stat charts, notification badges, bottom tab bar,
clean typography using SF Pro font,
soft shadows, glassmorphism effects,
4K resolution, Dribbble style, professional UI --ar 9:16
```

## 场景模板库

### 📱 移动端 App

#### 登录/注册页
```
Mobile login screen, clean minimal design, 
centered layout, brand logo at top,
email/password input fields with floating labels,
social login buttons (WeChat/Apple/Google),
"Forgot password" link at bottom,
gradient background {color}, no distractions,
iOS Human Interface Guidelines, 4K --ar 9:16
```

#### 首页/Feed流
```
Mobile home feed screen, 
infinite scroll content cards,
each card: cover image + title + author avatar + like/comment icons,
pull-down refresh animation visible,
bottom navigation bar with 5 tabs,
{style} design language, {color} accent color,
status bar showing time and battery, realistic mockup --ar 9:16
```

#### 个人中心页
```
Mobile profile page, 
large circular avatar with status indicator,
user stats row (posts/followers/following),
settings menu list with chevron arrows,
premium membership banner card,
dark mode or light mode option,
{app_type} app style, elegant typography --ar 9:16
```

#### 购物车/结算页
```
Mobile shopping cart screen,
product list with thumbnail/image/price/quantity selector,
coupon input field, price summary section,
prominent "Checkout" CTA button at bottom,
secure payment badges, delete/swipe gestures,
e-commerce app design, trustworthy feel, warm colors --ar 9:16
```

### 💻 Web端 / Desktop

#### 管理后台 Dashboard
```
Web admin dashboard, sidebar navigation on left,
top header with search/notification/profile dropdown,
main content area: KPI stat cards in a row,
data visualization charts (line/bar/pie),
recent activity table, clean data-dense layout,
SaaS product design, light theme, professional B2B feel --ar 16:9
```

#### 电商商品详情页
```
E-commerce product detail page web,
left: large image gallery with thumbnails,
right: product title/price/rating/variant selectors,
"Add to Cart" prominent button, trust badges below,
tabbed section for description/reviews/shipping info,
responsive layout, conversion-optimized design --ar 16:9
```

#### Landing Page 首屏
```
Website landing page hero section,
large bold headline with subtext,
CTA button (primary color), hero illustration or mockup,
floating feature badges, subtle background pattern,
modern SaaS marketing page, high contrast, attention-grabbing --ar 16:9
```

## 设计风格关键词库

### 风格流派
| 风格 | 关键词 |
|------|--------|
| 极简主义 | minimalist, clean, whitespace-focused, flat |
| 新拟态 | neumorphism, soft UI, extruded plastic look |
| 玻璃态 | glassmorphism, frosted glass, blur transparency |
| 扁平化 | flat design, material design, bold colors |
| 渐变风 | gradient mesh, vibrant gradients, aurora colors |
| 3D写实 | 3D render, isometric, depth of field, ray tracing |
| 插画风 | illustration style, hand-drawn, playful, whimsical |
| 暗黑模式 | dark mode, dark theme, OLED black, neon accents |
| 毛玻璃 | frosted glass, backdrop-filter blur, translucent |

### 配色方案
| 方案 | 关键词 |
|------|--------|
| 科技蓝 | blue gradient, tech feel, #667eea to #764ba2 |
| 活力橙 | orange accent, energetic, warm palette |
| 清新绿 | nature green, fresh, eco-friendly tones |
| 高级灰 | neutral grays, sophisticated, monochrome |
| 渐变紫 | purple gradient, dreamy, creative vibe |
| 黑金 | dark luxury, gold accents, premium feel |

### 布局关键词
| 类型 | 关键词 |
|------|--------|
| 卡片式 | card-based, grid layout, rounded corners |
| 列表式 | list view, row-based, information dense |
| 分栏 | multi-column, sidebar + main, dashboard layout |
| 全屏 | full-bleed, immersive, cinematic |
| 居中 | centered composition, focal point, balanced |

## 使用示例

### 示例1：快速生成
```
用户: 帮我生成一个健身App首页的设计提示词
→ 输出:
  Mobile fitness app home screen, workout plan cards,
  progress ring charts, daily streak counter,
  energetic orange-to-red gradient, motivational imagery,
  bottom nav: Home/Workout/Plan/Social/Profile,
  iOS style, modern sports app, 4K --ar 9:16
```

### 示例2：指定风格
```
用户: 要一个暗黑风格的加密货币交易界面
→ 输出:
  Crypto trading dashboard, dark mode OLED black background,
  neon green/red candlestick charts, real-time price tickers,
  order book depth visualization, glassmorphism panels,
  cyberpunk aesthetic, glowing accents, futuristic fintech UI,
  multiple monitor setup feel, data-rich display --ar 16:9
```

### 示例3：迭代优化
```
用户: 这个提示词生成的太花哨了，要更商务一点
→ 调整: 
  移除 "playful/vibrant/neon"
  替换为 "professional/corporate/minimal"
  配色改为 "navy blue + white"
  增加 "enterprise-grade/trustworthy"
```

## 最佳实践

- ✅ 明确指定宽高比 `--ar 16:9`(桌面) 或 `--ar 9:16`(手机)
- ✅ 加入参考平台 "iOS style" / "Material Design 3"
- ✅ 指定质量词 "4K" / "highly detailed" / "professional"
- ✅ 包含具体组件名称而非笼统描述
- ❌ 避免过长提示词（Midjourney < 200词效果最佳）
- ❌ 避免矛盾的风格关键词混搭
- ❌ 不要期望AI完美渲染精确文字内容
