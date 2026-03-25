import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000
})

export const authApi = {
  register: (data) => api.post('/auth/register', data),
  login: (data) => api.post('/auth/login', data)
}
