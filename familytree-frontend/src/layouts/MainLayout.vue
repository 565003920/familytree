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
