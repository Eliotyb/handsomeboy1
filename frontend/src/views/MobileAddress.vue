<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">收货地址</h1>
      <div class="header-right">
        <el-button type="primary" size="small" @click="showAddDialog = true">新增</el-button>
      </div>
    </header>

    <div class="address-content">
      <div v-if="addressList.length === 0" class="empty-state">
        <el-icon class="empty-icon"><Location /></el-icon>
        <p>暂无收货地址</p>
        <el-button type="primary" @click="showAddDialog = true">添加收货地址</el-button>
      </div>

      <div v-else class="address-list">
        <div class="address-item" v-for="(address, index) in addressList" :key="index"
             :class="{ 'default-address': address.isDefault }">
          <div class="address-info">
            <div class="address-header">
              <span class="contact-name">{{ address.name }}</span>
              <span class="contact-phone">{{ address.phone }}</span>
              <el-tag v-if="address.isDefault" type="danger" size="small" effect="dark">默认</el-tag>
            </div>
            <p class="address-detail">{{ address.province }}{{ address.city }}{{ address.district }}{{ address.detail }}</p>
          </div>
          <div class="address-actions">
            <el-button text type="primary" @click="editAddress(address)">编辑</el-button>
            <el-button text type="danger" @click="deleteAddress(index)">删除</el-button>
            <el-button v-if="!address.isDefault" text @click="setDefault(index)">设为默认</el-button>
          </div>
        </div>
      </div>
    </div>

    <el-dialog v-model="showAddDialog" :title="editingAddress ? '编辑地址' : '新增地址'" width="90%">
      <el-form :model="addressForm" label-position="top">
        <el-form-item label="收货人">
          <el-input v-model="addressForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addressForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="所在地区">
          <el-input v-model="addressForm.area" placeholder="省/市/区" />
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="addressForm.detail" type="textarea" placeholder="请输入详细地址" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="addressForm.isDefault">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="cancelEdit">取消</el-button>
        <el-button type="primary" @click="saveAddress">保存</el-button>
      </template>
    </el-dialog>

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
import { ArrowLeft, Location, House, Goods, ShoppingCart, UserFilled } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileAddress',
  components: {
    ArrowLeft,
    Location,
    House,
    Goods,
    ShoppingCart,
    UserFilled
  },
  data() {
    return {
      showAddDialog: false,
      editingAddress: null,
      editingIndex: -1,
      addressList: [],
      addressForm: {
        name: '',
        phone: '',
        area: '',
        detail: '',
        isDefault: false
      }
    }
  },
  created() {
    this.loadAddresses()
  },
  methods: {
    loadAddresses() {
      const saved = localStorage.getItem('userAddresses')
      if (saved) {
        this.addressList = JSON.parse(saved)
      }
    },
    saveAddresses() {
      localStorage.setItem('userAddresses', JSON.stringify(this.addressList))
    },
    editAddress(address) {
      this.editingAddress = address
      this.editingIndex = this.addressList.indexOf(address)
      this.addressForm = { ...address }
      this.showAddDialog = true
    },
    deleteAddress(index) {
      this.$confirm('确定要删除这个地址吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        this.addressList.splice(index, 1)
        this.saveAddresses()
        ElMessage.success('删除成功')
      }).catch(() => {})
    },
    setDefault(index) {
      this.addressList.forEach((addr, i) => {
        addr.isDefault = i === index
      })
      this.saveAddresses()
      ElMessage.success('已设为默认地址')
    },
    saveAddress() {
      if (!this.addressForm.name || !this.addressForm.phone || !this.addressForm.detail) {
        ElMessage.warning('请填写完整信息')
        return
      }

      const addressData = { ...this.addressForm }

      if (this.editingAddress) {
        this.addressList[this.editingIndex] = addressData
        ElMessage.success('修改成功')
      } else {
        if (addressData.isDefault) {
          this.addressList.forEach(addr => addr.isDefault = false)
        }
        this.addressList.push(addressData)
        ElMessage.success('添加成功')
      }

      this.saveAddresses()
      this.cancelEdit()
    },
    cancelEdit() {
      this.showAddDialog = false
      this.editingAddress = null
      this.editingIndex = -1
      this.addressForm = {
        name: '',
        phone: '',
        area: '',
        detail: '',
        isDefault: false
      }
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
  width: 60px;
  text-align: right;
}

.address-content {
  padding: 16px;
}

.empty-state {
  background: white;
  border-radius: 12px;
  padding: 60px 20px;
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

.address-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.address-item {
  background: white;
  border-radius: 12px;
  padding: 16px;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.address-item.default-address {
  border-color: #ff6b6b;
  background: linear-gradient(to right, #fff5f5, white);
}

.address-info {
  margin-bottom: 12px;
}

.address-header {
  display: flex;
  align-items: center;
  gap: 10px;
  margin-bottom: 8px;
}

.contact-name {
  font-size: 16px;
  font-weight: 600;
  color: #333;
}

.contact-phone {
  font-size: 14px;
  color: #666;
}

.address-detail {
  font-size: 13px;
  color: #666;
  line-height: 1.5;
  margin: 0;
}

.address-actions {
  display: flex;
  gap: 8px;
  padding-top: 12px;
  border-top: 1px solid #f0f0f0;
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
