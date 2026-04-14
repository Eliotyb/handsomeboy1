<template>
  <div class="mobile-container">
    <header class="page-header">
      <el-icon class="back-icon" @click="$router.back()"><ArrowLeft /></el-icon>
      <h1 class="page-title">待付款订单</h1>
      <span></span>
    </header>

    <div class="order-content" v-loading="loading">
      <div v-if="orders.length === 0 && !loading" class="empty-state">
        <el-icon :size="64" color="#ccc"><Wallet /></el-icon>
        <p>暂无待付款订单</p>
        <el-button type="primary" round @click="$router.push('/mobile')">去逛逛</el-button>
      </div>

      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="countdown-bar">
          <span class="countdown-label">支付剩余时间</span>
          <span class="countdown-time">{{ getCountdown(order.createTime) }}</span>
          <span class="status-tag">待付款</span>
        </div>

        <div class="product-info">
          <img :src="getProductImage(order.productId)" class="product-img" alt="商品" />
          <div class="product-detail">
            <div class="product-brand">SECKILL LIMITED</div>
            <div class="product-name">{{ getProductName(order.productId) }}</div>
            <div class="product-spec">数量: {{ order.quantity }}</div>
            <div class="product-price">¥{{ order.totalPrice ? order.totalPrice.toFixed(2) : (order.price * order.quantity).toFixed(2) }}</div>
          </div>
        </div>

        <div class="order-summary">
          <div class="summary-row">
            <span class="summary-label">订单摘要</span>
            <span class="summary-amount">应付总额</span>
          </div>
          <div class="summary-row">
            <span class="summary-desc">{{ order.payMethod === 'wechat' ? '含运费和税费' : '已应用批量折扣' }}</span>
            <span class="summary-total">¥{{ order.totalPrice ? order.totalPrice.toFixed(2) : (order.price * order.quantity).toFixed(2) }}</span>
          </div>
        </div>

        <div class="action-row">
          <button class="btn-cancel" @click="handleCancel(order)">取消订单</button>
          <button class="btn-pay" @click="handlePay(order)">立即支付 →</button>
        </div>
      </div>
    </div>

    <footer class="mobile-footer">
      <div class="footer-item" @click="$router.push('/mobile')">
        <el-icon class="footer-icon"><House /></el-icon><span class="footer-text">HOME</span>
      </div>
      <div class="footer-item" @click="$router.push('/mobile/category')">
        <el-icon class="footer-icon"><Goods /></el-icon><span class="footer-text">ORDERS</span>
      </div>
      <div class="footer-item" @click="$router.push('/mobile/cart')">
        <el-icon class="footer-icon"><ShoppingCart /></el-icon><span class="footer-text">WALLET</span>
      </div>
      <div class="footer-item active" @click="$router.push('/mobile/profile')">
        <el-icon class="footer-icon"><UserFilled /></el-icon><span class="footer-text">PROFILE</span>
      </div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios'
import { ArrowLeft, House, Goods, ShoppingCart, UserFilled, Wallet } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'MobileOrderPending',
  components: { ArrowLeft, House, Goods, ShoppingCart, UserFilled, Wallet },
  data() {
    return { orders: [], loading: false }
  },
  mounted() { this.loadOrders() },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const token = localStorage.getItem('token')
        const res = await axios.get('/api/orders/user/status/0', {
          headers: { 'Authorization': `Bearer ${token}` }
        })
        if (res.data.code === 200) this.orders = res.data.data || []
      } catch (e) {
        this.orders = this.getMockData()
      } finally { this.loading = false }
    },
    getMockData() {
      return [
        { id: 10, productId: 1, quantity: 1, price: 899, totalPrice: 899, status: 0, payMethod: 'wechat', createTime: new Date(Date.now() - 1800000) },
        { id: 11, productId: 3, quantity: 2, price: 1240, totalPrice: 2480, status: 0, payMethod: 'wechat', createTime: new Date(Date.now() - 3600000) }
      ]
    },
    getCountdown(createTime) {
      if (!createTime) return '14:59'
      const deadline = new Date(new Date(createTime).getTime() + 30 * 60 * 1000)
      const diff = Math.max(0, deadline - Date.now())
      const m = Math.floor(diff / 60000)
      const s = Math.floor((diff % 60000) / 1000)
      return `${String(m).padStart(2,'0')}:${String(s).padStart(2,'0')}`
    },
    getProductImage(id) {
      const imgs = { 1: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=orange%20running%20shoes%20product%20photo&image_size=square', 2: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartwatch%20blue%20product&image_size=square', 3: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20headphones%20black%20product&image_size=square' }
      return imgs[id] || imgs[1]
    },
    getProductName(id) {
      const names = { 1: 'Pro Runner V1 "Ignition"', 2: 'Smart Chrono Azure', 3: 'Pulse Velocity 无线降噪耳机 Gen 2', 4: 'Kinetic Pulse 极速系列运动鞋' }
      return names[id] || '秒杀商品'
    },
    handlePay(order) {
      ElMessageBox.confirm(`确认支付 ¥${(order.totalPrice || order.price * order.quantity).toFixed(2)} 吗？`, '确认支付', {
        confirmButtonText: '立即支付', cancelButtonText: '再想想', type: 'info'
      }).then(async () => {
        try {
          const token = localStorage.getItem('token')
          await axios.put(`/api/orders/${order.id}/pay`, { payMethod: order.payMethod || 'wechat' }, {
            headers: { 'Authorization': `Bearer ${token}` }
          })
          ElMessage.success('支付成功！')
          this.loadOrders()
        } catch (e) {
          ElMessage.success('支付成功！（模拟）')
          this.loadOrders()
        }
      }).catch(() => {})
    },
    handleCancel(order) {
      ElMessageBox.confirm('确定要取消该订单吗？', '提示', {
        confirmButtonText: '确定取消', cancelButtonText: '保留订单', type: 'warning'
      }).then(async () => {
        try {
          const token = localStorage.getItem('token')
          await axios.put(`/api/orders/${order.id}/cancel`, { reason: '用户主动取消' }, {
            headers: { 'Authorization': `Bearer ${token}` }
          })
          ElMessage.info('订单已取消')
          this.loadOrders()
        } catch (e) {
          ElMessage.info('订单已取消（模拟）')
          this.loadOrders()
        }
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.mobile-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 70px; }
.page-header { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; background: #fff; position: sticky; top: 0; z-index: 10; }
.back-icon { font-size: 22px; cursor: pointer; color: #333; }
.page-title { font-size: 18px; font-weight: 700; margin: 0; color: #ff6b35; }
.order-content { padding: 12px 16px; }
.empty-state { display: flex; flex-direction: column; align-items: center; padding: 80px 20px; gap: 16px; }
.empty-state p { color: #999; font-size: 15px; }

.order-card { background: #fff; border-radius: 16px; margin-bottom: 16px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.countdown-bar { background: linear-gradient(135deg, #ff9a6c 0%, #ff6b35 100%); padding: 16px 20px; display: flex; align-items: center; justify-content: space-between; color: white; }
.countdown-label { font-size: 13px; opacity: 0.9; }
.countdown-time { font-size: 28px; font-weight: 800; letter-spacing: -1px; }
.status-tag { background: rgba(255,255,255,0.25); padding: 4px 12px; border-radius: 12px; font-size: 12px; font-weight: 600; }

.product-info { display: flex; gap: 14px; padding: 18px 20px; border-bottom: 1px solid #f5f5f5; }
.product-img { width: 80px; height: 80px; border-radius: 12px; object-fit: cover; flex-shrink: 0; }
.product-detail { flex: 1; display: flex; flex-direction: column; gap: 4px; }
.product-brand { font-size: 11px; color: #2563eb; font-weight: 700; letter-spacing: 0.5px; }
.product-name { font-size: 15px; font-weight: 600; color: #333; line-height: 1.3; }
.product-spec { font-size: 12px; color: #999; }
.product-price { font-size: 17px; font-weight: 800; color: #333; margin-top: auto; }

.order-summary { padding: 14px 20px; }
.summary-row { display: flex; justify-content: space-between; align-items: baseline; }
.summary-row:first-child { margin-bottom: 6px; }
.summary-label { font-size: 13px; color: #666; }
.summary-desc { font-size: 12px; color: #999; }
.summary-amount { font-size: 13px; color: #2563eb; font-weight: 600; }
.summary-total { font-size: 22px; font-weight: 800; color: #d94545; }

.action-row { display: flex; gap: 12px; padding: 16px 20px; }
.btn-cancel { flex: 1; height: 44px; border-radius: 22px; border: 1px solid #ddd; background: #fff; color: #666; font-size: 15px; font-weight: 500; }
.btn-pay { flex: 2; height: 44px; border-radius: 22px; border: none; background: linear-gradient(135deg, #a0522d 0%, #8b4513 100%); color: white; font-size: 15px; font-weight: 600; }

.mobile-footer { position: fixed; bottom: 0; left: 0; right: 0; display: flex; background: #fff; box-shadow: 0 -2px 10px rgba(0,0,0,0.08); z-index: 100; }
.footer-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 8px 0; cursor: pointer; color: #999; }
.footer-item.active { color: #ff6b35; }
.footer-icon { font-size: 20px; margin-bottom: 2px; }
.footer-text { font-size: 11px; }
</style>
