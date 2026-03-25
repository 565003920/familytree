import axios from 'axios'

const API_BASE = 'http://localhost:8080/api'

export const createRelation = (data) => {
  return axios.post(`${API_BASE}/relations`, data)
}

export const getRelations = (memberId) => {
  return axios.get(`${API_BASE}/relations/member/${memberId}`)
}

export const deleteRelation = (id) => {
  return axios.delete(`${API_BASE}/relations/${id}`)
}

export const getTreeData = (familyId) => {
  return axios.get(`${API_BASE}/families/${familyId}/tree-data`)
}
