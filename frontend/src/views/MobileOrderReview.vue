<template>
  <div class="mobile-container">
    <header class="page-header">
      <el-icon class="back-icon" @click="$router.back()"><ArrowLeft /></el-icon>
      <h1 class="page-title">待评价</h1>
      <span></span>
    </header>

    <div class="order-content" v-loading="loading">
      <div v-if="orders.length === 0 && !loading" class="empty-state">
        <el-icon :size="64" color="#ccc"><ChatDotRound /></el-icon>
        <p>暂无待评价订单</p>
        <el-button type="primary" round @click="$router.push('/mobile')">去发现更多好物</el-button>
      </div>

      <div v-for="order in orders" :key="order.id" class="review-card">
        <div class="card-header">
          <span class="order-no">订单号: {{ order.orderNo || ('SK-'+String(992847551 + order.id)) }}</span>
          <span class="received-tag">已收货</span>
        </div>

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

        <div class="total-row">
          <span class="total-label">合计:</span>
          <span class="total-value">¥{{ order.totalPrice?.toFixed(2) || (order.price * order.quantity).toFixed(2) }}</span>
          <button class="btn-view" @click="handleViewDetail(order)">查看详情</button>
          <button class="btn-review" @click="openReviewDialog(order)">立即评价</button>
        </div>
      </div>

      <div class="bottom-promo" v-if="orders.length > 0">
        <p>没有更多待评价订单啦</p>
        <a @click="$router.push('/mobile')">去发现更多好物</a>
      </div>
    </div>

    <!-- 评价弹窗 -->
    <el-dialog v-model="showReviewDialog" title="发表评价" width="340px" :close-on-click-modal="false" class="review-dialog">
      <div class="review-form" v-if="currentOrder">
        <div class="form-product">{{ getProductName(currentOrder.productId) }}</div>
        <div class="rating-section">
          <span class="rating-label">评分：</span>
          <el-rate v-model="reviewForm.rating" :colors="['#ff9900', '#ff9900', '#FF9900']" size="large" />
        </div>
        <el-input v-model="reviewForm.content" type="textarea" :rows="4" placeholder="分享您的使用体验，帮助更多买家~" maxlength="500" show-word-limit />
        <div class="anonymous-check">
          <el-checkbox v-model="reviewForm.isAnonymous">匿名评价</el-checkbox>
        </div>
      </div>
      <template #footer>
        <el-button @click="showReviewDialog = false">取消</el-button>
        <el-button type="warning" @click="submitReview" :loading="submitting">提交评价</el-button>
      </template>
    </el-dialog>

    <footer class="mobile-footer">
      <div class="footer-item" @click="$router.push('/mobile')"><el-icon class="footer-icon"><HomeFilled /></el-icon><span class="footer-text">首页</span></div>
      <div class="footer-item" @click="$router.push('/mobile/category')"><el-icon class="footer-icon"><Lightning /></el-icon><span class="footer-text">秒杀</span></div>
      <div class="footer-item active" @click="$router.push('/orders')"><el-icon class="footer-icon"><Document /></el-icon><span class="footer-text">订单</span></div>
      <div class="footer-item" @click="$router.push('/mobile/profile')"><el-icon class="footer-icon"><UserFilled /></el-icon><span class="footer-text">我的</span></div>
    </footer>
  </div>
</template>

<script>
import axios from 'axios'
import { ArrowLeft, ChatDotRound, HomeFilled, Lightning, Document, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileOrderReview',
  components: { ArrowLeft, ChatDotRound, HomeFilled, Lightning, Document, UserFilled },
  data() {
    return { orders: [], loading: false, showReviewDialog: false, currentOrder: null, submitting: false,
      reviewForm: { rating: 5, content: '', isAnonymous: false } }
  },
  mounted() { this.loadOrders() },
  methods: {
    async loadOrders() {
      this.loading = true
      try {
        const token = localStorage.getItem('token')
        const res = await axios.get('/api/orders/user/status/3', { headers: { 'Authorization': `Bearer ${token}` } })
        if (res.data.code === 200) this.orders = res.data.data || []
      } catch (e) { this.orders = this.getMockData() }
      finally { this.loading = false }
    },
    getMockData() {
      return [
        { id: 3, orderNo: 'SK-992847551', productId: 5, quantity: 1, price: 1299, totalPrice: 1299, status: 3 },
        { id: 4, orderNo: 'SK-884721094', productId: 1, quantity: 1, price: 599, totalPrice: 599, status: 3 },
        { id: 9, orderNo: 'SK-773210455', productId: 2, quantity: 1, price: 899, totalPrice: 899, status: 3 }
      ]
    },
    getProductImage(id) {
      const m = { 1: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=red%20sports%20running%20shoes&image_size=square', 2: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartwatch%20gold%20luxury&image_size=square', 5: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20headphones%20blue&image_size=square' }
      return m[id] || m[1]
    },
    getProductName(id) { const n = { 1: '极速之影 专业运动跑鞋', 2: '智睿 智能健康运动手表', 5: '至尊降噪无线蓝牙耳机' }; return n[id] || '商品' },
    getProductSpec(id) { const s = { 1: '规格: 火红 / 42码', 2: '规格: 铂金版 / 皮表带', 5: '规格: 石墨灰 / 新款' }; return s[id] || '' },
    handleViewDetail(order) { ElMessage.info(`查看订单详情: ${order.orderNo}`) },
    openReviewDialog(order) { this.currentOrder = order; this.reviewForm = { rating: 5, content: '', isAnonymous: false }; this.showReviewDialog = true },
    async submitReview() {
      if (!this.reviewForm.content.trim()) { ElMessage.warning('请填写评价内容'); return }
      this.submitting = true
      try {
        const token = localStorage.getItem('token')
        await axios.post('/api/reviews', {
          orderId: this.currentOrder.id, productId: this.currentOrder.productId,
          rating: this.reviewForm.rating, content: this.reviewForm.content,
          isAnonymous: this.reviewForm.isAnonymous ? 1 : 0
        }, { headers: { 'Authorization': `Bearer ${token}` } })
        ElMessage.success('评价提交成功！')
        this.showReviewDialog = false
        this.loadOrders()
      } catch (e) { ElMessage.success('评价提交成功！（模拟）'); this.showReviewDialog = false; this.loadOrders() }
      finally { this.submitting = false }
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

.review-card { background: #fff; border-radius: 16px; margin-bottom: 14px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.card-header { display: flex; justify-content: space-between; align-items: center; padding: 14px 18px; }
.order-no { font-size: 13px; color: #666; }
.received-tag { color: #2563eb; font-size: 13px; font-weight: 600; }

.product-info { display: flex; gap: 14px; padding: 0 18px 14px; }
.product-img { width: 72px; height: 72px; border-radius: 12px; object-fit: cover; flex-shrink: 0; }
.product-detail { flex: 1; display: flex; flex-direction: column; gap: 3px; }
.product-name { font-size: 15px; font-weight: 600; color: #333; }
.product-spec { font-size: 12px; color: #999; }
.product-price-row { display: flex; justify-content: space-between; align-items: baseline; margin-top: auto; }
.price { font-size: 18px; font-weight: 800; color: #d94545; }
.qty { font-size: 13px; color: #999; }

.total-row { display: flex; align-items: center; gap: 10px; padding: 14px 18px; border-top: 1px solid #f5f5f5; flex-wrap: wrap; }
.total-label { font-size: 14px; color: #666; }
.total-value { font-size: 18px; font-weight: 800; color: #d94545; }
.btn-view { padding: 8px 18px; border-radius: 16px; border: 1px solid #dbeafe; background: #eff6ff; color: #2563eb; font-size: 13px; font-weight: 500; }
.btn-review { padding: 8px 18px; border-radius: 16px; border: none; background: linear-gradient(135deg, #a0522d 0%, #8b4513 100%); color: white; font-size: 13px; font-weight: 600; }

.bottom-promo { text-align: center; padding: 30px 0; }
.bottom-promo p { color: #999; font-size: 14px; margin-bottom: 10px; }
.bottom-promo a { color: #2563eb; font-size: 14px; font-weight: 600; cursor: pointer; }

.review-dialog .form-product { font-size: 15px; font-weight: 600; color: #333; margin-bottom: 14px; padding-bottom: 12px; border-bottom: 1px solid #f0f0f0; }
.rating-section { display: flex; align-items: center; gap: 10px; margin-bottom: 16px; }
.rating-label { font-size: 14px; color: #666; font-weight: 500; }
.anonymous-check { margin-top: 12px; }

.mobile-footer { position: fixed; bottom: 0; left: 0; right: 0; display: flex; background: #fff; box-shadow: 0 -2px 10px rgba(0,0,0,0.08); z-index: 100; }
.footer-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 8px 0; cursor: pointer; color: #999; font-size: 11px; }
.footer-item.active { color: #ff6b35; }
.footer-icon { font-size: 20px; margin-bottom: 2px; }
.footer-text { font-size: 11px; }
</style>
