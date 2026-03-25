import axios from 'axios'

const API_URL = 'http://localhost:8080/api'

export const albumApi = {
  getList: (familyId) => axios.get(`${API_URL}/albums/family/${familyId}`),
  create: (data) => axios.post(`${API_URL}/albums`, data),
  update: (id, data, userId) => axios.put(`${API_URL}/albums/${id}`, data, { headers: { 'X-User-Id': userId } }),
  delete: (id, userId) => axios.delete(`${API_URL}/albums/${id}`, { headers: { 'X-User-Id': userId } })
}

export const photoApi = {
  getList: (albumId) => axios.get(`${API_URL}/photos/album/${albumId}`),
  upload: (file, albumId) => {
    const formData = new FormData()
    formData.append('file', file)
    formData.append('albumId', albumId)
    return axios.post(`${API_URL}/photos/upload`, formData)
  },
  update: (id, data, userId) => axios.put(`${API_URL}/photos/${id}`, data, { headers: { 'X-User-Id': userId } }),
  delete: (id, userId) => axios.delete(`${API_URL}/photos/${id}`, { headers: { 'X-User-Id': userId } })
}
