<template>
  <div class="seckill-detail">
    <!-- 顶部导航 -->
    <header class="detail-header">
      <div class="header-left" @click="goBack">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <div class="header-center">
        <h1 class="page-title">秒杀详情</h1>
      </div>
      <div class="header-right">
        <el-icon class="share-icon"><Share /></el-icon>
      </div>
    </header>

    <!-- 活动头部 -->
    <div class="event-header">
      <div class="event-banner">
        <img :src="eventDetail.image" :alt="eventDetail.name" />
        <div class="banner-overlay">
          <h2 class="event-title">{{ eventDetail.name }}</h2>
          <p class="event-subtitle">{{ eventDetail.subtitle }}</p>
        </div>
      </div>
    </div>

    <!-- 活动信息 -->
    <div class="event-info">
      <div class="info-item">
        <el-icon class="info-icon"><Clock /></el-icon>
        <div class="info-content">
          <span class="info-label">活动时间</span>
          <span class="info-value">{{ eventDetail.time }}</span>
        </div>
      </div>
      <div class="info-item">
        <el-icon class="info-icon"><Location /></el-icon>
        <div class="info-content">
          <span class="info-label">活动地点</span>
          <span class="info-value">{{ eventDetail.location }}</span>
        </div>
      </div>
    </div>

    <!-- 倒计时 -->
    <div class="countdown-section">
      <div class="countdown-box">
        <span class="countdown-label">距离结束</span>
        <div class="countdown-timer">
          <div class="time-box">
            <span class="time-number">{{ hours }}</span>
          </div>
          <span class="time-separator">:</span>
          <div class="time-box">
            <span class="time-number">{{ minutes }}</span>
          </div>
          <span class="time-separator">:</span>
          <div class="time-box">
            <span class="time-number">{{ seconds }}</span>
          </div>
        </div>
      </div>
    </div>

    <!-- 活动详情 -->
    <div class="event-details">
      <h3 class="section-title">关于本次"革命"</h3>
      <div class="event-description">
        <p>{{ eventDetail.description }}</p>
      </div>
    </div>

    <!-- 参与要求 -->
    <div class="requirements">
      <h3 class="section-title">参与要求</h3>
      <div class="requirement-list">
        <div class="requirement-item" v-for="(req, index) in eventDetail.requirements" :key="index">
          <el-icon class="req-icon"><Check /></el-icon>
          <span class="req-text">{{ req }}</span>
        </div>
      </div>
    </div>

    <!-- 库存信息 -->
    <div class="stock-info">
      <div class="stock-progress">
        <div class="stock-header">
          <span class="stock-text">仅剩 {{ eventDetail.remainingStock }} 名</span>
          <span class="stock-status">火爆抢购中</span>
        </div>
        <div class="progress-bar">
          <div class="progress-fill" :style="{ width: eventDetail.progress + '%' }"></div>
        </div>
        <div class="progress-info">
          <span class="progress-item">已售 {{ eventDetail.soldCount }}</span>
          <span class="progress-item">总量 {{ eventDetail.totalStock }}</span>
        </div>
      </div>
    </div>

    <!-- 底部操作 -->
    <footer class="detail-footer">
      <div class="price-section">
        <span class="price-current">{{ eventDetail.seckillPrice }}</span>
        <span class="price-original">{{ eventDetail.originalPrice }}</span>
      </div>
      <el-button class="buy-button" type="primary" :disabled="eventDetail.disabled" @click="handleBuy">
        {{ eventDetail.disabled ? '已抢光' : '立即抢购' }}
      </el-button>
    </footer>
  </div>
</template>

<script>
import { ArrowLeft, Share, Clock, Location, Check } from '@element-plus/icons-vue'

export default {
  name: 'SeckillDetail',
  components: {
    ArrowLeft,
    Share,
    Clock,
    Location,
    Check
  },
  data() {
    return {
      hours: '08',
      minutes: '42',
      seconds: '19',
      eventDetail: {
        id: 1,
        name: '午夜低音革命',
        subtitle: '释放低音能量，开启五感沉浸',
        image: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=electronic%20music%20festival%20poster%2C%20neon%20lights%2C%20dark%20background%2C%20vibrant%20colors&image_size=portrait_16_9',
        time: '2026年10月31日 22:00 - 2026年11月1日 02:00',
        location: '学生活动中心 B2 地下俱乐部空间',
        description: '由电子音乐社呈现的一场视听盛宴。我们邀请了深圳本土知名DJ团队，在学生活动中心，用1000W的专业音响系统，带来一场电子音乐的盛宴。活动包含：视听娱乐系统体验、沉浸式灯光艺术投影、特装DJ区以及限量版周边等。',
        requirements: [
          '持有学生证',
          '电子票凭证',
          '年龄18周岁'
        ],
        originalPrice: '¥299.00',
        seckillPrice: '¥99.00',
        totalStock: 50,
        soldCount: 38,
        remainingStock: 12,
        progress: 76,
        disabled: false
      }
    }
  },
  mounted() {
    this.startCountdown()
  },
  methods: {
    startCountdown() {
      setInterval(() => {
        let totalSeconds = parseInt(this.hours) * 3600 + parseInt(this.minutes) * 60 + parseInt(this.seconds)
        if (totalSeconds > 0) {
          totalSeconds--
          this.hours = Math.floor(totalSeconds / 3600).toString().padStart(2, '0')
          this.minutes = Math.floor((totalSeconds % 3600) / 60).toString().padStart(2, '0')
          this.seconds = (totalSeconds % 60).toString().padStart(2, '0')
        }
      }, 1000)
    },
    goBack() {
      this.$router.push('/mobile')
    },
    handleBuy() {
      if (!this.eventDetail.disabled) {
        this.$router.push({
          path: '/seckill-success',
          query: {
            productId: this.eventDetail.id,
            name: this.eventDetail.name,
            image: this.eventDetail.image,
            price: this.eventDetail.seckillPrice.replace('¥', ''),
            originalPrice: this.eventDetail.originalPrice.replace('¥', ''),
            time: this.eventDetail.time
          }
        })
      }
    }
  }
}
</script>

<style scoped>
.seckill-detail {
  min-height: 100vh;
  background: #f5f5f5;
  position: relative;
  padding-bottom: 80px;
}

/* 顶部导航 */
.detail-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 16px 20px;
  background: white;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.1);
  position: sticky;
  top: 0;
  z-index: 100;
}

.back-icon,
.share-icon {
  font-size: 24px;
  color: #333;
  cursor: pointer;
}

.page-title {
  font-size: 18px;
  font-weight: 600;
  color: #333;
  margin: 0;
}

/* 活动头部 */
.event-header {
  position: relative;
  height: 200px;
  overflow: hidden;
}

.event-banner {
  position: relative;
  height: 100%;
}

.event-banner img {
  width: 100%;
  height: 100%;
  object-fit: cover;
}

.banner-overlay {
  position: absolute;
  bottom: 0;
  left: 0;
  right: 0;
  background: linear-gradient(transparent, rgba(0, 0, 0, 0.8));
  color: white;
  padding: 20px;
}

.event-title {
  font-size: 24px;
  font-weight: 700;
  margin: 0 0 8px 0;
}

.event-subtitle {
  font-size: 14px;
  margin: 0;
  opacity: 0.9;
}

/* 活动信息 */
.event-info {
  background: white;
  padding: 16px;
  margin: 12px 0;
}

.info-item {
  display: flex;
  align-items: center;
  gap: 12px;
  margin-bottom: 12px;
}

.info-icon {
  font-size: 20px;
  color: #ff6b6b;
  flex-shrink: 0;
}

.info-content {
  flex: 1;
}

.info-label {
  display: block;
  font-size: 12px;
  color: #999;
  margin-bottom: 4px;
}

.info-value {
  font-size: 14px;
  color: #333;
  font-weight: 500;
}

/* 倒计时 */
.countdown-section {
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  padding: 16px;
  margin: 12px 0;
  color: white;
}

.countdown-box {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.countdown-label {
  font-size: 14px;
  font-weight: 500;
}

.countdown-timer {
  display: flex;
  align-items: center;
  gap: 4px;
}

.time-box {
  background: rgba(255, 255, 255, 0.2);
  padding: 4px 8px;
  border-radius: 6px;
  min-width: 36px;
  text-align: center;
}

.time-number {
  font-size: 18px;
  font-weight: 700;
  color: white;
}

.time-separator {
  font-size: 18px;
  font-weight: 700;
  color: white;
  margin: 0 2px;
}

/* 活动详情 */
.event-details {
  background: white;
  padding: 16px;
  margin: 12px 0;
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 12px 0;
}

.event-description {
  font-size: 14px;
  color: #666;
  line-height: 1.5;
}

/* 参与要求 */
.requirements {
  background: white;
  padding: 16px;
  margin: 12px 0;
}

.requirement-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.requirement-item {
  display: flex;
  align-items: flex-start;
  gap: 10px;
}

.req-icon {
  font-size: 18px;
  color: #4caf50;
  flex-shrink: 0;
  margin-top: 2px;
}

.req-text {
  font-size: 14px;
  color: #666;
  line-height: 1.4;
}

/* 库存信息 */
.stock-info {
  background: white;
  padding: 16px;
  margin: 12px 0;
}

.stock-progress {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.stock-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
}

.stock-text {
  font-size: 14px;
  font-weight: 600;
  color: #ff6b6b;
}

.stock-status {
  font-size: 12px;
  color: #ff6b6b;
  background: #fff5f5;
  padding: 2px 8px;
  border-radius: 10px;
}

.progress-bar {
  height: 8px;
  background: #f0f0f0;
  border-radius: 4px;
  overflow: hidden;
}

.progress-fill {
  height: 100%;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border-radius: 4px;
  transition: width 0.3s ease;
}

.progress-info {
  display: flex;
  justify-content: space-between;
  font-size: 12px;
  color: #999;
}

/* 底部操作 */
.detail-footer {
  position: fixed;
  bottom: 0;
  left: 0;
  right: 0;
  display: flex;
  align-items: center;
  justify-content: space-between;
  background: white;
  padding: 16px 20px;
  box-shadow: 0 -2px 10px rgba(0, 0, 0, 0.1);
  z-index: 100;
}

.price-section {
  display: flex;
  align-items: baseline;
  gap: 8px;
}

.price-current {
  font-size: 24px;
  font-weight: 700;
  color: #ff6b6b;
}

.price-original {
  font-size: 14px;
  color: #999;
  text-decoration: line-through;
}

.buy-button {
  flex: 1;
  max-width: 160px;
  height: 44px;
  font-size: 16px;
  font-weight: 600;
  background: linear-gradient(135deg, #ff6b6b 0%, #ee5a6f 100%);
  border: none;
  border-radius: 22px;
  margin-left: 20px;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .event-header {
    height: 180px;
  }
  
  .event-title {
    font-size: 20px;
  }
  
  .buy-button {
    max-width: 140px;
  }
}
</style>