<template>
  <div class="mobile-container">
    <!-- 顶部导航 -->
    <header class="mobile-header">
      <div class="header-left">
        <span class="location">📍 北京</span>
      </div>
      <div class="header-center">
        <h1 class="app-title">限时秒杀</h1>
      </div>
      <div class="header-right">
        <el-icon class="user-icon"><Avatar /></el-icon>
      </div>
    </header>

    <!-- 倒计时 -->
    <div class="countdown-section">
      <div class="countdown-bg">
        <div class="countdown-info">
          <span class="countdown-label">距今日结束</span>
          <div class="countdown-timer">
            <div class="time-box">
              <span class="time-number">{{ hours }}</span>
            </div>
            <span class="time-separator">:</span>
            <div class="time-box">
              <span class="time-number">{{ minutes }}</span>
            </div>
            <span class="time-separator">:</span>
            <div class="time-box">
              <span class="time-number">{{ seconds }}</span>
            </div>
          </div>
        </div>
      </div>
    </div>

    <!-- 活动标签 -->
    <div class="activity-tabs">
      <div class="tab-item active">全场1折</div>
      <div class="tab-item">艺术</div>
      <div class="tab-item">体育</div>
      <div class="tab-item">学术</div>
    </div>

    <!-- 今日热门 -->
    <div class="section-title">
      <h2>今日热门</h2>
      <span class="more-btn">查看全部</span>
    </div>

    <!-- 商品列表 -->
    <div class="product-list">
      <div class="product-item" v-for="(product, index) in products" :key="index" @click="goToDetail">
        <div class="product-image">
          <img :src="product.image" :alt="product.name" />
          <div class="product-tag" v-if="product.tag">{{ product.tag }}</div>
        </div>
        <div class="product-info">
          <h3 class="product-name">{{ product.name }}</h3>
          <p class="product-desc">{{ product.description }}</p>
          <div class="product-price">
            <span class="price-current">{{ product.price }}</span>
            <span class="price-original">{{ product.originalPrice }}</span>
          </div>
          <div class="product-bottom">
            <span class="stock-info">已售 {{ product.sold }}%</span>
            <el-button class="buy-button" type="primary" size="small" :disabled="product.disabled" @click.stop="handleBuy(product)">
              {{ product.disabled ? '已抢光' : '立即抢购' }}
            </el-button>
          </div>
        </div>
      </div>
    </div>

    <!-- 即将开始 -->
    <div class="section-title">
      <h2>即将开始</h2>
      <span class="more-btn">查看全部</span>
    </div>

    <div class="coming-soon">
      <div class="coming-item">
        <div class="coming-time">
          <span class="time-label">12:00</span>
          <span class="time-status">即将开始</span>
        </div>
        <div class="coming-products">
          <div class="coming-product" v-for="(item, index) in comingProducts" :key="index">
            <div class="coming-image">
              <img :src="item.image" :alt="item.name" />
            </div>
            <div class="coming-info">
              <span class="coming-price">{{ item.price }}</span>
              <span class="coming-name">{{ item.name }}</span>
            </div>
          </div>
        </div>
        <el-button class="notify-button" size="small">提醒我</el-button>
      </div>
    </div>

    <!-- 底部导航 -->
    <footer class="mobile-footer">
      <div class="footer-item active" @click="navigateTo('/mobile')">
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
      <div class="footer-item" @click="navigateTo('/mobile/profile')">
        <el-icon class="footer-icon"><UserFilled /></el-icon>
        <span class="footer-text">我的</span>
      </div>
    </footer>
  </div>
</template>

<script>
import { House, Goods, ShoppingCart, UserFilled, Avatar } from '@element-plus/icons-vue'

export default {
  name: 'MobileHome',
  components: {
    House,
    Goods,
    ShoppingCart,
    UserFilled,
    Avatar
  },
  data() {
    return {
      hours: '02',
      minutes: '45',
      seconds: '12',
      products: [
        {
          id: 1,
          name: '双人吉他合奏课程',
          description: '人气爆款 地下拍客第4辑',
          price: '¥44.99',
          originalPrice: '¥299.00',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=two%20people%20playing%20guitars%20on%20stage%2C%20concert%20scene%2C%20warm%20lighting%2C%20musical%20ambiance&image_size=landscape_16_9',
          sold: 75,
          tag: '人气爆款',
          disabled: false
        },
        {
          id: 2,
          name: '荧光夜跑装备套装',
          description: '荧光5K跑',
          price: '¥0.00',
          originalPrice: '¥199.00',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=runner%20wearing%20neon%20sports%20gear%2C%20night%20running%2C%20fluorescent%20clothing%2C%20athletic%20pose&image_size=landscape_16_9',
          sold: 95,
          tag: '免费',
          disabled: true
        },
        {
          id: 3,
          name: 'AI与伦理全球峰会',
          description: '探讨AI发展与未来社会影响',
          price: '¥12.00',
          originalPrice: '¥299.00',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=tech%20conference%20stage%20with%20AI%20technology%20theme%2C%20futuristic%20design%2C%20professional%20setting&image_size=landscape_16_9',
          sold: 30,
          tag: '限量',
          disabled: false
        }
      ],
      comingProducts: [
        {
          id: 4,
          name: '智能手环',
          price: '¥99',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smart%20fitness%20band%20on%20wrist%2C%20modern%20design%2C%20blue%20screen&image_size=square'
        },
        {
          id: 5,
          name: '无线耳机',
          price: '¥199',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20earbuds%20case%2C%20premium%20design%2C%20white%20color&image_size=square'
        },
        {
          id: 6,
          name: '蓝牙音箱',
          price: '¥299',
          image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=portable%20bluetooth%20speaker%2C%20compact%20design%2C%20black%20color&image_size=square'
        }
      ]
    }
  },
  mounted() {
    this.startCountdown()
  },
  methods: {
    startCountdown() {
      setInterval(() => {
        let totalSeconds = parseInt(this.hours) * 3600 + parseInt(this.minutes) * 60 + parseInt(this.seconds)
        if (totalSeconds > 0) {
          totalSeconds--
          this.hours = Math.floor(totalSeconds / 3600).toString().padStart(2, '0')
          this.minutes = Math.floor((totalSeconds % 3600) / 60).toString().padStart(2, '0')
          this.seconds = (totalSeconds % 60).toString().padStart(2, '0')
        }
      }, 1000)
    },
    navigateTo(path) {
      this.$router.push(path)
    },
    goToDetail() {
      this.$router.push('/seckill-detail')
    },
    handleBuy(product) {
      if (!product.disabled) {
        this.$router.push('/seckill-detail')
      }
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

/* 顶部导航 */
.mobile-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.location {
  font-size: 14px;
  color: #333;
}

.app-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.user-icon {
  font-size: 24px;
  color: #666;
}

/* 倒计时 */
.countdown-section {
  margin: 16px 0;
}

.countdown-bg {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  padding: 20px;
  border-radius: 12px;
  margin: 0 20px;
  color: white;
}

.countdown-info {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.countdown-label {
  font-size: 14px;
  font-weight: 500;
}

.countdown-timer {
  display: flex;
  align-items: center;
  gap: 4px;
}

.time-box {
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 8px;
  border-radius: 6px;
  min-width: 36px;
  text-align: center;
}

.time-number {
  font-size: 18px;
  font-weight: 700;
  color: white;
}

.time-separator {
  font-size: 18px;
  font-weight: 700;
  color: white;
  margin: 0 2px;
}

/* 活动标签 */
.activity-tabs {
  display: flex;
  overflow-x: auto;
  padding: 16px 20px;
  gap: 16px;
  background: white;
  margin-bottom: 16px;
}

.activity-tabs::-webkit-scrollbar {
  display: none;
}

.tab-item {
  padding: 8px 16px;
  background: #f0f0f0;
  border-radius: 20px;
  font-size: 14px;
  white-space: nowrap;
  cursor: pointer;
  transition: all 0.3s;
}

.tab-item.active {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: white;
  font-weight: 500;
}

/* 标题 */
.section-title {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 20px 12px;
  margin-top: 24px;
}

.section-title h2 {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.more-btn {
  font-size: 14px;
  color: #999;
  cursor: pointer;
}

/* 商品列表 */
.product-list {
  padding: 0 20px;
  display: flex;
  flex-direction: column;
  gap: 16px;
}

.product-item {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
}

.product-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 16px rgba(0, 0, 0, 0.12);
}

.product-image {
  position: relative;
  height: 180px;
  overflow: hidden;
}

.product-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.product-tag {
  position: absolute;
  top: 12px;
  left: 12px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: white;
  padding: 4px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.product-info {
  padding: 16px;
}

.product-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px 0;
  line-height: 1.4;
}

.product-desc {
  font-size: 14px;
  color: #666;
  margin: 0 0 12px 0;
  line-height: 1.3;
}

.product-price {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 12px;
}

.price-current {
  font-size: 20px;
  font-weight: 700;
  color: #ff6b6b;
}

.price-original {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.product-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stock-info {
  font-size: 12px;
  color: #999;
}

.buy-button {
  border-radius: 20px;
  padding: 6px 20px;
  font-size: 14px;
  font-weight: 500;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border: none;
}

/* 即将开始 */
.coming-soon {
  padding: 0 20px;
}

.coming-item {
  background: white;
  border-radius: 12px;
  padding: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.coming-time {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 16px;
}

.time-label {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.time-status {
  font-size: 14px;
  color: #ff6b6b;
  background: #fff5f5;
  padding: 2px 8px;
  border-radius: 10px;
}

.coming-products {
  display: flex;
  gap: 12px;
  margin-bottom: 16px;
}

.coming-product {
  flex: 1;
  text-align: center;
}

.coming-image {
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 8px;
}

.coming-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.coming-price {
  font-size: 14px;
  font-weight: 600;
  color: #ff6b6b;
  display: block;
  margin-bottom: 4px;
}

.coming-name {
  font-size: 12px;
  color: #666;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.notify-button {
  width: 100%;
  border-radius: 20px;
  padding: 8px;
  font-size: 14px;
  border: 1px solid #ff6b6b;
  color: #ff6b6b;
  background: white;
}

/* 底部导航 */
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

/* 响应式设计 */
@media (max-width: 768px) {
  .mobile-container {
    padding-bottom: 60px;
  }
  
  .countdown-bg {
    margin: 0 16px;
    padding: 16px;
  }
  
  .activity-tabs {
    padding: 12px 16px;
  }
  
  .section-title {
    padding: 0 16px 12px;
  }
  
  .product-list {
    padding: 0 16px;
  }
  
  .coming-soon {
    padding: 0 16px;
  }
}
</style>