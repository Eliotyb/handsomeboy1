<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">浏览记录</h1>
      <div class="header-right" @click="clearHistory">
        <el-icon><Delete /></el-icon>
      </div>
    </header>

    <div class="history-content">
      <div v-if="historyList.length === 0" class="empty-state">
        <el-icon class="empty-icon"><Clock /></el-icon>
        <p>暂无浏览记录</p>
        <el-button type="primary" @click="$router.push('/mobile')">去逛逛</el-button>
      </div>

      <div v-else>
        <div class="history-date" v-for="(group, date) in groupedHistory" :key="date">
          <div class="date-label">{{ date }}</div>
          <div class="history-list">
            <div class="history-item" v-for="(item, index) in group" :key="index"
                 @click="viewProduct(item)">
              <img :src="item.image" :alt="item.name" class="item-image" />
              <div class="item-info">
                <h3 class="item-name">{{ item.name }}</h3>
                <p class="item-price">¥{{ item.price }}</p>
                <span class="item-time">{{ item.time }}</span>
              </div>
            </div>
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
import { ArrowLeft, Clock, Delete, House, Goods, ShoppingCart, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileHistory',
  components: {
    ArrowLeft,
    Clock,
    Delete,
    House,
    Goods,
    ShoppingCart,
    UserFilled
  },
  data() {
    return {
      historyList: [
        { id: 1, name: '双人吉他合奏课程', price: 44.99, image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=guitar%20lesson&image_size=square', time: '10:30', date: '今天' },
        { id: 2, name: '荧光夜跑装备套装', price: 0.00, image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=running%20gear&image_size=square', time: '09:15', date: '今天' },
        { id: 3, name: 'AI与伦理全球峰会', price: 12.00, image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=AI%20conference&image_size=square', time: '18:20', date: '昨天' }
      ]
    }
  },
  computed: {
    groupedHistory() {
      const groups = {}
      this.historyList.forEach(item => {
        if (!groups[item.date]) {
          groups[item.date] = []
        }
        groups[item.date].push(item)
      })
      return groups
    }
  },
  methods: {
    clearHistory() {
      this.$confirm('确定要清空所有浏览记录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.historyList = []
        ElMessage.success('已清空浏览记录')
      }).catch(() => {})
    },
    viewProduct(item) {
      ElMessage.info(`查看商品：${item.name}`)
      this.$router.push('/seckill-detail?productId=' + item.id)
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
  text-align: right;
  cursor: pointer;
  color: #999;
  font-size: 18px;
}

.history-content {
  padding: 16px;
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
  margin: 0 0 20px;
  font-size: 15px;
}

.history-date {
  margin-bottom: 20px;
}

.date-label {
  font-size: 14px;
  font-weight: 600;
  color: #666;
  margin-bottom: 12px;
  padding-left: 4px;
}

.history-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.history-item {
  background: white;
  border-radius: 12px;
  padding: 12px;
  display: flex;
  gap: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
  cursor: pointer;
  transition: all 0.3s;
}

.history-item:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.1);
}

.item-image {
  width: 90px;
  height: 90px;
  border-radius: 8px;
  object-fit: cover;
  flex-shrink: 0;
}

.item-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.item-name {
  font-size: 14px;
  font-weight: 600;
  color: #333;
  margin: 0;
  line-height: 1.4;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-price {
  font-size: 16px;
  font-weight: 700;
  color: #ff6b6b;
  margin: 4px 0;
}

.item-time {
  font-size: 11px;
  color: #999;
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
