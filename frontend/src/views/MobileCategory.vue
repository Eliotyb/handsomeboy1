<template>
  <div class="mobile-container">
    <header class="mobile-header">
      <div class="header-center">
        <h1 class="app-title">商品分类</h1>
      </div>
    </header>

    <div class="category-wrapper">
      <aside class="category-sidebar">
        <div 
          v-for="(category, index) in categories" 
          :key="index"
          class="sidebar-item"
          :class="{ active: activeCategory === index }"
          @click="selectCategory(index)"
        >
          {{ category.name }}
        </div>
      </aside>

      <main class="category-content">
        <div class="content-header">
          <h2 class="category-title">{{ categories[activeCategory]?.name || '' }}</h2>
        </div>
        <div class="product-grid">
          <div 
            v-for="(product, index) in currentProducts" 
            :key="product.id"
            class="grid-item"
            @click="addToCart(product)"
          >
            <div class="item-image">
              <img :src="product.image" :alt="product.name" />
            </div>
            <div class="item-info">
              <h3 class="item-name">{{ product.name }}</h3>
              <div class="item-price">
                <span class="price-current">¥{{ product.price }}</span>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>

    <footer class="mobile-footer">
      <div class="footer-item" @click="navigateTo('/mobile')">
        <el-icon class="footer-icon"><House /></el-icon>
        <span class="footer-text">首页</span>
      </div>
      <div class="footer-item active" @click="navigateTo('/mobile/category')">
        <el-icon class="footer-icon"><Goods /></el-icon>
        <span class="footer-text">分类</span>
      </div>
      <div class="footer-item" @click="navigateTo('/mobile/cart')">
        <el-icon class="footer-icon"><ShoppingCart /></el-icon>
        <span class="footer-text">购物车</span>
      </div>
      <div class="footer-item" @click="navigateTo('/mobile/profile')">
        <el-icon class="footer-icon"><UserFilled /></el-icon>
        <span class="footer-text">我的</span>
      </div>
    </footer>
  </div>
</template>

<script>
import { House, Goods, ShoppingCart, UserFilled } from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileCategory',
  components: {
    House,
    Goods,
    ShoppingCart,
    UserFilled
  },
  data() {
    return {
      activeCategory: 0,
      categories: [
        { name: '数码电子' },
        { name: '时尚美妆' },
        { name: '服装鞋包' },
        { name: '食品生鲜' },
        { name: '家居家装' },
        { name: '运动户外' },
        { name: '图书音像' },
        { name: '母婴用品' },
        { name: '汽车用品' },
        { name: '其他' }
      ],
      allProducts: [],
      loading: false
    }
  },
  computed: {
    currentProducts() {
      const categoryName = this.categories[this.activeCategory]?.name || ''
      const categoryProducts = this.allProducts.filter(p => p.category === categoryName)
      return categoryProducts
    }
  },
  mounted() {
    this.loadProducts()
  },
  methods: {
    async loadProducts() {
      try {
        this.loading = true
        const response = await axios.get('/api/products')
        if (response.data.code === 200) {
          this.allProducts = response.data.data
        }
      } catch (error) {
        console.error('加载商品失败:', error)
        ElMessage.error('加载商品失败')
      } finally {
        this.loading = false
      }
    },
    selectCategory(index) {
      this.activeCategory = index
    },
    async addToCart(product) {
      try {
        const token = localStorage.getItem('token')
        const response = await axios.post('/api/cart', {
          productId: product.id,
          quantity: 1
        }, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        if (response.data.code === 200) {
          ElMessage.success('已添加到购物车')
        } else {
          ElMessage.error(response.data.message || '添加失败')
        }
      } catch (error) {
        console.error('添加购物车失败:', error)
        ElMessage.error('添加购物车失败')
      }
    },
    navigateTo(path) {
      this.$router.push(path)
    }
  }
}
</script>

<style scoped>
.mobile-container {
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
  padding-bottom: 60px;
}

.mobile-header {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 16px 20px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.app-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.category-wrapper {
  display: flex;
  height: calc(100vh - 130px);
}

.category-sidebar {
  width: 90px;
  background: white;
  overflow-y: auto;
  border-right: 1px solid #eee;
}

.sidebar-item {
  padding: 16px 12px;
  font-size: 14px;
  color: #666;
  cursor: pointer;
  transition: all 0.3s;
  text-align: center;
}

.sidebar-item.active {
  background: #f5f5f5;
  color: #ff6b6b;
  font-weight: 500;
  border-left: 3px solid #ff6b6b;
}

.category-content {
  flex: 1;
  padding: 16px;
  overflow-y: auto;
}

.content-header {
  margin-bottom: 16px;
}

.category-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

.product-grid {
  display: grid;
  grid-template-columns: repeat(2, 1fr);
  gap: 12px;
}

.grid-item {
  background: white;
  border-radius: 12px;
  overflow: hidden;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
  transition: all 0.3s;
  cursor: pointer;
}

.grid-item:active {
  transform: scale(0.98);
}

.item-image {
  height: 120px;
  overflow: hidden;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  padding: 12px;
}

.item-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 8px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-price {
  display: flex;
  align-items: center;
}

.price-current {
  font-size: 18px;
  font-weight: 700;
  color: #ff6b6b;
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
