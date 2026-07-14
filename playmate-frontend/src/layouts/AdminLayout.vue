<template>
  <div class="admin-layout">
    <!-- 侧边栏 -->
    <el-aside width="220px" class="admin-sidebar">
      <div class="sidebar-logo">
        <span>🎮</span> PlayMate 管理
      </div>
      <el-menu
        :default-active="activeMenu"
        router
        background-color="#1e1e2d"
        text-color="#a2a3b7"
        active-text-color="#6c5ce7"
      >
        <el-menu-item index="/admin">
          <span>📊</span> 数据概览
        </el-menu-item>
        <el-menu-item index="/admin/users">
          <span>👥</span> 用户管理
        </el-menu-item>
        <el-menu-item index="/admin/players">
          <span>🎮</span> 陪玩师审核
        </el-menu-item>
        <el-menu-item index="/admin/orders">
          <span>📋</span> 订单管理
        </el-menu-item>
        <el-menu-item index="/admin/games">
          <span>🕹️</span> 游戏分类
        </el-menu-item>
        <el-menu-item index="/admin/withdraw">
          <span>💰</span> 提现审核
        </el-menu-item>
        <el-divider />
        <el-menu-item index="/">
          <span>🏠</span> 返回前台
        </el-menu-item>
      </el-menu>
    </el-aside>

    <!-- 右侧内容 -->
    <div class="admin-main">
      <el-header class="admin-header" height="56px">
        <div class="admin-header-left">
          <el-breadcrumb separator="/">
            <el-breadcrumb-item :to="{ path: '/admin' }">后台</el-breadcrumb-item>
            <el-breadcrumb-item>{{ currentTitle }}</el-breadcrumb-item>
          </el-breadcrumb>
        </div>
        <div class="admin-header-right">
          <el-dropdown trigger="click">
            <span class="admin-user">{{ userStore.userInfo?.nickname || '管理员' }}</span>
            <template #dropdown>
              <el-dropdown-menu>
                <el-dropdown-item @click="handleLogout">退出登录</el-dropdown-item>
              </el-dropdown-menu>
            </template>
          </el-dropdown>
        </div>
      </el-header>

      <el-main class="admin-content">
        <router-view />
      </el-main>
    </div>
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
const currentTitle = computed(() => route.meta.title || '数据概览')

function handleLogout() {
  userStore.logout()
  router.push('/login')
}
</script>

<style scoped>
.admin-layout {
  display: flex;
  min-height: 100vh;
}

.admin-sidebar {
  background: #1e1e2d;
  overflow-y: auto;
}

.sidebar-logo {
  padding: 20px;
  color: #fff;
  font-size: 18px;
  font-weight: 700;
  text-align: center;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.admin-main {
  flex: 1;
  display: flex;
  flex-direction: column;
}

.admin-header {
  background: #fff;
  box-shadow: 0 1px 4px rgba(0, 0, 0, 0.08);
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
}

.admin-user {
  cursor: pointer;
  color: var(--text-secondary);
}

.admin-content {
  background: var(--bg-color);
  flex: 1;
}
</style>
