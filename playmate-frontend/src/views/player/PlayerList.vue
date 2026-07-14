<template>
  <div class="page-container">
    <!-- 筛选栏 -->
    <div class="filter-bar page-card">
      <div class="filter-row">
        <span class="filter-label">游戏：</span>
        <el-radio-group v-model="filters.gameId" @change="loadPlayers">
          <el-radio-button :value="0">全部</el-radio-button>
          <el-radio-button v-for="g in games" :key="g.id" :value="g.id">{{ g.name }}</el-radio-button>
        </el-radio-group>
      </div>
      <div class="filter-row">
        <span class="filter-label">排序：</span>
        <el-radio-group v-model="filters.sort" @change="loadPlayers">
          <el-radio-button value="default">综合</el-radio-button>
          <el-radio-button value="price_asc">价格低到高</el-radio-button>
          <el-radio-button value="price_desc">价格高到低</el-radio-button>
          <el-radio-button value="rating">评分最高</el-radio-button>
          <el-radio-button value="orders">接单最多</el-radio-button>
        </el-radio-group>
      </div>
      <div class="filter-row">
        <span class="filter-label">价格：</span>
        <el-slider v-model="filters.priceRange" range :min="0" :max="200" :step="5" show-stops @change="loadPlayers" />
      </div>
    </div>

    <!-- 陪玩师列表 -->
    <div class="player-list">
      <div
        v-for="player in players"
        :key="player.id"
        class="player-list-item page-card"
        @click="$router.push(`/players/${player.id}`)"
      >
        <div class="pli-left">
          <el-avatar :size="80" :src="player.avatar">
            {{ player.nickname?.charAt(0) }}
          </el-avatar>
          <span class="online-badge" v-if="player.online">在线</span>
        </div>
        <div class="pli-center">
          <div class="pli-name">{{ player.nickname }}</div>
          <div class="pli-tags">
            <el-tag v-for="g in player.games" :key="g" size="small">{{ g }}</el-tag>
          </div>
          <div class="pli-desc">{{ player.description || '暂无简介' }}</div>
          <div class="pli-stats">
            <span>⭐ {{ player.rating || '5.0' }}</span>
            <span>📋 {{ player.orderCount || 0 }}单</span>
            <span>❤️ {{ player.likeCount || 0 }}赞</span>
          </div>
        </div>
        <div class="pli-right">
          <div class="pli-price">¥{{ player.price }}<small>/局</small></div>
          <el-button type="primary" @click.stop="goOrder(player.id)">下单</el-button>
        </div>
      </div>

      <el-empty v-if="players.length === 0" description="暂无符合条件的陪玩师" />
    </div>

    <!-- 分页 -->
    <div class="pagination-wrap" v-if="total > 0">
      <el-pagination
        v-model:current-page="page"
        :page-size="10"
        :total="total"
        layout="prev, pager, next"
        @current-change="loadPlayers"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getPlayerList, getGameCategories } from '../../api/player'

const route = useRoute()
const router = useRouter()
const games = ref([])
const players = ref([])
const total = ref(0)
const page = ref(1)

const filters = reactive({
  gameId: 0,
  sort: 'default',
  priceRange: [0, 200]
})

onMounted(async () => {
  if (route.query.gameId) {
    filters.gameId = Number(route.query.gameId)
  }
  try {
    const res = await getGameCategories()
    games.value = res.data || []
  } catch {
    games.value = [
      { id: 1, name: '英雄联盟' }, { id: 2, name: '王者荣耀' },
      { id: 3, name: '和平精英' }, { id: 4, name: '永劫无间' },
      { id: 5, name: 'CS2' }, { id: 6, name: 'DOTA2' }
    ]
  }
  loadPlayers()
})

async function loadPlayers() {
  try {
    const res = await getPlayerList({
      gameId: filters.gameId || undefined,
      sort: filters.sort,
      minPrice: filters.priceRange[0],
      maxPrice: filters.priceRange[1],
      page: page.value,
      pageSize: 10
    })
    players.value = res.data?.list || []
    total.value = res.data?.total || 0
  } catch {
    // 模拟数据
    players.value = [
      { id: 1, nickname: '小甜心', games: ['王者荣耀'], rating: '4.9', orderCount: 326, price: 30, online: true, likeCount: 128, description: '声音甜美，耐心教学，王者50星' },
      { id: 2, nickname: '技术流', games: ['英雄联盟', 'CS2'], rating: '5.0', orderCount: 512, price: 50, online: true, likeCount: 256, description: '前职业选手，保证上分体验' }
    ]
    total.value = 2
  }
}

function goOrder(playerId) {
  router.push({ path: '/orders/create', query: { playerId } })
}
</script>

<style scoped>
.filter-bar { margin-bottom: 20px; }
.filter-row {
  display: flex;
  align-items: center;
  margin-bottom: 16px;
}
.filter-row:last-child { margin-bottom: 0; }
.filter-label {
  font-weight: 600;
  width: 60px;
  flex-shrink: 0;
}

.player-list-item {
  display: flex;
  gap: 20px;
  margin-bottom: 16px;
  cursor: pointer;
  transition: all 0.2s;
}
.player-list-item:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 16px rgba(108, 92, 231, 0.12);
}

.pli-left { position: relative; flex-shrink: 0; }
.online-badge {
  position: absolute;
  bottom: -4px;
  left: 50%;
  transform: translateX(-50%);
  background: var(--success-color);
  color: #fff;
  font-size: 10px;
  padding: 1px 8px;
  border-radius: 8px;
}

.pli-center { flex: 1; }
.pli-name { font-size: 18px; font-weight: 700; margin-bottom: 8px; }
.pli-tags { display: flex; gap: 4px; margin-bottom: 8px; }
.pli-desc { font-size: 13px; color: var(--text-secondary); margin-bottom: 8px; }
.pli-stats { display: flex; gap: 16px; font-size: 13px; color: var(--text-secondary); }

.pli-right {
  display: flex;
  flex-direction: column;
  align-items: flex-end;
  justify-content: center;
  gap: 12px;
}
.pli-price { font-size: 24px; font-weight: 700; color: var(--accent-color); }
.pli-price small { font-size: 12px; font-weight: 400; color: var(--text-secondary); }

.pagination-wrap {
  display: flex;
  justify-content: center;
  margin-top: 24px;
}
</style>
