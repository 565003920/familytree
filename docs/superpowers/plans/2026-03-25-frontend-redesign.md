# 前端 UI 重构实施计划

> **For agentic workers:** REQUIRED: Use superpowers:subagent-driven-development (if subagents available) or superpowers:executing-plans to implement this plan. Steps use checkbox (`- [ ]`) syntax for tracking.

**Goal:** 将「家族传承」前端从紫色渐变无导航状态重构为古典温暖风格 + 侧边栏导航布局。

**Architecture:** 新增全局样式系统（variables.css + common.css）和 MainLayout.vue 布局组件，路由改为嵌套结构。各页面去掉独立背景，嵌入 MainLayout 内容区，样式统一引用 CSS 变量。

**Tech Stack:** Vue 3, Vite, Element Plus, Pinia, D3.js, Vue Router. 不引入新依赖。

**Spec:** `docs/superpowers/specs/2026-03-25-frontend-redesign-design.md`

**Note:** 本项目无测试框架，所有验证步骤为 `vite build` 编译检查 + 浏览器视觉验证。

---

## File Map

### 新建文件

| 文件 | 职责 |
|------|------|
| `familytree-frontend/src/styles/variables.css` | CSS 变量（配色、字体、圆角、阴影、Element Plus 覆盖） |
| `familytree-frontend/src/styles/common.css` | 公共样式类（.page-header, .card-grid, .card, .avatar） |
| `familytree-frontend/src/layouts/MainLayout.vue` | 侧边栏 + 内容区布局外壳 |

### 修改文件

| 文件 | 改动 |
|------|------|
| `familytree-frontend/src/main.js` | 添加 variables.css 和 common.css 导入 |
| `familytree-frontend/src/App.vue` | 添加全局 body 字体设置 |
| `familytree-frontend/src/router/index.js` | 改为嵌套路由，MainLayout 包裹内页 |
| `familytree-frontend/src/views/Login.vue` | 配色改为暖棕 |
| `familytree-frontend/src/views/Register.vue` | 配色改为暖棕，修复重复 style 块 |
| `familytree-frontend/src/views/FamilyList.vue` | 去掉独立背景，使用公共样式类 |
| `familytree-frontend/src/views/FamilyDetail.vue` | 去掉独立背景，使用公共样式类 |
| `familytree-frontend/src/views/FamilyTree.vue` | 去掉独立背景，D3 节点视觉美化 |
| `familytree-frontend/src/views/AlbumList.vue` | 去掉独立背景，使用公共样式类 |
| `familytree-frontend/src/views/AlbumDetail.vue` | 去掉独立背景，使用公共样式类 |

---

## Chunk 1: 基础设施（样式系统 + 布局 + 路由）

### Task 1: 创建 variables.css

**Files:**
- Create: `familytree-frontend/src/styles/variables.css`

- [ ] **Step 1: 创建 styles 目录和 variables.css**

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

- [ ] **Step 2: Commit**

```bash
git add familytree-frontend/src/styles/variables.css
git commit -m "feat: add CSS variables for warm classical theme"
```

---

### Task 2: 创建 common.css

**Files:**
- Create: `familytree-frontend/src/styles/common.css`

- [ ] **Step 1: 创建 common.css，定义公共样式类**

```css
/* 全局基础 */
body {
  font-family: var(--font-sans);
  color: var(--color-text);
  background: var(--color-bg);
}

/* 页面标题栏 */
.page-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.page-header h1 {
  font-size: 24px;
  font-weight: 700;
  color: var(--color-text);
  font-family: var(--font-serif);
  margin: 0;
  letter-spacing: 1px;
}

.page-header .subtitle {
  font-size: 13px;
  color: var(--color-text-muted);
  margin-top: 4px;
}

.page-header .actions {
  display: flex;
  gap: 12px;
}

/* 卡片网格 */
.card-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(280px, 1fr));
  gap: 24px;
}

/* 通用卡片 */
.card {
  background: white;
  border-radius: var(--radius-md);
  padding: 20px;
  border: 1px solid var(--color-surface);
  box-shadow: var(--shadow-card);
  transition: all 0.3s ease;
}

.card:hover {
  transform: translateY(-4px);
  box-shadow: var(--shadow-card-hover);
}

.card.clickable {
  cursor: pointer;
}

/* 头像 */
.avatar {
  width: 48px;
  height: 48px;
  border-radius: var(--radius-full);
  background: linear-gradient(135deg, var(--color-accent), var(--color-primary-light));
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  font-weight: 700;
  color: white;
  font-family: var(--font-serif);
  flex-shrink: 0;
}

.avatar.large {
  width: 64px;
  height: 64px;
  font-size: 28px;
}

/* 响应式 */
@media (max-width: 768px) {
  .card-grid {
    grid-template-columns: 1fr;
  }

  .page-header {
    flex-direction: column;
    align-items: flex-start;
    gap: 12px;
  }
}
```

- [ ] **Step 2: Commit**

```bash
git add familytree-frontend/src/styles/common.css
git commit -m "feat: add common CSS classes for cards, grid, header, avatar"
```

---

### Task 3: 更新 main.js 和 App.vue

**Files:**
- Modify: `familytree-frontend/src/main.js`
- Modify: `familytree-frontend/src/App.vue`

- [ ] **Step 1: 更新 main.js，添加样式导入**

在 `import 'element-plus/dist/index.css'` 之后添加：

```js
import './styles/variables.css'
import './styles/common.css'
```

完整 main.js：

```js
import { createApp } from 'vue'
import { createPinia } from 'pinia'
import ElementPlus from 'element-plus'
import 'element-plus/dist/index.css'
import './styles/variables.css'
import './styles/common.css'
import App from './App.vue'
import router from './router'
import './api/interceptors'

createApp(App)
  .use(createPinia())
  .use(router)
  .use(ElementPlus)
  .mount('#app')
```

- [ ] **Step 2: 更新 App.vue**

App.vue 当前内容已经是正确的（只有 `<router-view />` 和 reset 样式），无需修改。跳过此步。

- [ ] **Step 3: Commit**

```bash
git add familytree-frontend/src/main.js
git commit -m "feat: wire up global style imports in main.js"
```

---

### Task 4: 创建 MainLayout.vue

**Files:**
- Create: `familytree-frontend/src/layouts/MainLayout.vue`

- [ ] **Step 1: 创建 layouts 目录和 MainLayout.vue**

<!-- PLAN_PLACEHOLDER_MAINLAYOUT -->
```vue
<template>
  <div class="main-layout">
    <aside class="sidebar">
      <div class="sidebar-top">
        <div class="logo">族</div>
      </div>
      <nav class="sidebar-nav">
        <router-link
          to="/families"
          class="nav-item"
          :class="{ active: isActive('/families') || isActive('/family') }"
          title="我的家族"
        >
          <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round">
            <path d="M3 9l9-7 9 7v11a2 2 0 01-2 2H5a2 2 0 01-2-2z"/>
            <polyline points="9 22 9 12 15 12 15 22"/>
          </svg>
        </router-link>
      </nav>
      <div class="sidebar-bottom">
        <div class="user-avatar" @click="handleLogout" title="退出登录">
          {{ userInitial }}
        </div>
      </div>
    </aside>
    <main class="content">
      <router-view />
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useAuthStore } from '../stores/auth'
import { ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const authStore = useAuthStore()

const userInitial = computed(() => {
  const user = authStore.user
  if (user && user.username) return user.username.charAt(0)
  return '?'
})

const isActive = (prefix) => route.path.startsWith(prefix)

const handleLogout = async () => {
  try {
    await ElMessageBox.confirm('确定要退出登录吗？', '提示', {
      confirmButtonText: '确定',
      cancelButtonText: '取消',
      type: 'warning'
    })
    authStore.logout()
    router.push('/login')
  } catch {}
}
</script>
```

- [ ] **Step 2: 添加 MainLayout 的 scoped 样式**

```css
<style scoped>
.main-layout {
  display: flex;
  min-height: 100vh;
}

.sidebar {
  width: 72px;
  background: var(--color-primary);
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 20px 0;
  position: fixed;
  top: 0;
  left: 0;
  bottom: 0;
  z-index: 100;
}

.sidebar-top {
  margin-bottom: 32px;
}

.logo {
  width: 40px;
  height: 40px;
  border-radius: var(--radius-full);
  background: linear-gradient(135deg, var(--color-accent), var(--color-primary-light));
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
  font-size: 18px;
  font-family: var(--font-serif);
  font-weight: 700;
}

.sidebar-nav {
  display: flex;
  flex-direction: column;
  gap: 8px;
  flex: 1;
}

.nav-item {
  width: 44px;
  height: 44px;
  border-radius: 10px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-text-muted);
  text-decoration: none;
  transition: all 0.2s ease;
}

.nav-item:hover {
  background: rgba(196, 169, 125, 0.15);
  color: var(--color-accent);
}

.nav-item.active {
  background: rgba(196, 169, 125, 0.2);
  color: var(--color-accent);
}

.sidebar-bottom {
  margin-top: auto;
}

.user-avatar {
  width: 36px;
  height: 36px;
  border-radius: var(--radius-full);
  background: var(--color-primary-light);
  display: flex;
  align-items: center;
  justify-content: center;
  color: var(--color-bg);
  font-size: 14px;
  font-family: var(--font-serif);
  font-weight: 600;
  cursor: pointer;
  transition: background 0.2s ease;
}

.user-avatar:hover {
  background: var(--color-accent);
}

.content {
  flex: 1;
  margin-left: 72px;
  padding: 32px;
  background: var(--color-bg);
  min-height: 100vh;
}

/* 响应式：移动端底部 tab 栏 */
@media (max-width: 768px) {
  .sidebar {
    position: fixed;
    top: auto;
    left: 0;
    right: 0;
    bottom: 0;
    width: 100%;
    height: 56px;
    flex-direction: row;
    justify-content: space-around;
    padding: 0 16px;
  }

  .sidebar-top {
    margin-bottom: 0;
  }

  .logo {
    width: 32px;
    height: 32px;
    font-size: 14px;
  }

  .sidebar-nav {
    flex-direction: row;
    gap: 0;
  }

  .sidebar-bottom {
    margin-top: 0;
  }

  .content {
    margin-left: 0;
    padding: 20px 16px 72px;
  }
}
</style>
```

- [ ] **Step 3: Commit**

```bash
git add familytree-frontend/src/layouts/MainLayout.vue
git commit -m "feat: add MainLayout with sidebar navigation"
```

---

### Task 5: 更新路由为嵌套结构

**Files:**
- Modify: `familytree-frontend/src/router/index.js`

- [ ] **Step 1: 重写 router/index.js**

```js
import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import MainLayout from '../layouts/MainLayout.vue'
import FamilyList from '../views/FamilyList.vue'
import FamilyDetail from '../views/FamilyDetail.vue'
import FamilyTree from '../views/FamilyTree.vue'
import AlbumList from '../views/AlbumList.vue'
import AlbumDetail from '../views/AlbumDetail.vue'

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

export default createRouter({
  history: createWebHistory(),
  routes
})
```

- [ ] **Step 2: 验证编译**

Run: `cd familytree-frontend && npx vite build`
Expected: 编译成功，无错误

- [ ] **Step 3: Commit**

```bash
git add familytree-frontend/src/router/index.js
git commit -m "feat: restructure routes with nested MainLayout"
```

---

## Chunk 2: 页面重构

### Task 6: 重构 Login.vue

**Files:**
- Modify: `familytree-frontend/src/views/Login.vue`

- [ ] **Step 1: 更新 Login.vue 样式为暖棕配色**

仅修改 `<style scoped>` 部分，模板和脚本不变。替换以下样式值：

- `.login-page` background: `linear-gradient(135deg, #8b7355 0%, #5c4a32 100%)`
- `.title` color: `var(--color-text)`, font-family: `var(--font-serif)`
- `.subtitle` color: `var(--color-text-secondary)`
- `.login-btn` background: `linear-gradient(135deg, var(--color-accent), var(--color-primary-light))`, 保持 `border: none`
- 保留现有 `@keyframes slideUp` 和 `.login-container` 上的 `animation: slideUp 0.6s ease-out`，不要删除
- `.register-link a` color: `var(--color-accent)`

- [ ] **Step 2: Commit**

```bash
git add familytree-frontend/src/views/Login.vue
git commit -m "refactor: update Login page to warm classical palette"
```

---

### Task 7: 重构 Register.vue

**Files:**
- Modify: `familytree-frontend/src/views/Register.vue`

- [ ] **Step 1: 修复重复 style 块并更新配色**

Register.vue 有两个 `<style scoped>` 块（行 73-83 和 85-137）。合并为一个，并更新配色：

- 删除第一个 `<style scoped>` 块（行 73-83，包含 `.register-container` 和 `.register-form` 的旧样式）
- 保留第二个块，更新配色：
  - `.register-page` background: `linear-gradient(135deg, #8b7355 0%, #5c4a32 100%)`
  - `.title` color: `var(--color-text)`, font-family: `var(--font-serif)`
  - `.register-btn` background: `linear-gradient(135deg, var(--color-accent), var(--color-primary-light))`
  - `.login-link a` color: `var(--color-accent)`

- [ ] **Step 2: Commit**

```bash
git add familytree-frontend/src/views/Register.vue
git commit -m "refactor: fix duplicate style block, update Register to warm palette"
```

---

### Task 8: 重构 FamilyList.vue

**Files:**
- Modify: `familytree-frontend/src/views/FamilyList.vue`

- [ ] **Step 1: 更新模板，使用公共样式类**

将模板中的自定义 class 替换为公共类：

- 外层 `div.family-list-page` 去掉该 class（或保留但清空样式）
- `.header` → 改用 `class="page-header"`
- `.family-grid` → 改用 `class="card-grid"`
- `.family-card` → 添加 `class="card clickable"`
- `.surname` 颜色改为 `var(--color-accent)`

注意：FamilyList 没有头像元素，不需要使用 `.avatar` 类。

- [ ] **Step 2: 精简 scoped 样式**

删除已迁移到 common.css 的样式（`.family-list-page` 的 min-height/background/padding、`.header`、`.family-grid`、`.family-card` 基础样式）。仅保留页面特有样式：`.card-header`、`.surname`、`.card-body`、`.info-item`、`.card-actions`。

所有颜色值替换为 CSS 变量引用。

- [ ] **Step 3: Commit**

```bash
git add familytree-frontend/src/views/FamilyList.vue
git commit -m "refactor: FamilyList uses global layout and common styles"
```

---

### Task 9: 重构 FamilyDetail.vue

**Files:**
- Modify: `familytree-frontend/src/views/FamilyDetail.vue`

- [ ] **Step 1: 更新模板**

- 外层去掉 `.family-detail-page` 的独立背景样式
- `.header` → `class="page-header"`
- `.members-grid` → `class="card-grid"`
- `.member-card` → `class="card"`
- `.member-avatar` → `class="avatar large"`（64px 版本）
- FamilyDetail 没有返回按钮，无需移除。保留现有的"查看家族树"、"家族相册"、"+ 添加成员"按钮
- `.title-section h1` 改用 `font-family: var(--font-serif)`, `color: var(--color-text)`
- `.surname` 颜色改为 `var(--color-accent)`

- [ ] **Step 2: 精简 scoped 样式**

删除 `.family-detail-page` 的 min-height/background/padding、`.header`、`.members-grid`、`.member-card` 基础样式、`.member-avatar` 基础样式。

保留：`.title-section`、`.surname`、`.member-info`、`.info-row`、`.member-actions`。颜色值替换为 CSS 变量。

- [ ] **Step 3: Commit**

```bash
git add familytree-frontend/src/views/FamilyDetail.vue
git commit -m "refactor: FamilyDetail uses global layout and common styles"
```

---

### Task 10: 重构 FamilyTree.vue

**Files:**
- Modify: `familytree-frontend/src/views/FamilyTree.vue`

- [ ] **Step 1: 更新模板和 scoped 样式**

- 去掉 `.family-tree-page` 的 min-height/background/padding
- `.header` → `class="page-header"`
- `.header h1` 改用 `font-family: var(--font-serif)`, `color: var(--color-text)`
- 返回按钮文字改为"返回家族详情"，同时将 `$router.back()` 改为 `$router.push(\`/family/${route.params.id}\`)` 以确保导航目标明确
- `#tree-container` 背景改为 `white`，border 改为 `1px solid var(--color-surface)`

- [ ] **Step 2: 更新全局（非 scoped）D3 样式**

```css
<style>
.link {
  fill: none;
  stroke: #8b7355;
  stroke-width: 2px;
}

.node circle {
  fill: #c4a97d;
  stroke: #8b7355;
  stroke-width: 2px;
}

.node text {
  font-size: 14px;
  font-weight: 600;
  font-family: "Noto Serif SC", "Source Han Serif SC", "SimSun", serif;
}
</style>
```

- [ ] **Step 3: 更新 D3 渲染代码中的节点半径**

在 `renderTree` 函数中，将 `node.append('circle').attr('r', 6)` 改为 `.attr('r', 10)`，将 `node.append('text').attr('dy', -10)` 改为 `.attr('dy', -16)`。

- [ ] **Step 4: Commit**

```bash
git add familytree-frontend/src/views/FamilyTree.vue
git commit -m "refactor: FamilyTree warm palette with larger styled nodes"
```

---

### Task 11: 重构 AlbumList.vue

**Files:**
- Modify: `familytree-frontend/src/views/AlbumList.vue`

- [ ] **Step 1: 更新模板和样式**

- 去掉 `.album-page` 的 min-height/background/padding
- `.header` → `class="page-header"`
- `.header h1` 改用 `font-family: var(--font-serif)`, `color: var(--color-text)`
- `.album-grid` → `class="card-grid"`
- `.album-card` → `class="card clickable"`
- `.album-cover` 背景改为 `linear-gradient(135deg, var(--color-accent), var(--color-primary-light))`
- `.album-card h3` 颜色改为 `var(--color-text)`
- `.album-card p` 颜色改为 `var(--color-text-secondary)`

- [ ] **Step 2: 精简 scoped 样式**

删除已迁移的基础样式，保留 `.album-cover`（高度和渐变）、`.album-actions`、卡片内文字间距。

- [ ] **Step 3: Commit**

```bash
git add familytree-frontend/src/views/AlbumList.vue
git commit -m "refactor: AlbumList uses global layout and warm palette"
```

---

### Task 12: 重构 AlbumDetail.vue

**Files:**
- Modify: `familytree-frontend/src/views/AlbumDetail.vue`

- [ ] **Step 1: 更新模板和样式**

- 去掉 `.album-detail-page` 的 min-height/background/padding
- `.header` → `class="page-header"`
- `.header h1` 改用 `font-family: var(--font-serif)`, `color: var(--color-text)`
- `.photo-grid` 改用 `class="card-grid"`，调整 minmax 为 `250px`
- `.photo-card` 改用 `class="card"`，padding 设为 0，overflow hidden
- `.photo-desc` 颜色改为 `var(--color-text-secondary)`

- [ ] **Step 2: 精简 scoped 样式**

保留 `.photo-card img`（高度和 object-fit）、`.photo-info`、`.photo-actions`。

- [ ] **Step 3: Commit**

```bash
git add familytree-frontend/src/views/AlbumDetail.vue
git commit -m "refactor: AlbumDetail uses global layout and warm palette"
```

---

### Task 13: 最终验证

- [ ] **Step 1: 编译检查**

Run: `cd familytree-frontend && npx vite build`
Expected: 编译成功，无错误

- [ ] **Step 2: 浏览器视觉验证清单**

启动 dev server（手动运行 `npm run dev`），逐页检查：

1. `/login` — 暖棕渐变背景，白色卡片，金棕按钮
2. `/register` — 同上风格，无样式异常
3. `/families` — 侧边栏可见，米白背景，白底卡片，金棕头像
4. `/family/:id` — 成员卡片新风格，无返回按钮
5. `/family/:id/tree` — D3 节点金棕色，较大圆形，衬线字体
6. `/family/:id/albums` — 相册卡片新风格，浅棕封面
7. `/album/:id` — 照片卡片新风格
8. 移动端（DevTools 切换 375px 宽度）— 底部 tab 栏，单列卡片

- [ ] **Step 3: 最终 Commit**

```bash
git add -A
git commit -m "refactor: complete frontend UI redesign to warm classical theme"
```
