<template>
  <div class="seckill-success">
    <header class="success-header">
      <div class="header-left" @click="goBack">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1>抢购成功</h1>
      <div class="header-right"></div>
    </header>

    <div class="success-content">
      <div class="success-icon-container">
        <div class="success-circle">
          <el-icon class="check-icon"><CircleCheckFilled /></el-icon>
        </div>
        <h2 class="success-title">抢购成功！</h2>
        <p v-if="orderNo" class="order-number">订单号: {{ orderNo }}</p>
        <p v-else class="order-number">正在生成订单...</p>
      </div>

      <div class="event-card">
        <div class="event-image">
          <img :src="productInfo.image || defaultImage" :alt="productInfo.name" />
        </div>
        <div class="event-info">
          <div class="event-tag">秒杀商品</div>
          <h3 class="event-name">{{ productInfo.name }}</h3>
          <div class="price-row">
            <span class="seckill-price">¥{{ productInfo.seckillPrice }}</span>
            <span class="original-price">¥{{ productInfo.originalPrice }}</span>
          </div>
          <div class="info-item">
            <el-icon class="info-icon"><Clock /></el-icon>
            <span class="info-text">{{ productInfo.time || '限时秒杀活动' }}</span>
          </div>
        </div>
      </div>

      <div class="ticket-status">
        <div class="status-item">
          <span class="status-label">ORDER STATUS</span>
          <div class="status-badge success">
            <el-icon class="status-icon"><CircleCheckFilled /></el-icon>
            <span class="status-text">订单已确认</span>
          </div>
        </div>
      </div>

      <div class="action-buttons">
        <el-button class="primary-btn" type="primary" :loading="ordering" @click="confirmOrder">
          <el-icon><Ticket /></el-icon>
          {{ ordering ? '下单中...' : '确认下单' }}
        </el-button>
        <el-button class="secondary-btn" @click="saveToWallet">
          <el-icon><Wallet /></el-icon>
          保存到钱包
        </el-button>
      </div>

      <div class="speed-info" v-if="!ordered">
        <div class="speed-card">
          <el-icon class="speed-icon"><Star /></el-icon>
          <div class="speed-content">
            <p class="speed-text">你已超过 98% 的抢购者</p>
            <p class="speed-desc">本次活动名额有限，请尽快完成支付确认订单</p>
          </div>
        </div>
      </div>

      <div class="order-confirm" v-if="ordered">
        <div class="confirm-card">
          <el-icon class="confirm-icon"><SuccessFilled /></el-icon>
          <div class="confirm-content">
            <p class="confirm-text">订单已成功创建并保存到数据库</p>
            <p class="confirm-no">订单号: {{ orderNo }}</p>
          </div>
        </div>
      </div>
    </div>

    <footer class="success-footer">
      <div class="footer-item" @click="$router.push('/mobile')">
        <el-icon><House /></el-icon>
        <span>首页</span>
      </div>
      <div class="footer-item" @click="$router.push('/mobile/category')">
        <el-icon><Goods /></el-icon>
        <span>分类</span>
      </div>
      <div class="footer-item active">
        <el-icon><ShoppingCart /></el-icon>
        <span>购物车</span>
      </div>
      <div class="footer-item" @click="$router.push('/mobile/profile')">
        <el-icon><User /></el-icon>
        <span>我的</span>
      </div>
    </footer>
  </div>
</template>

<script>
import { ArrowLeft, CircleCheckFilled, Clock, Ticket, Star, Wallet, House, Goods, ShoppingCart, User, SuccessFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'SeckillSuccess',
  components: { ArrowLeft, CircleCheckFilled, Clock, Ticket, Star, Wallet, House, Goods, ShoppingCart, User, SuccessFilled },
  data() {
    return {
      defaultImage: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=seckill%20product%20flash%20sale%2C%20shopping%20bag%2C%20discount&image_size=square',
      productInfo: {},
      orderNo: '',
      ordering: false,
      ordered: false
    }
  },
  created() {
    const query = this.$route.query
    if (query.productId) {
      this.productInfo = {
        id: query.productId,
        name: query.name || '秒杀商品',
        image: query.image,
        seckillPrice: query.price || '99.00',
        originalPrice: query.originalPrice || '299.00',
        time: query.time
      }
    } else {
      this.productInfo = {
        id: 1,
        name: '午夜低音革命',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=concert%20stage%20with%20purple%20and%20blue%20lights%2C%20crowd%20cheering%2C%20live%20music%20event&image_size=portrait_16_9',
        seckillPrice: '99.00',
        originalPrice: '299.00',
        time: '2026年10月31日 21:00-02:00'
      }
    }
  },
  methods: {
    goBack() {
      this.$router.back()
    },
    async confirmOrder() {
      if (this.ordered) {
        ElMessage.success('订单已创建，无需重复操作')
        return
      }

      const token = localStorage.getItem('token')
      if (!token) {
        ElMessage.warning('请先登录后再下单')
        this.$router.push('/mobile/login')
        return
      }

      this.ordering = true
      try {
        const response = await axios.post('/api/orders/seckill', {
          productId: this.productInfo.id,
          price: parseFloat(this.productInfo.seckillPrice),
          quantity: 1
        }, {
          headers: { 'Authorization': `Bearer ${token}` }
        })

        if (response.data.code === 200) {
          this.orderNo = response.data.data.orderNo
          this.ordered = true
          ElMessage.success('订单创建成功！')
        } else {
          if (response.data.message && response.data.message.includes('登录')) {
            ElMessage.error('登录已过期，请重新登录')
            localStorage.removeItem('token')
            localStorage.removeItem('userInfo')
            setTimeout(() => {
              this.$router.push('/mobile/login')
            }, 1500)
          } else {
            ElMessage.error(response.data.message || '下单失败')
          }
        }
      } catch (error) {
        console.error('下单失败:', error)
        if (error.response && error.response.status === 401 || error.response?.data?.message?.includes('登录')) {
          ElMessage.error('登录已过期，请重新登录')
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          setTimeout(() => {
            this.$router.push('/mobile/login')
          }, 1500)
        } else {
          ElMessage.error('下单失败，请检查网络连接或稍后重试')
        }
      } finally {
        this.ordering = false
      }
    },
    async saveToWallet() {
      if (!this.ordered) {
        await this.confirmOrder()
      }
      if (this.ordered) {
        ElMessage.success('已保存到钱包')
      }
    }
  }
}
</script>

<style scoped>
.seckill-success {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 70px;
}

.success-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  background: #fff;
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

.header-right { width: 32px; }

.success-header h1 {
  font-size: 17px;
  font-weight: 600;
  margin: 0;
}

.success-content { padding: 16px; }

.success-icon-container {
  text-align: center;
  padding: 28px 0 20px;
  background: #fff;
  border-radius: 12px;
  margin-bottom: 12px;
}

.success-circle {
  width: 68px;
  height: 68px;
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 12px;
  box-shadow: 0 4px 14px rgba(82, 196, 26, 0.35);
}

.check-icon { font-size: 34px; color: #fff; }

.success-title {
  font-size: 21px;
  font-weight: 700;
  color: #333;
  margin: 0 0 6px;
}

.order-number {
  font-size: 13px;
  color: #999;
  margin: 0;
}

.event-card {
  background: #fff;
  padding: 16px;
  border-radius: 12px;
  margin-bottom: 12px;
}

.event-image {
  height: 120px;
  border-radius: 8px;
  overflow: hidden;
  margin-bottom: 12px;
}

.event-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.event-tag {
  display: inline-block;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: #fff;
  padding: 3px 10px;
  border-radius: 10px;
  font-size: 11px;
  font-weight: 500;
  margin-bottom: 8px;
}

.event-name {
  font-size: 17px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px;
}

.price-row {
  display: flex;
  align-items: baseline;
  gap: 8px;
  margin-bottom: 10px;
}

.seckill-price {
  font-size: 22px;
  font-weight: 700;
  color: #ee5a6f;
}

.original-price {
  font-size: 13px;
  color: #999;
  text-decoration: line-through;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 8px;
}

.info-icon { font-size: 15px; color: #ff6b6b; }

.info-text { font-size: 13px; color: #666; }

.ticket-status {
  background: #fff;
  padding: 14px 16px;
  border-radius: 12px;
  margin-bottom: 12px;
}

.status-item {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.status-label { font-size: 11px; color: #bbb; text-transform: uppercase; }

.status-badge {
  display: flex;
  align-items: center;
  gap: 5px;
  background: #f6ffed;
  padding: 5px 10px;
  border-radius: 12px;
}

.status-badge .status-icon { font-size: 13px; color: #52c41a; }

.status-badge .status-text { font-size: 13px; color: #52c41a; font-weight: 500; }

.action-buttons {
  display: flex;
  flex-direction: column;
  gap: 10px;
  margin-bottom: 12px;
}

.primary-btn, .secondary-btn {
  height: 46px;
  border-radius: 23px;
  font-size: 15px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
}

.primary-btn {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border: none;
}

.secondary-btn {
  background: #fff;
  border: 1px solid #ddd;
  color: #666;
}

.speed-info { margin-top: 4px; }

.speed-card {
  background: linear-gradient(135deg, #fff7e6 0%, #ffe7ba 100%);
  padding: 14px;
  border-radius: 12px;
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.speed-icon { font-size: 22px; color: #fa8c16; flex-shrink: 0; }

.speed-text { font-size: 13px; font-weight: 600; color: #d48806; margin: 0 0 3px; }

.speed-desc { font-size: 11px; color: #ad6800; margin: 0; line-height: 1.3; }

.order-confirm { margin-top: 4px; }

.confirm-card {
  background: linear-gradient(135deg, #f6ffed 0%, #d9f7be 100%);
  padding: 14px;
  border-radius: 12px;
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.confirm-icon { font-size: 22px; color: #52c41a; flex-shrink: 0; }

.confirm-text { font-size: 13px; font-weight: 600; color: #389e0d; margin: 0 0 3px; }

.confirm-no { font-size: 12px; color: #52c41a; margin: 0; }

.success-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  background: #fff;
  box-shadow: 0 -2px 10px rgba(0,0,0,0.06);
  z-index: 100;
}

.footer-item {
  flex: 1;
  display: flex;
  flex-direction: column;
  align-items: center;
  padding: 8px 0;
  cursor: pointer;
  font-size: 11px;
  color: #999;
}

.footer-item.active { color: #ff6b6b; }

.footer-item .el-icon { font-size: 20px; margin-bottom: 2px; }
</style>
