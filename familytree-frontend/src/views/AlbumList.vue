<template>
  <div class="album-page">
    <div class="page-header">
      <h1>家族相册</h1>
      <el-button type="primary" @click="showDialog = true" size="large">
        + 创建相册
      </el-button>
    </div>

    <div class="card-grid">
      <div v-for="album in albums" :key="album.id" class="card clickable album-card" @click="viewAlbum(album.id)">
        <div class="album-cover"></div>
        <h3>{{ album.name }}</h3>
        <p>{{ album.description }}</p>
        <div class="album-actions" @click.stop>
          <el-button size="small" @click="openEdit(album)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(album)">删除</el-button>
        </div>
      </div>
    </div>

    <el-dialog v-model="showDialog" :title="isEdit ? '编辑相册' : '创建相册'" width="500px">
      <el-form :model="form" label-width="80px">
        <el-form-item label="相册名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="描述">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="isEdit ? updateAlbum() : createAlbum()">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { albumApi } from '../api/album'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const router = useRouter()
const albums = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const currentUserId = 1
const form = ref({ name: '', description: '' })

const loadAlbums = async () => {
  const { data } = await albumApi.getList(route.params.id)
  albums.value = data
}

onMounted(loadAlbums)

const createAlbum = async () => {
  await albumApi.create({ ...form.value, familyId: route.params.id })
  showDialog.value = false
  form.value = { name: '', description: '' }
  loadAlbums()
}

const openEdit = (album) => {
  isEdit.value = true
  editId.value = album.id
  form.value = { name: album.name, description: album.description || '' }
  showDialog.value = true
}

const updateAlbum = async () => {
  await albumApi.update(editId.value, form.value, currentUserId)
  showDialog.value = false
  isEdit.value = false
  editId.value = null
  form.value = { name: '', description: '' }
  loadAlbums()
}

const handleDelete = async (album) => {
  try {
    await ElMessageBox.confirm(
      `此操作将删除「${album.name}」，数据将被标记为已删除。`,
      '确认删除？',
      { confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning' }
    )
    await albumApi.delete(album.id, currentUserId)
    ElMessage.success('删除成功')
    loadAlbums()
  } catch {}
}

const viewAlbum = (id) => {
  router.push(`/album/${id}`)
}
</script>

<style scoped>
.album-card {
  padding: 0;
  overflow: hidden;
}

.album-cover {
  width: 100%;
  height: 200px;
  background: linear-gradient(135deg, var(--color-accent), var(--color-primary-light));
}

.album-card h3 {
  font-size: 20px;
  font-weight: 600;
  margin: 16px;
  color: var(--color-text);
  font-family: var(--font-serif);
}

.album-card p {
  font-size: 14px;
  color: var(--color-text-secondary);
  margin: 0 16px 16px;
}

.album-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  padding: 0 16px 16px;
}
</style>

