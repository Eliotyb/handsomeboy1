<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">关于我们</h1>
      <div class="header-right"></div>
    </header>

    <div class="about-content">
      <div class="about-header">
        <div class="logo-section">
          <div class="logo-circle">
            <span class="logo-text">⚡</span>
          </div>
          <h2 class="app-name">PULSE VELOCITY</h2>
          <p class="app-slogan">瞬时秒杀，抢先一步</p>
        </div>
      </div>

      <div class="info-cards">
        <div class="info-card">
          <h3 class="card-title">📱 应用信息</h3>
          <div class="info-row">
            <span class="label">版本号</span>
            <span class="value">v1.0.0</span>
          </div>
          <div class="info-row">
            <span class="label">更新时间</span>
            <span class="value">2026-04-13</span>
          </div>
          <div class="info-row">
            <span class="label">开发团队</span>
            <span class="value">PULSE Team</span>
          </div>
        </div>

        <div class="info-card">
          <h3 class="card-title">🏢 公司信息</h3>
          <div class="info-row">
            <span class="label">公司名称</span>
            <span class="value">PULSE VELOCITY 社区团购平台</span>
          </div>
          <div class="info-row">
            <span class="label">联系电话</span>
            <span class="value">400-123-4567</span>
          </div>
          <div class="info-row">
            <span class="label">官方邮箱</span>
            <span class="value">contact@pulse.com</span>
          </div>
          <div class="info-row">
            <span class="label">公司地址</span>
            <span class="value">北京市朝阳区xxx大厦</span>
          </div>
        </div>
      </div>

      <div class="feature-section">
        <h3 class="section-title">✨ 核心功能</h3>
        <div class="feature-grid">
          <div class="feature-item">
            <el-icon class="feature-icon"><Timer /></el-icon>
            <span>限时秒杀</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><Goods /></el-icon>
            <span>精选商品</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><ShoppingCart /></el-icon>
            <span>便捷购物</span>
          </div>
          <div class="feature-item">
            <el-icon class="feature-icon"><TrophyBase /></el-icon>
            <span>品质保证</span>
          </div>
        </div>
      </div>

      <div class="links-section">
        <h3 class="section-title">🔗 相关链接</h3>
        <div class="link-list">
          <div class="link-item" @click="openLink('privacy')">
            <span>隐私政策</span>
            <el-icon><ArrowRight /></el-icon>
          </div>
          <div class="link-item" @click="openLink('terms')">
            <span>用户协议</span>
            <el-icon><ArrowRight /></el-icon>
          </div>
          <div class="link-item" @click="openLink('license')">
            <span>开源许可</span>
            <el-icon><ArrowRight /></el-icon>
          </div>
        </div>
      </div>

      <div class="social-section">
        <h3 class="section-title">🌐 关注我们</h3>
        <div class="social-icons">
          <div class="social-item" @click="openSocial('wechat')">
            <el-icon><ChatDotRound /></el-icon>
            <span>微信</span>
          </div>
          <a href="https://v.douyin.com/A2rDQZ4Uy3Q/" target="_blank" class="social-item douyin-link" title="关注我们的抖音">
            <el-icon><VideoPlay /></el-icon>
            <span>抖音</span>
          </a>
          <div class="social-item" @click="openSocial('weibo')">
            <el-icon><Promotion /></el-icon>
            <span>微博</span>
          </div>
        </div>
      </div>

      <div class="copyright">
        <p>© 2026 PULSE VELOCITY. All Rights Reserved.</p>
        <p class="tech-stack">Built with Vue 3 + Spring Boot ❤️</p>
      </div>
    </div>

    <footer class="mobile-footer">
      <div class="footer-item" @click="$router.push('/mobile')">
        <el-icon class="footer-icon"><House /></el-icon>
        <span class="footer-text">首页</span>
      </div>
      <div class="footer-item" @click="$router.push('/mobile/category')">
        <el-icon class="footer-icon"><Goods /></el-icon>
        <span class="footer-text">分类</span>
      </div>
      <div class="footer-item" @click="$router.push('/mobile/cart')">
        <el-icon class="footer-icon"><ShoppingCart /></el-icon>
        <span class="footer-text">购物车</span>
      </div>
      <div class="footer-item active" @click="$router.push('/mobile/profile')">
        <el-icon class="footer-icon"><UserFilled /></el-icon>
        <span class="footer-text">我的</span>
      </div>
    </footer>
  </div>
</template>

<script>
import { ArrowLeft, ArrowRight, Timer, Goods, ShoppingCart, TrophyBase,
         ChatDotRound, VideoPlay, Promotion, House, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileAbout',
  components: {
    ArrowLeft,
    ArrowRight,
    Timer,
    Goods,
    ShoppingCart,
    TrophyBase,
    ChatDotRound,
    VideoPlay,
    Promotion,
    House,
    UserFilled
  },
  methods: {
    openLink(type) {
      const links = {
        privacy: '隐私政策页面开发中',
        terms: '用户协议页面开发中',
        license: '开源许可信息页面开发中'
      }
      ElMessage.info(links[type])
    },
    openSocial(platform) {
      const messages = {
        wechat: '请搜索"PULSE_VELOCITY"添加微信',
        weibo: '请访问微博搜索"PULSE_VELOCITY"'
      }
      ElMessage.info(messages[platform] || '功能开发中')
    }
  }
}
</script>

<style scoped>
.mobile-container {
  min-height: 100vh;
  background: #f5f5f5;
  padding-bottom: 60px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left {
  width: 32px;
  font-size: 22px;
  color: #333;
  cursor: pointer;
}

.page-title {
  font-size: 17px;
  font-weight: 600;
  margin: 0;
  flex: 1;
  text-align: center;
}

.header-right {
  width: 32px;
}

.about-content {
  padding: 16px;
}

.about-header {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border-radius: 16px;
  padding: 40px 24px;
  text-align: center;
  color: white;
  margin-bottom: 16px;
}

.logo-circle {
  width: 80px;
  height: 80px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  font-size: 40px;
}

.app-name {
  font-size: 24px;
  font-weight: 700;
  margin: 0 0 8px;
  letter-spacing: 2px;
}

.app-slogan {
  font-size: 14px;
  opacity: 0.9;
  margin: 0;
}

.info-cards {
  display: flex;
  flex-direction: column;
  gap: 12px;
  margin-bottom: 16px;
}

.info-card {
  background: white;
  border-radius: 12px;
  padding: 20px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.card-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px;
}

.info-row {
  display: flex;
  justify-content: space-between;
  padding: 10px 0;
  border-bottom: 1px solid #f5f5f5;
}

.info-row:last-child {
  border-bottom: none;
}

.label {
  font-size: 14px;
  color: #666;
}

.value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

.feature-section, .links-section, .social-section {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px;
}

.feature-grid {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 12px;
}

.feature-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  padding: 16px 8px;
  background: #fafafa;
  border-radius: 8px;
}

.feature-icon {
  font-size: 28px;
  color: #ff6b6b;
}

.feature-item span {
  font-size: 12px;
  color: #666;
}

.link-list {
  display: flex;
  flex-direction: column;
}

.link-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 0;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: all 0.3s;
}

.link-item:last-child {
  border-bottom: none;
}

.link-item:hover {
  color: #ff6b6b;
}

.link-item span {
  font-size: 14px;
  color: #333;
}

.social-icons {
  display: flex;
  justify-content: space-around;
}

.social-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  gap: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.social-item:hover .el-icon {
  transform: scale(1.2);
}

.douyin-link {
  text-decoration: none;
  color: inherit;
}

.douyin-link:hover {
  transform: translateY(-4px) scale(1.1);
  box-shadow: 0 8px 20px rgba(255, 44, 85, 0.3);
  background: linear-gradient(135deg, #fff5f5 0%, #ffebee 100%);
}

.douyin-link .el-icon {
  color: #ff2c55;
}

.douyin-link span {
  color: #ff2c55;
  font-weight: 600;
}

.social-item .el-icon {
  font-size: 28px;
  color: #ff6b6b;
}

.social-item span {
  font-size: 12px;
  color: #666;
}

.copyright {
  text-align: center;
  padding: 24px 0;
  color: #999;
  font-size: 12px;
}

.copyright p {
  margin: 4px 0;
}

.tech-stack {
  color: #ff6b6b !important;
  font-weight: 500;
}

.mobile-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background: white;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.footer-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 0;
  cursor: pointer;
  transition: all 0.3s;
  color: #999;
}

.footer-item.active {
  color: #ff6b6b;
}

.footer-icon {
  font-size: 20px;
  margin-bottom: 4px;
}

.footer-text {
  font-size: 12px;
}
</style>
