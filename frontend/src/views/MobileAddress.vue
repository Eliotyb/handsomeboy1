<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">收货地址</h1>
      <div class="header-right">
        <el-button type="primary" size="small" @click="openAddDialog">新增</el-button>
      </div>
    </header>

    <div class="address-content">
      <div v-if="addressList.length === 0" class="empty-state">
        <el-icon class="empty-icon"><Location /></el-icon>
        <p>暂无收货地址</p>
        <el-button type="primary" @click="openAddDialog">添加收货地址</el-button>
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

    <el-dialog v-model="showAddDialog" :title="editingAddress ? '编辑地址' : '新增地址'" width="95%" top="5vh">
      <el-form :model="addressForm" label-position="top">
        <el-form-item label="收货人">
          <el-input v-model="addressForm.name" placeholder="请输入姓名" />
        </el-form-item>
        <el-form-item label="手机号">
          <el-input v-model="addressForm.phone" placeholder="请输入手机号" />
        </el-form-item>
        <el-form-item label="所在地区">
          <el-input v-model="addressForm.area" placeholder="点击下方按钮在地图上选择位置" readonly>
            <template #append>
              <div class="location-btns-group">
                <el-button type="primary" @click="getCurrentLocation" :loading="isLocating" class="gps-btn">
                  🎯 {{ isLocating ? '定位中' : 'GPS' }}
                </el-button>
                <el-button type="warning" @click="showMapPicker = true">📍 地图</el-button>
              </div>
            </template>
          </el-input>
        </el-form-item>
        <el-form-item label="详细地址">
          <el-input v-model="addressForm.detail" type="textarea" placeholder="从地图选择后自动填充，也可手动补充（如门牌号）" />
        </el-form-item>
        <el-form-item>
          <el-checkbox v-model="addressForm.isDefault">设为默认地址</el-checkbox>
        </el-form-item>
      </el-form>

      <div v-if="showMapPicker" class="map-picker-container">
        <div class="map-header">
          <span class="map-title">🗺️ 点击地图选择位置</span>
          <el-button type="danger" size="small" @click="closeMapPicker">关闭地图</el-button>
        </div>
        <div id="mapContainer" class="map-container"></div>
        <div v-if="selectedLocation.address" class="selected-address-info">
          <div class="address-label">已选择地址：</div>
          <div class="address-text">{{ selectedLocation.address }}</div>
        </div>
        <div v-else-if="isLoadingAddress" class="loading-hint">
          <el-icon class="is-loading"><Loading /></el-icon>
          <span>正在获取地址信息...</span>
        </div>
      </div>

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
import { ArrowLeft, Location, House, Goods, ShoppingCart, UserFilled, Loading } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'
import L from 'leaflet'
import axios from 'axios'
import 'leaflet/dist/leaflet.css'

const AMAP_KEY = import.meta.env.VITE_AMAP_KEY || ''

export default {
  name: 'MobileAddress',
  components: {
    ArrowLeft,
    Location,
    House,
    Goods,
    ShoppingCart,
    UserFilled,
    Loading
  },
  data() {
    return {
      showAddDialog: false,
      showMapPicker: false,
      isLocating: false,
      editingAddress: null,
      editingIndex: -1,
      map: null,
      marker: null,
      isLoadingAddress: false,
      selectedLocation: {
        lng: null,
        lat: null,
        address: '',
        province: '',
        city: '',
        district: '',
        detail: ''
      },
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
  watch: {
    showMapPicker(newVal) {
      if (newVal) {
        this.$nextTick(() => {
          setTimeout(() => {
            this.initMap()
          }, 100)
        })
      } else {
        this.destroyMap()
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
    openAddDialog() {
      this.editingAddress = null
      this.editingIndex = -1
      this.addressForm = {
        name: '',
        phone: '',
        area: '',
        detail: '',
        isDefault: false
      }
      this.selectedLocation = {
        lng: null,
        lat: null,
        address: '',
        province: '',
        city: '',
        district: '',
        detail: ''
      }
      this.showMapPicker = false
      this.showAddDialog = true
    },
    getCurrentLocation() {
      if (!navigator.geolocation) {
        ElMessage.error('❌ 您的浏览器不支持定位功能')
        return
      }

      this.isLocating = true

      navigator.geolocation.getCurrentPosition(
        async (position) => {
          const { latitude, longitude } = position.coords
          
          this.selectedLocation.lng = longitude
          this.selectedLocation.lat = latitude

          if (!this.showMapPicker) {
            this.showMapPicker = true
          }

          this.$nextTick(() => {
            setTimeout(() => {
              if (this.map) {
                this.map.setView([latitude, longitude], 17)
                this.handleMapClick({ lng: longitude, lat: latitude })
              } else {
                this.initMapWithLocation(latitude, longitude)
              }
            }, 300)
          })

          ElMessage.success(`🎯 定位成功！纬度: ${latitude.toFixed(6)}, 经度: ${longitude.toFixed(6)}`)
        },
        (error) => {
          console.error('定位错误:', error)
          switch (error.code) {
            case error.PERMISSION_DENIED:
              ElMessage.error('❌ 用户拒绝了定位请求')
              break
            case error.POSITION_UNAVAILABLE:
              ElMessage.error('❌ 无法获取位置信息')
              break
            case error.TIMEOUT:
              ElMessage.error('❌ 定位请求超时')
              break
            default:
              ElMessage.error('❌ 定位失败，请手动选择位置')
          }
          this.isLocating = false
        },
        {
          enableHighAccuracy: true,
          timeout: 10000,
          maximumAge: 0
        }
      )
    },
    initMapWithLocation(lat, lng) {
      const container = document.getElementById('mapContainer')
      if (!container || this.map) return

      this.map = L.map('mapContainer', {
        center: [lat, lng],
        zoom: 17,
        zoomControl: true
      })

      L.tileLayer('https://webrd0{s}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}', {
        subdomains: ['1', '2', '3', '4'],
        maxZoom: 18,
        attribution: '&copy; 高德地图'
      }).addTo(this.map)

      this.map.on('click', (e) => {
        this.handleMapClick(e.latlng)
      })

      setTimeout(() => {
        this.map.invalidateSize()
        this.handleMapClick({ lng, lat })
      }, 200)
    },
    initMap() {
      const container = document.getElementById('mapContainer')
      if (!container || this.map) return

      this.map = L.map('mapContainer', {
        center: [39.90923, 116.397428],
        zoom: 15,
        zoomControl: true
      })

      L.tileLayer('https://webrd0{s}.is.autonavi.com/appmaptile?lang=zh_cn&size=1&scale=1&style=8&x={x}&y={y}&z={z}', {
        subdomains: ['1', '2', '3', '4'],
        maxZoom: 18,
        attribution: '&copy; 高德地图'
      }).addTo(this.map)

      this.map.on('click', (e) => {
        this.handleMapClick(e.latlng)
      })

      setTimeout(() => {
        this.map.invalidateSize()
      }, 200)
    },
    handleMapClick(latlng) {
      const { lng, lat } = latlng

      if (this.marker) {
        this.marker.setLatLng([lat, lng])
      } else {
        const icon = L.divIcon({
          className: 'custom-marker',
          html: '<div style="background: #ff6b6b; width: 24px; height: 24px; border-radius: 50%; border: 3px solid white; box-shadow: 0 2px 6px rgba(0,0,0,0.3);"></div>',
          iconSize: [24, 24],
          iconAnchor: [12, 12]
        })

        this.marker = L.marker([lat, lng], { icon: icon }).addTo(this.map)
      }

      this.selectedLocation.lng = lng
      this.selectedLocation.lat = lat

      this.reverseGeocode(lng, lat)
    },
    async reverseGeocode(lng, lat) {
      this.isLoadingAddress = true

      try {
        const response = await axios.get('https://restapi.amap.com/v3/geocode/regeo', {
          params: {
            key: AMAP_KEY,
            location: `${lng},${lat}`,
            extensions: 'all',
            output: 'JSON'
          }
        })

        if (response.data.status === '1' && response.data.regeocode) {
          const regeocode = response.data.regeocode
          const addressComponent = regeocode.addressComponent

          this.selectedLocation.address = regeocode.formatted_address
          this.selectedLocation.province = addressComponent.province || ''
          this.selectedLocation.city = addressComponent.city || ''
          this.selectedLocation.district = addressComponent.district || ''

          let detailAddress = ''
          if (addressComponent.streetNumber && addressComponent.streetNumber.street) {
            detailAddress = addressComponent.streetNumber.street
            if (addressComponent.streetNumber.number) {
              detailAddress += addressComponent.streetNumber.number
            }
          }

          if (!detailAddress && regeocode.aois && regeocode.aois.length > 0) {
            detailAddress = regeocode.aois[0].name
          }

          if (!detailAddress && regeocode.pois && regeocode.pois.length > 0) {
            detailAddress = regeocode.pois[0].name
          }

          this.selectedLocation.detail = detailAddress
          this.addressForm.area = `${this.selectedLocation.province}${this.selectedLocation.city}${this.selectedLocation.district}`
          this.addressForm.detail = detailAddress

          ElMessage.success('✅ 地址获取成功！')
        } else {
          ElMessage.warning('⚠️ 地址解析失败，请重新选择位置')
        }
      } catch (error) {
        console.error('逆地理编码错误:', error)
        ElMessage.error('❌ 地址解析失败，请检查网络连接')
      } finally {
        this.isLoadingAddress = false
        this.isLocating = false
      }
    },
    closeMapPicker() {
      this.showMapPicker = false
    },
    destroyMap() {
      if (this.map) {
        this.map.remove()
        this.map = null
        this.marker = null
      }
    },
    editAddress(address) {
      this.editingAddress = address
      this.editingIndex = this.addressList.indexOf(address)
      this.addressForm = { ...address }
      this.showMapPicker = false
      this.showAddDialog = true

      if (address.lng && address.lat) {
        this.selectedLocation = {
          lng: address.lng,
          lat: address.lat,
          address: address.province + address.city + address.district + address.detail,
          province: address.province,
          city: address.city,
          district: address.district,
          detail: address.detail
        }
      }
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
        ElMessage.warning('请填写完整信息，建议使用地图选点功能')
        return
      }

      const addressData = {
        ...this.addressForm,
        lng: this.selectedLocation.lng,
        lat: this.selectedLocation.lat
      }

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
      this.showMapPicker = false
      this.editingAddress = null
      this.editingIndex = -1
      this.addressForm = {
        name: '',
        phone: '',
        area: '',
        detail: '',
        isDefault: false
      }
      this.selectedLocation = {
        lng: null,
        lat: null,
        address: '',
        province: '',
        city: '',
        district: '',
        detail: ''
      }
      this.destroyMap()
    }
  },
  beforeUnmount() {
    this.destroyMap()
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

.location-btns-group {
  display: flex;
  gap: 4px;
}

.gps-btn {
  font-size: 12px;
  padding: 6px 10px !important;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
}

.gps-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.map-picker-container {
  margin-top: 16px;
  border: 2px solid #e4e7ed;
  border-radius: 8px;
  overflow: hidden;
}

.map-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
  padding: 10px 12px;
  background: #f5f7fa;
  border-bottom: 1px solid #e4e7ed;
}

.map-title {
  font-size: 14px;
  font-weight: 600;
  color: #409eff;
}

.map-container {
  width: 100%;
  height: 350px;
  background: #f0f0f0;
  z-index: 1;
}

.selected-address-info {
  padding: 12px;
  background: #ecf5ff;
  border-top: 1px solid #d9ecff;
}

.address-label {
  font-size: 13px;
  color: #409eff;
  font-weight: 600;
  margin-bottom: 6px;
}

.address-text {
  font-size: 14px;
  color: #303133;
  line-height: 1.6;
  margin: 0;
  word-break: break-all;
}

.loading-hint {
  padding: 12px;
  background: #f5f7fa;
  border-top: 1px solid #e4e7ed;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #909399;
  font-size: 14px;
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
