# 前端 UI 重构设计文档

## 概述

对「家族传承」前端进行全面 UI 重构，从当前的紫色渐变无导航状态，改为古典温暖风格 + 侧边栏导航的现代化布局。

## 技术栈

保持不变：Vue 3 + Vite + Element Plus + Pinia + D3.js + Vue Router。不引入新依赖。

## 设计风格

古典温暖：暖色调（棕/金/米），标题衬线字体，正文无衬线，白底卡片 + 柔和阴影。传达传统、温馨的家族感。

### 字体

- 标题：`"Noto Serif SC", "Source Han Serif SC", "SimSun", serif`
- 正文：`system-ui, -apple-system, "Microsoft YaHei", sans-serif`

## 配色方案

### 主色板

| 名称 | 色值 | 用途 |
|------|------|------|
| Primary | `#5c4a32` | 侧边栏背景、主按钮、Element Plus 主色 |
| Primary Light | `#8b7355` | 悬停态、次要元素 |
| Accent | `#c4a97d` | 高亮、头像渐变、活跃导航项 |
| Background | `#f8f6f3` | 页面背景 |
| Surface | `#ede8e0` | 卡片边框、分割线 |

### 语义色

| 名称 | 色值 |
|------|------|
| Success | `#6b8f5e` |
| Warning | `#c4783c` |
| Danger | `#b54a4a` |
| Info | `#5a7a9b` |

### 文字色

| 名称 | 色值 | 用途 |
|------|------|------|
| Text Primary | `#2c2418` | 标题、重要文字 |
| Text Secondary | `#6b5d4d` | 正文、描述 |
| Text Muted | `#a89880` | 辅助信息、占位符 |

## 布局架构

### 新增文件

所有路径相对于 `familytree-frontend/`：

- `src/styles/variables.css` — CSS 变量定义（配色、圆角、阴影、字体）
- `src/styles/common.css` — 公共样式（卡片、网格、页面标题栏、按钮覆盖）
- `src/layouts/MainLayout.vue` — 侧边栏 + 内容区外壳

### 样式导入顺序

在 `main.js` 中按以下顺序导入：

```js
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/variables.css'   // 覆盖 Element Plus 变量 + 自定义变量
import './styles/common.css'      // 公共样式
```

### CSS 变量定义（variables.css）

```css
:root {
  /* 主色 */
  --color-primary: #5c4a32;
  --color-primary-light: #8b7355;
  --color-accent: #c4a97d;
  --color-bg: #f8f6f3;
  --color-surface: #ede8e0;

  /* 语义色 */
  --color-success: #6b8f5e;
  --color-warning: #c4783c;
  --color-danger: #b54a4a;
  --color-info: #5a7a9b;

  /* 文字色 */
  --color-text: #2c2418;
  --color-text-secondary: #6b5d4d;
  --color-text-muted: #a89880;

  /* 字体 */
  --font-serif: "Noto Serif SC", "Source Han Serif SC", "SimSun", serif;
  --font-sans: system-ui, -apple-system, "Microsoft YaHei", sans-serif;

  /* 圆角 */
  --radius-sm: 8px;
  --radius-md: 12px;
  --radius-lg: 16px;
  --radius-full: 50%;

  /* 阴影 */
  --shadow-card: 0 2px 12px rgba(92, 74, 50, 0.08);
  --shadow-card-hover: 0 8px 24px rgba(92, 74, 50, 0.12);

  /* Element Plus 覆盖 */
  --el-color-primary: #5c4a32;
  --el-color-primary-light-3: #8b7355;
  --el-color-primary-light-5: #a89880;
  --el-color-primary-light-7: #c4a97d;
  --el-color-primary-light-9: #ede8e0;
  --el-color-primary-dark-2: #4a3b28;
  --el-color-success: #6b8f5e;
  --el-color-warning: #c4783c;
  --el-color-danger: #b54a4a;
  --el-color-info: #5a7a9b;
}
```

### MainLayout.vue

- 左侧 72px 窄侧边栏，深棕背景 `#5c4a32`
- 顶部：Logo（圆形金棕渐变 + "族"字）
- 中部：导航图标，仅显示「我的家族」（对应 `/families`）。家族树和相册入口在 FamilyDetail 页面内提供，因为它们依赖 familyId
- 活跃导航项：`rgba(196,169,125,0.2)` 背景 + 金色 `#c4a97d` 图标。匹配规则：路由路径前缀匹配（如 `/family/` 开头的路由都高亮「我的家族」）
- 底部：当前用户头像（从 authStore.user 中取 username 首字，若无则显示默认图标）
- 右侧：米白背景内容区，下方 `<router-view>`
- 页面标题栏由各页面自行渲染（不在 MainLayout 中），因为每个页面的标题和操作按钮不同

### 路由改造

```js
const routes = [
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  {
    path: '/',
    component: MainLayout,
    redirect: '/families',
    children: [
      { path: 'families', component: FamilyList },
      { path: 'family/:id', component: FamilyDetail },
      { path: 'family/:id/tree', component: FamilyTree },
      { path: 'family/:id/albums', component: AlbumList },
      { path: 'album/:id', component: AlbumDetail },
    ]
  }
]
```

## 各页面改造

### 登录页 / 注册页

- 保持独立全屏布局
- 背景渐变改为 `#8b7355 → #5c4a32`
- 卡片、按钮配色统一为新主色
- 修复注册页重复 `<style scoped>` 块

### 家族列表页

- 去掉自带渐变背景和 header 样式（背景由 MainLayout 提供）
- 保留页面顶部标题栏（标题"我的家族" + "创建家族"按钮），改用新配色
- 卡片改为白底 + `#ede8e0` 边框 + `var(--shadow-card)` 阴影
- 头像圆形金棕渐变，显示姓氏首字
- 删除 `.family-list-page` 的 `min-height`、`background`、`padding`（由 MainLayout 内容区提供）

### 家族详情页

- 去掉独立背景，嵌入 MainLayout 内容区
- 保留页面顶部标题栏（家族名 + 操作按钮），改用新配色
- 成员卡片统一新风格
- 头像渐变改为金棕色
- 移除返回按钮（侧边栏导航已提供导航能力）

### 家族树页

- 去掉独立背景
- 保留页面顶部标题栏（"家族树" + 返回按钮），返回按钮改为"返回家族详情"
- D3 节点：`#c4a97d` 填充，`r=10`，`stroke: #8b7355`，`stroke-width: 2`
- D3 文字：`font-family: var(--font-serif)`，`font-size: 14px`，`dy: -16`
- 连线：`stroke: #8b7355`，`stroke-width: 2px`
- 保持现有全局（非 scoped）样式块模式，仅更新颜色值
- 仅改视觉样式，不改交互逻辑

### 相册列表 / 详情页

- 去掉独立背景
- 卡片统一新风格
- 相册封面占位改为浅棕渐变

## Element Plus 主题覆盖

已包含在 `variables.css` 的 CSS 变量定义中（见上方）。

## 样式迁移策略

各页面 `<style scoped>` 中的以下类别样式迁移到 `common.css`：
- `.page-header`：页面标题栏（标题 + 操作按钮的 flex 布局）
- `.card-grid`：卡片网格（`display: grid; grid-template-columns: repeat(auto-fill, minmax(280px, 1fr)); gap: 24px`）
- `.card`：通用卡片样式（白底、圆角、边框、阴影、hover 效果）
- `.avatar`：头像样式（圆形金棕渐变 + 居中文字）

各页面仅保留自身特有的 scoped 样式（如成员信息行、照片网格等）。

## 动画与过渡

- 卡片 hover：保持 `transition: all 0.3s ease` + `translateY(-4px)` + 阴影加深
- 路由切换：不添加过渡动画（保持简单）
- 登录页保持现有 `slideUp` 入场动画

## 响应式策略

- 断点：`768px`
- `≥768px`：侧边栏导航（72px）+ 右侧内容区
- `<768px`：
  - 侧边栏隐藏，底部显示 tab 栏（高度 56px，固定定位）
  - Tab 项：「我的家族」图标 + 文字标签，用户头像
  - 内容区变为全宽，底部留出 56px padding
  - 卡片网格变为单列（`grid-template-columns: 1fr`）
  - `el-dialog` 添加 `fullscreen` 属性

## 不在范围内

- 家族树交互增强（缩放/平移/展开折叠）
- 新功能添加
- 后端 API 变更
- 国际化
