<template>
  <div>
    <div class="page-card">
      <div class="table-header">
        <h3>游戏分类管理</h3>
        <el-button type="primary" @click="showDialog = true">添加游戏</el-button>
      </div>
      <el-table :data="games" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="icon" label="图标" width="80">
          <template #default="{ row }">{{ row.icon || '🎮' }}</template>
        </el-table-column>
        <el-table-column prop="name" label="游戏名称" />
        <el-table-column prop="playerCount" label="陪玩师数" />
        <el-table-column prop="orderCount" label="订单数" />
        <el-table-column prop="sort" label="排序" width="80" />
        <el-table-column label="操作" width="160">
          <template #default="{ row }">
            <el-button text type="primary" @click="editGame(row)">编辑</el-button>
            <el-button text type="danger" @click="deleteGame(row)">删除</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>

    <!-- 添加/编辑弹窗 -->
    <el-dialog v-model="showDialog" :title="editingId ? '编辑游戏' : '添加游戏'" width="460px">
      <el-form :model="gameForm" label-position="top">
        <el-form-item label="游戏名称">
          <el-input v-model="gameForm.name" placeholder="如：英雄联盟" />
        </el-form-item>
        <el-form-item label="图标（emoji）">
          <el-input v-model="gameForm.icon" placeholder="如：⚔️" />
        </el-form-item>
        <el-form-item label="排序">
          <el-input-number v-model="gameForm.sort" :min="0" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showDialog = false">取消</el-button>
        <el-button type="primary" @click="saveGame">保存</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { adminCreateGame, adminUpdateGame, adminDeleteGame } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const games = ref([])
const showDialog = ref(false)
const editingId = ref(null)

const gameForm = reactive({ name: '', icon: '', sort: 0 })

onMounted(() => loadGames())

async function loadGames() {
  try {
    // const res = await request.get('/admin/games')
    // games.value = res.data
  } catch {
    games.value = [
      { id: 1, icon: '⚔️', name: '英雄联盟', playerCount: 128, orderCount: 2580, sort: 1 },
      { id: 2, icon: '👑', name: '王者荣耀', playerCount: 256, orderCount: 3260, sort: 2 },
      { id: 3, icon: '🔫', name: '和平精英', playerCount: 98, orderCount: 1890, sort: 3 },
      { id: 4, icon: '🗡️', name: '永劫无间', playerCount: 64, orderCount: 980, sort: 4 }
    ]
  }
}

function editGame(row) {
  editingId.value = row.id
  Object.assign(gameForm, { name: row.name, icon: row.icon, sort: row.sort })
  showDialog.value = true
}

async function saveGame() {
  if (editingId.value) {
    await adminUpdateGame(editingId.value, gameForm)
  } else {
    await adminCreateGame(gameForm)
  }
  ElMessage.success('保存成功')
  showDialog.value = false
  editingId.value = null
  loadGames()
}

async function deleteGame(row) {
  await ElMessageBox.confirm(`确定删除游戏「${row.name}」？`, '警告', { type: 'warning' })
  await adminDeleteGame(row.id)
  ElMessage.success('已删除')
  loadGames()
}
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
</style>
