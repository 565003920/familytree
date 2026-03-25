import axios from 'axios'
import { useAuthStore } from '../stores/auth'
import { useUiStore } from '../stores/ui'

axios.interceptors.request.use(config => {
  const authStore = useAuthStore()
  if (authStore.token) {
    config.headers.Authorization = `Bearer ${authStore.token}`
  }
  return config
})

axios.interceptors.response.use(
  response => response,
  error => {
    const uiStore = useUiStore()
    uiStore.setError(error.response?.data?.message || '请求失败')
    return Promise.reject(error)
  }
)
