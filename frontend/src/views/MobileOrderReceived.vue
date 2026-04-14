<template>
  <div class="mobile-container">
    <header class="page-header">
      <el-icon class="back-icon" @click="$router.back()"><ArrowLeft /></el-icon>
      <h1 class="page-title">待收货</h1>
      <el-icon class="header-icon"><Bell /><el-icon><Van /></el-icon></el-icon>
    </header>

    <div class="order-content" v-loading="loading">
      <div v-if="orders.length === 0 && !loading" class="empty-state">
        <el-icon :size="64" color="#ccc"><Box /></el-icon>
        <p>暂无待收货订单</p>
      </div>

      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="shipping-banner" :class="{ delivered: isDelivered(order) }">
          <div class="banner-left">
            <el-icon class="banner-icon"><van v-if="!isDelivered(order)" /><CircleCheckFilled v-else /></el-icon>
            <div class="banner-text">
              <div class="banner-title">{{ isDelivered(order) ? '包裹已到达' : '物流动态' }}</div>
              <div class="banner-subtitle">{{ getShippingStatus(order) }}</div>
            </div>
          </div>
          <span class="banner-time">{{ formatTime(order.shipTime || order.createTime) }}</span>
        </div>

        <div v-if="isDelivered(order)" class="delivery-tip">
          <el-icon><Location /></el-icon>
          <span>即将进入派送环节，请保持电话畅通。</span>
        </div>

        <div class="order-no">订单号: {{ order.orderNo }}</div>

        <div class="product-info">
          <img :src="getProductImage(order.productId)" class="product-img" alt="" />
          <div class="product-detail">
            <div class="product-name">{{ getProductName(order.productId) }}</div>
            <div class="product-spec">{{ getProductSpec(order.productId) }}</div>
            <div class="product-price-row">
              <span class="price">¥{{ order.price?.toFixed(2) }}</span>
              <span class="qty">x{{ order.quantity }}</span>
            </div>
          </div>
        </div>

        <div class="price-breakdown">
          <div class="breakdown-row">
            <span>商品总计</span>
            <span>¥{{ (order.price * order.quantity).toFixed(2) }}</span>
          </div>
          <div class="breakdown-row">
            <span>运费</span>
            <span class="free-shipping">免费</span>
          </div>
          <div class="breakdown-row total">
            <span>应付总额</span>
            <span>¥{{ order.totalPrice?.toFixed(2) || (order.price * order.quantity).toFixed(2) }}</span>
          </div>
        </div>

        <div class="delivery-info" v-if="order.receiverName">
          <div class="info-header">
            <el-icon><Calendar /></el-icon>
            <span>配送信息</span>
          </div>
          <div class="info-body">
            <div class="info-row">
              <span class="info-label">收货地址</span>
              <span>{{ order.receiverName }} {{ order.receiverPhone }}<br/>{{ order.receiverAddress }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">配送方式</span>
              <span>{{ order.logisticsCompany || '顺丰速运' }}</span>
            </div>
            <div class="info-row">
              <span class="info-label">支付方式</span>
              <span>{{ getPayMethod(order.payMethod) }}</span>
            </div>
          </div>
        </div>

        <div class="action-row">
          <button class="btn-track" @click="handleTrack(order)">
            <el-icon><Location /></el-icon> 查看物流
          </button>
          <button class="btn-confirm" @click="handleConfirmReceive(order)">
            <el-icon><CircleCheckFilled /></el-icon> 确认收货
          </button>
        </div>

        <div class="service-link" @click="$router.push('/mobile/service')">
          <el-icon><Service /></el-icon>
          <div class="service-text">
            <span class="service-title">遇到问题了？</span>
            <span class="service-desc">24小时在线客服为您排忧解难</span>
          </div>
          <el-icon><ArrowRight /></el-icon>
        </div>
      </div>
    </div>

    <footer class="mobile-footer">
      <div class="footer-item" @click="$router.push('/mobile')"><el-icon class="footer-icon"><House /></el-icon><span class="footer-text">首页</span></div>
      <div class="footer-item" @click="$router.push('/mobile/category')"><el-icon class="footer-icon"><Lightning /></el-icon><span class="footer-text">秒杀</span></div>
      <div class="footer-item active" @click="$router.push('/orders')"><el-icon class="footer-icon"><Document /></el-icon><span class="footer-text">订单</span></div>
      <div class="footer-item" @click="$router.push('/mobile/profile')"><el-icon class="footer-icon"><UserFilled /></el-icon><span class="footer-text">个人中心</span></div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios'
import { ArrowLeft, Bell, Van, Box, Location, Calendar, CircleCheckFilled, Service, ArrowRight, House, Lightning, Document, UserFilled, Goods, ShoppingCart } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'MobileOrderReceived',
  components: { ArrowLeft, Bell, Van, Box, Location, Calendar, CircleCheckFilled, Service, ArrowRight, House, Lightning, Document, UserFilled, Goods, ShoppingCart },
  data() { return { orders: [], loading: false } },
  mounted() { this.loadOrders() },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const token = localStorage.getItem('token')
        const res = await axios.get('/api/orders/user/status/2', { headers: { 'Authorization': `Bearer ${token}` } })
        if (res.data.code === 200) this.orders = res.data.data || []
      } catch (e) { this.orders = this.getMockData() }
      finally { this.loading = false }
    },
    getMockData() {
      return [
        { id: 1, orderNo: '82930412847', productId: 1, quantity: 1, price: 799, totalPrice: 799, status: 2, receiverName: '张小野', receiverPhone: '138****9012', receiverAddress: '上海市浦东新区 某某路 888号 A1座1502室', logisticsCompany: '顺丰速运', payMethod: 'wechat', shipTime: '2026-04-11 09:00:00' }
      ]
    },
    isDelivered(o) { return o.logisticsNo && o.status === 2 },
    getShippingStatus(o) {
      if (!o.logisticsNo) return '商家正在积极配货中，请耐心等待...'
      return `包裹已到达${o.logisticsCompany || ''}上海分拨中心`
    },
    formatTime(t) { if (!t) return ''; const d = new Date(t); return `今天${String(d.getHours()).padStart(2,'0')}:${String(d.getMinutes()).padStart(2,'0')}` },
    getProductImage(id) {
      const m = { 1: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=orange%20sneakers%20sport%20shoes&image_size=square', 2: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartwatch%20blue%20product&image_size=square' }
      return m[id] || m[1]
    },
    getProductName(id) { const n = { 1: 'Editorial Energy 限量版运动板鞋', 2: 'Smart Chrono Azure 数显表' }; return n[id] || '商品' },
    getProductSpec(id) { const s = { 1: '尺码: 42|颜色: 橙岩橙', 2: '表盘: 蓝色|材质: 不锈钢' }; return s[id] || '' },
    getPayMethod(m) { return { wechat: '微信支付', alipay: '支付宝', balance: '余额支付' }[m] || '微信支付' },
    handleTrack(order) { ElMessage.info(`查看物流: ${order.logisticsNo || '暂无物流信息'}`) },
    handleConfirmReceive(order) {
      ElMessageBox.confirm('确认已收到商品吗？', '确认收货', { confirmButtonText: '确认收货', cancelButtonText: '取消', type: 'info' })
        .then(async () => {
          try { const t = localStorage.getItem('token'); await axios.put(`/api/orders/${order.id}/receive`, {}, { headers: { 'Authorization': `Bearer ${t}` } }); ElMessage.success('已确认收货'); this.loadOrders() }
          catch(e) { ElMessage.success('已确认收货（模拟）'); this.loadOrders() }
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
.header-icon { display: flex; gap: 12px; color: #666; font-size: 20px; }
.order-content { padding: 12px 16px; }
.empty-state { display: flex; flex-direction: column; align-items: center; padding: 80px 20px; gap: 16px; }
.empty-state p { color: #999; font-size: 15px; }

.order-card { background: #fff; border-radius: 16px; margin-bottom: 16px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.shipping-banner { padding: 18px 20px; display: flex; align-items: center; justify-content: space-between; background: linear-gradient(135deg, #ff9a6c 0%, #ff6b35 100%); color: white; }
.shipping-banner.delivered { background: linear-gradient(135deg, #ff7e5f 0%, #feb47b 100%); }
.banner-left { display: flex; align-items: center; gap: 12px; }
.banner-icon { font-size: 28px; }
.banner-title { font-size: 16px; font-weight: 700; }
.banner-subtitle { font-size: 12px; opacity: 0.85; margin-top: 2px; }
.banner-time { font-size: 12px; opacity: 0.75; }

.delivery-tip { display: flex; align-items: center; gap: 8px; padding: 12px 20px; background: #fef3e2; color: #b45309; font-size: 13px; }
.order-no { padding: 12px 20px; font-size: 13px; color: #666; }

.product-info { display: flex; gap: 14px; padding: 16px 20px; border-top: 1px solid #f5f5f5; }
.product-img { width: 72px; height: 72px; border-radius: 12px; object-fit: cover; flex-shrink: 0; }
.product-detail { flex: 1; display: flex; flex-direction: column; gap: 4px; }
.product-name { font-size: 15px; font-weight: 600; color: #333; }
.product-spec { font-size: 12px; color: #999; }
.product-price-row { display: flex; justify-content: space-between; align-items: baseline; margin-top: auto; }
.price { font-size: 18px; font-weight: 800; color: #d94545; }
.qty { font-size: 13px; color: #999; }

.price-breakdown { padding: 14px 20px; border-top: 1px solid #f5f5f5; }
.breakdown-row { display: flex; justify-content: space-between; padding: 4px 0; font-size: 13px; color: #666; }
.breakdown-row.total { padding-top: 8px; margin-top: 4px; border-top: 1px dashed #eee; font-size: 15px; font-weight: 700; color: #d94545; }
.free-shipping { color: #2563eb; font-weight: 600; }

.delivery-info { padding: 16px 20px; border-top: 1px solid #f5f5f5; }
.info-header { display: flex; align-items: center; gap: 6px; font-size: 14px; font-weight: 700; color: #d94545; margin-bottom: 10px; }
.info-body { background: #fafafa; border-radius: 10px; padding: 12px; }
.info-row { display: flex; gap: 8px; padding: 4px 0; font-size: 12px; }
.info-label { color: #999; width: 60px; flex-shrink: 0; }

.action-row { display: flex; gap: 12px; padding: 16px 20px; }
.btn-track { flex: 1; height: 44px; border-radius: 22px; border: none; background: #dbeafe; color: #2563eb; font-size: 14px; font-weight: 600; display: flex; align-items: center; justify-content: center; gap: 6px; }
.btn-confirm { flex: 1.3; height: 44px; border-radius: 22px; border: none; background: linear-gradient(135deg, #a0522d 0%, #8b4513 100%); color: white; font-size: 14px; font-weight: 600; display: flex; align-items: center; justify-content: center; gap: 6px; }

.service-link { display: flex; align-items: center; gap: 10px; padding: 14px 20px; border-top: 1px solid #f5f5f5; cursor: pointer; color: #666; }
.service-text { flex: 1; display: flex; flex-direction: column; }
.service-title { font-size: 14px; font-weight: 600; }
.service-desc { font-size: 11px; color: #999; }

.mobile-footer { position: fixed; bottom: 0; left: 0; right: 0; display: flex; background: #fff; box-shadow: 0 -2px 10px rgba(0,0,0,0.08); z-index: 100; }
.footer-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 8px 0; cursor: pointer; color: #999; font-size: 11px; }
.footer-item.active { color: #ff6b35; }
.footer-icon { font-size: 20px; margin-bottom: 2px; }
.footer-text { font-size: 11px; }
</style>
