<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">我的收藏</h1>
      <div class="header-right"></div>
    </header>

    <div class="favorites-content">
      <div v-if="favoriteList.length === 0" class="empty-state">
        <el-icon class="empty-icon"><Star /></el-icon>
        <p>暂无收藏商品</p>
        <el-button type="primary" @click="$router.push('/mobile/category')">去逛逛</el-button>
      </div>

      <div v-else class="product-list">
        <div class="product-item" v-for="(product, index) in favoriteList" :key="index">
          <img :src="product.image" :alt="product.name" class="product-image" />
          <div class="product-info">
            <h3 class="product-name">{{ product.name }}</h3>
            <div class="product-price">
              <span class="price-current">¥{{ product.price }}</span>
              <span class="price-original">¥{{ product.originalPrice }}</span>
            </div>
            <div class="product-actions">
              <el-button type="danger" size="small" @click="removeFavorite(index)">取消收藏</el-button>
              <el-button type="primary" size="small" @click="buyProduct(product)">立即购买</el-button>
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
import { ArrowLeft, Star, House, Goods, ShoppingCart, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileFavorites',
  components: {
    ArrowLeft,
    Star,
    House,
    Goods,
    ShoppingCart,
    UserFilled
  },
  data() {
    return {
      favoriteList: [
        { id: 1, name: '双人吉他合奏课程', price: 44.99, originalPrice: 299.00, image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=guitar%20lesson%2C%20music%20class&image_size=square' },
        { id: 2, name: 'AI与伦理全球峰会', price: 12.00, originalPrice: 299.00, image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=AI%20conference%2C%20tech%20event&image_size=square' },
        { id: 3, name: '荧光夜跑装备套装', price: 0.00, originalPrice: 199.00, image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=running%20gear%2C%20sports%20equipment&image_size=square' }
      ]
    }
  },
  methods: {
    removeFavorite(index) {
      this.$confirm('确定要取消收藏吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.favoriteList.splice(index, 1)
        ElMessage.success('已取消收藏')
      }).catch(() => {})
    },
    buyProduct(product) {
      ElMessage.success(`正在前往购买：${product.name}`)
      this.$router.push('/seckill-detail?productId=' + product.id)
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

.favorites-content {
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

.product-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.product-item {
  background: white;
  border-radius: 12px;
  padding: 12px;
  display: flex;
  gap: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.product-image {
  width: 100px;
  height: 100px;
  border-radius: 8px;
  object-fit: cover;
  flex-shrink: 0;
}

.product-info {
  flex: 1;
  display: flex;
  flex-direction: column;
  justify-content: space-between;
}

.product-name {
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

.product-price {
  display: flex;
  align-items: baseline;
  gap: 6px;
  margin: 8px 0;
}

.price-current {
  font-size: 18px;
  font-weight: 700;
  color: #ff6b6b;
}

.price-original {
  font-size: 12px;
  color: #999;
  text-decoration: line-through;
}

.product-actions {
  display: flex;
  gap: 8px;
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
