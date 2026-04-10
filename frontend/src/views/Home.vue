<template>
  <div class="home-container">
    <div class="welcome-section">
      <h2>欢迎来到社区团购秒杀后台管理系统</h2>
      <p>高效管理您的社区团购业务，轻松掌控秒杀活动</p>
    </div>

    <div class="stats-row">
      <el-card class="stat-card" shadow="hover" v-for="(item, index) in stats" :key="index">
        <div class="stat-content">
          <div class="stat-icon" :style="{ background: item.color }">
            <el-icon :size="28"><component :is="item.icon" /></el-icon>
          </div>
          <div class="stat-info">
            <div class="stat-value">{{ item.value }}</div>
            <div class="stat-label">{{ item.label }}</div>
          </div>
        </div>
      </el-card>
    </div>

    <div class="content-row">
      <el-card class="info-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><Odometer /></el-icon>
            <span>系统功能</span>
          </div>
        </template>
        <div class="feature-list">
          <div class="feature-item" v-for="(feature, index) in features" :key="index">
            <el-icon :color="feature.color"><component :is="feature.icon" /></el-icon>
            <span>{{ feature.title }}</span>
            <p>{{ feature.desc }}</p>
          </div>
        </div>
      </el-card>

      <el-card class="info-card" shadow="hover">
        <template #header>
          <div class="card-header">
            <el-icon><MagicStick /></el-icon>
            <span>快速操作</span>
          </div>
        </template>
        <div class="quick-actions">
          <el-button type="primary" size="large" @click="$router.push('/users')" :icon="User">
            用户管理
          </el-button>
          <el-button type="success" size="large" @click="$router.push('/products')" :icon="Goods">
            商品管理
          </el-button>
          <el-button type="warning" size="large" @click="$router.push('/seckill-activities')" :icon="Timer">
            秒杀活动
          </el-button>
          <el-button type="info" size="large" @click="$router.push('/orders')" :icon="Document">
            订单管理
          </el-button>
        </div>
      </el-card>
    </div>
  </div>
</template>

<script>
import { 
  User, 
  Goods, 
  Timer, 
  Document,
  Odometer,
  MagicStick
} from '@element-plus/icons-vue'

export default {
  name: 'Home',
  components: {
    User,
    Goods,
    Timer,
    Document,
    Odometer,
    MagicStick
  },
  data() {
    return {
      stats: [
        { label: '用户总数', value: '1,234', icon: 'User', color: 'linear-gradient(135deg, #667eea 0%, #764ba2 100%)' },
        { label: '商品总数', value: '568', icon: 'Goods', color: 'linear-gradient(135deg, #f093fb 0%, #f5576c 100%)' },
        { label: '秒杀活动', value: '12', icon: 'Timer', color: 'linear-gradient(135deg, #4facfe 0%, #00f2fe 100%)' },
        { label: '订单总数', value: '8,956', icon: 'Document', color: 'linear-gradient(135deg, #43e97b 0%, #38f9d7 100%)' }
      ],
      features: [
        { 
          title: '用户管理', 
          desc: '完整的用户信息管理，支持增删改查',
          icon: 'User',
          color: '#409EFF'
        },
        { 
          title: '商品管理', 
          desc: '高效管理商品信息，支持上架下架',
          icon: 'Goods',
          color: '#67C23A'
        },
        { 
          title: '秒杀活动', 
          desc: '灵活配置秒杀活动，提升销售业绩',
          icon: 'Timer',
          color: '#E6A23C'
        },
        { 
          title: '订单管理', 
          desc: '实时查看订单状态，快速处理订单',
          icon: 'Document',
          color: '#909399'
        }
      ]
    }
  }
}
</script>

<style scoped>
.home-container {
  padding: 0;
}

.welcome-section {
  margin-bottom: 24px;
}

.welcome-section h2 {
  margin: 0 0 8px 0;
  font-size: 24px;
  color: #303133;
}

.welcome-section p {
  margin: 0;
  color: #909399;
  font-size: 14px;
}

.stats-row {
  display: grid;
  grid-template-columns: repeat(4, 1fr);
  gap: 20px;
  margin-bottom: 24px;
}

.stat-card {
  border-radius: 12px;
  overflow: hidden;
}

.stat-content {
  display: flex;
  align-items: center;
  gap: 16px;
  padding: 8px;
}

.stat-icon {
  width: 64px;
  height: 64px;
  border-radius: 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  color: white;
}

.stat-info {
  flex: 1;
}

.stat-value {
  font-size: 28px;
  font-weight: 700;
  color: #303133;
  margin-bottom: 4px;
}

.stat-label {
  font-size: 14px;
  color: #909399;
}

.content-row {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
}

.info-card {
  border-radius: 12px;
}

.card-header {
  display: flex;
  align-items: center;
  gap: 8px;
  font-size: 16px;
  font-weight: 600;
  color: #303133;
}

.feature-list {
  display: flex;
  flex-direction: column;
  gap: 20px;
}

.feature-item {
  display: flex;
  align-items: flex-start;
  gap: 12px;
  padding: 12px;
  background: #f5f7fa;
  border-radius: 8px;
  transition: all 0.3s;
}

.feature-item:hover {
  background: #ecf5ff;
  transform: translateX(4px);
}

.feature-item .el-icon {
  font-size: 24px;
  flex-shrink: 0;
  margin-top: 2px;
}

.feature-item span {
  font-size: 15px;
  font-weight: 600;
  color: #303133;
  display: block;
  margin-bottom: 4px;
}

.feature-item p {
  margin: 0;
  font-size: 13px;
  color: #909399;
}

.quick-actions {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 16px;
}

.quick-actions .el-button {
  height: 60px;
  font-size: 15px;
  border-radius: 10px;
}

@media (max-width: 1200px) {
  .stats-row {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .content-row {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .stats-row {
    grid-template-columns: 1fr;
  }
  
  .quick-actions {
    grid-template-columns: 1fr;
  }
}
</style>
