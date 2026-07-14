<template>
  <div class="default-layout">
    <!-- 顶部导航栏 -->
    <el-header class="layout-header">
      <div class="header-content">
        <div class="logo" @click="$router.push('/')">
          <span class="logo-icon">🎮</span>
          <span class="logo-text">PlayMate</span>
        </div>

        <el-menu
          :default-active="activeMenu"
          mode="horizontal"
          router
          class="nav-menu"
        >
          <el-menu-item index="/">首页</el-menu-item>
          <el-menu-item index="/players">找陪玩</el-menu-item>
          <el-menu-item index="/orders" v-if="userStore.isLoggedIn">我的订单</el-menu-item>
          <el-menu-item index="/chat" v-if="userStore.isLoggedIn">消息</el-menu-item>
        </el-menu>

        <div class="header-right">
          <template v-if="userStore.isLoggedIn">
            <el-badge :value="0" class="wallet-badge">
              <el-button text @click="$router.push('/wallet')">
                💰 {{ userStore.userInfo?.balance || '0.00' }}
              </el-button>
            </el-badge>
            <el-dropdown trigger="click">
              <div class="user-avatar">
                <el-avatar :size="36" :src="userStore.userInfo?.avatar">
                  {{ userStore.userInfo?.nickname?.charAt(0) || 'U' }}
                </el-avatar>
              </div>
              <template #dropdown>
                <el-dropdown-menu>
                  <el-dropdown-item @click="$router.push('/profile')">个人中心</el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/apply-player')" v-if="!userStore.isPlayer">
                    成为陪玩师
                  </el-dropdown-item>
                  <el-dropdown-item @click="$router.push('/admin')" v-if="userStore.isAdmin">
                    管理后台
                  </el-dropdown-item>
                  <el-dropdown-item divided @click="handleLogout">退出登录</el-dropdown-item>
                </el-dropdown-menu>
              </template>
            </el-dropdown>
          </template>
          <template v-else>
            <el-button type="primary" @click="$router.push('/login')">登录</el-button>
            <el-button @click="$router.push('/register')">注册</el-button>
          </template>
        </div>
      </div>
    </el-header>

    <!-- 主内容区 -->
    <el-main class="layout-main">
      <router-view />
    </el-main>

    <!-- 底部 -->
    <el-footer class="layout-footer" height="60px">
      <p>© 2026 PlayMate 陪玩平台 — 实习项目</p>
    </el-footer>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRoute, useRouter } from 'vue-router'
import { useUserStore } from '../stores/user'

const route = useRoute()
const router = useRouter()
const userStore = useUserStore()

const activeMenu = computed(() => route.path)

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.default-layout {
  min-height: 100vh;
  display: flex;
  flex-direction: column;
}

.layout-header {
  background: #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  position: sticky;
  top: 0;
  z-index: 100;
  padding: 0 24px;
  height: 64px;
}

.header-content {
  max-width: 1200px;
  margin: 0 auto;
  display: flex;
  align-items: center;
  height: 100%;
}

.logo {
  display: flex;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  margin-right: 40px;
}

.logo-icon { font-size: 28px; }
.logo-text {
  font-size: 22px;
  font-weight: 700;
  background: linear-gradient(135deg, var(--primary-color), var(--accent-color));
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.nav-menu {
  flex: 1;
  border-bottom: none;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.user-avatar {
  cursor: pointer;
}

.layout-main {
  flex: 1;
  background: var(--bg-color);
}

.layout-footer {
  text-align: center;
  color: var(--text-secondary);
  font-size: 14px;
  background: #fff;
  border-top: 1px solid var(--border-color);
}
</style>
