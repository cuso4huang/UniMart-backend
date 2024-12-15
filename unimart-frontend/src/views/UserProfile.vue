<template>
  <div class="user-profile">
    <nav-bar></nav-bar>
    <div class="content">
      <el-row :gutter="20">
        <!-- 左侧菜单 -->
        <el-col :span="6">
          <el-card class="menu-card">
            <div class="user-info">
              <el-avatar :size="80" :src="userInfo.avatar || defaultAvatar"></el-avatar>
              <h3>{{ userInfo.username }}</h3>
            </div>
            <el-menu
              :default-active="activeMenu"
              class="profile-menu"
              @select="handleMenuSelect"
            >
              <el-menu-item index="info">
                <i class="el-icon-user"></i>
                <span>个人信息</span>
              </el-menu-item>
              <el-menu-item index="products">
                <i class="el-icon-goods"></i>
                <span>我的商品</span>
              </el-menu-item>
              <el-menu-item index="transactions">
                <i class="el-icon-shopping-cart-2"></i>
                <span>交易记录</span>
              </el-menu-item>
              <el-menu-item index="settings">
                <i class="el-icon-setting"></i>
                <span>账号设置</span>
              </el-menu-item>
            </el-menu>
          </el-card>
        </el-col>
        
        <!-- 右侧内容 -->
        <el-col :span="18">
          <!-- 个人信息 -->
          <el-card v-if="activeMenu === 'info'">
            <div slot="header">
              <span>个人信息</span>
              <el-button 
                style="float: right" 
                type="text"
                @click="editMode = !editMode"
              >
                {{ editMode ? '取消' : '编辑' }}
              </el-button>
            </div>
            <el-form 
              ref="userForm"
              :model="userForm"
              :rules="userRules"
              label-width="100px"
              :disabled="!editMode"
            >
              <el-form-item label="用户ID">
                <el-input v-model="userForm.id" disabled></el-input>
              </el-form-item>
              <el-form-item label="用户名">
                <el-input v-model="userForm.username" disabled></el-input>
              </el-form-item>
              <el-form-item label="邮箱">
                <el-input 
                  v-model="userForm.account" 
                  disabled
                ></el-input>
              </el-form-item>
              <el-form-item label="新用户名" prop="newUsername" v-if="editMode">
                <el-input 
                  v-model="userForm.newUsername"
                  placeholder="请输入新用户名"
                ></el-input>
              </el-form-item>
              <el-form-item label="新邮箱" prop="newAccount" v-if="editMode">
                <el-input 
                  v-model="userForm.newAccount"
                  placeholder="请输入新邮箱"
                ></el-input>
              </el-form-item>
              <el-form-item v-if="editMode">
                <el-button type="primary" @click="updateUserInfo">保存</el-button>
              </el-form-item>
            </el-form>
          </el-card>

          <!-- 我的商品 -->
          <el-card v-if="activeMenu === 'products'">
            <div slot="header">
              <span>我的商品</span>
            </div>
            <product-list 
              :products="myProducts" 
              v-loading="loading"
            ></product-list>
          </el-card>

          <!-- 交易记录 -->
          <el-card v-if="activeMenu === 'transactions'">
            <div slot="header">
              <span>交易记录</span>
            </div>
            <transaction-list></transaction-list>
          </el-card>

          <!-- 账号设置 -->
          <el-card v-if="activeMenu === 'settings'">
            <div slot="header">
              <span>账号设置</span>
            </div>
            <div class="settings-section">
              <h3>修改密码</h3>
              <el-form 
                ref="passwordForm"
                :model="passwordForm"
                :rules="passwordRules"
                label-width="100px"
                v-if="showChangePassword"
              >
                <el-form-item label="原密码" prop="oldPassword">
                  <el-input 
                    v-model="passwordForm.oldPassword" 
                    type="password"
                    show-password
                  ></el-input>
                </el-form-item>
                <el-form-item label="新密码" prop="newPassword">
                  <el-input 
                    v-model="passwordForm.newPassword" 
                    type="password"
                    show-password
                  ></el-input>
                </el-form-item>
                <el-form-item label="确认密码" prop="confirmPassword">
                  <el-input 
                    v-model="passwordForm.confirmPassword" 
                    type="password"
                    show-password
                  ></el-input>
                </el-form-item>
                <el-form-item>
                  <el-button type="primary" @click="handleChangePassword">确认修改</el-button>
                  <el-button @click="showChangePassword = false">取消</el-button>
                </el-form-item>
              </el-form>
              <el-button 
                v-else 
                type="text" 
                @click="showChangePassword = true"
              >
                修改密码
              </el-button>
            </div>
            <el-divider></el-divider>
            <div class="settings-section">
              <h3>账号注销</h3>
              <p class="warning-text">注意：账号注销后将无法恢复，请谨慎操作</p>
              <el-button 
                type="danger"
                @click="handleDeleteAccount"
              >
                注销账号
              </el-button>
            </div>
          </el-card>
        </el-col>
      </el-row>
    </div>
  </div>
</template>

<script>
import { mapState } from 'vuex'
import NavBar from '@/components/NavBar.vue'
import ProductList from '@/components/ProductList.vue'
import TransactionList from '@/components/TransactionList.vue'
import defaultAvatar from '@/assets/default-avatar.png'
import { getUserInfo, updateUserInfo, changePassword, deleteAccount } from '@/api/user'
import { getMyProducts } from '@/api/product'

export default {
  name: 'UserProfile',
  components: {
    NavBar,
    ProductList,
    TransactionList
  },
  data() {
    const validateConfirmPassword = (rule, value, callback) => {
      if (value !== this.passwordForm.newPassword) {
        callback(new Error('两次输入的密码不一致'))
      } else {
        callback()
      }
    }
    return {
      defaultAvatar,
      activeMenu: 'info',
      editMode: false,
      loading: false,
      myProducts: [],
      showChangePassword: false,
      userForm: {
        id: '',
        username: '',
        account: '',
        newUsername: '',
        newAccount: ''
      },
      userRules: {
        newUsername: [
          { required: true, message: '请输入新用户名', trigger: 'blur' }
        ],
        newAccount: [
          { required: true, message: '请输入新邮箱', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      },
      passwordForm: {
        oldPassword: '',
        newPassword: '',
        confirmPassword: ''
      },
      passwordRules: {
        oldPassword: [
          { required: true, message: '请输入原密码', trigger: 'blur' }
        ],
        newPassword: [
          { required: true, message: '请输入新密码', trigger: 'blur' },
          { min: 6, message: '密码长度不能小于6位', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请确认密码', trigger: 'blur' },
          { validator: validateConfirmPassword, trigger: 'blur' }
        ]
      }
    }
  },
  computed: {
    ...mapState({
      userInfo: state => state.user.userInfo
    })
  },
  created() {
    this.fetchUserInfo()
    this.fetchMyProducts()
  },
  methods: {
    handleMenuSelect(key) {
      this.activeMenu = key
      if (key === 'products') {
        this.fetchMyProducts()
      }
    },
    async fetchUserInfo() {
      try {
        console.log('开始获取用户信息')
        const response = await getUserInfo()
        console.log('获取到用户信息:', response)
        
        if (!response) {
          throw new Error('未获取到用户信息')
        }
        
        this.userForm = {
          id: response.id,
          username: response.username,
          account: response.account,
          newUsername: '',
          newAccount: ''
        }
        console.log('更新后的表单数据:', this.userForm)
        
        this.$store.commit('user/SET_USER_INFO', {
          id: response.id,
          account: response.account,
          username: response.username,
          avatar: response.avatar
        })
      } catch (error) {
        console.error('获取用户信息失败:', {
          error: error.originalError || error,
          message: error.message,
          status: error.status,
          data: error.data,
          config: error.config
        })

        if (error.status === 401) {
          this.$message.error('请先登录')
          this.$router.push('/login')
        } else {
          this.$message.error(error.message || '获取用户信息失败')
        }
      }
    },
    async fetchMyProducts() {
      this.loading = true
      try {
        const response = await getMyProducts()
        this.myProducts = response
      } catch (error) {
        console.error('获取我的商品失败:', error)
        this.$message.error('获取我的商品失败')
      } finally {
        this.loading = false
      }
    },
    async updateUserInfo() {
      try {
        await this.$refs.userForm.validate()
        
        console.log('当前表单数据:', this.userForm)
        
        const updateData = {}
        if (this.userForm.newUsername) {
          updateData.username = this.userForm.newUsername
        }
        if (this.userForm.newAccount) {
          updateData.account = this.userForm.newAccount
          console.log('新的邮箱地址:', this.userForm.newAccount)
        }

        if (Object.keys(updateData).length === 0) {
          this.$message.warning('没有要更新的内容')
          return
        }

        console.log('发送更新请求的数据:', updateData)
        
        const response = await updateUserInfo(updateData)
        console.log('更新响应:', response)
        
        this.$message.success('更新成功')
        this.editMode = false
        
        this.userForm.newUsername = ''
        this.userForm.newAccount = ''
        
        await this.fetchUserInfo()
      } catch (error) {
        console.error('更新失败:', error)
        console.error('错误详情:', {
          response: error.response,
          data: error.response?.data,
          status: error.response?.status,
          config: error.config
        })
        this.$message.error(error.response?.data?.message || '更新失败')
      }
    },
    async handleChangePassword() {
      try {
        await this.$refs.passwordForm.validate()
        await changePassword(this.passwordForm)
        this.$message.success('密码修改成功，请重新登录')
        this.showChangePassword = false
        await this.$store.dispatch('user/logout')
        this.$router.push('/login')
      } catch (error) {
        console.error('修改密码失败:', error)
        if (error.response?.status === 401) {
          this.$message.error('请先登录')
          this.$router.push('/login')
        } else {
          this.$message.error(error.response?.data?.message || '修改密码失败')
        }
      }
    },
    handleDeleteAccount() {
      this.$confirm('此操作将永久删除您的账号, 是否继续?', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          await deleteAccount()
          this.$message.success('账号已注销')
          await this.$store.dispatch('user/logout')
          this.$router.push('/login')
        } catch (error) {
          console.error('注销账号失败:', error)
          if (error.response?.status === 401) {
            this.$message.error('请先登录')
            this.$router.push('/login')
          } else {
            this.$message.error(error.response?.data?.message || '注销账号失败')
          }
        }
      }).catch(() => {
        this.$message.info('已取消注销')
      })
    }
  }
}
</script>

<style scoped>
.user-profile {
  min-height: 100vh;
  background-color: #f5f5f5;
}

.content {
  max-width: 1200px;
  margin: 20px auto;
  padding: 0 20px;
}

.menu-card {
  margin-bottom: 20px;
}

.user-info {
  text-align: center;
  padding: 20px 0;
  border-bottom: 1px solid #eee;
}

.user-info h3 {
  margin: 10px 0;
  color: #303133;
}

.profile-menu {
  border-right: none;
}

.el-menu-item {
  height: 50px;
  line-height: 50px;
}

.el-menu-item i {
  margin-right: 5px;
}

.settings-section {
  margin: 20px 0;
}

.settings-section h3 {
  margin-bottom: 15px;
  font-size: 16px;
  color: #303133;
}

.warning-text {
  color: #f56c6c;
  margin: 10px 0;
  font-size: 14px;
}
</style> 