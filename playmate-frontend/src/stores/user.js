import { defineStore } from 'pinia'
import { login as loginApi, getUserInfo } from '../api/user'
import { getToken, setToken, removeToken } from '../utils/auth'

export const useUserStore = defineStore('user', {
  state: () => ({
    token: getToken() || '',
    userInfo: null,
    role: '' // 'user' | 'player' | 'admin'
  }),

  getters: {
    isLoggedIn: (state) => !!state.token,
    isAdmin: (state) => state.role === 'admin',
    isPlayer: (state) => state.role === 'player'
  },

  actions: {
    async login(loginForm) {
      const res = await loginApi(loginForm)
      this.token = res.data.token
      setToken(res.data.token)
      this.userInfo = res.data.userInfo
      this.role = res.data.role
      return res
    },

    async fetchUserInfo() {
      const res = await getUserInfo()
      this.userInfo = res.data
      this.role = res.data.role
      return res
    },

    logout() {
      this.token = ''
      this.userInfo = null
      this.role = ''
      removeToken()
    }
  }
})
