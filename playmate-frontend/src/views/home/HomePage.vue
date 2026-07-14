<template>
  <div class="home-page">
    <!-- Hero 区域 -->
    <section class="hero-section">
      <div class="hero-content">
        <h1>找到你的专属游戏搭档</h1>
        <p>专业陪玩师在线接单，带你上分带你飞</p>
        <div class="hero-search">
          <el-select v-model="selectedGame" placeholder="选择游戏" size="large" clearable>
            <el-option
              v-for="game in games"
              :key="game.id"
              :label="game.name"
              :value="game.id"
            />
          </el-select>
          <el-button type="primary" size="large" @click="goPlayerList">立即找陪玩</el-button>
        </div>
      </div>
    </section>

    <!-- 热门游戏分类 -->
    <section class="section">
      <div class="page-container">
        <h2 class="section-title">热门游戏</h2>
        <div class="game-grid">
          <div
            v-for="game in games"
            :key="game.id"
            class="game-card"
            @click="selectGame(game.id)"
          >
            <div class="game-icon">{{ game.icon || '🎮' }}</div>
            <div class="game-name">{{ game.name }}</div>
            <div class="game-count">{{ game.playerCount || 0 }}位陪玩</div>
          </div>
        </div>
      </div>
    </section>

    <!-- 推荐陪玩师 -->
    <section class="section">
      <div class="page-container">
        <div class="section-header">
          <h2 class="section-title">推荐陪玩师</h2>
          <el-button text type="primary" @click="$router.push('/players')">查看更多 →</el-button>
        </div>
        <div class="player-grid">
          <div
            v-for="player in recommendedPlayers"
            :key="player.id"
            class="player-card page-card"
            @click="$router.push(`/players/${player.id}`)"
          >
            <div class="player-avatar">
              <el-avatar :size="64" :src="player.avatar">
                {{ player.nickname?.charAt(0) }}
              </el-avatar>
              <span class="online-dot" v-if="player.online"></span>
            </div>
            <div class="player-info">
              <div class="player-name">{{ player.nickname }}</div>
              <div class="player-games">
                <el-tag v-for="g in player.games" :key="g" size="small" type="info">{{ g }}</el-tag>
              </div>
              <div class="player-meta">
                <span class="rating">⭐ {{ player.rating || '5.0' }}</span>
                <span class="orders">{{ player.orderCount || 0 }}单</span>
              </div>
              <div class="player-price">
                ¥{{ player.price || '0' }}<small>/局</small>
              </div>
            </div>
          </div>
        </div>
      </div>
    </section>

    <!-- 平台数据 -->
    <section class="section stats-section">
      <div class="page-container">
        <div class="stats-grid">
          <div class="stat-item">
            <div class="stat-number">10,000+</div>
            <div class="stat-label">注册陪玩师</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">50,000+</div>
            <div class="stat-label">完成订单</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">4.9</div>
            <div class="stat-label">平均评分</div>
          </div>
          <div class="stat-item">
            <div class="stat-number">99%</div>
            <div class="stat-label">好评率</div>
          </div>
        </div>
      </div>
    </section>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { getGameCategories } from '../../api/player'

const router = useRouter()
const selectedGame = ref('')
const games = ref([])
const recommendedPlayers = ref([])

onMounted(async () => {
  try {
    const res = await getGameCategories()
    games.value = res.data || []
  } catch (e) {
    // 接口未就绪时使用模拟数据
    games.value = [
      { id: 1, name: '英雄联盟', icon: '⚔️', playerCount: 128 },
      { id: 2, name: '王者荣耀', icon: '👑', playerCount: 256 },
      { id: 3, name: '和平精英', icon: '🔫', playerCount: 98 },
      { id: 4, name: '永劫无间', icon: '🗡️', playerCount: 64 },
      { id: 5, name: 'CS2', icon: '💣', playerCount: 45 },
      { id: 6, name: 'DOTA2', icon: '🛡️', playerCount: 38 },
      { id: 7, name: '原神', icon: '✨', playerCount: 72 },
      { id: 8, name: '第五人格', icon: '🎭', playerCount: 55 }
    ]
    // 模拟推荐陪玩师
    recommendedPlayers.value = [
      { id: 1, nickname: '小甜心', avatar: '', games: ['王者荣耀', '原神'], rating: '4.9', orderCount: 326, price: '30', online: true },
      { id: 2, nickname: '技术流', avatar: '', games: ['英雄联盟', 'CS2'], rating: '5.0', orderCount: 512, price: '50', online: true },
      { id: 3, nickname: '暖心酱', avatar: '', games: ['和平精英', '永劫无间'], rating: '4.8', orderCount: 198, price: '25', online: false },
      { id: 4, nickname: '大神666', avatar: '', games: ['王者荣耀', '英雄联盟'], rating: '4.9', orderCount: 889, price: '40', online: true }
    ]
  }
})

function selectGame(gameId) {
  router.push({ path: '/players', query: { gameId } })
}

function goPlayerList() {
  const query = selectedGame.value ? { gameId: selectedGame.value } : {}
  router.push({ path: '/players', query })
}
</script>

<style scoped>
.hero-section {
  background: linear-gradient(135deg, #6c5ce7 0%, #a29bfe 50%, #fd79a8 100%);
  padding: 80px 24px;
  text-align: center;
  color: #fff;
}

.hero-content h1 {
  font-size: 42px;
  font-weight: 800;
  margin-bottom: 16px;
}

.hero-content p {
  font-size: 18px;
  opacity: 0.9;
  margin-bottom: 32px;
}

.hero-search {
  display: flex;
  gap: 12px;
  justify-content: center;
  max-width: 500px;
  margin: 0 auto;
}

.hero-search .el-select {
  flex: 1;
}

.section {
  padding: 48px 0;
}

.section-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.section-title {
  font-size: 24px;
  font-weight: 700;
  margin-bottom: 24px;
}

.game-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(140px, 1fr));
  gap: 16px;
}

.game-card {
  background: #fff;
  border-radius: 12px;
  padding: 24px 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
  border: 2px solid transparent;
}

.game-card:hover {
  border-color: var(--primary-color);
  transform: translateY(-2px);
}

.game-icon { font-size: 40px; margin-bottom: 8px; }
.game-name { font-weight: 600; margin-bottom: 4px; }
.game-count { font-size: 12px; color: var(--text-secondary); }

.player-grid {
  display: grid;
  grid-template-columns: repeat(auto-fill, minmax(260px, 1fr));
  gap: 20px;
}

.player-card {
  display: flex;
  gap: 16px;
  align-items: flex-start;
}

.player-avatar {
  position: relative;
  flex-shrink: 0;
}

.online-dot {
  position: absolute;
  bottom: 2px;
  right: 2px;
  width: 12px;
  height: 12px;
  background: var(--success-color);
  border-radius: 50%;
  border: 2px solid #fff;
}

.player-info { flex: 1; }
.player-name { font-weight: 600; font-size: 16px; margin-bottom: 6px; }
.player-games { display: flex; gap: 4px; flex-wrap: wrap; margin-bottom: 8px; }
.player-meta { font-size: 13px; color: var(--text-secondary); margin-bottom: 8px; display: flex; gap: 12px; }
.player-price { font-size: 20px; font-weight: 700; color: var(--accent-color); }
.player-price small { font-size: 12px; font-weight: 400; color: var(--text-secondary); }

.stats-section {
  background: linear-gradient(135deg, #2d3436, #636e72);
  color: #fff;
}

.stats-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 24px;
  text-align: center;
}

.stat-number { font-size: 36px; font-weight: 800; margin-bottom: 8px; }
.stat-label { font-size: 14px; opacity: 0.8; }
</style>
