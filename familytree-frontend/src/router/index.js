import { createRouter, createWebHistory } from 'vue-router'
import Login from '../views/Login.vue'
import Register from '../views/Register.vue'
import FamilyList from '../views/FamilyList.vue'
import FamilyDetail from '../views/FamilyDetail.vue'
import FamilyTree from '../views/FamilyTree.vue'
import AlbumList from '../views/AlbumList.vue'
import AlbumDetail from '../views/AlbumDetail.vue'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: Login },
  { path: '/register', component: Register },
  { path: '/families', component: FamilyList },
  { path: '/family/:id', component: FamilyDetail },
  { path: '/family/:id/tree', component: FamilyTree },
  { path: '/family/:id/albums', component: AlbumList },
  { path: '/album/:id', component: AlbumDetail }
]

export default createRouter({
  history: createWebHistory(),
  routes
})
