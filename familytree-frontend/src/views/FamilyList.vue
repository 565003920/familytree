<template>
  <div class="family-list-page">
    <div class="page-header">
      <h1>我的家族</h1>
      <el-button type="primary" @click="showDialog = true" size="large">
        <span>+ 创建家族</span>
      </el-button>
    </div>

    <div class="card-grid">
      <div
        v-for="family in families"
        :key="family.id"
        class="card clickable"
        @click="viewFamily(family.id)"
      >
        <div class="card-header">
          <h3>{{ family.name }}</h3>
          <span class="surname">{{ family.surname }}氏</span>
        </div>
        <div class="card-body">
          <div class="info-item">
            <span class="label">成员数</span>
            <span class="value">{{ family.memberCount || 0 }}人</span>
          </div>
          <div class="info-item" v-if="family.originPlace">
            <span class="label">祖籍</span>
            <span class="value">{{ family.originPlace }}</span>
          </div>
        </div>
        <div class="card-actions" v-if="family.adminId === currentUserId" @click.stop>
          <el-button size="small" @click="openEdit(family)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDelete(family)">删除</el-button>
        </div>
      </div>
    </div>

    <el-dialog v-model="showDialog" :title="isEdit ? '编辑家族' : '创建家族'">
      <el-form :model="form">
        <el-form-item label="家族名称">
          <el-input v-model="form.name" />
        </el-form-item>
        <el-form-item label="姓氏">
          <el-input v-model="form.surname" />
        </el-form-item>
        <el-form-item label="祖籍">
          <el-input v-model="form.originPlace" />
        </el-form-item>
        <el-form-item label="简介">
          <el-input v-model="form.description" type="textarea" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="isEdit ? updateFamily() : createFamily()">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useFamilyStore } from '../stores/family'
import { familyApi } from '../api/family'
import { useRouter } from 'vue-router'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const familyStore = useFamilyStore()
const families = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const currentUserId = 1
const form = ref({ name: '', surname: '', originPlace: '', description: '' })

const loadFamilies = async () => {
  const { data } = await familyApi.getList(currentUserId)
  families.value = data
}

onMounted(loadFamilies)

const createFamily = async () => {
  await familyApi.create({ ...form.value, adminId: currentUserId })
  showDialog.value = false
  form.value = { name: '', surname: '', originPlace: '', description: '' }
  loadFamilies()
}

const openEdit = (family) => {
  isEdit.value = true
  editId.value = family.id
  form.value = { name: family.name, surname: family.surname, originPlace: family.originPlace || '', description: family.description || '', adminId: family.adminId }
  showDialog.value = true
}

const updateFamily = async () => {
  await familyApi.update(editId.value, form.value)
  showDialog.value = false
  isEdit.value = false
  editId.value = null
  form.value = { name: '', surname: '', originPlace: '', description: '' }
  loadFamilies()
}

const handleDelete = async (family) => {
  try {
    await ElMessageBox.confirm(
      `此操作将删除「${family.name}」，数据将被标记为已删除。`,
      '确认删除？',
      { confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning' }
    )
    await familyApi.delete(family.id, currentUserId)
    ElMessage.success('删除成功')
    loadFamilies()
  } catch {}
}

const viewFamily = (id) => {
  const family = families.value.find(f => f.id === id)
  familyStore.setCurrentFamily(family)
  router.push(`/family/${id}`)
}
</script>

<style scoped>
.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 16px;
  padding-bottom: 16px;
  border-bottom: 2px solid var(--color-surface);
}

.card-header h3 {
  font-size: 24px;
  font-weight: 600;
  color: var(--color-text);
  font-family: var(--font-serif);
  margin: 0;
}

.surname {
  font-size: 16px;
  color: var(--color-accent);
  font-weight: 600;
}

.card-body {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.info-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.info-item .label {
  font-size: 14px;
  color: var(--color-text-muted);
}

.info-item .value {
  font-size: 16px;
  font-weight: 600;
  color: var(--color-text);
}

.card-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid var(--color-surface);
}
</style>
