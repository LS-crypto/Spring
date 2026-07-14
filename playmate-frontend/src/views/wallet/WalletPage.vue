<template>
  <div class="page-container">
    <h2 style="margin-bottom: 24px;">我的钱包</h2>

    <div class="wallet-layout">
      <!-- 余额卡片 -->
      <div class="balance-card">
        <div class="balance-label">可用余额</div>
        <div class="balance-amount">¥{{ balance }}</div>
        <div class="balance-actions">
          <el-button type="primary" @click="showRecharge = true">充值</el-button>
          <el-button @click="showWithdraw = true">提现</el-button>
        </div>
      </div>

      <!-- 交易记录 -->
      <div class="page-card">
        <h3 style="margin-bottom: 16px;">交易记录</h3>
        <el-table :data="transactions" stripe>
          <el-table-column prop="time" label="时间" width="180" />
          <el-table-column prop="type" label="类型">
            <template #default="{ row }">
              <el-tag :type="row.type === 'recharge' ? 'success' : row.type === 'withdraw' ? 'warning' : 'primary'" size="small">
                {{ typeText(row.type) }}
              </el-tag>
            </template>
          </el-table-column>
          <el-table-column prop="amount" label="金额">
            <template #default="{ row }">
              <span :style="{ color: row.amount > 0 ? 'var(--success-color)' : 'var(--danger-color)' }">
                {{ row.amount > 0 ? '+' : '' }}{{ row.amount }}
              </span>
            </template>
          </el-table-column>
          <el-table-column prop="remark" label="备注" />
        </el-table>
      </div>
    </div>

    <!-- 充值弹窗 -->
    <el-dialog v-model="showRecharge" title="充值" width="400px">
      <div class="recharge-options">
        <div v-for="opt in rechargeOptions" :key="opt.amount" class="recharge-option"
             :class="{ active: rechargeAmount === opt.amount }" @click="rechargeAmount = opt.amount">
          <div class="recharge-value">¥{{ opt.amount }}</div>
          <div class="recharge-gift" v-if="opt.gift">赠送 ¥{{ opt.gift }}</div>
        </div>
      </div>
      <template #footer>
        <el-button type="primary" @click="handleRecharge">确认充值 ¥{{ rechargeAmount }}</el-button>
      </template>
    </el-dialog>

    <!-- 提现弹窗 -->
    <el-dialog v-model="showWithdraw" title="申请提现" width="400px">
      <el-form label-position="top">
        <el-form-item label="提现金额">
          <el-input-number v-model="withdrawAmount" :min="10" :max="balance" :step="10" />
        </el-form-item>
        <el-form-item label="提现方式">
          <el-radio-group v-model="withdrawMethod">
            <el-radio value="alipay">支付宝</el-radio>
            <el-radio value="wechat">微信</el-radio>
            <el-radio value="bank">银行卡</el-radio>
          </el-radio-group>
        </el-form-item>
        <el-form-item label="账号">
          <el-input v-model="withdrawAccount" placeholder="请输入收款账号" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="handleWithdraw">提交申请</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { getWalletBalance, getTransactions, recharge, withdraw } from '../../api/wallet'
import { ElMessage } from 'element-plus'

const balance = ref(0)
const transactions = ref([])
const showRecharge = ref(false)
const showWithdraw = ref(false)
const rechargeAmount = ref(50)
const withdrawAmount = ref(100)
const withdrawMethod = ref('alipay')
const withdrawAccount = ref('')

const rechargeOptions = [
  { amount: 10, gift: 0 },
  { amount: 50, gift: 5 },
  { amount: 100, gift: 15 },
  { amount: 200, gift: 40 },
  { amount: 500, gift: 120 }
]

function typeText(type) {
  const map = { recharge: '充值', withdraw: '提现', order_pay: '订单支付', order_income: '订单收入', refund: '退款' }
  return map[type] || type
}

onMounted(async () => {
  try {
    const res = await getWalletBalance()
    balance.value = res.data?.balance || 0
  } catch {
    balance.value = 288.50
  }
  try {
    const res = await getTransactions()
    transactions.value = res.data?.list || []
  } catch {
    transactions.value = [
      { time: '2026-07-14 10:30', type: 'order_pay', amount: -150, remark: '下单 - 技术流 - 英雄联盟' },
      { time: '2026-07-13 20:00', type: 'recharge', amount: 200, remark: '在线充值' },
      { time: '2026-07-12 15:30', type: 'order_pay', amount: -60, remark: '下单 - 小甜心 - 王者荣耀' }
    ]
  }
})

async function handleRecharge() {
  try {
    await recharge({ amount: rechargeAmount.value })
    ElMessage.success('充值成功')
    showRecharge.value = false
    balance.value += rechargeAmount.value
  } catch (e) { /* handled */ }
}

async function handleWithdraw() {
  if (!withdrawAccount.value) {
    ElMessage.warning('请输入收款账号')
    return
  }
  try {
    await withdraw({ amount: withdrawAmount.value, method: withdrawMethod.value, account: withdrawAccount.value })
    ElMessage.success('提现申请已提交')
    showWithdraw.value = false
  } catch (e) { /* handled */ }
}
</script>

<style scoped>
.wallet-layout { display: grid; gap: 24px; }

.balance-card {
  background: linear-gradient(135deg, #6c5ce7, #a29bfe);
  border-radius: 16px;
  padding: 32px;
  color: #fff;
}
.balance-label { font-size: 14px; opacity: 0.8; margin-bottom: 8px; }
.balance-amount { font-size: 42px; font-weight: 800; margin-bottom: 20px; }
.balance-actions { display: flex; gap: 12px; }

.recharge-options {
  display: grid;
  grid-template-columns: repeat(3, 1fr);
  gap: 12px;
}
.recharge-option {
  border: 2px solid var(--border-color);
  border-radius: 8px;
  padding: 16px;
  text-align: center;
  cursor: pointer;
  transition: all 0.2s;
}
.recharge-option.active,
.recharge-option:hover {
  border-color: var(--primary-color);
  background: rgba(108, 92, 231, 0.05);
}
.recharge-value { font-size: 20px; font-weight: 700; }
.recharge-gift { font-size: 12px; color: var(--accent-color); margin-top: 4px; }
</style>
