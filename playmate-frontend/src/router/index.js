import { createRouter, createWebHistory } from 'vue-router'
import { useUserStore } from '../stores/user'

const routes = [
  {
    path: '/',
    component: () => import('../layouts/DefaultLayout.vue'),
    children: [
      { path: '', name: 'Home', component: () => import('../views/home/HomePage.vue') },
      { path: 'players', name: 'PlayerList', component: () => import('../views/player/PlayerList.vue') },
      { path: 'players/:id', name: 'PlayerDetail', component: () => import('../views/player/PlayerDetail.vue') },
      { path: 'orders', name: 'MyOrders', component: () => import('../views/order/MyOrders.vue'), meta: { requiresAuth: true } },
      { path: 'orders/:id', name: 'OrderDetail', component: () => import('../views/order/OrderDetail.vue'), meta: { requiresAuth: true } },
      { path: 'wallet', name: 'Wallet', component: () => import('../views/wallet/WalletPage.vue'), meta: { requiresAuth: true } },
      { path: 'profile', name: 'Profile', component: () => import('../views/user/ProfilePage.vue'), meta: { requiresAuth: true } },
      { path: 'apply-player', name: 'ApplyPlayer', component: () => import('../views/player/ApplyPlayer.vue'), meta: { requiresAuth: true } },
      { path: 'chat', name: 'Chat', component: () => import('../views/chat/ChatPage.vue'), meta: { requiresAuth: true } }
    ]
  },
  { path: '/login', name: 'Login', component: () => import('../views/login/LoginPage.vue') },
  { path: '/register', name: 'Register', component: () => import('../views/login/RegisterPage.vue') },
  // 管理员后台
  {
    path: '/admin',
    component: () => import('../layouts/AdminLayout.vue'),
    meta: { requiresAuth: true, requiresAdmin: true },
    children: [
      { path: '', name: 'AdminDashboard', component: () => import('../views/admin/Dashboard.vue') },
      { path: 'users', name: 'AdminUsers', component: () => import('../views/admin/UserManage.vue') },
      { path: 'players', name: 'AdminPlayers', component: () => import('../views/admin/PlayerManage.vue') },
      { path: 'orders', name: 'AdminOrders', component: () => import('../views/admin/OrderManage.vue') },
      { path: 'games', name: 'AdminGames', component: () => import('../views/admin/GameManage.vue') },
      { path: 'withdraw', name: 'AdminWithdraw', component: () => import('../views/admin/WithdrawManage.vue') }
    ]
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

// 路由守卫
router.beforeEach((to, from, next) => {
  const userStore = useUserStore()

  if (to.meta.requiresAuth && !userStore.isLoggedIn) {
    next({ path: '/login', query: { redirect: to.fullPath } })
  } else if (to.meta.requiresAdmin && userStore.role !== 'admin') {
    next('/')
  } else {
    next()
  }
})

export default router
