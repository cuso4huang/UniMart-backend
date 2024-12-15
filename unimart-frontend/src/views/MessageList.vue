<template>
  <div class="message-list-page">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card>
        <div class="message-list">
          <div 
            v-for="chat in chatList" 
            :key="chat.userId"
            class="chat-item"
            @click="$router.push(`/message/${chat.userId}`)"
          >
            <el-avatar :size="40" :src="chat.avatar"></el-avatar>
            <div class="chat-info">
              <div class="chat-header">
                <span class="username">{{ chat.userName }}</span>
                <span class="time">{{ formatDate(chat.lastMessageTime) }}</span>
              </div>
              <div class="last-message">{{ chat.lastMessage }}</div>
            </div>
            <el-badge 
              :value="chat.unreadCount" 
              :hidden="chat.unreadCount === 0"
            ></el-badge>
          </div>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import NavBar from '@/components/NavBar.vue'
import { formatDate } from '@/utils/date'
import { getMessages, markAsRead, getUnreadCount } from '@/api/message'

export default {
  name: 'MessageList',
  components: {
    NavBar
  },
  data() {
    return {
      chatList: [],
      loading: false
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  created() {
    if (this.userInfo) {
      this.fetchMessages()
    }
  },
  methods: {
    formatDate,
    async fetchMessages() {
      this.loading = true
      try {
        const response = await getMessages()
        if (response && response.length > 0) {
          // 标记所有未读消息为已读
          const unreadMessages = response.filter(
            msg => msg.senderId !== this.userInfo.id && !msg.isRead
          )
          if (unreadMessages.length > 0) {
            await Promise.all(
              unreadMessages.map(msg => markAsRead(msg.messageId))
            )
            // 更新未读消息数
            const newUnreadCount = await getUnreadCount()
            this.$store.dispatch('user/updateUnreadCount', newUnreadCount)
          }
          
          this.chatList = this.processChatList(response)
        }
      } catch (error) {
        console.error('获取消息列表失败:', error)
        this.$message.error('获取消息列表失败')
      } finally {
        this.loading = false
      }
    },
    processChatList(messages) {
      if (!this.userInfo) return []
      
      // 按用户分组，只显示最新一条消息
      const chatMap = new Map()
      
      messages.forEach(msg => {
        if (!msg.sender || !msg.receiver) return
        
        const otherUserId = msg.senderId === this.userInfo.id ? msg.receiverId : msg.senderId
        const otherUser = msg.senderId === this.userInfo.id ? msg.receiver : msg.sender
        
        if (!chatMap.has(otherUserId)) {
          chatMap.set(otherUserId, {
            userId: otherUserId,
            userName: otherUser.userName || '未知用户',
            avatar: otherUser.avatar,
            lastMessage: msg.content,
            lastMessageTime: msg.createTime,
            unreadCount: msg.senderId !== this.userInfo.id && !msg.isRead ? 1 : 0
          })
        } else {
          const chat = chatMap.get(otherUserId)
          if (new Date(msg.createTime) > new Date(chat.lastMessageTime)) {
            chat.lastMessage = msg.content
            chat.lastMessageTime = msg.createTime
          }
          if (msg.senderId !== this.userInfo.id && !msg.isRead) {
            chat.unreadCount++
          }
        }
      })
      
      return Array.from(chatMap.values())
        .sort((a, b) => new Date(b.lastMessageTime) - new Date(a.lastMessageTime))
    }
  }
}
</script>

<style scoped>
.message-list-page {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.chat-item {
  display: flex;
  align-items: center;
  padding: 15px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.chat-item:hover {
  background-color: #f5f7fa;
}

.chat-info {
  flex: 1;
  margin: 0 15px;
}

.chat-header {
  display: flex;
  justify-content: space-between;
  margin-bottom: 5px;
}

.username {
  font-weight: bold;
  color: #303133;
}

.time {
  font-size: 12px;
  color: #909399;
}

.last-message {
  color: #606266;
  font-size: 13px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}
</style> 