---
name: "find-skills"
description: "用自然语言搜索和发现所需AI技能。当用户询问有什么技能可用、想找特定功能的技能、或不知道该用什么技能完成任务时调用此技能。"
---

# 🔍 Find Skills - AI技能搜索器

## 技能概述

帮助用户通过自然语言描述快速找到最匹配的AI技能，解决"我想做XX但不知道用什么技能"的问题。

## 核心能力

### 1. 自然语言理解
- 理解用户的模糊需求描述
- 提取关键意图和关键词
- 匹配最相关的技能组合

### 2. 技能推荐
- 单技能精准推荐
- 多技能组合方案
- 替代方案建议
- 技能使用顺序规划

### 3. 技能说明
- 简要介绍每个推荐技能的功能
- 说明适用场景和不适用场景
- 提供快速上手指引

## 使用场景

| 用户输入 | 推荐逻辑 |
|----------|----------|
| "我想做个PPT" | → ppt-generator |
| "帮我润色这段话" | → humanizer-zh |
| "写个产品需求文档" | → product-spec-builder |
| "帮我调研一下XX行业" | → deep-research |
| "设计一个好看的界面" | → ui-prompt-generator + ui-ux-pro-max |
| "我有好多想法不知道怎么整理" | → brainstorming |
| "帮我处理这个Excel" | → minimax-office(xlsx) |
| "自动填这个表" | → agent-browser |
| "写代码改bug" | → dev-builder |
| "我不知道能用AI做什么" | → 展示全部技能分类 |

## 工作流程

```
1. 接收用户自然语言输入
2. 分析意图关键词
3. 在技能库中匹配候选技能
4. 计算相关度排序
5. 返回 Top-N 推荐 + 使用建议
6. 如有需要可直接调用目标技能
```

## 技能分类索引

### 📦 新手必装（入门必备）
| 技能 | 一句话说明 |
|------|-----------|
| agent-browser | 浏览器自动化，自动浏览/填表/抓数据 |
| find-skills | 就是我！帮你找技能的 |
| skill-creator | 把你的重复任务变成自定义技能 |
| deep-research | 多来源深度调研，出专业报告 |

### 💻 开发工作（程序员向）
| 技能 | 一句话说明 |
|------|-----------|
| product-spec-builder | 自动生成专业PRD产品需求文档 |
| ui-prompt-generator | 一键生成UI设计提示词 |
| ui-ux-pro-max | 智能提供UI/UX设计方案与设计系统 |
| dev-builder | 全栈开发辅助，快速写代码/改BUG |

### ✍️ 文字工作（内容创作向）
| 技能 | 一句话说明 |
|------|-----------|
| brainstorming | 九步创意头脑风暴流程 |
| minimax-office | 强化版Office三件套(docx/pdf/xlsx) |
| ppt-generator | 自动生成专业排版PPT |
| humanizer-zh | 中文文本润色，去AI痕迹 |

## 推荐算法

### 关键词匹配规则

```python
# 示例匹配逻辑（非实际代码）
keywords_map = {
    "agent-browser": ["浏览器", "爬虫", "抓取", "自动", "填表", "测试", "网页", "截图"],
    "deep-research": ["调研", "研究", "报告", "分析", "行业", "竞品", "趋势", "资料"],
    "product-spec-builder": ["需求", "PRD", "产品", "规格", "文档", "功能"],
    "ui-prompt-generator": ["UI", "界面", "设计稿", "提示词", "prompt", "原型"],
    "ui-ux-pro-max": ["UX", "体验", "交互", "设计系统", "配色", "布局"],
    "dev-builder": ["代码", "开发", "编程", "bug", "调试", "全栈", "前后端"],
    "brainstorming": ["头脑风暴", "创意", "想法", "点子", "灵感", "发散"],
    "minimax-office": ["Word", "Excel", "PPT", "Office", "文档", "表格", "pdf"],
    "ppt-generator": ["PPT", "幻灯片", "演示文稿", "汇报", "演讲"],
    "humanizer-zh": ["润色", "改写", "优化", "文采", "通顺", "AI痕迹"]
}
```

## 使用示例

### 示例1：直接查询
```
用户: 有没有能帮我做市场调研的技能？
→ 推荐: deep-research
→ 说明: 支持多来源深度调研，自动汇总生成专业报告
→ 是否立即使用? [Y/N]
```

### 示例2：模糊匹配
```
用户: 我要给老板做个汇报材料
→ 推荐:
  1. ppt-generator (生成演示文稿)
  2. deep-research (补充调研数据)
  3. humanizer-zh (润色文案)
→ 建议: 先用deep-research收集素材，再用ppt-generator制作
```

### 示例3：组合推荐
```
用户: 我想做一个新的APP
→ 推荐组合:
  Step1: brainstorming → 确定核心功能和创意方向
  Step2: product-spec-builder → 输出完整PRD文档
  Step3: ui-prompt-generator + ui-ux-pro-max → 设计UI方案
  Step4: dev-builder → 开始编码实现
```

## 自定义扩展

如果现有技能无法满足需求：
1. 使用 `skill-creator` 创建新技能
2. 将新技能注册到本搜索器的分类索引中
3. 更新关键词匹配规则
