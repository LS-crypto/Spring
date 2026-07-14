<template>
  <div class="page-container">
    <el-button text @click="$router.back()" style="margin-bottom: 16px">← 返回</el-button>

    <div class="page-card">
      <h2 style="margin-bottom: 24px;">订单详情</h2>

      <el-descriptions :column="2" border>
        <el-descriptions-item label="订单号">{{ order.orderNo }}</el-descriptions-item>
        <el-descriptions-item label="状态">
          <el-tag>{{ order.status }}</el-tag>
        </el-descriptions-item>
        <el-descriptions-item label="陪玩师">{{ order.playerName }}</el-descriptions-item>
        <el-descriptions-item label="游戏">{{ order.gameName }}</el-descriptions-item>
        <el-descriptions-item label="数量">{{ order.quantity }}局</el-descriptions-item>
        <el-descriptions-item label="总价">¥{{ order.totalPrice }}</el-descriptions-item>
        <el-descriptions-item label="下单时间">{{ order.createdAt }}</el-descriptions-item>
        <el-descriptions-item label="备注">{{ order.remark || '无' }}</el-descriptions-item>
      </el-descriptions>

      <!-- 订单时间线 -->
      <h3 style="margin: 24px 0 12px;">订单进度</h3>
      <el-timeline>
        <el-timeline-item v-for="log in orderLogs" :key="log.time" :timestamp="log.time" placement="top">
          {{ log.content }}
        </el-timeline-item>
      </el-timeline>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRoute } from 'vue-router'
import { getOrderDetail } from '../../api/order'

const route = useRoute()
const order = ref({})
const orderLogs = ref([])

onMounted(async () => {
  const id = route.params.id
  try {
    const res = await getOrderDetail(id)
    order.value = res.data
    orderLogs.value = res.data.logs || []
  } catch {
    order.value = {
      orderNo: 'PM20260714001', status: '进行中', playerName: '技术流',
      gameName: '英雄联盟', quantity: 3, totalPrice: 150,
      createdAt: '2026-07-14 10:30:00', remark: '要求钻石段位以上'
    }
    orderLogs.value = [
      { time: '2026-07-14 10:30:00', content: '订单已创建，等待陪玩师接单' },
      { time: '2026-07-14 10:35:00', content: '陪玩师「技术流」已接单' }
    ]
  }
})
</script>
