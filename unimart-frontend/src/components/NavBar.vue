<template>
  <div class="nav-bar">
    <div class="nav-content">
      <div class="left">
        <router-link to="/" class="logo">
          校园二手交易平台
        </router-link>
      </div>
      
      <div class="right">
        <template v-if="userInfo">
          <el-button 
            type="primary" 
            icon="el-icon-plus"
            @click="$router.push('/publish')"
            class="publish-btn"
          >
            发布商品
          </el-button>

          <el-badge :value="unreadCount" :hidden="unreadCount === 0" class="message-badge">
            <el-button 
              type="text" 
              icon="el-icon-message" 
              @click="$router.push('/messages')"
            >
              消息
            </el-button>
          </el-badge>

          <el-dropdown trigger="click" @command="handleCommand">
            <span class="user-info">
              <el-avatar :size="32" :src="userInfo.avatar || defaultAvatar"></el-avatar>
              <span class="username">{{ userInfo.username }}</span>
              <i class="el-icon-arrow-down"></i>
            </span>
            <el-dropdown-menu slot="dropdown">
              <el-dropdown-item command="profile">个人中心</el-dropdown-item>
              <el-dropdown-item command="my">我的商品</el-dropdown-item>
              <el-dropdown-item command="logout">退出登录</el-dropdown-item>
            </el-dropdown-menu>
          </el-dropdown>
        </template>
        <template v-else>
          <el-button type="text" @click="$router.push('/login')">登录</el-button>
          <el-button type="text" @click="$router.push('/register')">注册</el-button>
        </template>
      </div>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import defaultAvatar from '@/assets/default-avatar.png'
import { getUnreadCount } from '@/api/message'

export default {
  name: 'NavBar',
  data() {
    return {
      defaultAvatar,
      unreadCount: 0
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  created() {
    this.fetchUnreadCount()
  },
  methods: {
    async fetchUnreadCount() {
      if (this.userInfo) {
        try {
          const count = await getUnreadCount()
          this.unreadCount = count
        } catch (error) {
          console.error('获取未读消息数失败:', error)
        }
      }
    },
    handleCommand(command) {
      switch (command) {
        case 'profile':
          this.$router.push('/profile')
          break
        case 'my':
          this.$router.push('/my')
          break
        case 'logout':
          this.$store.dispatch('user/logout')
          this.$router.push('/login')
          break
      }
    }
  }
}
</script>

<style scoped>
.nav-bar {
  background-color: #fff;
  box-shadow: 0 2px 4px rgba(0, 0, 0, 0.1);
}

.nav-content {
  max-width: 1200px;
  margin: 0 auto;
  height: 60px;
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 0 20px;
}

.logo {
  color: #303133;
  font-size: 18px;
  text-decoration: none;
  font-weight: bold;
}

.user-info {
  display: flex;
  align-items: center;
  cursor: pointer;
}

.username {
  margin: 0 8px;
  color: #303133;
}

.el-dropdown-link {
  cursor: pointer;
  color: #409EFF;
}

.publish-btn {
  margin-right: 20px;
}

.publish-btn i {
  margin-right: 4px;
}

.message-badge {
  margin-right: 20px;
}

.message-badge .el-button {
  font-size: 14px;
  padding: 0;
}

.message-badge .el-button [class^="el-icon-"] {
  margin-right: 4px;
}
</style> 