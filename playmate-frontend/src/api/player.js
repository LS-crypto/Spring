import request from '../utils/request'

// 获取陪玩师列表（支持筛选）
export function getPlayerList(params) {
  return request.get('/player/list', { params })
}

// 获取陪玩师详情
export function getPlayerDetail(id) {
  return request.get(`/player/${id}`)
}

// 申请成为陪玩师
export function applyPlayer(data) {
  return request.post('/player/apply', data)
}

// 更新陪玩师资料
export function updatePlayer(data) {
  return request.put('/player/profile', data)
}

// 获取我的陪玩师信息（已入驻的）
export function getMyPlayerInfo() {
  return request.get('/player/mine')
}

// 设置/更新接单价格
export function updatePricing(data) {
  return request.put('/player/pricing', data)
}

// 获取陪玩师评价列表
export function getPlayerReviews(playerId, params) {
  return request.get(`/player/${playerId}/reviews`, { params })
}

// 获取游戏分类列表
export function getGameCategories() {
  return request.get('/game/categories')
}
