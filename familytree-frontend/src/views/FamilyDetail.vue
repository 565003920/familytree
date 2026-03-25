<template>
  <div class="family-detail-page">
    <div class="page-header">
      <div class="title-section">
        <h1>{{ family.name }}</h1>
        <span class="surname">{{ family.surname }}氏</span>
      </div>
      <div class="actions">
        <el-button @click="$router.push(`/family/${route.params.id}/tree`)" size="large">
          查看家族树
        </el-button>
        <el-button @click="$router.push(`/family/${route.params.id}/albums`)" size="large">
          家族相册
        </el-button>
        <el-button type="primary" @click="showDialog = true" size="large">
          + 添加成员
        </el-button>
      </div>
    </div>

    <div class="card-grid">
      <div v-for="member in members" :key="member.id" class="card">
        <div class="avatar large">
          {{ member.name.charAt(0) }}
        </div>
        <div class="member-info">
          <h3>{{ member.name }}</h3>
          <div class="info-row">
            <span class="label">性别</span>
            <span class="value">{{ member.gender === 'male' ? '男' : '女' }}</span>
          </div>
          <div class="info-row" v-if="member.birthDate">
            <span class="label">出生</span>
            <span class="value">{{ member.birthDate }}</span>
          </div>
          <div class="info-row" v-if="member.generation">
            <span class="label">辈分</span>
            <span class="value">{{ member.generation }}</span>
          </div>
        </div>
        <div class="member-actions" v-if="isAdmin">
          <el-button size="small" @click="openEditMember(member)">编辑</el-button>
          <el-button size="small" type="danger" @click="handleDeleteMember(member)">删除</el-button>
        </div>
      </div>
    </div>

    <el-dialog v-model="showDialog" :title="isEdit ? '编辑成员' : '添加成员'" width="600px">
      <el-form :model="form" label-width="80px">
        <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 0 16px;">
          <el-form-item label="姓名">
            <el-input v-model="form.name" />
          </el-form-item>
          <el-form-item label="性别">
            <el-select v-model="form.gender" style="width: 100%">
              <el-option label="男" value="male" />
              <el-option label="女" value="female" />
            </el-select>
          </el-form-item>
          <el-form-item label="出生日期">
            <el-date-picker v-model="form.birthDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
          <el-form-item label="逝世日期">
            <el-date-picker v-model="form.deathDate" type="date" value-format="YYYY-MM-DD" style="width: 100%" />
          </el-form-item>
          <el-form-item label="辈分">
            <el-input v-model="form.generation" />
          </el-form-item>
          <el-form-item label="排行">
            <el-input-number v-model="form.ranking" :min="1" style="width: 100%" />
          </el-form-item>
          <el-form-item label="父亲">
            <el-select v-model="form.fatherId" clearable style="width: 100%">
              <el-option v-for="m in malemembers" :key="m.id" :label="m.name" :value="m.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="母亲">
            <el-select v-model="form.motherId" clearable style="width: 100%">
              <el-option v-for="m in femalemembers" :key="m.id" :label="m.name" :value="m.id" />
            </el-select>
          </el-form-item>
          <el-form-item label="配偶">
            <el-select v-model="form.spouseId" clearable style="width: 100%">
              <el-option v-for="m in members" :key="m.id" :label="m.name" :value="m.id" />
            </el-select>
          </el-form-item>
        </div>
        <el-form-item label="生平">
          <el-input v-model="form.bio" type="textarea" :rows="3" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="isEdit ? updateMember() : addMember()">确定</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted, computed } from 'vue'
import { useRoute } from 'vue-router'
import { familyApi, memberApi } from '../api/family'
import { ElMessage, ElMessageBox } from 'element-plus'

const route = useRoute()
const family = ref({})
const members = ref([])
const showDialog = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const currentUserId = 1
const defaultForm = { name: '', gender: '', birthDate: null, deathDate: null, generation: '', ranking: null, bio: '', fatherId: null, motherId: null, spouseId: null }
const form = ref({ ...defaultForm })

const isAdmin = computed(() => family.value.adminId === currentUserId)
const malemembers = computed(() => members.value.filter(m => m.gender === 'male'))
const femalemembers = computed(() => members.value.filter(m => m.gender === 'female'))

const loadMembers = async () => {
  members.value = (await memberApi.getList(route.params.id)).data
}

onMounted(async () => {
  family.value = (await familyApi.getById(route.params.id)).data
  loadMembers()
})

const addMember = async () => {
  await memberApi.create({ ...form.value, familyId: route.params.id })
  showDialog.value = false
  form.value = { ...defaultForm }
  loadMembers()
}

const openEditMember = (member) => {
  isEdit.value = true
  editId.value = member.id
  form.value = {
    name: member.name, gender: member.gender, birthDate: member.birthDate, deathDate: member.deathDate,
    generation: member.generation || '', ranking: member.ranking, bio: member.bio || '',
    fatherId: member.fatherId, motherId: member.motherId, spouseId: member.spouseId
  }
  showDialog.value = true
}

const updateMember = async () => {
  await memberApi.update(editId.value, form.value, currentUserId)
  showDialog.value = false
  isEdit.value = false
  editId.value = null
  form.value = { ...defaultForm }
  loadMembers()
}

const handleDeleteMember = async (member) => {
  try {
    await ElMessageBox.confirm(
      `此操作将删除「${member.name}」，数据将被标记为已删除。`,
      '确认删除？',
      { confirmButtonText: '确认删除', cancelButtonText: '取消', type: 'warning' }
    )
    await memberApi.delete(member.id, currentUserId)
    ElMessage.success('删除成功')
    loadMembers()
  } catch {}
}
</script>

<style scoped>
.title-section {
  display: flex;
  align-items: center;
  gap: 16px;
}

.title-section h1 {
  font-size: 28px;
  font-weight: 700;
  color: var(--color-text);
  font-family: var(--font-serif);
  margin: 0;
  letter-spacing: 2px;
}

.surname {
  font-size: 20px;
  color: var(--color-accent);
  font-weight: 600;
}

.member-info h3 {
  font-size: 20px;
  font-weight: 600;
  color: var(--color-text);
  margin: 0 0 12px 0;
  text-align: center;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 8px 0;
  border-bottom: 1px solid var(--color-surface);
}

.info-row:last-child {
  border-bottom: none;
}

.info-row .label {
  font-size: 14px;
  color: var(--color-text-muted);
}

.info-row .value {
  font-size: 14px;
  font-weight: 600;
  color: var(--color-text);
}

.member-actions {
  display: flex;
  justify-content: center;
  gap: 8px;
  margin-top: 16px;
  padding-top: 12px;
  border-top: 1px solid var(--color-surface);
}
</style>

