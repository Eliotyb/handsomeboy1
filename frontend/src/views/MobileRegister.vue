<template>
  <div class="mobile-register-container">
    <div class="register-content">
      <div class="back-btn" @click="goBack">
        <el-icon><ArrowLeft /></el-icon>
      </div>

      <div class="header-section">
        <div class="brand-tag">
          <el-icon class="brand-icon"><Lightning /></el-icon>
          <span>PULSE VELOCITY</span>
        </div>
        <h1 class="page-title">加入<br/>Pulse Velocity</h1>
        <p class="page-desc">体验极致秒杀，社群速度见证精彩时刻。</p>
      </div>

      <div class="form-section">
        <el-form :model="registerForm" :rules="rules" ref="registerFormRef" class="register-form">
          <div class="form-item-wrapper">
            <div class="form-label">
              <span class="label-text">用户名 (USERNAME)</span>
            </div>
            <div class="input-wrapper">
              <el-icon class="input-icon"><User /></el-icon>
              <el-input
                v-model="registerForm.username"
                placeholder="输入用户名"
                class="custom-input"
              />
            </div>
          </div>

          <div class="form-item-wrapper">
            <div class="form-label">
              <span class="label-text">密码 (PASSWORD)</span>
            </div>
            <div class="input-wrapper">
              <el-icon class="input-icon"><Lock /></el-icon>
              <el-input
                v-model="registerForm.password"
                :type="showPassword ? 'text' : 'password'"
                placeholder="至少8位字符"
                class="custom-input"
              >
                <template #suffix>
                  <el-icon class="eye-icon" @click="showPassword = !showPassword">
                    <View v-if="!showPassword" />
                    <Hide v-else />
                  </el-icon>
                </template>
              </el-input>
            </div>
          </div>

          <div class="form-item-wrapper">
            <div class="form-label">
              <span class="label-text">姓名 (NAME)</span>
            </div>
            <div class="input-wrapper">
              <el-icon class="input-icon"><User /></el-icon>
              <el-input
                v-model="registerForm.name"
                placeholder="真实姓名"
                class="custom-input"
              />
            </div>
          </div>

          <div class="form-item-wrapper">
            <div class="form-label">
              <span class="label-text">手机号 (PHONE)</span>
            </div>
            <div class="input-wrapper">
              <el-icon class="input-icon"><Iphone /></el-icon>
              <el-input
                v-model="registerForm.phone"
                placeholder="输入手机号"
                class="custom-input"
              />
            </div>
          </div>

          <div class="form-item-wrapper">
            <div class="form-label">
              <span class="label-text">收货地址 (ADDRESS)</span>
              <div class="address-actions-btns">
                <el-button type="primary" size="small" class="location-btn" @click="getCurrentLocation" :loading="isLocating">
                  🎯 {{ isLocating ? '定位中...' : '获取位置' }}
                </el-button>
                <el-button type="warning" size="small" class="map-picker-btn" @click="toggleMapPicker">
                  {{ showMapPicker ? '关闭地图' : '📍 地图选点' }}
                </el-button>
              </div>
            </div>
            
            <div v-if="showMapPicker" class="map-container-wrapper">
              <div id="registerMapContainer" class="map-display"></div>
              <div v-if="selectedLocation.address" class="selected-address-box">
                <div class="address-tag">已选择：</div>
                <div class="address-content">{{ selectedLocation.address }}</div>
              </div>
              <div v-else-if="isLoadingAddress" class="loading-box">
                <el-icon class="is-loading"><Loading /></el-icon>
                <span>正在获取地址...</span>
              </div>
              <div v-else class="map-hint">
                <el-icon><Pointer /></el-icon>
                <span>点击地图选择位置</span>
              </div>
            </div>

            <div class="input-wrapper">
              <el-icon class="input-icon"><Location /></el-icon>
              <el-input
                v-model="registerForm.address"
                type="textarea"
                :rows="2"
                placeholder="输入您的详细收货地址（可从地图选择或手动输入）"
                class="custom-textarea"
              />
            </div>
          </div>

          <div class="agreement-wrapper">
            <el-checkbox v-model="agreeTerms" class="agreement-checkbox">
              我已阅读并同意
              <span class="agreement-link">《用户服务协议》</span>
              与
              <span class="agreement-link">《隐私权政策》</span>
            </el-checkbox>
          </div>

          <el-button 
            type="primary" 
            class="register-btn" 
            @click="handleRegister" 
            :loading="loading"
            :disabled="!agreeTerms"
          >
            立即注册 (REGISTER NOW)
          </el-button>
        </el-form>

        <div class="login-link-section">
          <p class="login-text">已有账号？</p>
          <span class="login-link" @click="goToLogin">立即登录</span>
        </div>
      </div>
    </div>

    <div class="divider-decoration">
      <div class="divider-line"></div>
      <el-icon class="divider-icon"><Lightning /></el-icon>
      <div class="divider-line"></div>
    </div>
  </div>
</template>

<script>
import axios from 'axios'
import { ElMessage } from 'element-plus'
import { 
  Lightning, User, Lock, View, Hide, Iphone, Location, ArrowLeft, Loading, Pointer
} from '@element-plus/icons-vue'
import L from 'leaflet'
import 'leaflet/dist/leaflet.css'

const AMAP_KEY = import.meta.env.VITE_AMAP_KEY || ''

export default {
  name: 'MobileRegister',
  components: {
    Lightning,
    User,
    Lock,
    View,
    Hide,
    Iphone,
    Location,
    ArrowLeft,
    Loading,
    Pointer
  },
  data() {
    return {
      registerForm: {
        username: '',
        password: '',
        name: '',
        phone: '',
        address: ''
      },
      rules: {
        username: [{ required: true, message: '请输入用户名', trigger: 'blur' }],
        password: [
          { required: true, message: '请输入密码', trigger: 'blur' },
          { min: 8, message: '密码至少8位字符', trigger: 'blur' }
        ],
        name: [{ required: true, message: '请输入姓名', trigger: 'blur' }],
        phone: [{ required: true, message: '请输入手机号', trigger: 'blur' }],
        address: [{ required: true, message: '请输入收货地址', trigger: 'blur' }]
      },
      loading: false,
      showPassword: false,
      agreeTerms: false,
      showMapPicker: false,
      isLocating: false,
      map: null,
      marker: null,
      isLoadingAddress: false,
      selectedLocation: {
        lng: null,
        lat: null,
        address: ''
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
  methods: {
    toggleMapPicker() {
      this.showMapPicker = !this.showMapPicker
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
      const container = document.getElementById('registerMapContainer')
      if (!container || this.map) return

      this.map = L.map('registerMapContainer', {
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
      const container = document.getElementById('registerMapContainer')
      if (!container || this.map) return

      this.map = L.map('registerMapContainer', {
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
          html: '<div style="background: #ff4500; width: 28px; height: 28px; border-radius: 50%; border: 3px solid white; box-shadow: 0 3px 8px rgba(255,69,0,0.4); animation: pulse 1.5s infinite;"></div>',
          iconSize: [28, 28],
          iconAnchor: [14, 14]
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
          
          this.selectedLocation.address = regeocode.formatted_address
          this.registerForm.address = regeocode.formatted_address

          ElMessage.success('✅ 地址获取成功！')
        } else {
          ElMessage.warning('⚠️ 地址解析失败，请重新选择')
        }
      } catch (error) {
        console.error('逆地理编码错误:', error)
        ElMessage.error('❌ 地址解析失败')
      } finally {
        this.isLoadingAddress = false
        this.isLocating = false
      }
    },
    destroyMap() {
      if (this.map) {
        this.map.remove()
        this.map = null
        this.marker = null
      }
    },
    handleRegister() {
      this.$refs.registerFormRef.validate((valid) => {
        if (valid) {
          if (!this.agreeTerms) {
            ElMessage.warning('请先同意用户服务协议和隐私权政策')
            return
          }
          
          this.loading = true
          axios.post('/api/auth/register', this.registerForm)
            .then(response => {
              const res = response.data
              if (res.code === 200) {
                ElMessage.success('注册成功')
                this.$router.push('/mobile/login')
              } else {
                ElMessage.error(res.message || '注册失败')
              }
            })
            .catch(error => {
              console.error('注册失败:', error)
              ElMessage.error('注册失败，请检查网络连接')
            })
            .finally(() => {
              this.loading = false
            })
        }
      })
    },
    goBack() {
      this.$router.go(-1)
    },
    goToLogin() {
      this.$router.push('/mobile/login')
    }
  },
  beforeUnmount() {
    this.destroyMap()
  }
}
</script>

<style scoped>
.mobile-register-container {
  min-height: 100vh;
  background: linear-gradient(180deg, #fff5f5 0%, #ffffff 100%);
  position: relative;
  overflow: hidden;
  padding: 20px;
}

.register-content {
  position: relative;
  z-index: 10;
  max-width: 400px;
  margin: 0 auto;
}

.back-btn {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  background: white;
  display: flex;
  align-items: center;
  justify-content: center;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.08);
  margin-bottom: 24px;
  cursor: pointer;
  transition: all 0.3s;
}

.back-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 16px rgba(0, 0, 0, 0.12);
}

.header-section {
  margin-bottom: 40px;
}

.brand-tag {
  display: inline-flex;
  align-items: center;
  gap: 6px;
  background: linear-gradient(135deg, #fff5f5 0%, #ffe4e6 100%);
  padding: 6px 14px;
  border-radius: 20px;
  margin-bottom: 20px;
}

.brand-icon {
  color: #ff4500;
  font-size: 16px;
}

.brand-tag span {
  font-size: 11px;
  font-weight: 600;
  color: #dc2626;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.page-title {
  font-size: 40px;
  font-weight: 800;
  color: #4a1c00;
  margin: 0 0 12px;
  line-height: 1.1;
  letter-spacing: -1px;
}

.page-desc {
  font-size: 14px;
  color: #8d6e63;
  margin: 0;
  line-height: 1.6;
}

.form-section {
  background: rgba(255, 255, 255, 0.9);
  backdrop-filter: blur(20px);
  border-radius: 40px;
  padding: 40px 30px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  box-shadow: 0 20px 60px rgba(255, 107, 53, 0.1);
}

.register-form {
  margin-bottom: 24px;
}

.form-item-wrapper {
  margin-bottom: 20px;
}

.form-label {
  margin-bottom: 10px;
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.label-text {
  font-size: 12px;
  font-weight: 600;
  color: #1e40af;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.map-picker-btn {
  font-size: 11px;
  padding: 4px 12px;
  border-radius: 16px;
  background: linear-gradient(135deg, #ff4500 0%, #ff6b00 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(255, 69, 0, 0.3);
  transition: all 0.3s;
}

.map-picker-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(255, 69, 0, 0.4);
}

.address-actions-btns {
  display: flex;
  gap: 6px;
}

.location-btn {
  font-size: 11px;
  padding: 4px 12px;
  border-radius: 16px;
  background: linear-gradient(135deg, #3b82f6 0%, #1d4ed8 100%);
  border: none;
  box-shadow: 0 2px 8px rgba(59, 130, 246, 0.3);
  transition: all 0.3s;
}

.location-btn:hover {
  transform: translateY(-1px);
  box-shadow: 0 4px 12px rgba(59, 130, 246, 0.4);
}

.input-wrapper {
  position: relative;
  display: flex;
  align-items: center;
  background: #fff5f5;
  border-radius: 24px;
  padding: 4px 8px 4px 18px;
  border: 2px solid transparent;
  transition: all 0.3s;
}

.input-wrapper:focus-within {
  border-color: #ff6b35;
  background: white;
  box-shadow: 0 4px 12px rgba(255, 107, 53, 0.15);
}

.input-icon {
  color: #9ca3af;
  font-size: 18px;
  margin-right: 10px;
}

.custom-input {
  flex: 1;
}

.custom-input :deep(.el-input__wrapper) {
  box-shadow: none !important;
  padding: 0;
  background: transparent;
}

.custom-input :deep(.el-input__inner) {
  font-size: 15px;
  color: #374151;
}

.custom-textarea {
  flex: 1;
}

.custom-textarea :deep(.el-textarea__inner) {
  box-shadow: none !important;
  padding: 10px 0;
  background: transparent;
  border: none;
  font-size: 15px;
  color: #374151;
  resize: none;
}

.eye-icon {
  color: #9ca3af;
  font-size: 18px;
  cursor: pointer;
}

.map-container-wrapper {
  margin-bottom: 16px;
  border-radius: 20px;
  overflow: hidden;
  border: 2px solid #fecaca;
  background: white;
  box-shadow: 0 4px 16px rgba(255, 69, 0, 0.1);
}

.map-display {
  width: 100%;
  height: 280px;
  background: #f0f0f0;
  z-index: 1;
}

.selected-address-box {
  padding: 12px 16px;
  background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%);
  border-top: 2px solid #fbbf24;
  display: flex;
  align-items: flex-start;
  gap: 8px;
}

.address-tag {
  font-size: 12px;
  font-weight: 700;
  color: #92400e;
  white-space: nowrap;
  padding-top: 2px;
}

.address-content {
  font-size: 13px;
  color: #78350f;
  line-height: 1.5;
  word-break: break-all;
}

.loading-box {
  padding: 12px;
  background: #f0f9ff;
  border-top: 2px solid #bae6fd;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #0369a1;
  font-size: 13px;
}

.map-hint {
  padding: 12px;
  background: #f0fdf4;
  border-top: 2px solid #bbf7d0;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  color: #166534;
  font-size: 13px;
  font-weight: 500;
}

.agreement-wrapper {
  margin-bottom: 24px;
}

.agreement-checkbox :deep(.el-checkbox__label) {
  font-size: 13px;
  color: #4b5563;
  line-height: 1.5;
}

.agreement-link {
  color: #1e40af;
  cursor: pointer;
  text-decoration: underline;
}

.register-btn {
  width: 100%;
  height: 60px;
  border-radius: 50px;
  background: linear-gradient(135deg, #ff4500 0%, #ff6b00 100%);
  border: none;
  font-size: 16px;
  font-weight: 700;
  color: white;
  text-transform: uppercase;
  letter-spacing: 0.5px;
  box-shadow: 0 8px 24px rgba(255, 69, 0, 0.35);
  transition: all 0.3s;
}

.register-btn:hover:not(:disabled) {
  transform: translateY(-2px);
  box-shadow: 0 12px 32px rgba(255, 69, 0, 0.45);
}

.register-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
}

.login-link-section {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  padding-top: 20px;
  border-top: 1px solid #f3f4f6;
}

.login-text {
  font-size: 14px;
  color: #4b5563;
  margin: 0;
}

.login-link {
  font-size: 14px;
  color: #1e40af;
  cursor: pointer;
  font-weight: 600;
  text-decoration: underline;
}

.divider-decoration {
  position: absolute;
  bottom: 40px;
  left: 50%;
  transform: translateX(-50%);
  display: flex;
  align-items: center;
  gap: 16px;
  width: 80%;
  max-width: 300px;
  z-index: 5;
}

.divider-line {
  flex: 1;
  height: 1px;
  background: linear-gradient(90deg, transparent 0%, #e5e7eb 50%, transparent 100%);
}

.divider-icon {
  color: #d1d5db;
  font-size: 20px;
}
</style>
