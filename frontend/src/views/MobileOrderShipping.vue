<template>
  <div class="mobile-container">
    <header class="page-header">
      <el-icon class="back-icon" @click="$router.back()"><ArrowLeft /></el-icon>
      <h1 class="page-title">待发货</h1>
      <el-icon class="search-icon" @click="showSearch = !showSearch"><Search /></el-icon>
    </header>

    <div class="order-content" v-loading="loading">
      <div v-if="orders.length === 0 && !loading" class="empty-state">
        <el-icon :size="64" color="#ccc"><Box /></el-icon>
        <p>暂无待发货订单</p>
      </div>

      <div v-for="order in orders" :key="order.id" class="order-card">
        <div class="status-banner">
          <el-icon class="banner-icon"><Box /></el-icon>
          <div class="banner-text">
            <div class="banner-title">订单已支付</div>
            <div class="banner-subtitle">商家正在积极配货中，请耐心等待...</div>
          </div>
        </div>

        <div class="order-item" v-for="i in [order]" :key="'item-'+i.id">
          <div class="item-header">
            <span class="order-no">NO. {{ i.orderNo || ('88294'+String(i.id).padStart(5,'0')) }}</span>
            <span class="status-badge pending">待发货</span>
          </div>
          <div class="item-body">
            <img :src="getProductImage(i.productId)" class="item-img" alt="" />
            <div class="item-info">
              <div class="item-name">{{ getProductName(i.productId) }}</div>
              <div class="item-spec">{{ getProductSpec(i.productId) }}</div>
              <div class="item-price-row">
                <span class="price">¥{{ i.price?.toFixed(2) }}</span>
                <span class="qty">x{{ i.quantity }}</span>
              </div>
            </div>
          </div>
          <div class="item-footer">
            <span class="total-hint">共{{ i.quantity }}件商品 实付款:</span>
            <span class="total-amount">¥{{ i.totalPrice?.toFixed(2) || (i.price * i.quantity).toFixed(2) }}</span>
          </div>
          <div class="item-actions">
            <button class="btn-remind" @click="handleRemind(order)">
              <el-icon><Bell /></el-icon> 提醒发货
            </button>
            <button class="btn-detail" @click="handleDetail(order)">
              订单详情
            </button>
          </div>
        </div>
      </div>

      <div class="bottom-tip" v-if="orders.length > 0">
        <el-icon><Star /></el-icon>
        <span>甄选馆 · 加载更多好物</span>
      </div>
    </div>

    <footer class="mobile-footer">
      <div class="footer-item" @click="$router.push('/mobile')"><el-icon class="footer-icon"><HomeFilled /></el-icon><span class="footer-text">首页</span></div>
      <div class="footer-item" @click="$router.push('/mobile/category')"><el-icon class="footer-icon"><Goods /></el-icon><span class="footer-text">分类</span></div>
      <div class="footer-item active" @click="$router.push('/orders')"><el-icon class="footer-icon"><Document /></el-icon><span class="footer-text">订单</span></div>
      <div class="footer-item" @click="$router.push('/mobile/profile')"><el-icon class="footer-icon"><UserFilled /></el-icon><span class="footer-text">我的</span></div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios'
import { ArrowLeft, Search, Box, Bell, Star, HomeFilled, Goods, Document, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileOrderShipping',
  components: { ArrowLeft, Search, Box, Bell, Star, HomeFilled, Goods, Document, UserFilled },
  data() { return { orders: [], loading: false, showSearch: false } },
  mounted() { this.loadOrders() },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const token = localStorage.getItem('token')
        const res = await axios.get('/api/orders/user/status/1', { headers: { 'Authorization': `Bearer ${token}` } })
        if (res.data.code === 200) this.orders = res.data.data || []
      } catch (e) { this.orders = this.getMockData() }
      finally { this.loading = false }
    },
    getMockData() {
      return [
        { id: 2, orderNo: '8829471052', productId: 4, quantity: 1, price: 1299, totalPrice: 1299, status: 1, payTime: '2026-04-10 15:30:00' },
        { id: 8, orderNo: '8829471091', productId: 1, quantity: 1, price: 899, totalPrice: 899, status: 1 }
      ]
    },
    getProductImage(id) {
      const m = { 1: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20earbuds%20black%20box&image_size=square', 4: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=red%20sports%20running%20shoes&image_size=square' }
      return m[id] || m[1]
    },
    getProductName(id) { const n = { 4: 'Pulse Velocity 无线降噪耳机 Gen 2', 1: 'Kinetic Pulse 极速系列运动鞋' }; return n[id] || '商品' },
    getProductSpec(id) { const s = { 4: '颜色: 经典黑 / 焦糖 / 标准版', 1: '颜色: 极昼蓝 / 尺码: 42码' }; return s[id] || '' },
    handleRemind(order) { ElMessage.success('已提醒商家尽快发货！') },
    handleDetail(order) { ElMessage.info(`查看订单: ${order.orderNo || order.id}`) }
  }
}
</script>

<style scoped>
.mobile-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 70px; }
.page-header { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; background: #fff; position: sticky; top: 0; z-index: 10; }
.back-icon { font-size: 22px; cursor: pointer; color: #333; }
.page-title { font-size: 18px; font-weight: 700; margin: 0; color: #ff6b35; }
.search-icon { font-size: 20px; cursor: pointer; color: #666; }
.order-content { padding: 12px 16px; }
.empty-state { display: flex; flex-direction: column; align-items: center; padding: 80px 20px; gap: 16px; }
.empty-state p { color: #999; font-size: 15px; }

.order-card { background: transparent; margin-bottom: 12px; }
.status-banner { display: flex; align-items: center; gap: 14px; padding: 20px; background: linear-gradient(135deg, #c2410c 0%, #ea580c 50%, #f97316 100%); border-radius: 20px 20px 0 0; color: white; }
.banner-icon { font-size: 32px; }
.banner-title { font-size: 17px; font-weight: 700; }
.banner-subtitle { font-size: 12px; opacity: 0.85; margin-top: 3px; }

.order-item { background: #fff; border-radius: 0 0 16px 16px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.item-header { display: flex; justify-content: space-between; align-items: center; padding: 14px 18px; }
.order-no { font-size: 13px; color: #2563eb; font-weight: 600; }
.status-badge { padding: 3px 10px; border-radius: 10px; font-size: 11px; font-weight: 600; }
.status-badge.pending { background: #fef3c7; color: #d97706; }

.item-body { display: flex; gap: 12px; padding: 0 18px 14px; }
.item-img { width: 76px; height: 76px; border-radius: 12px; object-fit: cover; flex-shrink: 0; }
.item-info { flex: 1; display: flex; flex-direction: column; gap: 3px; }
.item-name { font-size: 15px; font-weight: 600; color: #333; line-height: 1.3; }
.item-spec { font-size: 12px; color: #999; }
.item-price-row { display: flex; justify-content: space-between; align-items: baseline; margin-top: auto; }
.price { font-size: 18px; font-weight: 800; color: #d94545; }
.qty { font-size: 13px; color: #999; }

.item-footer { display: flex; justify-content: flex-end; align-items: baseline; gap: 8px; padding: 10px 18px; border-top: 1px solid #f5f5f5; }
.total-hint { font-size: 13px; color: #666; }
.total-amount { font-size: 18px; font-weight: 800; color: #d94545; }

.item-actions { display: flex; gap: 10px; padding: 14px 18px; }
.btn-remind { flex: 1; height: 40px; border-radius: 20px; border: none; background: #dbeafe; color: #2563eb; font-size: 13px; font-weight: 600; display: flex; align-items: center; justify-content: center; gap: 4px; }
.btn-detail { flex: 1; height: 40px; border-radius: 20px; border: none; background: linear-gradient(135deg, #a0522d 0%, #8b4513 100%); color: white; font-size: 13px; font-weight: 600; }

.bottom-tip { display: flex; align-items: center; justify-content: center; gap: 6px; padding: 30px; color: #bbb; font-size: 13px; }

.mobile-footer { position: fixed; bottom: 0; left: 0; right: 0; display: flex; background: #fff; box-shadow: 0 -2px 10px rgba(0,0,0,0.08); z-index: 100; }
.footer-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 8px 0; cursor: pointer; color: #999; font-size: 11px; }
.footer-item.active { color: #ff6b35; }
.footer-icon { font-size: 20px; margin-bottom: 2px; }
.footer-text { font-size: 11px; }
</style>
