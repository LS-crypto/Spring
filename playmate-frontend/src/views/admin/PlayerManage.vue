<template>
  <div>
    <div class="page-card">
      <div class="table-header">
        <h3>陪玩师审核</h3>
        <el-radio-group v-model="filter" @change="loadApplications">
          <el-radio-button value="pending">待审核</el-radio-button>
          <el-radio-button value="approved">已通过</el-radio-button>
          <el-radio-button value="rejected">已拒绝</el-radio-button>
        </el-radio-group>
      </div>
      <el-table :data="applications" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="nickname" label="昵称" />
        <el-table-column prop="realName" label="真实姓名" />
        <el-table-column prop="games" label="擅长游戏">
          <template #default="{ row }">
            <el-tag v-for="g in row.games" :key="g" size="small" style="margin-right: 4px;">{{ g }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="rank" label="段位" />
        <el-table-column prop="price" label="定价">
          <template #default="{ row }">¥{{ row.price }}/局</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" />
        <el-table-column label="操作" width="200">
          <template #default="{ row }">
            <template v-if="row.status === '待审核'">
              <el-button type="success" text @click="approve(row)">通过</el-button>
              <el-button type="danger" text @click="reject(row)">拒绝</el-button>
            </template>
            <el-button text @click="viewDetail(row)">查看详情</el-button>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminGetPlayerApplications, adminApprovePlayer, adminRejectPlayer } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const applications = ref([])
const filter = ref('pending')

onMounted(() => loadApplications())

async function loadApplications() {
  try {
    const res = await adminGetPlayerApplications({ status: filter.value })
    applications.value = res.data?.list || []
  } catch {
    applications.value = [
      { id: 1, nickname: '大神666', realName: '张三', games: ['英雄联盟', '王者荣耀'], rank: '钻石I', price: 40, status: '待审核' }
    ]
  }
}

async function approve(row) {
  await ElMessageBox.confirm(`通过「${row.nickname}」的陪玩师申请？`)
  await adminApprovePlayer(row.id)
  ElMessage.success('已通过')
  loadApplications()
}

async function reject(row) {
  const { value: reason } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝申请')
  await adminRejectPlayer(row.id, reason)
  ElMessage.success('已拒绝')
  loadApplications()
}

function viewDetail(row) { /* TODO: 详情弹窗 */ }
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
</style>
