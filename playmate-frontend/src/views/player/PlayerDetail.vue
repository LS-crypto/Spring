<template>
  <div class="page-container">
    <el-button text @click="$router.back()" style="margin-bottom: 16px">← 返回</el-button>

    <div class="detail-layout">
      <!-- 左侧：陪玩师信息 -->
      <div class="detail-left page-card">
        <div class="profile-header">
          <el-avatar :size="100" :src="player.avatar">
            {{ player.nickname?.charAt(0) }}
          </el-avatar>
          <div class="profile-basic">
            <h2>{{ player.nickname }}</h2>
            <div class="profile-tags">
              <el-tag v-for="g in player.games" :key="g" type="primary">{{ g }}</el-tag>
            </div>
            <div class="profile-stats">
              <div class="stat"><strong>{{ player.rating || '5.0' }}</strong><span>评分</span></div>
              <div class="stat"><strong>{{ player.orderCount || 0 }}</strong><span>接单</span></div>
              <div class="stat"><strong>{{ player.likeCount || 0 }}</strong><span>获赞</span></div>
            </div>
          </div>
        </div>

        <el-divider />

        <div class="profile-section">
          <h3>个人介绍</h3>
          <p>{{ player.description || '暂无介绍' }}</p>
        </div>

        <div class="profile-section">
          <h3>擅长游戏</h3>
          <div class="game-list">
            <div v-for="g in player.gameDetails" :key="g.gameId" class="game-item">
              <span>{{ g.gameName }}</span>
              <span class="game-rank">段位：{{ g.rank || '未填写' }}</span>
              <span class="game-price">¥{{ g.price }}/局</span>
            </div>
          </div>
        </div>

        <div class="profile-section">
          <h3>服务评价 ({{ reviews.length }})</h3>
          <div v-for="review in reviews" :key="review.id" class="review-item">
            <div class="review-header">
              <span class="review-user">{{ review.userName }}</span>
              <el-rate v-model="review.rating" disabled />
              <span class="review-time">{{ review.createdAt }}</span>
            </div>
            <p>{{ review.content }}</p>
          </div>
          <el-empty v-if="reviews.length === 0" description="暂无评价" :image-size="60" />
        </div>
      </div>

      <!-- 右侧：下单面板 -->
      <div class="detail-right">
        <div class="order-panel page-card">
          <div class="price-display">
            ¥{{ player.price || 0 }}<small>/局</small>
          </div>

          <el-form label-position="top">
            <el-form-item label="选择游戏">
              <el-select v-model="orderForm.gameId" placeholder="请选择游戏" style="width: 100%">
                <el-option
                  v-for="g in player.gameDetails"
                  :key="g.gameId"
                  :label="`${g.gameName} - ¥${g.price}/局`"
                  :value="g.gameId"
                />
              </el-select>
            </el-form-item>
            <el-form-item label="下单数量（局数）">
              <el-input-number v-model="orderForm.quantity" :min="1" :max="20" />
            </el-form-item>
            <el-form-item label="备注">
              <el-input v-model="orderForm.remark" type="textarea" :rows="3" placeholder="告诉陪玩师你的需求..." />
            </el-form-item>
          </el-form>

          <div class="order-summary">
            <div class="summary-row">
              <span>单价</span><span>¥{{ currentPrice }}</span>
            </div>
            <div class="summary-row">
              <span>数量</span><span>{{ orderForm.quantity }}局</span>
            </div>
            <div class="summary-row total">
              <span>合计</span><span>¥{{ totalPrice }}</span>
            </div>
          </div>

          <el-button type="primary" size="large" style="width: 100%" @click="handleOrder">
            立即下单
          </el-button>
        </div>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, computed, onMounted } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { getPlayerDetail, getPlayerReviews } from '../../api/player'
import { createOrder } from '../../api/order'
import { ElMessage } from 'element-plus'

const route = useRoute()
const router = useRouter()
const player = ref({})
const reviews = ref([])

const orderForm = reactive({
  gameId: '',
  quantity: 1,
  remark: ''
})

const currentPrice = computed(() => {
  const game = player.value.gameDetails?.find(g => g.gameId === orderForm.gameId)
  return game?.price || player.value.price || 0
})

const totalPrice = computed(() => currentPrice.value * orderForm.quantity)

onMounted(async () => {
  const id = route.params.id
  try {
    const res = await getPlayerDetail(id)
    player.value = res.data
  } catch {
    player.value = {
      id: 1, nickname: '技术流', games: ['英雄联盟', 'CS2'],
      rating: '5.0', orderCount: 512, likeCount: 256, price: 50,
      description: '前职业选手，英雄联盟钻石段位，CS2 S级。保证良好的游戏体验，耐心教学，可带飞可陪练。',
      gameDetails: [
        { gameId: 1, gameName: '英雄联盟', rank: '钻石I', price: 50 },
        { gameId: 5, gameName: 'CS2', rank: 'S级', price: 60 }
      ]
    }
  }
  try {
    const res = await getPlayerReviews(id)
    reviews.value = res.data || []
  } catch {
    reviews.value = [
      { id: 1, userName: '小明', rating: 5, content: '技术很好，很耐心，下次还点！', createdAt: '2026-07-10' }
    ]
  }
})

async function handleOrder() {
  if (!orderForm.gameId) {
    ElMessage.warning('请选择游戏')
    return
  }
  try {
    await createOrder({
      playerId: player.value.id,
      gameId: orderForm.gameId,
      quantity: orderForm.quantity,
      remark: orderForm.remark,
      totalPrice: totalPrice.value
    })
    ElMessage.success('下单成功')
    router.push('/orders')
  } catch (e) {
    // handled by interceptor
  }
}
</script>

<style scoped>
.detail-layout {
  display: grid;
  grid-template-columns: 1fr 340px;
  gap: 24px;
  align-items: start;
}

.profile-header {
  display: flex;
  gap: 24px;
  align-items: flex-start;
}

.profile-basic h2 { font-size: 24px; margin-bottom: 8px; }
.profile-tags { display: flex; gap: 6px; margin-bottom: 16px; }
.profile-stats { display: flex; gap: 32px; }
.profile-stats .stat { text-align: center; }
.profile-stats strong { display: block; font-size: 20px; color: var(--primary-color); }
.profile-stats span { font-size: 12px; color: var(--text-secondary); }

.profile-section { margin-bottom: 24px; }
.profile-section h3 { font-size: 16px; margin-bottom: 12px; }

.game-item {
  display: flex;
  justify-content: space-between;
  padding: 12px 0;
  border-bottom: 1px solid var(--border-color);
}
.game-rank { color: var(--text-secondary); }
.game-price { color: var(--accent-color); font-weight: 600; }

.review-item {
  padding: 12px 0;
  border-bottom: 1px solid var(--border-color);
}
.review-header {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 8px;
}
.review-user { font-weight: 600; }
.review-time { font-size: 12px; color: var(--text-secondary); margin-left: auto; }

.order-panel { position: sticky; top: 80px; }
.price-display {
  font-size: 32px;
  font-weight: 800;
  color: var(--accent-color);
  margin-bottom: 24px;
}
.price-display small { font-size: 14px; font-weight: 400; color: var(--text-secondary); }

.order-summary {
  background: var(--bg-color);
  border-radius: 8px;
  padding: 16px;
  margin-bottom: 20px;
}
.summary-row {
  display: flex;
  justify-content: space-between;
  margin-bottom: 8px;
  font-size: 14px;
  color: var(--text-secondary);
}
.summary-row.total {
  font-size: 18px;
  font-weight: 700;
  color: var(--text-primary);
  border-top: 1px solid var(--border-color);
  padding-top: 8px;
  margin-bottom: 0;
}
</style>
