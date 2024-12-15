<template>
  <div class="register-container">
    <el-card class="register-card">
      <h2>注册</h2>
      <el-form 
        ref="registerForm" 
        :model="registerForm" 
        :rules="rules" 
        label-width="80px"
      >
        <el-form-item label="用户名" prop="username">
          <el-input 
            v-model="registerForm.username" 
            placeholder="请输入用户名"
          ></el-input>
        </el-form-item>

        <el-form-item label="密码" prop="password">
          <el-input 
            v-model="registerForm.password" 
            type="password" 
            placeholder="请输入密码"
          ></el-input>
        </el-form-item>

        <el-form-item label="确认密码" prop="confirmPassword">
          <el-input 
            v-model="registerForm.confirmPassword" 
            type="password" 
            placeholder="请再次输入密码"
          ></el-input>
        </el-form-item>

        <el-form-item label="邮箱" prop="account">
          <el-input 
            v-model="registerForm.account" 
            placeholder="请输入邮箱"
          ></el-input>
        </el-form-item>

        <el-form-item>
          <el-button type="primary" @click="handleRegister">注册</el-button>
          <el-button @click="$router.push('/login')">返回登录</el-button>
        </el-form-item>
      </el-form>
    </el-card>
  </div>
</template>

<script>
import { register } from '@/api/auth'

export default {
  name: 'Register',
  data() {
    const validatePass2 = (rule, value, callback) => {
      if (value !== this.registerForm.password) {
        callback(new Error('两次输入密码不一致!'))
      } else {
        callback()
      }
    }
    return {
      registerForm: {
        username: '',
        password: '',
        confirmPassword: '',
        account: ''
      },
      rules: {
        username: [
          { required: true, message: '请输入用户名', trigger: 'blur' },
          { min: 3, max: 20, message: '长度在 3 到 20 个字符', trigger: 'blur' }
        ],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 6, max: 20, message: '长度在 6 到 20 个字符', trigger: 'blur' }
        ],
        confirmPassword: [
          { required: true, message: '请再次输入密码', trigger: 'blur' },
          { validator: validatePass2, trigger: 'blur' }
        ],
        account: [
          { required: true, message: '请输入邮箱地址', trigger: 'blur' },
          { type: 'email', message: '请输入正确的邮箱地址', trigger: 'blur' }
        ]
      }
    }
  },
  methods: {
    handleRegister() {
      this.$refs.registerForm.validate(async valid => {
        if (valid) {
          try {
            await register({
              username: this.registerForm.username,
              password: this.registerForm.password,
              account: this.registerForm.account
            })
            this.$message.success('注册成功')
            this.$router.push('/login')
          } catch (error) {
            console.error('注册失败:', error)
            this.$message.error(error.response?.data?.message || '注册失败')
          }
        }
      })
    }
  }
}
</script>

<style scoped>
.register-container {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100vh;
  background-color: #f5f7fa;
}

.register-card {
  width: 400px;
}

.register-card h2 {
  text-align: center;
  margin-bottom: 30px;
  color: #303133;
}

.el-button {
  width: 100%;
  margin-top: 10px;
}

.el-button + .el-button {
  margin-left: 0;
  margin-top: 10px;
}
</style> 