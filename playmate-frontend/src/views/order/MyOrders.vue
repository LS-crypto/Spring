<template>
  <div class="page-container">
    <h2 style="margin-bottom: 24px;">我的订单</h2>

    <!-- 订单筛选 -->
    <div class="page-card" style="margin-bottom: 20px;">
      <el-tabs v-model="activeTab" @tab-change="loadOrders">
        <el-tab-pane label="全部" name="all" />
        <el-tab-pane label="待接单" name="pending" />
        <el-tab-pane label="进行中" name="active" />
        <el-tab-pane label="待确认" name="completing" />
        <el-tab-pane label="已完成" name="completed" />
        <el-tab-pane label="已取消" name="cancelled" />
      </el-tabs>
    </div>

    <!-- 订单列表 -->
    <div v-for="order in orders" :key="order.id" class="order-item page-card">
      <div class="order-header">
        <span class="order-no">订单号：{{ order.orderNo }}</span>
        <el-tag :type="statusTagType(order.status)" size="small">{{ statusText(order.status) }}</el-tag>
      </div>
      <div class="order-body" @click="$router.push(`/orders/${order.id}`)">
        <div class="order-player">
          <el-avatar :size="48" :src="order.playerAvatar">
            {{ order.playerName?.charAt(0) }}
          </el-avatar>
          <div class="order-info">
            <div class="order-player-name">{{ order.playerName }}</div>
            <div class="order-game">{{ order.gameName }} × {{ order.quantity }}局</div>
          </div>
        </div>
        <div class="order-price">¥{{ order.totalPrice }}</div>
      </div>
      <div class="order-actions">
        <el-button v-if="order.status === 'pending'" type="danger" text @click="handleCancel(order.id)">
          取消订单
        </el-button>
        <el-button v-if="order.status === 'completing'" type="primary" @click="handleConfirm(order.id)">
          确认完成
        </el-button>
        <el-button v-if="order.status === 'completed' && !order.reviewed" type="warning" text @click="goReview(order.id)">
          去评价
        </el-button>
        <el-button text @click="$router.push(`/orders/${order.id}`)">查看详情</el-button>
      </div>
    </div>

    <el-empty v-if="orders.length === 0" description="暂无订单" />
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getMyOrders, cancelOrder, confirmOrder } from '../../api/order'
import { ElMessage, ElMessageBox } from 'element-plus'

const router = useRouter()
const orders = ref([])
const activeTab = ref('all')

const statusMap = {
  pending: '待接单', active: '进行中', completing: '待确认',
  completed: '已完成', cancelled: '已取消', refunded: '已退款'
}

function statusText(status) { return statusMap[status] || status }
function statusTagType(status) {
  const map = { pending: 'warning', active: 'primary', completing: '', completed: 'success', cancelled: 'info', refunded: 'danger' }
  return map[status] || ''
}

onMounted(() => loadOrders())

async function loadOrders() {
  try {
    const res = await getMyOrders({ status: activeTab.value === 'all' ? undefined : activeTab.value })
    orders.value = res.data?.list || []
  } catch {
    orders.value = [
      { id: 1, orderNo: 'PM20260714001', status: 'active', playerName: '技术流', playerAvatar: '', gameName: '英雄联盟', quantity: 3, totalPrice: 150 },
      { id: 2, orderNo: 'PM20260713002', status: 'completed', playerName: '小甜心', playerAvatar: '', gameName: '王者荣耀', quantity: 2, totalPrice: 60, reviewed: false }
    ]
  }
}

async function handleCancel(orderId) {
  await ElMessageBox.confirm('确定取消此订单？', '提示', { type: 'warning' })
  await cancelOrder(orderId, '用户主动取消')
  ElMessage.success('订单已取消')
  loadOrders()
}

async function handleConfirm(orderId) {
  await ElMessageBox.confirm('确认订单已完成？', '确认完成')
  await confirmOrder(orderId)
  ElMessage.success('已确认完成')
  loadOrders()
}

function goReview(orderId) {
  router.push({ path: `/orders/${orderId}`, query: { review: 1 } })
}
</script>

<style scoped>
.order-item { margin-bottom: 12px; }
.order-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}
.order-no { font-size: 13px; color: var(--text-secondary); }
.order-body {
  display: flex;
  justify-content: space-between;
  align-items: center;
  cursor: pointer;
  padding: 8px 0;
}
.order-player { display: flex; gap: 12px; align-items: center; }
.order-player-name { font-weight: 600; }
.order-game { font-size: 13px; color: var(--text-secondary); }
.order-price { font-size: 20px; font-weight: 700; color: var(--accent-color); }
.order-actions {
  display: flex;
  justify-content: flex-end;
  gap: 8px;
  border-top: 1px solid var(--border-color);
  padding-top: 12px;
}
</style>
