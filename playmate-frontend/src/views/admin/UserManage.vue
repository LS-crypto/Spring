<template>
  <div>
    <div class="page-card">
      <div class="table-header">
        <h3>用户管理</h3>
        <el-input v-model="search" placeholder="搜索用户..." style="width: 240px" prefix-icon="Search" @input="loadUsers" />
      </div>
      <el-table :data="users" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="phone" label="手机号" />
        <el-table-column prop="role" label="角色">
          <template #default="{ row }">
            <el-tag :type="row.role === 'admin' ? 'danger' : row.role === 'player' ? 'primary' : 'info'" size="small">
              {{ roleText(row.role) }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag :type="row.banned ? 'danger' : 'success'" size="small">
              {{ row.banned ? '已封禁' : '正常' }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="注册时间" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <el-button text type="primary" @click="editUser(row)">编辑</el-button>
            <el-button text :type="row.banned ? 'success' : 'danger'" @click="toggleBan(row)">
              {{ row.banned ? '解封' : '封禁' }}
            </el-button>
          </template>
        </el-table-column>
      </el-table>
      <el-pagination v-model:current-page="page" :total="total" :page-size="10" layout="prev, pager, next" style="margin-top: 16px; justify-content: center;" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminGetUsers, adminBanUser } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const users = ref([])
const search = ref('')
const page = ref(1)
const total = ref(0)

function roleText(role) {
  return { user: '普通用户', player: '陪玩师', admin: '管理员' }[role] || role
}

onMounted(() => loadUsers())

async function loadUsers() {
  try {
    const res = await adminGetUsers({ search: search.value, page: page.value })
    users.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {
    users.value = [
      { id: 1, nickname: '小明', phone: '138****1234', role: 'user', banned: false, createdAt: '2026-07-01' },
      { id: 2, nickname: '技术流', phone: '139****5678', role: 'player', banned: false, createdAt: '2026-06-28' }
    ]
    total.value = 2
  }
}

function editUser(row) { /* TODO: 弹窗编辑 */ }

async function toggleBan(row) {
  const action = row.banned ? '解封' : '封禁'
  await ElMessageBox.confirm(`确定${action}用户「${row.nickname}」？`)
  await adminBanUser(row.id, !row.banned)
  ElMessage.success(`已${action}`)
  row.banned = !row.banned
}
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
</style>
