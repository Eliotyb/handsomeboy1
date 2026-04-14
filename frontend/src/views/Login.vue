<template>
  <div class="login-container">
    <div class="login-wrapper">
      <div class="login-left">
        <div class="logo-section">
          <h1 class="logo-text">⚡ SECKILLADMIN</h1>
        </div>
        <div class="welcome-section">
          <h2 class="welcome-title">社团管理<br/>秒杀系统</h2>
          <p class="welcome-desc">欢迎登录</p>
        </div>
        <div class="illustration-section">
          <img :src="illustrationUrl" alt="Illustration" class="illustration-img" />
        </div>
      </div>
      <div class="login-right">
        <div class="login-box">
          <div class="avatar-section">
            <div class="avatar-circle">
              <img :src="avatarUrl" alt="Avatar" class="avatar-img" />
            </div>
          </div>
          <div class="login-header">
            <h2 class="login-title">管理员登录</h2>
            <p class="login-subtitle">输入您的凭据以访问控制面板</p>
          </div>
          <el-form :model="loginForm" :rules="rules" ref="loginFormRef" class="login-form">
            <el-form-item prop="username">
              <span class="input-label">用户名</span>
              <el-input
                v-model="loginForm.username"
                placeholder="请输入用户名"
                size="large"
                class="login-input"
              >
                <template #prefix>
                  <el-icon><User /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item prop="password">
              <span class="input-label">密码</span>
              <el-input
                v-model="loginForm.password"
                type="password"
                placeholder="请输入密码"
                size="large"
                class="login-input"
                @keyup.enter="handleLogin"
              >
                <template #prefix>
                  <el-icon><Lock /></el-icon>
                </template>
              </el-input>
            </el-form-item>
            <el-form-item class="remember-item">
              <el-checkbox v-model="rememberMe" class="remember-checkbox">记住我</el-checkbox>
              <span class="forgot-password">忘记密码？</span>
            </el-form-item>
            <el-form-item>
              <el-button type="primary" size="large" class="login-button" @click="handleLogin" :loading="loading">
                <span>登录</span>
                <el-icon class="button-icon"><ArrowRight /></el-icon>
              </el-button>
            </el-form-item>
          </el-form>
          <div class="login-footer">
            <p>© 2024 PULSE VELOCITY SECKILL. KINETIC PULSE SYSTEM.</p>
            <div class="social-links">
              <a href="https://v.douyin.com/A2rDQZ4Uy3Q/" target="_blank" class="douyin-link" title="关注我们的抖音">
                <svg viewBox="0 0 24 24" width="20" height="20" fill="currentColor">
                  <path d="M19.59 6.69a4.83 4.83 0 01-3.77-4.25V2h-3.45v13.67a2.89 2.89 0 01-5.2 1.74 2.89 2.89 0 015.2-1.74V7.34a8.27 8.27 0 006.22 3V6.69zM12.5 2h-3.45v13.67a2.89 2.89 0 01-5.2 1.74 2.89 2.89 0 015.2-1.74V2z"/>
                </svg>
                <span>抖音</span>
              </a>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { ElMessage, ElIcon } from 'element-plus'
import { ArrowRight, User, Lock } from '@element-plus/icons-vue'

export default {
  name: 'Login',
  components: {
    ArrowRight,
    User,
    Lock,
    ElIcon
  },
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
      illustrationUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=speeding%20delivery%20van%20on%20dark%20background%2C%20motion%20blur%2C%20orange%20theme%2C%20dynamic%20scene&image_size=landscape_16_9',
      avatarUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20portrait%2C%20formal%20photo%2C%20business%20person%2C%20square%20image&image_size=square'
    }
  },
  methods: {
    handleLogin() {
      this.$refs.loginFormRef.validate((valid) => {
        if (valid) {
          this.loading = true
          axios.post('/api/auth/admin/login', this.loginForm)
            .then(response => {
              const res = response.data
              if (res.code === 200) {
                localStorage.setItem('token', res.data.token)
                localStorage.setItem('userInfo', JSON.stringify(res.data))
                ElMessage.success('登录成功')
                this.$router.push('/dashboard')
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
  background: linear-gradient(135deg, #fff5f5 0%, #ffebee 100%);
  overflow: hidden;
  padding: 40px;
}

.login-wrapper {
  width: 1100px;
  display: flex;
  background: white;
  border-radius: 32px;
  box-shadow: 0 40px 100px rgba(0, 0, 0, 0.15);
  overflow: hidden;
}

.login-left {
  flex: 1;
  background: linear-gradient(135deg, #e65100 0%, #ff6b35 50%, #ff8a65 100%);
  padding: 60px 50px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
  color: white;
  position: relative;
  min-height: 600px;
}

.logo-section {
  margin-bottom: 40px;
}

.logo-text {
  font-size: 28px;
  font-weight: 700;
  margin: 0;
  letter-spacing: 2px;
  text-shadow: 0 2px 10px rgba(0, 0, 0, 0.2);
}

.welcome-section {
  margin-bottom: 40px;
}

.welcome-title {
  font-size: 48px;
  font-weight: 700;
  margin: 0 0 12px 0;
  line-height: 1.2;
}

.welcome-desc {
  font-size: 18px;
  opacity: 0.85;
  margin: 0;
  font-weight: 500;
}

.illustration-section {
  flex: 1;
  display: flex;
  align-items: flex-end;
  justify-content: center;
  position: relative;
  overflow: hidden;
}

.illustration-img {
  width: 100%;
  max-height: 280px;
  border-radius: 20px 20px 0 0;
  object-fit: cover;
  object-position: center center;
  clip-path: inset(0 round 20px 20px 0 0);
}

.illustration-section::after {
  content: '';
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  height: 70px;
  background: linear-gradient(to top, 
    rgba(230, 81, 0, 0.95) 0%, 
    rgba(255, 107, 53, 0.7) 40%,
    rgba(255, 138, 101, 0.4) 70%,
    transparent 100%
  );
  pointer-events: none;
  z-index: 10;
}

.illustration-section::before {
  content: '';
  position: absolute;
  bottom: 8px;
  right: 15px;
  width: 80px;
  height: 28px;
  background: linear-gradient(135deg, #e65100 0%, #ff6b35 50%, #ff8a65 100%);
  border-radius: 6px;
  box-shadow: 0 2px 8px rgba(230, 81, 0, 0.5);
  pointer-events: none;
  z-index: 11;
}

.login-right {
  width: 480px;
  padding: 60px 70px;
  display: flex;
  align-items: center;
  justify-content: center;
  background: white;
}

.login-box {
  width: 100%;
}

.avatar-section {
  text-align: center;
  margin-bottom: 32px;
}

.avatar-circle {
  width: 120px;
  height: 120px;
  border-radius: 50%;
  background: linear-gradient(135deg, #e65100 0%, #ff6b35 100%);
  padding: 6px;
  margin: 0 auto;
  box-shadow: 0 8px 24px rgba(230, 81, 0, 0.3);
}

.avatar-img {
  width: 100%;
  height: 100%;
  border-radius: 50%;
  object-fit: cover;
  background: white;
}

.login-header {
  text-align: center;
  margin-bottom: 40px;
}

.login-title {
  color: #5d4037;
  margin: 0 0 8px 0;
  font-size: 26px;
  font-weight: 700;
}

.login-subtitle {
  color: #8d6e63;
  margin: 0;
  font-size: 14px;
}

.login-form {
  margin-top: 20px;
}

.input-label {
  display: block;
  font-size: 12px;
  font-weight: 600;
  color: #8d6e63;
  margin-bottom: 8px;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.login-input {
  border-radius: 32px;
  overflow: hidden;
}

.login-input :deep(.el-input__wrapper) {
  border-radius: 32px;
  padding: 10px 24px;
  background: #fff3e0;
  box-shadow: none;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.login-input :deep(.el-input__wrapper:hover),
.login-input :deep(.el-input__wrapper.is-focus) {
  border-color: #e65100;
  background: white;
}

.remember-item {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 24px;
}

.remember-checkbox {
  color: #8d6e63;
}

.remember-checkbox :deep(.el-checkbox__label) {
  color: #8d6e63;
  font-size: 14px;
}

.forgot-password {
  color: #c62828;
  cursor: pointer;
  font-size: 14px;
  font-weight: 600;
}

.forgot-password:hover {
  text-decoration: underline;
}

.login-button {
  width: 100%;
  border-radius: 32px;
  height: 56px;
  font-size: 16px;
  font-weight: 700;
  background: linear-gradient(135deg, #b71c1c 0%, #8d1c00 100%);
  border: none;
  transition: all 0.3s;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  box-shadow: 0 8px 24px rgba(183, 28, 28, 0.4);
}

.login-button:hover {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(183, 28, 28, 0.5);
}

.button-icon {
  font-size: 18px;
}

.login-footer {
  text-align: center;
  margin-top: 48px;
}

.login-footer p {
  color: #a1887f;
  font-size: 11px;
  margin: 0 0 16px 0;
  line-height: 1.5;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.social-links {
  display: flex;
  justify-content: center;
  gap: 20px;
}

.douyin-link {
  display: flex;
  align-items: center;
  gap: 6px;
  color: #ff2c55;
  text-decoration: none;
  font-size: 13px;
  font-weight: 600;
  padding: 8px 16px;
  border-radius: 20px;
  background: linear-gradient(135deg, #fff5f5 0%, #ffebee 100%);
  transition: all 0.3s;
  border: 1px solid transparent;
}

.douyin-link:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(255, 44, 85, 0.3);
  border-color: #ff2c55;
  background: white;
}

@media (max-width: 1200px) {
  .login-wrapper {
    width: 900px;
  }
  
  .login-left {
    padding: 50px 40px;
  }
  
  .login-right {
    width: 420px;
    padding: 50px 50px;
  }
  
  .welcome-title {
    font-size: 40px;
  }
}

@media (max-width: 968px) {
  .login-container {
    padding: 20px;
  }
  
  .login-wrapper {
    flex-direction: column;
    width: 100%;
    max-width: 500px;
  }
  
  .login-left {
    min-height: 400px;
    padding: 40px 30px;
  }
  
  .login-right {
    width: 100%;
    padding: 50px 40px;
  }
  
  .welcome-title {
    font-size: 36px;
  }
  
  .illustration-img {
    max-height: 200px;
  }
}

@media (max-width: 480px) {
  .login-container {
    padding: 0;
  }
  
  .login-wrapper {
    border-radius: 0;
  }
  
  .login-left {
    padding: 30px 20px;
  }
  
  .login-right {
    padding: 40px 24px;
  }
  
  .welcome-title {
    font-size: 28px;
  }
  
  .avatar-circle {
    width: 100px;
    height: 100px;
  }
}
</style>
