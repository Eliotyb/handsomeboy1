<template>
  <div class="mobile-container">
    <header class="profile-header">
      <div class="header-bg"></div>
      <div class="user-info">
        <img :src="userAvatar" class="user-avatar" alt="用户头像" />
        <div class="user-details">
          <h2 class="user-name">{{ userName }}</h2>
          <p class="user-tag">{{ getRoleText() }}</p>
        </div>
        <el-button class="settings-btn" circle @click="goToSettings">
          <el-icon><Setting /></el-icon>
        </el-button>
      </div>
    </header>

    <div class="profile-content">
      <div class="order-section">
        <div class="section-header">
          <h3 class="section-title">我的订单</h3>
          <span class="more-link" @click="goToOrders">全部订单</span>
        </div>
        <div class="order-stats">
          <div class="stat-item" @click="$router.push('/mobile/order-pending')">
            <div class="stat-icon">
              <el-icon><Wallet /></el-icon>
            </div>
            <span class="stat-text">待付款</span>
            <span class="stat-badge" v-if="orderStats.pendingPayment > 0">{{ orderStats.pendingPayment }}</span>
          </div>
          <div class="stat-item" @click="$router.push('/mobile/order-shipping')">
            <div class="stat-icon">
              <el-icon><Van /></el-icon>
            </div>
            <span class="stat-text">待发货</span>
            <span class="stat-badge" v-if="orderStats.pendingShipment > 0">{{ orderStats.pendingShipment }}</span>
          </div>
          <div class="stat-item" @click="$router.push('/mobile/order-received')">
            <div class="stat-icon">
              <el-icon><Box /></el-icon>
            </div>
            <span class="stat-text">待收货</span>
            <span class="stat-badge" v-if="orderStats.pendingReceive > 0">{{ orderStats.pendingReceive }}</span>
          </div>
          <div class="stat-item" @click="$router.push('/mobile/order-review')">
            <div class="stat-icon">
              <el-icon><ChatDotRound /></el-icon>
            </div>
            <span class="stat-text">待评价</span>
            <span class="stat-badge" v-if="orderStats.pendingReview > 0">{{ orderStats.pendingReview }}</span>
          </div>
          <div class="stat-item" @click="$router.push('/mobile/after-sale')">
            <div class="stat-icon">
              <el-icon><RefreshRight /></el-icon>
            </div>
            <span class="stat-text">退换货</span>
            <span class="stat-badge" v-if="orderStats.refund > 0">{{ orderStats.refund }}</span>
          </div>
        </div>
      </div>

      <div class="menu-section">
        <div class="menu-item" @click="handleMenuClick('address')">
          <div class="menu-left">
            <el-icon class="menu-icon"><Location /></el-icon>
            <span class="menu-text">收货地址</span>
          </div>
          <el-icon class="menu-arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="handleMenuClick('coupon')">
          <div class="menu-left">
            <el-icon class="menu-icon"><Ticket /></el-icon>
            <span class="menu-text">优惠券</span>
          </div>
          <el-icon class="menu-arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="handleMenuClick('favorite')">
          <div class="menu-left">
            <el-icon class="menu-icon"><Star /></el-icon>
            <span class="menu-text">我的收藏</span>
          </div>
          <el-icon class="menu-arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="handleMenuClick('history')">
          <div class="menu-left">
            <el-icon class="menu-icon"><Clock /></el-icon>
            <span class="menu-text">浏览记录</span>
          </div>
          <el-icon class="menu-arrow"><ArrowRight /></el-icon>
        </div>
      </div>

      <div class="menu-section">
        <div class="menu-item" @click="handleMenuClick('service')">
          <div class="menu-left">
            <el-icon class="menu-icon"><Service /></el-icon>
            <span class="menu-text">客服中心</span>
          </div>
          <el-icon class="menu-arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="handleMenuClick('help')">
          <div class="menu-left">
            <el-icon class="menu-icon"><QuestionFilled /></el-icon>
            <span class="menu-text">帮助中心</span>
          </div>
          <el-icon class="menu-arrow"><ArrowRight /></el-icon>
        </div>
        <div class="menu-item" @click="handleMenuClick('about')">
          <div class="menu-left">
            <el-icon class="menu-icon"><InfoFilled /></el-icon>
            <span class="menu-text">关于我们</span>
          </div>
          <el-icon class="menu-arrow"><ArrowRight /></el-icon>
        </div>
      </div>

      <div class="logout-section">
        <el-button class="logout-btn" @click="handleLogout">
          退出登录
        </el-button>
      </div>
    </div>

    <footer class="mobile-footer">
      <div class="footer-item" @click="navigateTo('/mobile')">
        <el-icon class="footer-icon"><House /></el-icon>
        <span class="footer-text">首页</span>
      </div>
      <div class="footer-item" @click="navigateTo('/mobile/category')">
        <el-icon class="footer-icon"><Goods /></el-icon>
        <span class="footer-text">分类</span>
      </div>
      <div class="footer-item" @click="navigateTo('/mobile/cart')">
        <el-icon class="footer-icon"><ShoppingCart /></el-icon>
        <span class="footer-text">购物车</span>
      </div>
      <div class="footer-item active" @click="navigateTo('/mobile/profile')">
        <el-icon class="footer-icon"><UserFilled /></el-icon>
        <span class="footer-text">我的</span>
      </div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios'
import {
  House, Goods, ShoppingCart, UserFilled, Setting,
  Wallet, Van, Box, ChatDotRound, RefreshRight,
  Location, Ticket, Star, Clock, Service,
  QuestionFilled, InfoFilled, ArrowRight
} from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'MobileProfile',
  components: {
    House,
    Goods,
    ShoppingCart,
    UserFilled,
    Setting,
    Wallet,
    Van,
    Box,
    ChatDotRound,
    RefreshRight,
    Location,
    Ticket,
    Star,
    Clock,
    Service,
    QuestionFilled,
    InfoFilled,
    ArrowRight
  },
  data() {
    return {
      userName: '',
      userAvatar: '',
      userRole: 0,
      userId: null,
      orderStats: {
        pendingPayment: 0,
        pendingShipment: 0,
        pendingReceive: 0,
        pendingReview: 0,
        refund: 0
      }
    }
  },
  mounted() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const userInfo = localStorage.getItem('userInfo')
      if (userInfo) {
        const user = JSON.parse(userInfo)
        this.userId = user.userId
        this.userName = user.name || user.username || '用户'
        this.userRole = user.role || 0

        if (user.username) {
          this.userAvatar = `https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=avatar%20of%20user%20${encodeURIComponent(user.username)}%2C%20cartoon%20style%2C%20friendly%20smile&image_size=square`
        } else {
          this.userAvatar = 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=friendly%20user%20avatar%2C%20cartoon%20style%2C%20smiling%20face%2C%20portrait&image_size=square'
        }

        if (this.userId) {
          this.loadOrderStats()
        }
      }
    },

    async loadOrderStats() {
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get('/api/orders/user/my-orders', {
          headers: { 'Authorization': 'Bearer ' + token }
        })

        if (response.data.code === 200 && response.data.data) {
          const orders = response.data.data
          this.orderStats.pendingPayment = orders.filter(o => o.status === 0).length
          this.orderStats.pendingShipment = orders.filter(o => o.status === 1).length
          this.orderStats.pendingReceive = orders.filter(o => o.status === 2).length
          this.orderStats.pendingReview = orders.filter(o => o.status === 3).length
          this.orderStats.refund = orders.filter(o => o.status === 4).length
        }
      } catch (error) {
        console.error('获取订单统计失败:', error)
      }
    },

    getRoleText() {
      switch (this.userRole) {
        case 1:
          return '管理员'
        default:
          return '普通会员'
      }
    },

    navigateTo(path) {
      this.$router.push(path)
    },
    goToSettings() {
      this.$router.push('/mobile/about')
    },
    goToOrders() {
      this.$router.push('/orders')
    },
    handleMenuClick(type) {
      const routeMap = {
        'address': '/mobile/address',
        'coupon': '/mobile/coupon',
        'favorite': '/mobile/favorites',
        'history': '/mobile/history',
        'service': '/mobile/service',
        'help': '/mobile/help',
        'about': '/mobile/about'
      }
      const route = routeMap[type]
      if (route) {
        this.$router.push(route)
      } else {
        ElMessage.info('功能开发中')
      }
    },
    handleLogout() {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        const token = localStorage.getItem('token')
        axios.post('/api/auth/logout', {}, {
          headers: { 'Authorization': 'Bearer ' + token }
        }).finally(() => {
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          this.$router.push('/mobile/login')
          ElMessage.success('退出登录成功')
        })
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.mobile-container {
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
  padding-bottom: 60px;
}

.profile-header {
  position: relative;
  padding: 20px 20px 40px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  overflow: hidden;
}

.header-bg {
  position: absolute;
  top: 0;
  left: 0;
  right: 0;
  bottom: 0;
  background: url('data:image/svg+xml,<svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 100 100"><defs><pattern id="grid" width="10" height="10" patternUnits="userSpaceOnUse"><path d="M 10 0 L 0 0 0 10" fill="none" stroke="rgba(255,255,255,0.1)" stroke-width="0.5"/></pattern></defs><rect width="100" height="100" fill="url(%23grid)"/></svg>');
  opacity: 0.3;
}

.user-info {
  position: relative;
  display: flex;
  align-items: center;
  z-index: 1;
}

.user-avatar {
  width: 70px;
  height: 70px;
  border-radius: 50%;
  border: 3px solid white;
  object-fit: cover;
  margin-right: 16px;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
}

.user-details {
  flex: 1;
}

.user-name {
  font-size: 20px;
  font-weight: 600;
  color: white;
  margin: 0 0 6px 0;
}

.user-tag {
  font-size: 12px;
  color: rgba(255, 255, 255, 0.9);
  background: rgba(255, 255, 255, 0.2);
  padding: 2px 10px;
  border-radius: 10px;
  margin: 0;
  display: inline-block;
}

.settings-btn {
  background: rgba(255, 255, 255, 0.2);
  border: none;
  color: white;
}

.profile-content {
  margin-top: -20px;
  padding: 0 16px 16px;
}

.order-section {
  background: white;
  border-radius: 16px;
  padding: 20px;
  margin-bottom: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 16px;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.more-link {
  font-size: 13px;
  color: #999;
  cursor: pointer;
}

.order-stats {
  display: flex;
  justify-content: space-around;
}

.stat-item {
  display: flex;
  flex-direction: column;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s;
  position: relative;
}

.stat-item:hover {
  transform: translateY(-2px);
}

.stat-icon {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: #fff5f5;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-bottom: 8px;
  font-size: 20px;
  color: #ff6b6b;
}

.stat-text {
  font-size: 12px;
  color: #666;
}

.stat-badge {
  position: absolute;
  top: -4px;
  right: -8px;
  background: #ff6b6b;
  color: white;
  font-size: 10px;
  min-width: 16px;
  height: 16px;
  border-radius: 8px;
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 4px;
  font-weight: 600;
}

.menu-section {
  background: white;
  border-radius: 16px;
  margin-bottom: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.menu-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  cursor: pointer;
  transition: background 0.3s;
  border-bottom: 1px solid #f5f5f5;
}

.menu-item:last-child {
  border-bottom: none;
}

.menu-item:hover {
  background: #fafafa;
}

.menu-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.menu-icon {
  font-size: 20px;
  color: #ff6b6b;
}

.menu-text {
  font-size: 14px;
  color: #333;
}

.menu-arrow {
  font-size: 16px;
  color: #ccc;
}

.logout-section {
  margin-top: 24px;
}

.logout-btn {
  width: 100%;
  height: 48px;
  border-radius: 24px;
  background: white;
  border: 1px solid #ddd;
  color: #666;
  font-size: 15px;
  font-weight: 500;
}

.logout-btn:hover {
  border-color: #ff6b6b;
  color: #ff6b6b;
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
