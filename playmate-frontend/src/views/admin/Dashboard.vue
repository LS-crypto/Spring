<template>
  <div>
    <h2 style="margin-bottom: 24px;">数据概览</h2>

    <!-- 数据卡片 -->
    <div class="dashboard-cards">
      <div class="dash-card" v-for="item in dashData" :key="item.label">
        <div class="dash-icon">{{ item.icon }}</div>
        <div class="dash-info">
          <div class="dash-value">{{ item.value }}</div>
          <div class="dash-label">{{ item.label }}</div>
        </div>
        <div class="dash-trend" :class="item.trend > 0 ? 'up' : 'down'">
          {{ item.trend > 0 ? '+' : '' }}{{ item.trend }}%
        </div>
      </div>
    </div>

    <!-- 图表区域 -->
    <div class="chart-row">
      <div class="page-card" style="flex: 2;">
        <h3 style="margin-bottom: 16px;">近7天订单趋势</h3>
        <div class="chart-placeholder">
          <p style="color: var(--text-secondary);">📊 图表区域 — 接入后端后使用 ECharts 渲染</p>
        </div>
      </div>
      <div class="page-card" style="flex: 1;">
        <h3 style="margin-bottom: 16px;">热门游戏</h3>
        <div v-for="(game, idx) in topGames" :key="game.name" class="top-game-item">
          <span class="rank">{{ idx + 1 }}</span>
          <span>{{ game.name }}</span>
          <span class="count">{{ game.count }}单</span>
        </div>
      </div>
    </div>

    <!-- 最近订单 -->
    <div class="page-card" style="margin-top: 24px;">
      <h3 style="margin-bottom: 16px;">最近订单</h3>
      <el-table :data="recentOrders" stripe>
        <el-table-column prop="orderNo" label="订单号" />
        <el-table-column prop="userName" label="用户" />
        <el-table-column prop="playerName" label="陪玩师" />
        <el-table-column prop="gameName" label="游戏" />
        <el-table-column prop="totalPrice" label="金额">
          <template #default="{ row }">¥{{ row.totalPrice }}</template>
        </el-table-column>
        <el-table-column prop="status" label="状态">
          <template #default="{ row }">
            <el-tag size="small">{{ row.status }}</el-tag>
          </template>
        </el-table-column>
        <el-table-column prop="createdAt" label="时间" />
      </el-table>
    </div>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { adminGetDashboard } from '../../api/admin'

const dashData = ref([])
const topGames = ref([])
const recentOrders = ref([])

onMounted(async () => {
  try {
    const res = await adminGetDashboard()
    // use res.data
  } catch {
    dashData.value = [
      { icon: '👥', label: '注册用户', value: '1,286', trend: 12.5 },
      { icon: '🎮', label: '陪玩师', value: '89', trend: 8.3 },
      { icon: '📋', label: '今日订单', value: '156', trend: 23.1 },
      { icon: '💰', label: '今日营收', value: '¥8,960', trend: 15.7 }
    ]
    topGames.value = [
      { name: '王者荣耀', count: 326 },
      { name: '英雄联盟', count: 258 },
      { name: '和平精英', count: 189 },
      { name: '永劫无间', count: 98 },
      { name: '原神', count: 72 }
    ]
    recentOrders.value = [
      { orderNo: 'PM20260714001', userName: '小明', playerName: '技术流', gameName: '英雄联盟', totalPrice: 150, status: '进行中', createdAt: '2026-07-14 10:30' },
      { orderNo: 'PM20260714002', userName: '小红', playerName: '小甜心', gameName: '王者荣耀', totalPrice: 60, status: '已完成', createdAt: '2026-07-14 09:15' }
    ]
  }
})
</script>

<style scoped>
.dashboard-cards {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.dash-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px;
  display: flex;
  align-items: center;
  gap: 16px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}

.dash-icon { font-size: 36px; }
.dash-info { flex: 1; }
.dash-value { font-size: 28px; font-weight: 800; }
.dash-label { font-size: 13px; color: var(--text-secondary); }
.dash-trend { font-size: 13px; font-weight: 600; }
.dash-trend.up { color: var(--success-color); }
.dash-trend.down { color: var(--danger-color); }

.chart-row { display: flex; gap: 20px; }
.chart-placeholder {
  height: 240px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: var(--bg-color);
  border-radius: 8px;
}

.top-game-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 10px 0;
  border-bottom: 1px solid var(--border-color);
}
.top-game-item .rank {
  width: 24px;
  height: 24px;
  background: var(--primary-color);
  color: #fff;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 12px;
  font-weight: 700;
}
.top-game-item .count { margin-left: auto; color: var(--text-secondary); font-size: 13px; }
</style>
