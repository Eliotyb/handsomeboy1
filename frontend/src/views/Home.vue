<template>
  <div class="admin-dashboard">
    <aside class="sidebar">
      <div class="sidebar-header">
        <div class="brand">
          <div class="brand-name">Pulse Admin</div>
          <div class="brand-tag">秒杀管理系统</div>
        </div>
      </div>
      
      <nav class="sidebar-nav">
        <div class="nav-item" :class="{ active: activeNav === 'dashboard' }" @click="navigateTo('/dashboard')">
          <el-icon class="nav-icon"><Grid /></el-icon>
          <span>仪表盘</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'products' }" @click="navigateTo('/products')">
          <el-icon class="nav-icon"><Box /></el-icon>
          <span>商品管理</span>
        </div>
        <div class="nav-item active" :class="{ active: activeNav === 'activities' }" @click="navigateTo('/seckill-activities')">
          <el-icon class="nav-icon"><Lightning /></el-icon>
          <span>秒杀活动</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'orders' }" @click="navigateTo('/orders')">
          <el-icon class="nav-icon"><Document /></el-icon>
          <span>订单管理</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'users' }" @click="navigateTo('/users')">
          <el-icon class="nav-icon"><UserFilled /></el-icon>
          <span>用户管理</span>
        </div>
      </nav>
      
      <div class="sidebar-footer">
        <div class="user-profile">
          <img :src="avatarUrl" class="user-avatar" alt="管理员" />
          <div class="user-details">
            <div class="user-name">管理员</div>
            <div class="user-role">超级管理员</div>
          </div>
        </div>
        <el-button class="create-activity-btn" @click="handleCreate">
          <el-icon><Plus /></el-icon>
          创建秒杀
        </el-button>
      </div>
    </aside>

    <main class="main-content">
      <header class="top-header">
        <div class="header-search">
          <el-icon class="search-icon"><Search /></el-icon>
          <input type="text" placeholder="搜索秒杀活动..." class="search-input" />
        </div>
        <div class="header-actions">
          <el-icon class="action-icon"><Bell /></el-icon>
          <el-icon class="action-icon"><Setting /></el-icon>
          <div class="header-brand">
            <span>Pulse Velocity</span>
          </div>
        </div>
      </header>

      <div class="content-area">
        <div class="page-header">
          <div class="page-title-section">
            <div class="page-breadcrumb">秒杀活动管理</div>
            <h1 class="page-title">秒杀活动管理</h1>
          </div>
          <el-button class="new-activity-btn" @click="handleCreate">
            <el-icon><Plus /></el-icon>
            创建新活动
          </el-button>
        </div>

        <div class="stats-cards">
          <div class="stat-card primary">
            <div class="stat-label">正在进行</div>
            <div class="stat-value">04</div>
          </div>
          <div class="stat-card">
            <div class="stat-icon"><Lightning /></div>
          </div>
          <div class="stat-card">
            <div class="stat-label">待开始</div>
            <div class="stat-value blue">12</div>
          </div>
          <div class="stat-card">
            <div class="stat-label">本月成交额</div>
            <div class="stat-value">¥128,400</div>
          </div>
          <div class="stat-card warning">
            <div class="stat-label">库存预警</div>
            <div class="stat-value">03</div>
          </div>
        </div>

        <div class="activities-container">
          <div class="container-header">
            <h3 class="container-title">活动列表</h3>
            <div class="filter-tabs">
              <span class="tab active">全部</span>
              <span class="tab">进行中</span>
              <span class="tab">已结束</span>
            </div>
          </div>
          
          <div class="activities-table">
            <div class="table-header">
              <div class="table-cell">ID</div>
              <div class="table-cell">活动名称</div>
              <div class="table-cell">起止时间</div>
              <div class="table-cell">状态</div>
              <div class="table-cell">操作</div>
            </div>
            <div class="table-body">
              <div class="table-row" v-for="(activity, index) in activities" :key="index">
                <div class="table-cell">
                  <span class="activity-id">#ACT-{{ String(activity.id).padStart(4, '0') }}</span>
                </div>
                <div class="table-cell">
                  <div class="activity-info">
                    <div class="activity-name">{{ activity.name }}</div>
                    <div class="activity-desc">全场电子产品5折起</div>
                  </div>
                </div>
                <div class="table-cell">
                  <div class="time-info">
                    <div class="time-item">2024-08-20 10:00</div>
                    <div class="time-item">至 2024-08-27 23:59</div>
                  </div>
                </div>
                <div class="table-cell">
                  <span class="status-badge" :class="activity.statusClass">{{ activity.status }}</span>
                </div>
                <div class="table-cell">
                  <div class="action-btns">
                    <el-button class="action-btn" circle><el-icon><Document /></el-icon></el-button>
                    <el-button class="action-btn" circle><el-icon><Edit /></el-icon></el-button>
                  </div>
                </div>
              </div>
            </div>
          </div>
          
          <div class="pagination">
            <span class="pagination-info">显示1到3条，共24条记录</span>
            <div class="pagination-controls">
              <span class="page-btn">&lt;</span>
              <span class="page-btn active">1</span>
              <span class="page-btn">2</span>
              <span class="page-btn">3</span>
            </div>
          </div>
        </div>

        <div class="bottom-section">
          <div class="promo-card">
            <div class="promo-content">
              <h2 class="promo-title">
                优化您的秒杀策略<br />
                提升 200% 转化率
              </h2>
              <p class="promo-desc">
                通过智能算法分析用户行为，自动推荐最佳秒杀时间段和商品库存配置。开启智能助手让管理更简单。
              </p>
              <div class="promo-btns">
                <el-button class="promo-btn primary">立即开启</el-button>
                <el-button class="promo-btn">查看报告</el-button>
              </div>
            </div>
            <div class="promo-decoration">
              <el-icon class="promo-icon"><TrendCharts /></el-icon>
            </div>
          </div>
          
          <div class="quick-actions">
            <h3 class="section-title">快捷操作</h3>
            <div class="action-list">
              <div class="action-item">
                <div class="action-icon blue">
                  <el-icon><Document /></el-icon>
                </div>
                <div class="action-details">
                  <div class="action-name">数据对账单</div>
                  <div class="action-desc">下载上一活动成交明细</div>
                </div>
              </div>
              <div class="action-item">
                <div class="action-icon yellow">
                  <el-icon><Bell /></el-icon>
                </div>
                <div class="action-details">
                  <div class="action-name">预热消息推送</div>
                  <div class="action-desc">向关注用户发送通知</div>
                </div>
              </div>
            </div>
            <div class="settings-link">
              <span>进入系统设置</span>
              <el-icon><ArrowRight /></el-icon>
            </div>
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import { 
  Search, Bell, Setting, Plus, Grid, Lightning, UserFilled, 
  Box, Document, Edit, TrendCharts, ArrowRight
} from '@element-plus/icons-vue'
import axios from 'axios'

export default {
  name: 'Home',
  components: {
    Search, Bell, Setting, Plus, Grid, Lightning, UserFilled,
    Box, Document, Edit, TrendCharts, ArrowRight
  },
  data() {
    return {
      activeNav: 'activities',
      avatarUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20portrait%2C%20formal%20photo%2C%20business%20person%2C%20square%20image&image_size=square',
      activities: []
    }
  },
  mounted() {
    this.loadActivities()
  },
  methods: {
    async loadActivities() {
      try {
        const response = await axios.get('/api/seckill-activities')
        if (response.data.code === 200) {
          this.activities = response.data.data.map(activity => {
            let status = '进行中'
            let statusClass = 'active'
            
            if (activity.status === 0) {
              status = '待开始'
              statusClass = 'pending'
            } else if (activity.status === 2) {
              status = '已结束'
              statusClass = 'completed'
            }
            
            return {
              ...activity,
              status: status,
              statusClass: statusClass
            }
          })
        }
      } catch (error) {
        console.error('加载活动失败:', error)
        this.activities = [
          { id: 1, name: '盛夏狂欢数码周', status: '进行中', statusClass: 'active' },
          { id: 2, name: '九月开学季礼包', status: '待开始', statusClass: 'pending' },
          { id: 3, name: '深夜食堂特惠', status: '已结束', statusClass: 'completed' }
        ]
      }
    },
    navigateTo(path) {
      this.$router.push(path)
    },
    handleCreate() {
      this.$router.push('/seckill-activities')
    }
  }
}
</script>

<style scoped>
.admin-dashboard {
  display: flex;
  min-height: 100vh;
  background: linear-gradient(135deg, #f8fafc 0%, #fff7f5 100%);
}

.sidebar {
  width: 260px;
  background: linear-gradient(180deg, #ffffff 0%, #fefefe 100%);
  border-right: 1px solid #f0f0f0;
  display: flex;
  flex-direction: column;
  padding: 32px 20px;
}

.sidebar-header {
  margin-bottom: 40px;
}

.brand-name {
  font-size: 20px;
  font-weight: 800;
  color: #1e293b;
  letter-spacing: -0.5px;
  margin-bottom: 4px;
}

.brand-tag {
  font-size: 11px;
  color: #f97316;
  font-weight: 500;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.sidebar-nav {
  flex: 1;
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.nav-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 14px 16px;
  border-radius: 12px;
  cursor: pointer;
  transition: all 0.2s;
  color: #64748b;
  font-size: 14px;
  font-weight: 500;
}

.nav-item:hover {
  background: #fff1e6;
  color: #f97316;
}

.nav-item.active {
  background: linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%);
  color: #c2410c;
  border-left: 3px solid #ea580c;
  font-weight: 600;
}

.nav-icon {
  font-size: 20px;
}

.sidebar-footer {
  margin-top: auto;
  display: flex;
  flex-direction: column;
  gap: 16px;
  padding-top: 24px;
  border-top: 1px solid #f3f4f6;
}

.user-profile {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px;
  background: #f9fafb;
  border-radius: 16px;
}

.user-avatar {
  width: 44px;
  height: 44px;
  border-radius: 50%;
  object-fit: cover;
  border: 2px solid #fff;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.user-details {
  display: flex;
  flex-direction: column;
}

.user-name {
  font-size: 13px;
  font-weight: 600;
  color: #1e293b;
}

.user-role {
  font-size: 11px;
  color: #94a3b8;
}

.create-activity-btn {
  width: 100%;
  height: 44px;
  border-radius: 22px;
  background: linear-gradient(135deg, #ea580c 0%, #f97316 100%);
  border: none;
  color: white;
  font-weight: 600;
  font-size: 13px;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 6px;
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.3);
}

.create-activity-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(249, 115, 22, 0.4);
}

.main-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  overflow: hidden;
}

.top-header {
  height: 72px;
  background: #ffffff;
  border-bottom: 1px solid #f3f4f6;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 40px;
}

.header-search {
  display: flex;
  align-items: center;
  background: #f8fafc;
  border-radius: 24px;
  padding: 10px 18px;
  gap: 10px;
  width: 320px;
}

.search-icon {
  color: #94a3b8;
  font-size: 18px;
}

.search-input {
  flex: 1;
  border: none;
  background: transparent;
  font-size: 14px;
  outline: none;
  color: #334155;
}

.search-input::placeholder {
  color: #94a3b8;
}

.header-actions {
  display: flex;
  align-items: center;
  gap: 24px;
}

.action-icon {
  font-size: 22px;
  color: #475569;
  cursor: pointer;
  transition: color 0.2s;
}

.action-icon:hover {
  color: #f97316;
}

.header-brand {
  padding-left: 24px;
  border-left: 1px solid #e2e8f0;
}

.header-brand span {
  font-size: 16px;
  font-weight: 700;
  color: #f97316;
}

.content-area {
  flex: 1;
  overflow-y: auto;
  padding: 32px 40px;
}

.page-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 32px;
}

.page-title-section {
  display: flex;
  flex-direction: column;
  gap: 8px;
}

.page-breadcrumb {
  font-size: 12px;
  color: #2563eb;
  font-weight: 600;
  text-transform: uppercase;
  letter-spacing: 0.5px;
}

.page-title {
  font-size: 32px;
  font-weight: 800;
  color: #1e293b;
  margin: 0;
  letter-spacing: -1px;
}

.new-activity-btn {
  height: 48px;
  border-radius: 24px;
  padding: 0 24px;
  background: linear-gradient(135deg, #7f1d1d 0%, #991b1b 100%);
  border: none;
  color: white;
  font-weight: 600;
  font-size: 14px;
  display: flex;
  align-items: center;
  gap: 8px;
  box-shadow: 0 4px 12px rgba(127, 29, 29, 0.3);
}

.new-activity-btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 6px 20px rgba(127, 29, 29, 0.4);
}

.stats-cards {
  display: grid;
  grid-template-columns: repeat(5, 1fr);
  gap: 20px;
  margin-bottom: 32px;
}

.stat-card {
  background: #ffffff;
  border-radius: 28px;
  padding: 28px 24px;
  display: flex;
  flex-direction: column;
  gap: 8px;
  position: relative;
  overflow: hidden;
}

.stat-card.primary {
  background: linear-gradient(135deg, #fefefe 0%, #fdfdfd 100%);
}

.stat-card.warning {
  background: linear-gradient(135deg, #ff6b35 0%, #ff8a5c 100%);
}

.stat-label {
  font-size: 13px;
  color: #64748b;
  font-weight: 500;
}

.stat-card.warning .stat-label {
  color: rgba(255, 255, 255, 0.9);
}

.stat-value {
  font-size: 32px;
  font-weight: 800;
  color: #7f1d1d;
  line-height: 1.1;
}

.stat-value.blue {
  color: #2563eb;
}

.stat-card.warning .stat-value {
  color: white;
}

.stat-icon {
  position: absolute;
  right: 20px;
  top: 50%;
  transform: translateY(-50%);
  font-size: 64px;
  color: #e5e7eb;
  opacity: 0.5;
}

.activities-container {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
  border-radius: 32px;
  padding: 32px;
  margin-bottom: 32px;
  border: 1px solid rgba(255, 255, 255, 0.8);
}

.container-header {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-bottom: 24px;
}

.container-title {
  font-size: 18px;
  font-weight: 700;
  color: #1e293b;
  margin: 0;
}

.filter-tabs {
  display: flex;
  gap: 8px;
  background: #ffffff;
  padding: 4px;
  border-radius: 20px;
}

.tab {
  padding: 6px 14px;
  font-size: 12px;
  font-weight: 500;
  color: #64748b;
  border-radius: 16px;
  cursor: pointer;
  transition: all 0.2s;
}

.tab.active {
  background: #f8fafc;
  color: #1e293b;
  font-weight: 600;
}

.activities-table {
  width: 100%;
}

.table-header {
  display: grid;
  grid-template-columns: 1fr 2fr 1.5fr 1fr 1fr;
  gap: 16px;
  padding: 16px 20px;
  border-bottom: 1px solid rgba(249, 115, 22, 0.1);
  font-size: 11px;
  font-weight: 600;
  color: #94a3b8;
  text-transform: uppercase;
  letter-spacing: 1px;
}

.table-body {
  display: flex;
  flex-direction: column;
}

.table-row {
  display: grid;
  grid-template-columns: 1fr 2fr 1.5fr 1fr 1fr;
  gap: 16px;
  padding: 20px;
  border-bottom: 1px solid rgba(248, 250, 252, 0.8);
  align-items: center;
  transition: background 0.2s;
}

.table-row:hover {
  background: rgba(255, 255, 255, 0.5);
}

.table-cell {
  display: flex;
  align-items: center;
}

.activity-id {
  font-size: 12px;
  color: #cbd5e1;
  font-family: 'Monaco', monospace;
}

.activity-info {
  display: flex;
  flex-direction: column;
  gap: 4px;
}

.activity-name {
  font-size: 14px;
  font-weight: 600;
  color: #475569;
}

.activity-desc {
  font-size: 11px;
  color: #cbd5e1;
}

.time-info {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.time-item {
  font-size: 12px;
  color: #64748b;
}

.status-badge {
  padding: 6px 12px;
  border-radius: 12px;
  font-size: 12px;
  font-weight: 500;
}

.status-badge.active {
  background: rgba(167, 139, 250, 0.2);
  color: #6366f1;
}

.status-badge.pending {
  background: rgba(251, 191, 36, 0.15);
  color: #d97706;
}

.status-badge.completed {
  background: rgba(226, 232, 240, 0.7);
  color: #94a3b8;
}

.action-btns {
  display: flex;
  gap: 8px;
}

.action-btn {
  width: 36px;
  height: 36px;
  border-radius: 50%;
  background: #fecaca;
  border: none;
  color: #991b1b;
  display: flex;
  align-items: center;
  justify-content: center;
}

.action-btn:hover {
  background: #fca5a5;
}

.pagination {
  display: flex;
  align-items: center;
  justify-content: space-between;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid rgba(248, 250, 252, 0.5);
}

.pagination-info {
  font-size: 12px;
  color: #94a3b8;
}

.pagination-controls {
  display: flex;
  gap: 8px;
}

.page-btn {
  width: 32px;
  height: 32px;
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 13px;
  color: #64748b;
  cursor: pointer;
  transition: all 0.2s;
}

.page-btn:hover {
  background: #f1f5f9;
}

.page-btn.active {
  background: linear-gradient(135deg, #7f1d1d 0%, #991b1b 100%);
  color: white;
}

.bottom-section {
  display: grid;
  grid-template-columns: 2fr 1fr;
  gap: 24px;
}

.promo-card {
  background: linear-gradient(135deg, #7f1d1d 0%, #9a3412 50%, #c2410c 100%);
  border-radius: 40px;
  padding: 40px;
  display: flex;
  align-items: center;
  justify-content: space-between;
  position: relative;
  overflow: hidden;
}

.promo-content {
  z-index: 1;
}

.promo-title {
  font-size: 28px;
  font-weight: 800;
  color: white;
  margin: 0 0 16px 0;
  line-height: 1.2;
  letter-spacing: -0.5px;
}

.promo-desc {
  font-size: 14px;
  color: rgba(255, 255, 255, 0.8);
  margin: 0 0 28px 0;
  max-width: 420px;
  line-height: 1.7;
}

.promo-btns {
  display: flex;
  gap: 12px;
}

.promo-btn {
  padding: 12px 24px;
  border-radius: 20px;
  font-size: 14px;
  font-weight: 600;
  border: none;
  cursor: pointer;
  transition: all 0.2s;
}

.promo-btn.primary {
  background: white;
  color: #7f1d1d;
}

.promo-btn.primary:hover {
  transform: translateY(-2px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.2);
}

.promo-btn:not(.primary) {
  background: rgba(255, 255, 255, 0.15);
  color: white;
  backdrop-filter: blur(10px);
}

.promo-decoration {
  position: absolute;
  right: 40px;
  bottom: 0;
  opacity: 0.3;
}

.promo-icon {
  font-size: 120px;
  color: white;
}

.quick-actions {
  background: rgba(255, 255, 255, 0.7);
  backdrop-filter: blur(10px);
  border-radius: 32px;
  padding: 32px;
  border: 1px solid rgba(255, 255, 255, 0.8);
  display: flex;
  flex-direction: column;
}

.section-title {
  font-size: 16px;
  font-weight: 700;
  color: #1e293b;
  margin: 0 0 24px 0;
}

.action-list {
  display: flex;
  flex-direction: column;
  gap: 16px;
  margin-bottom: auto;
}

.action-item {
  display: flex;
  align-items: center;
  gap: 14px;
  padding: 16px;
  background: white;
  border-radius: 20px;
  transition: all 0.2s;
}

.action-item:hover {
  transform: translateX(4px);
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.05);
}

.action-icon {
  width: 44px;
  height: 44px;
  border-radius: 14px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-size: 20px;
  flex-shrink: 0;
}

.action-icon.blue {
  background: linear-gradient(135deg, #dbeafe 0%, #e0f2fe 100%);
  color: #2563eb;
}

.action-icon.yellow {
  background: linear-gradient(135deg, #fef9c3 0%, #fef3c7 100%);
  color: #b45309;
}

.action-details {
  display: flex;
  flex-direction: column;
  gap: 2px;
}

.action-name {
  font-size: 13px;
  font-weight: 600;
  color: #1e293b;
}

.action-desc {
  font-size: 11px;
  color: #94a3b8;
}

.settings-link {
  display: flex;
  align-items: center;
  gap: 6px;
  margin-top: 24px;
  padding-top: 20px;
  border-top: 1px solid rgba(248, 250, 252, 0.8);
  cursor: pointer;
  color: #7f1d1d;
  font-size: 13px;
  font-weight: 600;
  transition: color 0.2s;
}

.settings-link:hover {
  color: #c2410c;
}

@media (max-width: 1400px) {
  .stats-cards {
    grid-template-columns: repeat(3, 1fr);
  }
  
  .bottom-section {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 1024px) {
  .stats-cards {
    grid-template-columns: repeat(2, 1fr);
  }
  
  .table-header,
  .table-row {
    grid-template-columns: 1.5fr 1.5fr 1fr;
  }
  
  .table-cell:nth-child(1),
  .table-cell:nth-child(5) {
    display: none;
  }
}
</style>
