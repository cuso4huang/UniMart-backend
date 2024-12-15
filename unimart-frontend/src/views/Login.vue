<template>
  <div class="login-container">
    <div class="login-box">
      <h2 class="title">校园二手交易平台</h2>
      <el-form 
        ref="loginForm" 
        :model="loginForm" 
        :rules="loginRules"
        class="login-form"
      >
        <el-form-item prop="username">
          <el-input 
            v-model="loginForm.username"
            prefix-icon="el-icon-user"
            placeholder="用户名"
          ></el-input>
        </el-form-item>
        <el-form-item prop="password">
          <el-input 
            v-model="loginForm.password" 
            prefix-icon="el-icon-lock"
            type="password" 
            placeholder="密码"
            @keyup.enter.native="handleLogin"
            show-password
          ></el-input>
        </el-form-item>
        <el-form-item>
          <el-button 
            :loading="loading" 
            type="primary" 
            class="login-button"
            @click="handleLogin"
          >
            {{ loading ? '登录中...' : '登录' }}
          </el-button>
        </el-form-item>
        <div class="register-link">
          <span>还没有账号？</span>
          <router-link to="/register">立即注册</router-link>
        </div>
      </el-form>
    </div>
  </div>
</template>

<script>
import { mapActions } from 'vuex'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      loginRules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ]
      },
      loading: false
    }
  },
  methods: {
    ...mapActions('user', ['login']),
    async handleLogin() {
      try {
        await this.$refs.loginForm.validate()
        this.loading = true
        
        const formData = {
          username: this.loginForm.username,
          password: this.loginForm.password
        }
        
        const response = await this.login(formData)
        
        if (response && response.token) {
          this.$message({
            type: 'success',
            message: '登录成功',
            duration: 2000
          })
          
          // 延迟跳转，让用户看到成功提示
          setTimeout(() => {
            const redirect = this.$route.query.redirect || '/'
            this.$router.push(redirect)
          }, 1000)
        }
      } catch (error) {
        console.error('Login Error:', error)
        this.$message({
          type: 'error',
          message: error.message || '登录失败，请重试',
          duration: 3000
        })
      } finally {
        this.loading = false
      }
    }
  }
}
</script>

<style scoped>
.login-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f5f5;
  background-image: linear-gradient(120deg, #e0c3fc 0%, #8ec5fc 100%);
}

.login-box {
  width: 400px;
  padding: 40px;
  background: #fff;
  border-radius: 8px;
  box-shadow: 0 2px 12px 0 rgba(0, 0, 0, 0.1);
}

.title {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
  font-size: 24px;
}

.login-form {
  margin-top: 20px;
}

.login-button {
  width: 100%;
  margin-top: 10px;
  padding: 12px 0;
}

.register-link {
  text-align: center;
  margin-top: 20px;
  font-size: 14px;
  color: #606266;
}

.register-link a {
  color: #409EFF;
  margin-left: 5px;
  text-decoration: none;
}

.register-link a:hover {
  color: #66b1ff;
}

.el-input {
  margin-bottom: 10px;
}

.el-button--primary {
  background-color: #409EFF;
  border-color: #409EFF;
}

.el-button--primary:hover {
  background-color: #66b1ff;
  border-color: #66b1ff;
}
</style> 