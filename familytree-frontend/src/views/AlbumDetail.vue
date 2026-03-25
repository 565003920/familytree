<template>
  <div class="album-detail-page">
    <div class="header">
      <h1>相册详情</h1>
      <el-upload :http-request="uploadPhoto" :show-file-list="false">
        <el-button type="primary" size="large">+ 上传照片</el-button>
      </el-upload>
    </div>

    <div class="photo-grid">
      <div v-for="photo in photos" :key="photo.id" class="photo-card">
        <img :src="`http://localhost:8080${photo.url}`" />
        <div class="photo-info">
          <p class="photo-desc">{{ photo.description || '' }}</p>
          <div class="photo-actions">
            <el-button size="small" @click="openEditPhoto(photo)">编辑描述</el-button>
            <el-button size="small" type="danger" @click="handleDeletePhoto(photo)">删除</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="showEditDialog" title="编辑照片描述" width="400px">
      <el-form :model="editForm" label-width="60px">
        <el-form-item label="描述">
          <el-input v-model="editForm.description" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showEditDialog = false">取消</el-button>
        <el-button type="primary" @click="updatePhoto">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { photoApi } from '../api/album'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const photos = ref([])
const showEditDialog = ref(false)
const editPhotoId = ref(null)
const editForm = ref({ description: '' })
const currentUserId = 1

onMounted(() => {
  loadPhotos()
})

const loadPhotos = async () => {
  const { data } = await photoApi.getList(route.params.id)
  photos.value = data
}

const uploadPhoto = async ({ file }) => {
  try {
    await photoApi.upload(file, route.params.id)
    ElMessage.success('上传成功')
    loadPhotos()
  } catch (error) {
    ElMessage.error('上传失败')
  }
}

const openEditPhoto = (photo) => {
  editPhotoId.value = photo.id
  editForm.value = { description: photo.description || '' }
  showEditDialog.value = true
}

const updatePhoto = async () => {
  await photoApi.update(editPhotoId.value, editForm.value, currentUserId)
  showEditDialog.value = false
  editPhotoId.value = null
  loadPhotos()
}

const handleDeletePhoto = async (photo) => {
  try {
    await ElMessageBox.confirm(
      '此操作将删除该照片，数据将被标记为已删除。',
      '确认删除？',
      { confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning' }
    )
    await photoApi.delete(photo.id, currentUserId)
    ElMessage.success('删除成功')
    loadPhotos()
  } catch {}
}
</script>

<style scoped>
.album-detail-page {
  min-height: 100vh;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 40px 20px;
}

.header {
  max-width: 1200px;
  margin: 0 auto 40px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.header h1 {
  font-size: 36px;
  font-weight: 700;
  color: white;
  margin: 0;
}

.photo-grid {
  max-width: 1200px;
  margin: 0 auto;
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(250px, 1fr));
  gap: 20px;
}

.photo-card {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.1);
}

.photo-card img {
  width: 100%;
  height: 250px;
  object-fit: cover;
}

.photo-info {
  padding: 12px;
}

.photo-desc {
  font-size: 13px;
  color: #666;
  margin: 0 0 8px;
  min-height: 20px;
}

.photo-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
}
</style>

