import request from '../utils/request'

// 获取钱包余额
export function getWalletBalance() {
  return request.get('/wallet/balance')
}

// 充值
export function recharge(data) {
  return request.post('/wallet/recharge', data)
}

// 申请提现
export function withdraw(data) {
  return request.post('/wallet/withdraw', data)
}

// 获取交易流水
export function getTransactions(params) {
  return request.get('/wallet/transactions', { params })
}

// 获取提现记录
export function getWithdrawRecords(params) {
  return request.get('/wallet/withdraw-records', { params })
}
