<template>
  <div class="mobile-container">
    <header class="mobile-header">
      <div class="header-center">
        <h1 class="app-title">购物车</h1>
      </div>
    </header>

    <div class="cart-content" v-if="cartItems.length > 0">
      <div class="cart-list">
        <div class="cart-item" v-for="(item, index) in cartItems" :key="item.id">
          <div class="item-check">
            <el-checkbox v-model="item.checked" @change="updateTotal"></el-checkbox>
          </div>
          <div class="item-image" @click="goToDetail">
            <img :src="item.productImage" :alt="item.productName" />
          </div>
          <div class="item-info">
            <h3 class="item-name">{{ item.productName }}</h3>
            <p class="item-spec">{{ item.productDescription }}</p>
            <div class="item-bottom">
              <span class="item-price">¥{{ item.productPrice }}</span>
              <div class="quantity-control">
                <el-button size="small" circle @click="decreaseQuantity(item)">-</el-button>
                <span class="quantity">{{ item.quantity }}</span>
                <el-button size="small" circle @click="increaseQuantity(item)">+</el-button>
              </div>
            </div>
          </div>
          <div class="item-delete" @click="deleteItem(item)">
            <el-icon><Delete /></el-icon>
          </div>
        </div>
      </div>
    </div>

    <div class="cart-empty" v-else>
      <div class="empty-icon">
        <el-icon><ShoppingCart /></el-icon>
      </div>
      <p class="empty-text">购物车空空如也</p>
      <el-button type="primary" class="go-shopping-btn" @click="navigateTo('/mobile')">
        去逛逛
      </el-button>
    </div>

    <div class="cart-footer" v-if="cartItems.length > 0">
      <div class="footer-left">
        <el-checkbox v-model="selectAll" @change="toggleSelectAll">
          全选
        </el-checkbox>
      </div>
      <div class="footer-right">
        <div class="total-price">
          合计：<span class="price">{{ totalPrice }}</span>
        </div>
        <el-button type="primary" class="checkout-btn" @click="handleCheckout">
          结算({{ selectedCount }})
        </el-button>
      </div>
    </div>

    <footer class="mobile-footer">
      <div class="footer-item" @click="navigateTo('/mobile')">
        <el-icon class="footer-icon"><House /></el-icon>
        <span class="footer-text">首页</span>
      </div>
      <div class="footer-item" @click="navigateTo('/mobile/category')">
        <el-icon class="footer-icon"><Goods /></el-icon>
        <span class="footer-text">分类</span>
      </div>
      <div class="footer-item active" @click="navigateTo('/mobile/cart')">
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
import axios from 'axios'
import { House, Goods, ShoppingCart, UserFilled, Delete } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'MobileCart',
  components: {
    House,
    Goods,
    ShoppingCart,
    UserFilled,
    Delete
  },
  data() {
    return {
      cartItems: [],
      loading: false
    }
  },
  computed: {
    selectAll: {
      get() {
        return this.cartItems.length > 0 && this.cartItems.every(item => item.checked)
      },
      set(value) {
        this.cartItems.forEach(item => {
          item.checked = value
        })
      }
    },
    selectedCount() {
      return this.cartItems.filter(item => item.checked).length
    },
    totalPrice() {
      let total = 0
      this.cartItems.forEach(item => {
        if (item.checked) {
          total += parseFloat(item.productPrice) * item.quantity
        }
      })
      return '¥' + total.toFixed(2)
    }
  },
  mounted() {
    this.loadCart()
  },
  methods: {
    navigateTo(path) {
      this.$router.push(path)
    },
    goToDetail() {
      this.$router.push('/seckill-detail')
    },
    async loadCart() {
      try {
        this.loading = true
        const token = localStorage.getItem('token')
        const response = await axios.get('/api/cart', {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        if (response.data.code === 200) {
          this.cartItems = response.data.data || []
        }
      } catch (error) {
        console.error('加载购物车失败:', error)
        ElMessage.error('加载购物车失败')
      } finally {
        this.loading = false
      }
    },
    async increaseQuantity(item) {
      try {
        const token = localStorage.getItem('token')
        await axios.put(`/api/cart/${item.id}`, {
          quantity: item.quantity + 1
        }, {
          headers: {
            'Authorization': `Bearer ${token}`
          }
        })
        item.quantity++
        this.updateTotal()
      } catch (error) {
        console.error('更新数量失败:', error)
        ElMessage.error('更新失败')
      }
    },
    async decreaseQuantity(item) {
      if (item.quantity > 1) {
        try {
          const token = localStorage.getItem('token')
          await axios.put(`/api/cart/${item.id}`, {
            quantity: item.quantity - 1
          }, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          })
          item.quantity--
          this.updateTotal()
        } catch (error) {
          console.error('更新数量失败:', error)
          ElMessage.error('更新失败')
        }
      }
    },
    async deleteItem(item) {
      ElMessageBox.confirm('确定要删除这个商品吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(async () => {
        try {
          const token = localStorage.getItem('token')
          await axios.delete(`/api/cart/${item.id}`, {
            headers: {
              'Authorization': `Bearer ${token}`
            }
          })
          this.cartItems = this.cartItems.filter(i => i.id !== item.id)
          ElMessage.success('删除成功')
        } catch (error) {
          console.error('删除失败:', error)
          ElMessage.error('删除失败')
        }
      }).catch(() => {})
    },
    toggleSelectAll() {
      this.cartItems.forEach(item => {
        item.checked = this.selectAll
      })
    },
    updateTotal() {
    },
    async handleCheckout() {
      if (this.selectedCount === 0) {
        ElMessage.warning('请选择要结算的商品')
        return
      }
      try {
        const token = localStorage.getItem('token')
        const response = await axios.post('/api/orders/create', {}, {
          headers: { 'Authorization': `Bearer ${token}` }
        })
        if (response.data.code === 200) {
          const data = response.data.data
          const orderData = JSON.stringify({
            orderNo: data.orderNo,
            items: this.cartItems.filter(i => i.checked),
            totalAmount: data.totalAmount,
            totalCount: data.totalCount
          })
          this.$router.push({
            path: '/mobile/pay-success',
            query: { orderData }
          })
          this.cartItems = []
          ElMessage.success('支付成功！')
        } else {
          ElMessage.error(response.data.message || '创建订单失败')
        }
      } catch (error) {
        console.error('结算失败:', error)
        ElMessage.error('结算失败')
      }
    }
  }
}
</script>

<style scoped>
.mobile-container {
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
  padding-bottom: 120px;
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

.cart-content {
  padding: 16px;
}

.cart-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.cart-item {
  display: flex;
  align-items: center;
  background: white;
  border-radius: 12px;
  padding: 12px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.item-check {
  margin-right: 12px;
}

.item-image {
  width: 80px;
  height: 80px;
  border-radius: 8px;
  overflow: hidden;
  margin-right: 12px;
  flex-shrink: 0;
}

.item-image img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.item-info {
  flex: 1;
  min-width: 0;
}

.item-name {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  margin: 0 0 4px 0;
  display: -webkit-box;
  -webkit-line-clamp: 2;
  -webkit-box-orient: vertical;
  overflow: hidden;
}

.item-spec {
  font-size: 12px;
  color: #999;
  margin: 0 0 8px 0;
}

.item-bottom {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.item-price {
  font-size: 16px;
  font-weight: 700;
  color: #ff6b6b;
}

.quantity-control {
  display: flex;
  align-items: center;
  gap: 8px;
}

.quantity {
  font-size: 14px;
  font-weight: 500;
  color: #333;
  min-width: 24px;
  text-align: center;
}

.item-delete {
  margin-left: 12px;
  color: #999;
  cursor: pointer;
  font-size: 18px;
}

.item-delete:hover {
  color: #ff6b6b;
}

.cart-empty {
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  padding: 80px 20px;
}

.empty-icon {
  font-size: 80px;
  color: #ddd;
  margin-bottom: 16px;
}

.empty-text {
  font-size: 14px;
  color: #999;
  margin: 0 0 24px 0;
}

.go-shopping-btn {
  border-radius: 20px;
  padding: 10px 32px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border: none;
}

.cart-footer {
  position: fixed;
  bottom: 60px;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 12px 20px;
  background: white;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 99;
}

.footer-left {
  display: flex;
  align-items: center;
}

.footer-right {
  display: flex;
  align-items: center;
  gap: 12px;
}

.total-price {
  font-size: 14px;
  color: #333;
}

.total-price .price {
  font-size: 20px;
  font-weight: 700;
  color: #ff6b6b;
}

.checkout-btn {
  border-radius: 20px;
  padding: 10px 24px;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border: none;
  font-weight: 500;
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
