<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="logo-section">
          <h1 class="system-title">🛒 社团管理秒杀系统</h1>
        </div>
        <div class="welcome-section">
          <h2 class="welcome-title">欢迎登录</h2>
          <p class="welcome-desc">高效管理 · 便捷团购 · 精准秒杀</p>
        </div>
        <div class="illustration-section">
          <img :src="illustrationUrl" alt="Illustration" class="illustration-img" />
        </div>
      </div>
      <div class="login-right">
        <div class="login-box">
          <div class="login-header">
            <h2 class="login-title">管理员登录</h2>
          </div>
          <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
            <el-form-item prop="username">
              <el-input
                v-model="loginForm.username"
                placeholder="请输入账号"
                size="large"
                prefix-icon="User"
                class="login-input"
              />
            </el-form-item>
            <el-form-item prop="password">
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                prefix-icon="Lock"
                class="login-input"
                @keyup.enter="handleLogin"
              />
            </el-form-item>
            <el-form-item class="remember-item">
              <el-checkbox v-model="rememberMe">记住我</el-checkbox>
              <span class="forgot-password">忘记密码？</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" class="login-button" @click="handleLogin" :loading="loading">
                登录
              </el-button>
            </el-form-item>
          </el-form>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'Login',
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入账号', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false,
      rememberMe: false,
      illustrationUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=e-commerce%20shopping%20cart%20and%20mobile%20phone%20with%20discount%20tags%2C%20coins%20and%20shopping%20bags%2C%20blue%20and%20purple%20gradient%20background%2C%20flat%20vector%20illustration%20style&image_size=landscape_16_9'
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
          this.loading = true
          axios.post('/api/auth/login', this.loginForm)
            .then(response => {
              const res = response.data
              if (res.code === 200) {
                localStorage.setItem('token', res.data.token)
                localStorage.setItem('userInfo', JSON.stringify(res.data))
                ElMessage.success('登录成功')
                this.$router.push('/')
              } else {
                ElMessage.error(res.message || '登录失败')
              }
            })
            .catch(error => {
              console.error('登录失败:', error)
              ElMessage.error('登录失败，请检查网络连接')
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    }
  }
}
</script>

<style scoped>
.login-container {
  min-height: 100vh;
  display: flex;
  align-items: center;
  justify-content: center;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 50%, #a855f7 100%);
  overflow: hidden;
}

.login-wrapper {
  width: 1000px;
  display: flex;
  background: white;
  border-radius: 24px;
  box-shadow: 0 30px 80px rgba(0, 0, 0, 0.25);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  padding: 50px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: white;
  position: relative;
}

.logo-section {
  margin-bottom: 30px;
}

.system-title {
  font-size: 32px;
  font-weight: 700;
  margin: 0;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.welcome-section {
  margin-bottom: 40px;
}

.welcome-title {
  font-size: 36px;
  font-weight: 600;
  margin: 0 0 10px 0;
}

.welcome-desc {
  font-size: 16px;
  opacity: 0.9;
  margin: 0;
}

.illustration-section {
  flex: 1;
  display: flex;
  align-items: center;
  justify-content: center;
}

.illustration-img {
  max-width: 100%;
  max-height: 300px;
  border-radius: 16px;
}

.login-right {
  width: 420px;
  padding: 60px 50px;
  display: flex;
  align-items: center;
  justify-content: center;
}

.login-box {
  width: 100%;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-title {
  color: #333;
  margin: 0;
  font-size: 28px;
  font-weight: 600;
}

.login-form {
  margin-top: 20px;
}

.login-input {
  border-radius: 28px;
  overflow: hidden;
}

.login-input :deep(.el-input__wrapper) {
  border-radius: 28px;
  padding: 8px 20px;
  background: #f5f7fa;
  box-shadow: none;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.login-input :deep(.el-input__wrapper:hover),
.login-input :deep(.el-input__wrapper.is-focus) {
  border-color: #667eea;
  background: white;
}

.remember-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 20px;
}

.forgot-password {
  color: #667eea;
  cursor: pointer;
  font-size: 14px;
}

.forgot-password:hover {
  text-decoration: underline;
}

.login-button {
  width: 100%;
  border-radius: 28px;
  height: 50px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  border: none;
  transition: all 0.3s;
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 8px 20px rgba(102, 126, 234, 0.4);
}
</style>
