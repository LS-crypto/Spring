<template>
  <div>
    <div class="page-card">
      <div class="table-header">
        <h3>提现审核</h3>
        <el-radio-group v-model="filter" @change="loadRecords">
          <el-radio-button value="pending">待审核</el-radio-button>
          <el-radio-button value="approved">已通过</el-radio-button>
          <el-radio-button value="rejected">已拒绝</el-radio-button>
        </el-radio-group>
      </div>
      <el-table :data="records" stripe>
        <el-table-column prop="id" label="ID" width="60" />
        <el-table-column prop="playerName" label="陪玩师" />
        <el-table-column prop="amount" label="金额" width="120">
          <template #default="{ row }">¥{{ row.amount }}</template>
        </el-table-column>
        <el-table-column prop="method" label="提现方式" width="100">
          <template #default="{ row }">
            {{ { alipay: '支付宝', wechat: '微信', bank: '银行卡' }[row.method] || row.method }}
          </template>
        </el-table-column>
        <el-table-column prop="account" label="收款账号" />
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag :type="row.status === '待审核' ? 'warning' : row.status === '已通过' ? 'success' : 'danger'" size="small">
              {{ row.status }}
            </el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="申请时间" width="180" />
        <el-table-column label="操作" width="180">
          <template #default="{ row }">
            <template v-if="row.status === '待审核'">
              <el-button type="success" text @click="approve(row)">通过</el-button>
              <el-button type="danger" text @click="reject(row)">拒绝</el-button>
            </template>
          </template>
        </el-table-column>
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminGetWithdrawRequests, adminApproveWithdraw, adminRejectWithdraw } from '../../api/admin'
import { ElMessage, ElMessageBox } from 'element-plus'

const records = ref([])
const filter = ref('pending')

onMounted(() => loadRecords())

async function loadRecords() {
  try {
    const res = await adminGetWithdrawRequests({ status: filter.value })
    records.value = res.data?.list || []
  } catch {
    records.value = [
      { id: 1, playerName: '技术流', amount: 500, method: 'alipay', account: 'tech***@163.com', status: '待审核', createdAt: '2026-07-14 11:00' },
      { id: 2, playerName: '小甜心', amount: 200, method: 'wechat', account: 'sweety_wx', status: '待审核', createdAt: '2026-07-13 18:30' }
    ]
  }
}

async function approve(row) {
  await ElMessageBox.confirm(`确认通过「${row.playerName}」的 ¥${row.amount} 提现申请？`)
  await adminApproveWithdraw(row.id)
  ElMessage.success('已通过')
  loadRecords()
}

async function reject(row) {
  const { value: reason } = await ElMessageBox.prompt('请输入拒绝原因', '拒绝提现')
  await adminRejectWithdraw(row.id, reason)
  ElMessage.success('已拒绝')
  loadRecords()
}
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
</style>
