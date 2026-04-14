<template>
  <div class="mobile-container">
    <header class="page-header">
      <el-icon class="back-icon" @click="$router.back()"><ArrowLeft /></el-icon>
      <h1 class="page-title">退换货/售后</h1>
      <el-icon class="search-icon"><Search /></el-icon>
    </header>

    <div class="tab-bar">
      <div class="tab-item" :class="{ active: activeTab === 'all' }" @click="activeTab = 'all'">全部售后</div>
      <div class="tab-item" :class="{ active: activeTab === 'pending' }" @click="activeTab = 'pending'">待审核</div>
      <div class="tab-item" :class="{ active: activeTab === 'processing' }" @click="activeTab = 'processing'">处理中</div>
      <div class="tab-item" :class="{ active: activeTab === 'done' }" @click="activeTab = 'done'">已完成</div>
    </div>

    <div class="refund-content" v-loading="loading">
      <div v-if="filteredRefunds.length === 0 && !loading" class="empty-state">
        <el-icon :size="64" color="#ccc"><RefreshRight /></el-icon>
        <p>暂无售后记录</p>
      </div>

      <div v-for="refund in filteredRefunds" :key="refund.id" class="refund-card">
        <div class="card-header">
          <span class="refund-no">订单号：A{{ String(refund.orderId || 88294710).padStart(9, '-') }}</span>
          <span class="status-tag" :class="getStatusClass(refund.status)">{{ getStatusText(refund.status) }}</span>
        </div>

        <div class="type-badge" :class="refund.refundType === 2 ? 'return-type' : 'refund-only'">
          {{ refund.refundType === 2 ? '退货退款' : '仅退款' }}
        </div>

        <div class="product-info">
          <img :src="getProductImage(refund.orderId)" class="product-img" alt="" />
          <div class="product-detail">
            <div class="product-name">{{ getProductName(refund.orderId) }}</div>
            <div class="product-spec">{{ getProductSpec(refund.orderId) }}</div>
            <div class="product-qty">数量：{{ refund.refundAmount > 1000 ? 1 : 1 }}</div>
          </div>
        </div>

        <div v-if="refund.status === 0" class="pending-tip">
          <el-icon><InfoFilled /></el-icon>
          <span>您的换货申请已递请，还有 <strong style="color:#d94545">48小时</strong> 内将商品寄回，逾期申请将自动失效。</span>
        </div>

        <div class="action-row">
          <button class="btn-contact" @click="$router.push('/mobile/service')">联系客服</button>
          <button class="btn-detail" @click="handleDetail(refund)">查看详情</button>
        </div>
      </div>
    </div>

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
import { ArrowLeft, Search, RefreshRight, InfoFilled, HomeFilled, Lightning, Document, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileAfterSale',
  components: { ArrowLeft, Search, RefreshRight, InfoFilled, HomeFilled, Lightning, Document, UserFilled },
  data() { return { refunds: [], loading: false, activeTab: 'all' } },
  computed: {
    filteredRefunds() {
      if (this.activeTab === 'all') return this.refunds
      const map = { pending: [0], processing: [1, 2], done: [3, 4] }
      return this.refunds.filter(r => map[this.activeTab]?.includes(r.status))
    }
  },
  mounted() { this.loadRefunds() },
  methods: {
    async loadRefunds() {
      this.loading = true
      try {
        const token = localStorage.getItem('token')
        const res = await axios.get('/api/refunds/my', { headers: { 'Authorization': `Bearer ${token}` } })
        if (res.data.code === 200) this.refunds = res.data.data || []
      } catch (e) { this.refunds = this.getMockData() }
      finally { this.loading = false }
    },
    getMockData() {
      return [
        { id: 1, orderId: 5, refundType: 1, reason: '不想要了', description: '拍错了，颜色不对', refundAmount: 299, status: 0 },
        { id: 2, orderId: 6, refundType: 2, reason: '质量问题', description: '耳机左耳有杂音，无法正常使用，要求退货退款', refundAmount: 1299, status: 1 },
        { id: 3, orderId: 7, refundType: 1, reason: '尺寸不合', description: '手表表带太短，戴不上', refundAmount: 899, status: 3 },
        { id: 4, orderId: 8, refundType: 2, reason: '极简脉动', description: '', refundAmount: 899, status: 4 }
      ]
    },
    getProductImage(id) {
      const m = { 5: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=red%20sneakers%20hot%20sale&image_size=square', 6: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=wireless%20headphones%20pro&image_size=square', 7: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=smartwatch%20minimal%20lite&image_size=square' }
      return m[id] || m[5]
    },
    getProductName(id) {
      const n = { 5: '极速动力 2.0 跑鞋 - 炽热橙限量版', 6: '声动脉冲 无线降噪耳机 PRO', 7: '极简脉动 智能腕表 Lite' }
      return n[id] || '商品'
    },
    getProductSpec(id) {
      const s = { 5: '尺码: 42|颜色: 火电燃', 6: '款式: 天猫版 | 配件: 全套', 7: '版本: 全球版 | 表带: 墨晶' }
      return s[id] || ''
    },
    getStatusClass(s) {
      return { 0: 'auditing', 1: 'waiting', 2: 'processing', 3: 'done-success', 4: 'done-reject' }[s] || ''
    },
    getStatusText(s) {
      return { 0: '审核中', 1: '待寄回', 2: '处理中', 3: '退款成功', 4: '退款拒绝' }[s] || '未知'
    },
    handleDetail(r) { ElMessage.info(`售后单号: ${r.refundNo || ('AS-' + String(88294 + r.id).padStart(7, '0'))} 详情`) }
  }
}
</script>

<style scoped>
.mobile-container { min-height: 100vh; background: #f5f5f5; padding-bottom: 70px; }
.page-header { display: flex; align-items: center; justify-content: space-between; padding: 16px 20px; background: #fff; position: sticky; top: 0; z-index: 10; }
.back-icon { font-size: 22px; cursor: pointer; color: #333; }
.page-title { font-size: 18px; font-weight: 700; margin: 0; color: #ff6b35; }
.search-icon { font-size: 20px; cursor: pointer; color: #666; }

.tab-bar { display: flex; background: #fff; padding: 0 8px; position: sticky; top: 57px; z-index: 9; border-bottom: 1px solid #f0f0f0; }
.tab-item { flex: 1; text-align: center; padding: 14px 0; font-size: 13px; font-weight: 500; color: #666; cursor: pointer; position: relative; transition: all 0.2s; }
.tab-item.active { color: #ea580c; font-weight: 700; }
.tab-item.active::after { content: ''; position: absolute; bottom: 0; left: 20%; right: 20%; height: 3px; background: #ea580c; border-radius: 2px; }

.refund-content { padding: 12px 16px; }
.empty-state { display: flex; flex-direction: column; align-items: center; padding: 80px 20px; gap: 16px; }
.empty-state p { color: #999; font-size: 15px; }

.refund-card { background: #fff; border-radius: 16px; margin-bottom: 14px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.04); }
.card-header { display: flex; justify-content: space-between; align-items: center; padding: 14px 18px; }
.refund-no { font-size: 13px; color: #2563eb; font-weight: 500; }
.status-tag { padding: 3px 10px; border-radius: 10px; font-size: 11px; font-weight: 600; }
.status-tag.auditing { background: #fef3c7; color: #d97706; }
.status-tag.waiting { background: #fef3c7; color: #d97706; }
.status-tag.processing { background: #dbeafe; color: #2563eb; }
.status-tag.done-success { background: #dcfce7; color: #16a34a; }
.status-tag.done-reject { background: #f3f4f6; color: #94a3b8; }

.type-badge { display: inline-block; margin: 0 18px 10px; padding: 3px 12px; border-radius: 8px; font-size: 11px; font-weight: 600; }
.return-type { background: #2563eb; color: white; }
.refund-only { background: #dc2626; color: white; }

.product-info { display: flex; gap: 12px; padding: 0 18px 12px; }
.product-img { width: 68px; height: 68px; border-radius: 10px; object-fit: cover; flex-shrink: 0; }
.product-detail { flex: 1; display: flex; flex-direction: column; gap: 2px; }
.product-name { font-size: 14px; font-weight: 600; color: #333; line-height: 1.3; }
.product-spec { font-size: 11px; color: #999; }
.product-qty { font-size: 12px; color: #666; }

.pending-tip { display: flex; gap: 8px; margin: 0 18px 12px; padding: 10px 12px; background: #fef3c7; border-radius: 10px; font-size: 12px; color: #92400e; line-height: 1.5; }

.action-row { display: flex; gap: 10px; padding: 14px 18px; border-top: 1px solid #f5f5f5; }
.btn-contact { flex: 1; height: 38px; border-radius: 19px; border: 1px solid #e5e7eb; background: #fff; color: #666; font-size: 13px; font-weight: 500; }
.btn-detail { flex: 1; height: 38px; border-radius: 19px; border: none; background: linear-gradient(135deg, #a0522d 0%, #8b4513 100%); color: white; font-size: 13px; font-weight: 600; }

.mobile-footer { position: fixed; bottom: 0; left: 0; right: 0; display: flex; background: #fff; box-shadow: 0 -2px 10px rgba(0,0,0,0.08); z-index: 100; }
.footer-item { flex: 1; display: flex; flex-direction: column; align-items: center; padding: 8px 0; cursor: pointer; color: #999; font-size: 11px; }
.footer-item.active { color: #ff6b35; }
.footer-icon { font-size: 20px; margin-bottom: 2px; }
.footer-text { font-size: 11px; }
</style>
