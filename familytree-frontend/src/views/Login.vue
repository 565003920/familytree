<template>
  <div class="login-page">
    <div class="login-container">
      <div class="login-header">
        <h1 class="title">家族传承</h1>
        <p class="subtitle">记录家族故事，传承家族文化</p>
      </div>

      <el-form :model="form" class="login-form">
        <el-form-item>
          <el-input
            v-model="form.phone"
            placeholder="手机号"
            size="large"
            prefix-icon="Phone"
          />
        </el-form-item>
        <el-form-item>
          <el-input
            v-model="form.password"
            type="password"
            placeholder="密码"
            size="large"
            prefix-icon="Lock"
          />
        </el-form-item>
        <el-button
          type="primary"
          @click="handleLogin"
          size="large"
          class="login-btn"
        >
          登录
        </el-button>
        <div class="register-link">
          还没有账号？<router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { useAuthStore } from '../stores/auth'
import { authApi } from '../api/auth'
import { ElMessage } from 'element-plus'
import { useRouter } from 'vue-router'

const router = useRouter()
const authStore = useAuthStore()
const form = reactive({
  phone: '',
  password: ''
})

const handleLogin = async () => {
  try {
    const { data } = await authApi.login(form)
    authStore.login(data.token, data.user)
    ElMessage.success('登录成功')
    router.push('/families')
  } catch (error) {
    ElMessage.error('登录失败')
  }
}
</script>

<style scoped>
.login-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #8b7355 0%, #5c4a32 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.login-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  padding: 48px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
  animation: slideUp 0.6s ease-out;
}

@keyframes slideUp {
  from {
    opacity: 0;
    transform: translateY(30px);
  }
  to {
    opacity: 1;
    transform: translateY(0);
  }
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 32px;
  font-weight: 700;
  color: var(--color-text);
  font-family: var(--font-serif);
  margin: 0 0 12px 0;
  letter-spacing: 2px;
}

.subtitle {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin: 0;
}

.login-form {
  width: 100%;
}

.login-btn {
  width: 100%;
  margin-top: 8px;
  height: 48px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, var(--color-accent), var(--color-primary-light));
  border: none;
}

.register-link {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: var(--color-text-secondary);
}

.register-link a {
  color: var(--color-accent);
  text-decoration: none;
  font-weight: 600;
}

.register-link a:hover {
  text-decoration: underline;
}
</style>
