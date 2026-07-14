import request from '../utils/request'

// 创建订单（下单点陪玩）
export function createOrder(data) {
  return request.post('/order/create', data)
}

// 获取我的订单列表（用户视角）
export function getMyOrders(params) {
  return request.get('/order/my', { params })
}

// 获取陪玩师收到的订单列表
export function getPlayerOrders(params) {
  return request.get('/order/received', { params })
}

// 获取订单详情
export function getOrderDetail(id) {
  return request.get(`/order/${id}`)
}

// 陪玩师接单
export function acceptOrder(orderId) {
  return request.put(`/order/${orderId}/accept`)
}

// 陪玩师拒绝接单
export function rejectOrder(orderId, reason) {
  return request.put(`/order/${orderId}/reject`, { reason })
}

// 完成订单
export function completeOrder(orderId) {
  return request.put(`/order/${orderId}/complete`)
}

// 取消订单
export function cancelOrder(orderId, reason) {
  return request.put(`/order/${orderId}/cancel`, { reason })
}

// 确认收货（用户确认完成）
export function confirmOrder(orderId) {
  return request.put(`/order/${orderId}/confirm`)
}

// 申请退款
export function refundOrder(orderId, reason) {
  return request.post(`/order/${orderId}/refund`, { reason })
}
