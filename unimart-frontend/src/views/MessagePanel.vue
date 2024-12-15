<template>
  <div class="message-panel">
    <nav-bar></nav-bar>
    <div class="content">
      <el-card>
        <div class="panel-header">
          <div class="user-info">
            <el-avatar :size="40" :src="receiverAvatar"></el-avatar>
            <div class="user-detail">
              <h2>{{ receiverName || '用户' + $route.params.receiverId }}</h2>
              <div class="product-info" v-if="product">
                <span>关于商品：{{ product.productName }}</span>
                <span class="price">¥{{ product.price }}</span>
              </div>
            </div>
          </div>
        </div>
        
        <div class="message-list" ref="messageList">
          <div 
            v-for="message in sortedMessages" 
            :key="message.messageId"
            :class="['message-item', message.senderId === userInfo.id ? 'sent' : 'received']"
          >
            <el-avatar 
              :size="40" 
              :src="getMessageAvatar(message)"
            ></el-avatar>
            <div class="message-content">
              <div class="message-time">{{ formatDate(message.createTime) }}</div>
              <div class="message-text">{{ message.content }}</div>
            </div>
          </div>
        </div>

        <div class="message-input">
          <el-input
            v-model="newMessage"
            type="textarea"
            :rows="3"
            placeholder="请输入消息内容"
            @keyup.enter.native="sendMessage"
          ></el-input>
          <el-button 
            type="primary" 
            @click="sendMessage"
            :loading="sending"
          >
            发送
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import NavBar from '@/components/NavBar.vue'
import { sendMessage, getChatHistory, markAsRead, getUnreadCount } from '@/api/message'
import { getProductDetail } from '@/api/product'
import { formatDate } from '@/utils/date'
import { getUserById } from '@/api/user'

export default {
  name: 'MessagePanel',
  components: {
    NavBar
  },
  data() {
    return {
      messages: [],
      newMessage: '',
      sending: false,
      receiverName: '',
      receiverAvatar: '',
      product: null,
      loading: false
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    }),
    sortedMessages() {
      return [...this.messages].sort((a, b) => 
        new Date(a.createTime) - new Date(b.createTime)
      )
    }
  },
  created() {
    this.init()
  },
  methods: {
    formatDate,
    getMessageAvatar(message) {
      if (message.senderId === this.userInfo.id) {
        return this.userInfo.avatar
      }
      return message.sender?.avatar || this.receiverAvatar
    },
    getMessageSender(message) {
      if (message.senderId === this.userInfo.id) {
        return this.userInfo.username
      }
      return message.sender?.username || this.receiverName
    },
    async init() {
      this.loading = true
      try {
        const { receiverId, productId } = this.$route.params
        console.log('初始化参数:', { receiverId, productId })
        
        // 获取商品信息
        if (productId) {
          const product = await getProductDetail(productId)
          console.log('商品信息:', product)
          this.product = product
          
          // 如果是从商品详情页进入，使用商品卖家信息
          if (product && product.seller) {
            this.receiverName = product.seller.username
            this.receiverAvatar = product.seller.avatar
          }
        }

        // 如果没有商品信息或者卖家信息，尝试获取用户信息
        if (!this.receiverName) {
          try {
            const userResponse = await getUserById(receiverId)
            this.receiverName = userResponse.username || '未知用户'
            this.receiverAvatar = userResponse.avatar
          } catch (userError) {
            console.error('获取用户信息失败:', userError)
            this.receiverName = '用户' + receiverId
          }
        }

        // 获取聊天记录
        const messages = await getChatHistory(receiverId)
        console.log('聊天记录:', messages)
        this.messages = messages || []
        
        // 标记消息为已读
        const unreadMessages = messages.filter(
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
        
        this.scrollToBottom()

      } catch (error) {
        console.error('初始化失败:', error)
        this.$message.error('加载失败')
      } finally {
        this.loading = false
      }
    },
    async sendMessage() {
      if (!this.newMessage.trim()) {
        this.$message.warning('请输入消息内容')
        return
      }

      this.sending = true
      try {
        const message = await sendMessage({
          receiverId: this.$route.params.receiverId,
          content: this.newMessage.trim()
        })

        this.messages.push(message)
        this.newMessage = ''
        this.scrollToBottom()
      } catch (error) {
        console.error('发送失败:', error)
        this.$message.error('发送失败')
      } finally {
        this.sending = false
      }
    },
    scrollToBottom() {
      this.$nextTick(() => {
        const messageList = this.$refs.messageList
        messageList.scrollTop = messageList.scrollHeight
      })
    }
  }
}
</script>

<style scoped>
.message-panel {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 800px;
  margin: 20px auto;
  padding: 0 20px;
}

.panel-header {
  margin-bottom: 20px;
  padding-bottom: 15px;
  border-bottom: 1px solid #eee;
}

.user-info {
  display: flex;
  align-items: center;
}

.user-detail {
  margin-left: 15px;
}

.user-detail h2 {
  margin: 0 0 5px;
  font-size: 18px;
  color: #303133;
}

.product-info {
  color: #606266;
  font-size: 14px;
}

.product-info .price {
  margin-left: 15px;
  color: #f56c6c;
}

.message-list {
  height: 400px;
  overflow-y: auto;
  padding: 20px;
  background: #f9f9f9;
  border-radius: 4px;
  display: flex;
  flex-direction: column;
}

.message-item {
  display: flex;
  align-items: flex-start;
  margin-bottom: 20px;
  width: 100%;
}

.message-item.sent {
  flex-direction: row-reverse;
}

.message-content {
  margin: 0 12px;
  max-width: 70%;
}

.message-time {
  font-size: 12px;
  color: #909399;
  margin-bottom: 4px;
}

.sent .message-time {
  text-align: right;
}

.message-text {
  padding: 10px 15px;
  background: #fff;
  border-radius: 4px;
  box-shadow: 0 2px 4px rgba(0,0,0,0.1);
  word-break: break-all;
}

.sent .message-text {
  background: #409EFF;
  color: #fff;
}

.message-input {
  margin-top: 20px;
}

.message-input .el-button {
  margin-top: 10px;
  float: right;
}
</style> 