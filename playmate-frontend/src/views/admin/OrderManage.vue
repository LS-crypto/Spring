<template>
  <div>
    <div class="page-card">
      <div class="table-header">
        <h3>订单管理</h3>
        <div class="table-actions">
          <el-select v-model="filterStatus" placeholder="订单状态" clearable @change="loadOrders" style="width: 140px;">
            <el-option label="全部" value="" />
            <el-option label="待接单" value="pending" />
            <el-option label="进行中" value="active" />
            <el-option label="已完成" value="completed" />
            <el-option label="已取消" value="cancelled" />
          </el-select>
          <el-input v-model="searchNo" placeholder="搜索订单号" style="width: 200px;" prefix-icon="Search" @input="loadOrders" />
        </div>
      </div>
      <el-table :data="orders" stripe>
        <el-table-column prop="orderNo" label="订单号" width="180" />
        <el-table-column prop="userName" label="用户" />
        <el-table-column prop="playerName" label="陪玩师" />
        <el-table-column prop="gameName" label="游戏" />
        <el-table-column prop="quantity" label="局数" width="60" />
        <el-table-column prop="totalPrice" label="金额" width="100">
          <template #default="{ row }">¥{{ row.totalPrice }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态" width="100">
          <template #default="{ row }">
            <el-tag size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="创建时间" width="180" />
      </el-table>
      <el-pagination v-model:current-page="page" :total="total" :page-size="10" layout="prev, pager, next" style="margin-top: 16px; justify-content: center;" />
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminGetOrders } from '../../api/admin'

const orders = ref([])
const filterStatus = ref('')
const searchNo = ref('')
const page = ref(1)
const total = ref(0)

onMounted(() => loadOrders())

async function loadOrders() {
  try {
    const res = await adminGetOrders({ status: filterStatus.value, orderNo: searchNo.value, page: page.value })
    orders.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {
    orders.value = [
      { orderNo: 'PM20260714001', userName: '小明', playerName: '技术流', gameName: '英雄联盟', quantity: 3, totalPrice: 150, status: '进行中', createdAt: '2026-07-14 10:30' },
      { orderNo: 'PM20260714002', userName: '小红', playerName: '小甜心', gameName: '王者荣耀', quantity: 2, totalPrice: 60, status: '已完成', createdAt: '2026-07-14 09:15' }
    ]
    total.value = 2
  }
}
</script>

<style scoped>
.table-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 16px; }
.table-actions { display: flex; gap: 12px; }
</style>
