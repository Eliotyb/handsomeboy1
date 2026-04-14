<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">客服中心</h1>
      <div class="header-right"></div>
    </header>

    <div class="service-content">
      <div class="service-banner">
        <div class="banner-icon">
          <el-icon><Service /></el-icon>
        </div>
        <h2 class="banner-title">在线客服</h2>
        <p class="banner-desc">工作时间：9:00 - 21:00</p>
        <el-button type="primary" size="large" round @click="startChat">
          <el-icon><ChatDotRound /></el-icon>
          立即咨询
        </el-button>
      </div>

      <div class="service-section">
        <h3 class="section-title">常见问题</h3>
        <div class="faq-list">
          <el-collapse v-model="activeFaq">
            <el-collapse-item title="如何下单购买商品？" name="1">
              <p>您可以在商品详情页点击"立即抢购"或"加入购物车"，然后在购物车页面结算下单。</p>
            </el-collapse-item>
            <el-collapse-item title="订单支付后可以退款吗？" name="2">
              <p>未发货的订单可以申请全额退款，已发货的订单需要先退货再退款。具体请查看退款政策。</p>
            </el-collapse-item>
            <el-collapse-item title="优惠券如何使用？" name="3">
              <p>在结算时可以选择可用的优惠券，系统会自动计算优惠金额。</p>
            </el-collapse-item>
            <el-collapse-item title="配送需要多长时间？" name="4">
              <p>一般情况下，同城配送1-3天，异地配送3-7天。具体以商品详情页说明为准。</p>
            </el-collapse-item>
          </el-collapse>
        </div>
      </div>

      <div class="contact-section">
        <h3 class="section-title">联系我们</h3>
        <div class="contact-list">
          <div class="contact-item">
            <el-icon class="contact-icon"><Phone /></el-icon>
            <div class="contact-info">
              <span class="contact-label">客服热线</span>
              <span class="contact-value">400-123-4567</span>
            </div>
          </div>
          <div class="contact-item">
            <el-icon class="contact-icon"><Message /></el-icon>
            <div class="contact-info">
              <span class="contact-label">邮箱</span>
              <span class="contact-value">support@groupon.com</span>
            </div>
          </div>
          <div class="contact-item">
            <el-icon class="contact-icon"><LocationInformation /></el-icon>
            <div class="contact-info">
              <span class="contact-label">地址</span>
              <span class="contact-value">北京市朝阳区xxx大厦</span>
            </div>
          </div>
        </div>
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
import { ArrowLeft, Service, ChatDotRound, Phone, Message, LocationInformation, House, Goods, ShoppingCart, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileService',
  components: {
    ArrowLeft,
    Service,
    ChatDotRound,
    Phone,
    Message,
    LocationInformation,
    House,
    Goods,
    ShoppingCart,
    UserFilled
  },
  data() {
    return {
      activeFaq: ['1']
    }
  },
  methods: {
    startChat() {
      ElMessage.info('客服功能开发中，敬请期待！')
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

.service-content {
  padding: 16px;
}

.service-banner {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border-radius: 16px;
  padding: 40px 24px;
  text-align: center;
  color: white;
  margin-bottom: 16px;
}

.banner-icon {
  width: 72px;
  height: 72px;
  background: rgba(255, 255, 255, 0.2);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 16px;
  font-size: 36px;
}

.banner-title {
  font-size: 22px;
  font-weight: 700;
  margin: 0 0 8px;
}

.banner-desc {
  font-size: 14px;
  opacity: 0.9;
  margin: 0 0 24px;
}

.service-section, .contact-section {
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

.faq-list :deep(.el-collapse-item__header) {
  font-size: 14px;
  font-weight: 500;
}

.faq-list :deep(.el-collapse-item__content) {
  font-size: 13px;
  color: #666;
  line-height: 1.6;
  padding: 12px 0;
}

.contact-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.contact-item {
  display: flex;
  align-items: center;
  gap: 12px;
}

.contact-icon {
  font-size: 24px;
  color: #ff6b6b;
  width: 40px;
  height: 40px;
  background: #fff5f5;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  flex-shrink: 0;
}

.contact-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.contact-label {
  font-size: 12px;
  color: #999;
}

.contact-value {
  font-size: 15px;
  color: #333;
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
