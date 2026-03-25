import { defineStore } from 'pinia'
import { ref } from 'vue'

export const useUiStore = defineStore('ui', () => {
  const loading = ref(false)
  const error = ref(null)
  const theme = ref('light')

  const showLoading = () => { loading.value = true }
  const hideLoading = () => { loading.value = false }
  const setError = (msg) => { error.value = msg }
  const clearError = () => { error.value = null }

  return { loading, error, theme, showLoading, hideLoading, setError, clearError }
})
