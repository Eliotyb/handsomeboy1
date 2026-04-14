<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">我的优惠券</h1>
      <div class="header-right"></div>
    </header>

    <div class="coupon-tabs">
      <div class="tab-item" :class="{ active: currentTab === 'available' }" @click="currentTab = 'available'">
        可使用
      </div>
      <div class="tab-item" :class="{ active: currentTab === 'used' }" @click="currentTab = 'used'">
        已使用
      </div>
      <div class="tab-item" :class="{ active: currentTab === 'expired' }" @click="currentTab = 'expired'">
        已过期
      </div>
    </div>

    <div class="coupon-content">
      <div v-if="filteredCoupons.length === 0" class="empty-state">
        <el-icon class="empty-icon"><Ticket /></el-icon>
        <p>{{ emptyText }}</p>
      </div>

      <div v-else class="coupon-list">
        <div class="coupon-item" v-for="(coupon, index) in filteredCoupons" :key="index"
             :class="{ 'coupon-disabled': coupon.status !== 'available' }">
          <div class="coupon-left">
            <div class="coupon-value">
              <span class="currency">¥</span>
              <span class="amount">{{ coupon.value }}</span>
            </div>
            <div class="coupon-condition">
              满{{ coupon.minAmount }}元可用
            </div>
          </div>
          <div class="coupon-divider"></div>
          <div class="coupon-right">
            <h3 class="coupon-title">{{ coupon.title }}</h3>
            <p class="coupon-time">{{ coupon.startTime }} - {{ coupon.endTime }}</p>
            <el-button v-if="coupon.status === 'available'" type="danger" size="small"
                       @click="useCoupon(coupon)">立即使用</el-button>
            <el-tag v-else-if="coupon.status === 'used'" type="info" size="small">已使用</el-tag>
            <el-tag v-else type="info" size="small">已过期</el-tag>
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
import { ArrowLeft, Ticket, House, Goods, ShoppingCart, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import axios from 'axios'

export default {
  name: 'MobileCoupon',
  components: {
    ArrowLeft,
    Ticket,
    House,
    Goods,
    ShoppingCart,
    UserFilled
  },
  data() {
    return {
      currentTab: 'available',
      couponList: [],
      loading: false
    }
  },
  created() {
    this.loadCoupons()
  },
  computed: {
    filteredCoupons() {
      const now = new Date()
      return this.couponList.filter(c => {
        const startTime = new Date(c.startTime)
        const endTime = new Date(c.endTime)
        let status = c.status

        if (status === 0) {
          return this.currentTab === 'available'
        } else if (status === 1) {
          return this.currentTab === 'used'
        }

        if (endTime < now || (c.totalCount && c.usedCount >= c.totalCount)) {
          return this.currentTab === 'expired'
        }
        
        return this.currentTab === 'available'
      })
    },
    emptyText() {
      const texts = {
        available: '暂无可用优惠券',
        used: '暂无已使用的优惠券',
        expired: '暂无已过期的优惠券'
      }
      return texts[this.currentTab]
    }
  },
  methods: {
    async loadCoupons() {
      this.loading = true
      try {
        const token = localStorage.getItem('token')
        const response = await axios.get('/api/coupons/user', {
          headers: { 'Authorization': `Bearer ${token}` }
        })

        if (response.data.code === 200 && response.data.data) {
          this.couponList = response.data.data.map(coupon => ({
            id: coupon.id,
            title: coupon.title,
            value: parseFloat(coupon.value),
            minAmount: parseFloat(coupon.minAmount),
            startTime: coupon.startTime ? coupon.startTime.substring(0, 10) : '',
            endTime: coupon.endTime ? coupon.endTime.substring(0, 10) : '',
            status: coupon.status === 0 ? 'available' : 
                   coupon.status === 1 ? 'used' : 'expired',
            totalCount: coupon.totalCount,
            usedCount: coupon.usedCount
          }))
        }
      } catch (error) {
        console.error('获取优惠券失败:', error)
        ElMessage.error('获取优惠券失败')
      } finally {
        this.loading = false
      }
    },
    useCoupon(coupon) {
      ElMessage.success(`已选择优惠券：${coupon.title}`)
      localStorage.setItem('selectedCoupon', JSON.stringify(coupon))
      this.$router.push('/mobile/cart')
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

.coupon-tabs {
  display: flex;
  background: white;
  padding: 12px 16px;
  gap: 12px;
  margin-bottom: 12px;
}

.tab-item {
  flex: 1;
  text-align: center;
  padding: 8px 0;
  border-radius: 20px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  background: #f5f5f5;
}

.tab-item.active {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: white;
  font-weight: 500;
}

.coupon-content {
  padding: 0 16px;
}

.empty-state {
  background: white;
  border-radius: 12px;
  padding: 80px 20px;
  text-align: center;
}

.empty-icon {
  font-size: 64px;
  color: #ddd;
  margin-bottom: 16px;
}

.empty-state p {
  color: #999;
  margin: 0;
  font-size: 15px;
}

.coupon-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.coupon-item {
  background: white;
  border-radius: 12px;
  display: flex;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.coupon-disabled {
  opacity: 0.6;
}

.coupon-left {
  width: 120px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  color: white;
  padding: 20px 16px;
  text-align: center;
  display: flex;
  flex-direction: column;
  justify-content: center;
}

.coupon-value {
  display: flex;
  align-items: baseline;
  justify-content: center;
  margin-bottom: 4px;
}

.currency {
  font-size: 16px;
  font-weight: 600;
}

.amount {
  font-size: 36px;
  font-weight: 700;
}

.coupon-condition {
  font-size: 11px;
  opacity: 0.9;
}

.coupon-divider {
  width: 2px;
  background: repeating-linear-gradient(to bottom, #ff6b6b 0, #ff6b6b 8px, transparent 8px, transparent 16px);
}

.coupon-right {
  flex: 1;
  padding: 16px;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.coupon-title {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0 0 8px;
}

.coupon-time {
  font-size: 11px;
  color: #999;
  margin: 0 0 8px;
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
