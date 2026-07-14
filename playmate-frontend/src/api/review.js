import request from '../utils/request'

// 提交评价
export function createReview(data) {
  return request.post('/review/create', data)
}

// 获取订单的评价
export function getReviewByOrder(orderId) {
  return request.get(`/review/order/${orderId}`)
}
