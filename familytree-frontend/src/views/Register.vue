<template>
  <div class="register-page">
    <div class="register-container">
      <div class="register-header">
        <h1 class="title">加入我们</h1>
        <p class="subtitle">开始记录您的家族故事</p>
      </div>

      <el-form :model="form" class="register-form">
        <el-form-item>
          <el-input
            v-model="form.username"
            placeholder="用户名"
            size="large"
            prefix-icon="User"
          />
        </el-form-item>
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
          @click="handleRegister"
          size="large"
          class="register-btn"
        >
          注册
        </el-button>
        <div class="login-link">
          已有账号？<router-link to="/login">立即登录</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { reactive } from 'vue'
import { authApi } from '../api/auth'
import { ElMessage } from 'element-plus'

const form = reactive({
  username: '',
  phone: '',
  password: ''
})

const handleRegister = async () => {
  try {
    const { data } = await authApi.register(form)
    localStorage.setItem('token', data)
    ElMessage.success('注册成功')
  } catch (error) {
    ElMessage.error('注册失败')
  }
}
</script>

<style scoped>
.register-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #8b7355 0%, #5c4a32 100%);
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 20px;
}

.register-container {
  background: rgba(255, 255, 255, 0.95);
  backdrop-filter: blur(10px);
  border-radius: 24px;
  padding: 48px;
  width: 100%;
  max-width: 440px;
  box-shadow: 0 20px 60px rgba(0, 0, 0, 0.3);
}

.register-header {
  text-align: center;
  margin-bottom: 40px;
}

.title {
  font-size: 32px;
  font-weight: 700;
  color: var(--color-text);
  font-family: var(--font-serif);
  margin: 0 0 12px 0;
}

.register-btn {
  width: 100%;
  margin-top: 8px;
  height: 48px;
  font-size: 16px;
  background: linear-gradient(135deg, var(--color-accent), var(--color-primary-light));
  border: none;
}

.login-link {
  text-align: center;
  margin-top: 24px;
  font-size: 14px;
  color: var(--color-text-secondary);
}

.login-link a {
  color: var(--color-accent);
  text-decoration: none;
}
</style>
