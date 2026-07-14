<template>
  <div class="page-container">
    <h2 style="margin-bottom: 24px;">消息</h2>
    <div class="chat-layout page-card">
      <!-- 会话列表 -->
      <div class="chat-sidebar">
        <div class="chat-search">
          <el-input v-model="searchText" placeholder="搜索联系人" prefix-icon="Search" />
        </div>
        <div
          v-for="conv in conversations"
          :key="conv.id"
          class="conv-item"
          :class="{ active: currentConv?.id === conv.id }"
          @click="selectConversation(conv)"
        >
          <el-avatar :size="44" :src="conv.avatar">{{ conv.name?.charAt(0) }}</el-avatar>
          <div class="conv-info">
            <div class="conv-name">{{ conv.name }}</div>
            <div class="conv-last">{{ conv.lastMessage }}</div>
          </div>
          <div class="conv-meta">
            <div class="conv-time">{{ conv.time }}</div>
            <el-badge v-if="conv.unread" :value="conv.unread" />
          </div>
        </div>
      </div>

      <!-- 聊天区域 -->
      <div class="chat-main">
        <template v-if="currentConv">
          <div class="chat-header">
            <span>{{ currentConv.name }}</span>
          </div>
          <div class="chat-messages" ref="messagesRef">
            <div
              v-for="msg in messages"
              :key="msg.id"
              class="message-item"
              :class="{ mine: msg.isMine }"
            >
              <el-avatar :size="36" :src="msg.avatar">{{ msg.sender?.charAt(0) }}</el-avatar>
              <div class="message-bubble">{{ msg.content }}</div>
            </div>
          </div>
          <div class="chat-input">
            <el-input
              v-model="inputText"
              placeholder="输入消息..."
              @keyup.enter="sendMessage"
            >
              <template #append>
                <el-button type="primary" @click="sendMessage">发送</el-button>
              </template>
            </el-input>
          </div>
        </template>
        <el-empty v-else description="选择一个会话开始聊天" :image-size="120" />
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'

const searchText = ref('')
const currentConv = ref(null)
const inputText = ref('')
const messagesRef = ref(null)

const conversations = ref([
  { id: 1, name: '技术流', avatar: '', lastMessage: '好的，我们开始吧', time: '10:35', unread: 2 },
  { id: 2, name: '小甜心', avatar: '', lastMessage: '谢谢老板，下次再来哦~', time: '昨天', unread: 0 }
])

const messages = ref([
  { id: 1, sender: '技术流', avatar: '', content: '你好，我是技术流，有什么需要帮忙的吗？', isMine: false },
  { id: 2, sender: '我', avatar: '', content: '你好！我想打英雄联盟，能带我上分吗？', isMine: true },
  { id: 3, sender: '技术流', avatar: '', content: '当然可以！我钻石段位，保证带你飞', isMine: false },
  { id: 4, sender: '技术流', avatar: '', content: '好的，我们开始吧', isMine: false }
])

function selectConversation(conv) {
  currentConv.value = conv
  conv.unread = 0
}

function sendMessage() {
  if (!inputText.value.trim()) return
  messages.value.push({
    id: Date.now(),
    sender: '我',
    avatar: '',
    content: inputText.value,
    isMine: true
  })
  inputText.value = ''
}
</script>

<style scoped>
.chat-layout {
  display: grid;
  grid-template-columns: 300px 1fr;
  height: calc(100vh - 200px);
  min-height: 500px;
  overflow: hidden;
}

.chat-sidebar {
  border-right: 1px solid var(--border-color);
  overflow-y: auto;
}

.chat-search { padding: 12px; }

.conv-item {
  display: flex;
  gap: 10px;
  padding: 12px 16px;
  cursor: pointer;
  transition: background 0.2s;
  border-bottom: 1px solid var(--border-color);
}
.conv-item:hover, .conv-item.active { background: rgba(108, 92, 231, 0.05); }

.conv-info { flex: 1; overflow: hidden; }
.conv-name { font-weight: 600; font-size: 14px; }
.conv-last { font-size: 12px; color: var(--text-secondary); white-space: nowrap; overflow: hidden; text-overflow: ellipsis; }

.conv-meta { text-align: right; flex-shrink: 0; }
.conv-time { font-size: 11px; color: var(--text-secondary); margin-bottom: 4px; }

.chat-main { display: flex; flex-direction: column; }

.chat-header {
  padding: 16px;
  border-bottom: 1px solid var(--border-color);
  font-weight: 600;
}

.chat-messages {
  flex: 1;
  overflow-y: auto;
  padding: 16px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.message-item {
  display: flex;
  gap: 8px;
  align-items: flex-start;
}
.message-item.mine { flex-direction: row-reverse; }

.message-bubble {
  max-width: 60%;
  padding: 10px 14px;
  border-radius: 12px;
  background: #f0f0f0;
  font-size: 14px;
  line-height: 1.5;
}
.message-item.mine .message-bubble {
  background: var(--primary-color);
  color: #fff;
}

.chat-input {
  padding: 12px 16px;
  border-top: 1px solid var(--border-color);
}
</style>
