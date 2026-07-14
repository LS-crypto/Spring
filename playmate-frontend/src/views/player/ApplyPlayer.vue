<template>
  <div class="apply-page page-container">
    <div class="page-card" style="max-width: 640px; margin: 0 auto;">
      <h2 style="margin-bottom: 24px;">申请成为陪玩师</h2>

      <el-form ref="formRef" :model="form" :rules="rules" label-position="top">
        <el-form-item label="真实姓名" prop="realName">
          <el-input v-model="form.realName" placeholder="请输入真实姓名" />
        </el-form-item>

        <el-form-item label="擅长游戏" prop="gameIds">
          <el-select v-model="form.gameIds" multiple placeholder="选择你擅长的游戏" style="width: 100%">
            <el-option v-for="g in games" :key="g.id" :label="g.name" :value="g.id" />
          </el-select>
        </el-form-item>

        <el-form-item label="游戏段位" prop="rank">
          <el-input v-model="form.rank" placeholder="如：英雄联盟钻石、王者50星" />
        </el-form-item>

        <el-form-item label="每局定价（元）" prop="price">
          <el-input-number v-model="form.price" :min="5" :max="500" :step="5" />
        </el-form-item>

        <el-form-item label="个人介绍" prop="description">
          <el-input v-model="form.description" type="textarea" :rows="4" placeholder="介绍一下自己的游戏经历和服务特色..." />
        </el-form-item>

        <el-form-item label="语音展示（可选）">
          <el-input v-model="form.voiceUrl" placeholder="语音介绍链接" />
        </el-form-item>

        <el-form-item>
          <el-button type="primary" :loading="loading" style="width: 100%" @click="handleSubmit">
            提交申请
          </el-button>
        </el-form-item>
      </el-form>
    </div>
  </div>
</template>

<script setup>
import { ref, reactive, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import { applyPlayer, getGameCategories } from '../../api/player'
import { ElMessage } from 'element-plus'

const router = useRouter()
const formRef = ref(null)
const loading = ref(false)
const games = ref([])

const form = reactive({
  realName: '',
  gameIds: [],
  rank: '',
  price: 30,
  description: '',
  voiceUrl: ''
})

const rules = {
  realName: [{ required: true, message: '请输入真实姓名', trigger: 'blur' }],
  gameIds: [{ required: true, message: '请选择擅长游戏', trigger: 'change' }],
  rank: [{ required: true, message: '请输入游戏段位', trigger: 'blur' }],
  description: [{ required: true, message: '请填写个人介绍', trigger: 'blur' }]
}

onMounted(async () => {
  try {
    const res = await getGameCategories()
    games.value = res.data || []
  } catch {
    games.value = [
      { id: 1, name: '英雄联盟' }, { id: 2, name: '王者荣耀' },
      { id: 3, name: '和平精英' }, { id: 4, name: '永劫无间' }
    ]
  }
})

async function handleSubmit() {
  const valid = await formRef.value.validate().catch(() => false)
  if (!valid) return

  loading.value = true
  try {
    await applyPlayer(form)
    ElMessage.success('申请已提交，等待审核')
    router.push('/')
  } catch (e) {
    // handled
  } finally {
    loading.value = false
  }
}
</script>
