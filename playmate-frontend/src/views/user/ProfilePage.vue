<template>
  <div class="page-container">
    <h2 style="margin-bottom: 24px;">个人中心</h2>

    <div class="profile-layout">
      <!-- 头像与基本信息 -->
      <div class="page-card profile-card">
        <div class="avatar-section">
          <el-upload
            action=""
            :show-file-list="false"
            :before-upload="handleAvatarUpload"
            accept="image/*"
          >
            <el-avatar :size="100" :src="userStore.userInfo?.avatar">
              {{ userStore.userInfo?.nickname?.charAt(0) || 'U' }}
            </el-avatar>
            <div class="avatar-tip">点击更换头像</div>
          </el-upload>
        </div>

        <el-form ref="formRef" :model="form" label-position="top" style="margin-top: 24px;">
          <el-form-item label="昵称">
            <el-input v-model="form.nickname" />
          </el-form-item>
          <el-form-item label="手机号">
            <el-input v-model="form.phone" disabled />
          </el-form-item>
          <el-form-item label="性别">
            <el-radio-group v-model="form.gender">
              <el-radio value="male">男</el-radio>
              <el-radio value="female">女</el-radio>
              <el-radio value="secret">保密</el-radio>
            </el-radio-group>
          </el-form-item>
          <el-form-item label="个人签名">
            <el-input v-model="form.bio" type="textarea" :rows="3" placeholder="介绍一下自己..." />
          </el-form-item>
          <el-form-item>
            <el-button type="primary" @click="handleSave">保存修改</el-button>
          </el-form-item>
        </el-form>
      </div>

      <!-- 快捷操作 -->
      <div class="side-card">
        <div class="page-card">
          <h3 style="margin-bottom: 16px;">快捷操作</h3>
          <el-menu>
            <el-menu-item @click="$router.push('/orders')">📋 我的订单</el-menu-item>
            <el-menu-item @click="$router.push('/wallet')">💰 我的钱包</el-menu-item>
            <el-menu-item @click="$router.push('/apply-player')" v-if="!userStore.isPlayer">🎮 成为陪玩师</el-menu-item>
            <el-menu-item @click="showPassword = true">🔒 修改密码</el-menu-item>
          </el-menu>
        </div>
      </div>
    </div>

    <!-- 修改密码弹窗 -->
    <el-dialog v-model="showPassword" title="修改密码" width="400px">
      <el-form label-position="top">
        <el-form-item label="原密码">
          <el-input v-model="pwdForm.oldPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="新密码">
          <el-input v-model="pwdForm.newPassword" type="password" show-password />
        </el-form-item>
        <el-form-item label="确认新密码">
          <el-input v-model="pwdForm.confirmPassword" type="password" show-password />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button type="primary" @click="handleChangePassword">确认修改</el-button>
      </template>
    </el-dialog>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useUserStore } from '../../stores/user'
import { updateUser, uploadAvatar, changePassword } from '../../api/user'
import { ElMessage } from 'element-plus'

const userStore = useUserStore()
const showPassword = ref(false)

const form = reactive({
  nickname: '',
  phone: '',
  gender: 'secret',
  bio: ''
})

const pwdForm = reactive({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

onMounted(() => {
  if (userStore.userInfo) {
    Object.assign(form, {
      nickname: userStore.userInfo.nickname,
      phone: userStore.userInfo.phone,
      gender: userStore.userInfo.gender || 'secret',
      bio: userStore.userInfo.bio || ''
    })
  }
})

async function handleSave() {
  try {
    await updateUser(form)
    await userStore.fetchUserInfo()
    ElMessage.success('保存成功')
  } catch (e) { /* handled */ }
}

async function handleAvatarUpload(file) {
  try {
    await uploadAvatar(file)
    await userStore.fetchUserInfo()
    ElMessage.success('头像已更新')
  } catch (e) { /* handled */ }
  return false
}

async function handleChangePassword() {
  if (pwdForm.newPassword !== pwdForm.confirmPassword) {
    ElMessage.error('两次密码不一致')
    return
  }
  try {
    await changePassword({ oldPassword: pwdForm.oldPassword, newPassword: pwdForm.newPassword })
    ElMessage.success('密码修改成功')
    showPassword.value = false
  } catch (e) { /* handled */ }
}
</script>

<style scoped>
.profile-layout {
  display: grid;
  grid-template-columns: 1fr 300px;
  gap: 24px;
  align-items: start;
}

.avatar-section { text-align: center; }
.avatar-tip { font-size: 12px; color: var(--text-secondary); margin-top: 8px; }
</style>
