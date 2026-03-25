import axios from 'axios'

const api = axios.create({
  baseURL: '/api',
  timeout: 5000
})

export const familyApi = {
  create: (data) => api.post('/families', data),
  getList: (adminId) => api.get('/families', { params: { adminId } }),
  getById: (id) => api.get(`/families/${id}`),
  update: (id, data) => api.put(`/families/${id}`, data, { headers: { 'X-User-Id': data.adminId } }),
  delete: (id, userId) => api.delete(`/families/${id}`, { headers: { 'X-User-Id': userId } })
}

export const memberApi = {
  create: (data) => api.post('/members', data),
  getList: (familyId) => api.get('/members', { params: { familyId } }),
  getById: (id) => api.get(`/members/${id}`),
  update: (id, data, userId) => api.put(`/members/${id}`, data, { headers: { 'X-User-Id': userId } }),
  delete: (id, userId) => api.delete(`/members/${id}`, { headers: { 'X-User-Id': userId } })
}
