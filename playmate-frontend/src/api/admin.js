import request from '../utils/request'

// ===== 管理员 - 用户管理 =====
export function adminGetUsers(params) {
  return request.get('/admin/users', { params })
}

export function adminUpdateUser(id, data) {
  return request.put(`/admin/users/${id}`, data)
}

export function adminBanUser(id, banned) {
  return request.put(`/admin/users/${id}/ban`, { banned })
}

// ===== 管理员 - 陪玩师审核 =====
export function adminGetPlayerApplications(params) {
  return request.get('/admin/player-applications', { params })
}

export function adminApprovePlayer(id) {
  return request.put(`/admin/player-applications/${id}/approve`)
}

export function adminRejectPlayer(id, reason) {
  return request.put(`/admin/player-applications/${id}/reject`, { reason })
}

// ===== 管理员 - 订单管理 =====
export function adminGetOrders(params) {
  return request.get('/admin/orders', { params })
}

export function adminGetOrderDetail(id) {
  return request.get(`/admin/orders/${id}`)
}

// ===== 管理员 - 数据统计 =====
export function adminGetDashboard() {
  return request.get('/admin/dashboard')
}

export function adminGetStats(params) {
  return request.get('/admin/stats', { params })
}

// ===== 管理员 - 游戏分类管理 =====
export function adminCreateGame(data) {
  return request.post('/admin/games', data)
}

export function adminUpdateGame(id, data) {
  return request.put(`/admin/games/${id}`, data)
}

export function adminDeleteGame(id) {
  return request.delete(`/admin/games/${id}`)
}

// ===== 管理员 - 提现审核 =====
export function adminGetWithdrawRequests(params) {
  return request.get('/admin/withdraw-requests', { params })
}

export function adminApproveWithdraw(id) {
  return request.put(`/admin/withdraw-requests/${id}/approve`)
}

export function adminRejectWithdraw(id, reason) {
  return request.put(`/admin/withdraw-requests/${id}/reject`, { reason })
}
