<template>
  <div class="pay-success">
    <header class="pay-header">
      <div class="header-left" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
      </div>
      <h1>支付结果</h1>
      <div class="header-right"></div>
    </header>

    <div class="success-content">
      <div class="success-icon-wrapper">
        <div class="success-circle">
          <el-icon class="check-icon"><CircleCheckFilled /></el-icon>
        </div>
        <h2 class="success-text">支付成功</h2>
        <p class="order-no">订单号: {{ orderNo }}</p>
      </div>

      <div class="order-summary">
        <h3>订单详情</h3>
        <div v-for="item in orderItems" :key="item.id" class="order-item">
          <img :src="item.productImage" :alt="item.productName" class="item-img" />
          <div class="item-info">
            <p class="item-name">{{ item.productName }}</p>
            <p class="item-price">¥{{ item.productPrice?.toFixed(2) }} × {{ item.quantity }}</p>
          </div>
          <span class="item-total">¥{{ (item.productPrice * item.quantity).toFixed(2) }}</span>
        </div>
      </div>

      <div class="amount-card">
        <div class="amount-row">
          <span>商品数量</span>
          <span>{{ totalCount }} 件</span>
        </div>
        <div class="amount-row total">
          <span>实付金额</span>
          <span class="total-price">¥{{ totalAmount.toFixed(2) }}</span>
        </div>
      </div>

      <div class="action-buttons">
        <el-button type="primary" class="btn-primary" @click="goHome">继续购物</el-button>
        <el-button class="btn-secondary" @click="viewOrders">查看订单</el-button>
      </div>
    </div>

    <footer class="pay-footer">
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
import { ArrowLeft, CircleCheckFilled, House, Goods, ShoppingCart, User } from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'MobilePaySuccess',
  components: { ArrowLeft, CircleCheckFilled, House, Goods, ShoppingCart, User },
  data() {
    return {
      orderNo: '',
      orderItems: [],
      totalAmount: 0,
      totalCount: 0
    }
  },
  created() {
    const orderData = this.$route.query.orderData
    if (orderData) {
      try {
        const data = JSON.parse(orderData)
        this.orderNo = data.orderNo || ''
        this.orderItems = data.items || []
        this.totalAmount = data.totalAmount || 0
        this.totalCount = data.totalCount || 0
      } catch (e) {
        this.orderNo = 'ORD-' + Date.now()
        this.orderItems = []
      }
    }
  },
  methods: {
    goBack() {
      this.$router.push('/mobile')
    },
    goHome() {
      this.$router.push('/mobile')
    },
    viewOrders() {
      this.$router.push('/mobile/profile')
    }
  }
}
</script>

<style scoped>
.pay-success {
  min-height: 100vh;
  background: #f5f6fa;
  padding-bottom: 70px;
}

.pay-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 14px 16px;
  background: #fff;
  position: sticky;
  top: 0;
  z-index: 100;
}

.header-left, .header-right {
  width: 32px;
  font-size: 22px;
  color: #333;
  cursor: pointer;
}

.pay-header h1 {
  font-size: 17px;
  font-weight: 600;
  margin: 0;
}

.success-content {
  padding: 20px 16px;
}

.success-icon-wrapper {
  text-align: center;
  padding: 30px 0 24px;
}

.success-circle {
  width: 72px;
  height: 72px;
  background: linear-gradient(135deg, #52c41a 0%, #389e0d 100%);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin: 0 auto 14px;
  box-shadow: 0 4px 16px rgba(82, 196, 26, 0.35);
}

.check-icon {
  font-size: 36px;
  color: #fff;
}

.success-text {
  font-size: 22px;
  font-weight: 700;
  color: #333;
  margin: 0 0 8px;
}

.order-no {
  font-size: 13px;
  color: #999;
  margin: 0;
}

.order-summary {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 12px;
}

.order-summary h3 {
  font-size: 15px;
  font-weight: 600;
  color: #333;
  margin: 0 0 12px;
  padding-bottom: 10px;
  border-bottom: 1px solid #f0f0f0;
}

.order-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 10px 0;
  border-bottom: 1px solid #fafafa;
}

.order-item:last-child {
  border-bottom: none;
}

.item-img {
  width: 56px;
  height: 56px;
  border-radius: 8px;
  object-fit: cover;
  flex-shrink: 0;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-name {
  font-size: 13px;
  color: #333;
  margin: 0 0 4px;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
}

.item-price {
  font-size: 12px;
  color: #999;
  margin: 0;
}

.item-total {
  font-size: 14px;
  font-weight: 600;
  color: #ee5a6f;
  flex-shrink: 0;
}

.amount-card {
  background: #fff;
  border-radius: 12px;
  padding: 16px;
  margin-bottom: 20px;
}

.amount-row {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 8px 0;
  font-size: 14px;
  color: #666;
}

.amount-row.total {
  border-top: 1px solid #f0f0f0;
  margin-top: 4px;
  padding-top: 12px;
  font-size: 15px;
}

.total-price {
  font-size: 20px;
  font-weight: 700;
  color: #ee5a6f;
}

.action-buttons {
  display: flex;
  gap: 12px;
}

.btn-primary, .btn-secondary {
  flex: 1;
  height: 44px;
  border-radius: 22px;
  font-size: 15px;
  font-weight: 600;
}

.btn-primary {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border: none;
}

.btn-secondary {
  background: #fff;
  border: 1px solid #ddd;
  color: #666;
}

.pay-footer {
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

.footer-item.active {
  color: #ff6b6b;
}

.footer-item .el-icon {
  font-size: 20px;
  margin-bottom: 2px;
}
</style>
