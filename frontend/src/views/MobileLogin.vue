<template>
  <div class="mobile-login-container">
    <div class="login-content">
      <div class="logo-section">
        <div class="logo-icon">
          <el-icon class="logo-icon-el"><Lightning /></el-icon>
        </div>
        <h1 class="logo-text">PULSE</h1>
        <p class="logo-tag">VELOCITY COMMUNITY</p>
      </div>

      <div class="form-section">
        <h2 class="welcome-title">欢迎回来</h2>
        <p class="welcome-desc">瞬时秒杀，抢先一步。请登录您的账号。</p>

        <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
          <div class="form-item-wrapper">
            <div class="form-label">
              <span class="label-text">手机号 / 用户名</span>
            </div>
            <div class="input-wrapper">
              <el-icon class="input-icon"><User /></el-icon>
              <el-input
                v-model="loginForm.username"
                placeholder="输入手机号或用户名"
                class="custom-input"
              />
            </div>
          </div>

          <div class="form-item-wrapper">
            <div class="form-label">
              <span class="label-text">密码</span>
              <span class="forgot-link">忘记密码？</span>
            </div>
            <div class="input-wrapper">
              <el-icon class="input-icon"><Lock /></el-icon>
              <el-input
                v-model="loginForm.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="输入您的密码"
                class="custom-input"
                @keyup.enter="handleLogin"
              >
                <template #suffix>
                  <el-icon class="eye-icon" @click="showPassword = !showPassword">
                    <View v-if="!showPassword" />
                    <Hide v-else />
                  </el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <el-button 
            type="primary" 
            class="login-btn" 
            @click="handleLogin" 
            :loading="loading"
          >
            立即登录
            <el-icon><ArrowRight /></el-icon>
          </el-button>
        </el-form>

        <div class="divider-section">
          <div class="divider-line"></div>
          <span class="divider-text">快速登录</span>
          <div class="divider-line"></div>
        </div>

        <div class="social-login">
          <div class="social-item">
            <span class="social-emoji">📱</span>
          </div>
          <div class="social-item">
            <span class="social-emoji">🍎</span>
          </div>
        </div>

        <div class="register-section">
          <p class="register-text">还没有脉动账号？</p>
          <el-button class="register-btn" @click="goToRegister">
            <el-icon><UserFilled /></el-icon>
            立即注册成为脉动成员
          </el-button>
        </div>
      </div>
    </div>

    <div class="decoration-circle circle-1"></div>
    <div class="decoration-circle circle-2"></div>
    <div class="decoration-line"></div>
  </div>
</template>

<script>
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { 
  Lightning, User, Lock, View, Hide, ArrowRight, UserFilled 
} from '@element-plus/icons-vue'

export default {
  name: 'MobileLogin',
  components: {
    Lightning,
    User,
    Lock,
    View,
    Hide,
    ArrowRight,
    UserFilled
  },
  data() {
    return {
      loginForm: {
        username: '',
        password: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [{ required: true, message: '请输入密码', trigger: 'blur' }]
      },
      loading: false,
      showPassword: false
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
                this.$router.push('/mobile')
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
    },
    goToRegister() {
      this.$router.push('/mobile/register')
    }
  }
}
</script>

<style scoped>
.mobile-login-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #fff5f5 0%, #ffffff 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.login-content {
  position: relative;
  z-index: 10;
  max-width: 400px;
  margin: 0 auto;
}

.logo-section {
  text-align: center;
  padding: 40px 0 60px;
}

.logo-icon {
  width: 80px;
  height: 80px;
  background: linear-gradient(135deg, #ff6b35 0%, #ff8a5c 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 20px;
  box-shadow: 0 8px 24px rgba(255, 107, 53, 0.3);
}

.logo-icon-el {
  font-size: 40px;
  color: white;
}

.logo-text {
  font-size: 48px;
  font-weight: 800;
  color: #ff6b35;
  margin: 0 0 8px;
  letter-spacing: -2px;
}

.logo-tag {
  font-size: 12px;
  color: #8d6e63;
  margin: 0;
  text-transform: uppercase;
  letter-spacing: 2px;
  font-weight: 600;
}

.form-section {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 40px;
  padding: 40px 30px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 20px 60px rgba(255, 107, 53, 0.1);
}

.welcome-title {
  font-size: 36px;
  font-weight: 800;
  color: #4a1c00;
  margin: 0 0 8px;
  letter-spacing: -1px;
}

.welcome-desc {
  font-size: 14px;
  color: #8d6e63;
  margin: 0 0 40px;
  line-height: 1.6;
}

.login-form {
  margin-bottom: 32px;
}

.form-item-wrapper {
  margin-bottom: 24px;
}

.form-label {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 12px;
}

.label-text {
  font-size: 13px;
  font-weight: 600;
  color: #1e40af;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.forgot-link {
  font-size: 13px;
  color: #b91c1c;
  cursor: pointer;
  font-weight: 500;
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  background: white;
  border-radius: 50px;
  padding: 4px 8px 4px 20px;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.input-wrapper:focus-within {
  border-color: #ff6b35;
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.15);
}

.input-icon {
  color: #9ca3af;
  font-size: 20px;
  margin-right: 12px;
}

.custom-input {
  flex: 1;
}

.custom-input :deep(.el-input__wrapper) {
  box-shadow: none !important;
  padding: 0;
  background: transparent;
}

.custom-input :deep(.el-input__inner) {
  font-size: 16px;
  color: #374151;
}

.eye-icon {
  color: #9ca3af;
  font-size: 20px;
  cursor: pointer;
}

.login-btn {
  width: 100%;
  height: 60px;
  border-radius: 50px;
  background: linear-gradient(135deg, #ff4500 0%, #ff6b00 100%);
  border: none;
  font-size: 18px;
  font-weight: 700;
  color: white;
  margin-top: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 8px 24px rgba(255, 69, 0, 0.35);
  transition: all 0.3s;
}

.login-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(255, 69, 0, 0.45);
}

.divider-section {
  display: flex;
  align-items: center;
  gap: 16px;
  margin: 32px 0 24px;
}

.divider-line {
  flex: 1;
  height: 1px;
  background: #e5e7eb;
}

.divider-text {
  font-size: 12px;
  color: #6b7280;
  font-weight: 500;
}

.social-login {
  display: flex;
  justify-content: center;
  gap: 24px;
  margin-bottom: 32px;
}

.social-item {
  width: 56px;
  height: 56px;
  background: white;
  border-radius: 16px;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  border: 1px solid #f3f4f6;
}

.social-emoji {
  font-size: 28px;
}

.register-section {
  text-align: center;
  padding-top: 16px;
  border-top: 1px solid #f3f4f6;
}

.register-text {
  font-size: 14px;
  color: #4b5563;
  margin: 0 0 16px;
}

.register-btn {
  width: 100%;
  height: 56px;
  border-radius: 50px;
  background: white;
  border: 2px solid #ff4500;
  color: #ff4500;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  transition: all 0.3s;
}

.register-btn:hover {
  background: #fff7ed;
  transform: translateY(-2px);
}

.decoration-circle {
  position: absolute;
  border-radius: 50%;
  opacity: 0.3;
  pointer-events: none;
}

.circle-1 {
  width: 300px;
  height: 300px;
  background: linear-gradient(135deg, #ff6b35 0%, transparent 50%);
  right: -150px;
  top: 100px;
}

.circle-2 {
  width: 200px;
  height: 200px;
  background: linear-gradient(135deg, transparent 50%, #ff8a5c 100%);
  left: -100px;
  bottom: 50px;
}

.decoration-line {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 8px;
  background: linear-gradient(90deg, #8b4513 0%, #ff6b35 50%, #ff8a5c 100%);
  pointer-events: none;
}
</style>
