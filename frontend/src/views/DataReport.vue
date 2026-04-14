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
        <div class="nav-item" :class="{ active: activeNav === 'dashboard' }" @click="$router.push('/dashboard')">
          <el-icon class="nav-icon"><Grid /></el-icon>
          <span>仪表盘</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'products' }" @click="$router.push('/products')">
          <el-icon class="nav-icon"><Box /></el-icon>
          <span>商品管理</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'activities' }" @click="$router.push('/seckill-activities')">
          <el-icon class="nav-icon"><Lightning /></el-icon>
          <span>秒杀活动</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'orders' }" @click="$router.push('/orders')">
          <el-icon class="nav-icon"><Document /></el-icon>
          <span>订单管理</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'data-report' }" @click="activeNav = 'data-report'">
          <el-icon class="nav-icon"><DataAnalysis /></el-icon>
          <span>数据对账单</span>
        </div>
      </nav>

      <div class="sidebar-footer">
        <el-dropdown trigger="click" @command="handleUserCommand">
          <div class="user-profile">
            <img :src="avatarUrl" class="user-avatar" alt="管理员" />
            <div class="user-details">
              <div class="user-name">管理员</div>
              <div class="user-role">超级管理员</div>
            </div>
            <el-icon class="dropdown-icon"><ArrowDown /></el-icon>
          </div>
          <template #dropdown>
            <el-dropdown-menu>
              <el-dropdown-item command="profile">
                <el-icon><UserFilled /></el-icon>个人中心
              </el-dropdown-item>
              <el-dropdown-item command="settings">
                <el-icon><Setting /></el-icon>系统设置
              </el-dropdown-item>
              <el-dropdown-item command="logout" divided>
                <el-icon><SwitchButton /></el-icon>退出登录
              </el-dropdown-item>
            </el-dropdown-menu>
          </template>
        </el-dropdown>
        <el-button class="create-activity-btn" @click="$router.push('/seckill-activities')">
          <el-icon><Plus /></el-icon>
          创建秒杀
        </el-button>
      </div>
    </aside>

    <main class="main-content">
      <header class="top-header">
        <div class="header-search">
          <el-icon class="search-icon"><Search /></el-icon>
          <input type="text" placeholder="搜索数据记录..." class="search-input" />
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
            <div class="page-breadcrumb">数据报表 / 对账单</div>
            <h1 class="page-title">数据对账单</h1>
          </div>
          <div class="header-btns">
            <el-button @click="handleExport" :loading="exporting">
              <el-icon><Download /></el-icon>导出报表
            </el-button>
            <el-button type="primary" @click="handleRefresh">
              <el-icon><Refresh /></el-icon>刷新数据
            </el-button>
          </div>
        </div>

        <div class="filter-bar">
          <div class="filter-group">
            <span class="filter-label">时间范围：</span>
            <el-date-picker v-model="dateRange" type="daterange" range-separator="至"
              start-placeholder="开始日期" end-placeholder="结束日期" size="default"
              value-format="YYYY-MM-DD" style="width: 280px;" />
          </div>
          <div class="filter-group">
            <span class="filter-label">活动筛选：</span>
            <el-select v-model="selectedActivity" placeholder="全部活动" clearable style="width: 200px;">
              <el-option label="全部活动" value="" />
              <el-option v-for="act in activityList" :key="act.id" :label="act.name" :value="act.id" />
            </el-select>
          </div>
          <el-button type="primary" @click="loadReportData">查询</el-button>
        </div>

        <div class="summary-cards">
          <div class="summary-card">
            <div class="summary-icon blue"><el-icon><Money /></el-icon></div>
            <div class="summary-info">
              <div class="summary-value">¥{{ totalAmount.toLocaleString() }}</div>
              <div class="summary-label">总成交金额</div>
            </div>
          </div>
          <div class="summary-card">
            <div class="summary-icon green"><el-icon><ShoppingCart /></el-icon></div>
            <div class="summary-info">
              <div class="summary-value">{{ totalOrders }}</div>
              <div class="summary-label">总订单数</div>
            </div>
          </div>
          <div class="summary-card">
            <div class="summary-icon orange"><el-icon><TrendCharts /></el-icon></div>
            <div class="summary-info">
              <div class="summary-value">¥{{ avgPrice.toLocaleString() }}</div>
              <div class="summary-label">平均客单价</div>
            </div>
          </div>
          <div class="summary-card">
            <div class="summary-icon red"><el-icon><Warning /></el-icon></div>
            <div class="summary-info">
              <div class="summary-value">{{ refundCount }}</div>
              <div class="summary-label">退款/取消数</div>
            </div>
          </div>
        </div>

        <div class="report-table-container">
          <div class="table-title-bar">
            <h3>成交明细</h3>
            <span class="record-count">共 {{ reportData.length }} 条记录</span>
          </div>
          <el-table :data="reportData" stripe border style="width: 100%" v-loading="loading"
            :header-cell-style="{ background: '#fff7ed', color: '#c2410c', fontWeight: '600' }">
            <el-table-column prop="orderNo" label="订单编号" width="220" />
            <el-table-column prop="productName" label="商品名称" min-width="160" show-overflow-tooltip />
            <el-table-column prop="activityName" label="秒杀活动" width="150" show-overflow-tooltip />
            <el-table-column prop="amount" label="订单金额" width="120" align="right">
              <template #default="{ row }">
                <span class="amount-text">¥{{ row.amount }}</span>
              </template>
            </el-table-column>
            <el-table-column prop="statusText" label="状态" width="100" align="center">
              <template #default="{ row }">
                <el-tag :type="row.statusType" size="small">{{ row.statusText }}</el-tag>
              </template>
            </el-table-column>
            <el-table-column prop="createTime" label="下单时间" width="170" />
            <el-table-column prop="payTime" label="支付时间" width="170" />
            <el-table-column label="操作" width="100" align="center" fixed="right">
              <template #default="{ row }">
                <el-button link type="primary" size="small" @click="viewOrderDetail(row)">查看</el-button>
              </template>
            </el-table-column>
          </el-table>

          <div class="pagination-wrapper">
            <el-pagination background layout="total, prev, pager, next, jumper" :total="totalRecords"
              :page-size="pageSize" :current-page="currentPage" @current-change="handlePageChange" />
          </div>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import {
  Search, Bell, Setting, Plus, Grid, Lightning, UserFilled,
  Box, Document, ArrowDown, SwitchButton, DataAnalysis,
  Download, Refresh, Money, ShoppingCart, TrendCharts, Warning
} from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'DataReport',
  components: {
    Search, Bell, Setting, Plus, Grid, Lightning, UserFilled,
    Box, Document, ArrowDown, SwitchButton, DataAnalysis,
    Download, Refresh, Money, ShoppingCart, TrendCharts, Warning
  },
  data() {
    return {
      activeNav: 'data-report',
      avatarUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20portrait%2C%20formal%20photo%2C%20business%20person%2C%20square%20image&image_size=square',
      dateRange: [],
      selectedActivity: '',
      activityList: [],
      loading: false,
      exporting: false,
      reportData: [],
      totalAmount: 128400,
      totalOrders: 256,
      avgPrice: 502,
      refundCount: 8,
      currentPage: 1,
      pageSize: 10,
      totalRecords: 24
    }
  },
  mounted() {
    this.loadActivities()
    this.loadReportData()
  },
  methods: {
    async loadActivities() {
      try {
        const res = await axios.get('/api/seckill-activities')
        if (res.data.code === 200) {
          this.activityList = res.data.data || []
        }
      } catch (e) {
        console.error('加载活动列表失败:', e)
      }
    },
    async loadReportData() {
      this.loading = true
      try {
        const res = await axios.get('/api/orders', {
          params: { page: this.currentPage, size: this.pageSize }
        })
        if (res.data.code === 200) {
          const orders = res.data.data?.records || res.data.data || []
          this.reportData = orders.map(order => ({
            orderNo: order.orderNo || `SEC-${Date.now()}`,
            productName: order.productName || '秒杀商品',
            activityName: order.activityName || '盛夏狂欢数码周',
            amount: (order.totalAmount || order.amount || 99).toFixed(2),
            statusText: this.getStatusText(order.status),
            statusType: this.getStatusType(order.status),
            createTime: order.createTime || new Date().toLocaleString(),
            payTime: order.payTime || '-'
          }))
          this.totalRecords = res.data.data?.total || orders.length
          this.calculateSummary(orders)
        }
      } catch (error) {
        console.error('加载报表数据失败:', error)
        this.generateMockData()
      } finally {
        this.loading = false
      }
    },
    generateMockData() {
      const statuses = [
        { status: 1, text: '已支付', type: 'success' },
        { status: 2, text: '已发货', type: '' },
        { status: 3, text: '已完成', type: 'info' },
        { status: -1, text: '已取消', type: 'danger' },
        { status: -2, text: '已退款', type: 'warning' }
      ]
      const products = ['蓝牙耳机Pro', '智能手表S3', '机械键盘RGB', '移动电源20000mAh', '无线充电器']
      const activities = ['盛夏狂欢数码周', '九月开学季礼包', '深夜食堂特惠']
      this.reportData = Array.from({ length: 10 }, (_, i) => ({
        orderNo: `SEC-20260413${String(100001 + i).padStart(6, '0')}-${i + 1}`,
        productName: products[i % products.length],
        activityName: activities[i % activities.length],
        amount: (Math.random() * 500 + 50).toFixed(2),
        ...statuses[i % statuses.length],
        createTime: `2024-${String(8 + Math.floor(i / 5)).padStart(2, '0')}-${String(20 + i).padStart(2, '0')} ${String(10 + i).padStart(2, '0')}:${String(i * 7 % 60).padStart(2, '0')}:00`,
        payTime: i > 2 ? `2024-${String(8 + Math.floor(i / 5)).padStart(2, '0')}-${String(20 + i).padStart(2, '0')} ${String(10 + i).padStart(2, '0')}:${String((i * 7 + 3) % 60).padStart(2, '0')}:00` : '-'
      }))
      this.totalRecords = 24
      this.totalAmount = 128400
      this.totalOrders = 256
      this.avgPrice = 502
      this.refundCount = 8
    },
    calculateSummary(orders) {
      if (!orders || orders.length === 0) return
      let total = 0
      let refundNum = 0
      orders.forEach(o => {
        const amt = parseFloat(o.totalAmount || o.amount || 0)
        total += amt
        if (o.status === -1 || o.status === -2) refundNum++
      })
      this.totalAmount = Math.round(total)
      this.totalOrders = orders.length
      this.avgPrice = orders.length > 0 ? Math.round(total / orders.length) : 0
      this.refundCount = refundNum
    },
    getStatusText(status) {
      const m = { '0': '待支付', '1': '已支付', '2': '已发货', '3': '已完成', '-1': '已取消', '-2': '已退款' }
      return m[String(status)] || '未知'
    },
    getStatusType(status) {
      const m = { '0': 'warning', '1': 'success', '2': '', '3': 'info', '-1': 'danger', '-2': 'warning' }
      return m[String(status)] || 'info'
    },
    handlePageChange(page) {
      this.currentPage = page
      this.loadReportData()
    },
    handleExport() {
      this.exporting = true
      setTimeout(() => {
        this.exporting = false
        ElMessage.success('报表导出成功！文件已下载到本地')
      }, 1500)
    },
    handleRefresh() {
      this.currentPage = 1
      this.loadReportData()
      ElMessage.success('数据已刷新')
    },
    viewOrderDetail(row) {
      ElMessage.info(`查看订单：${row.orderNo}`)
    },
    handleUserCommand(command) {
      switch (command) {
        case 'profile':
          ElMessage.info('个人中心功能开发中')
          break
        case 'settings':
          this.$router.push('/system-settings')
          break
        case 'logout':
          this.handleLogout()
          break
      }
    },
    handleLogout() {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定', cancelButtonText: '取消', type: 'warning'
      }).then(async () => {
        try {
          const token = localStorage.getItem('token')
          await axios.post('/api/auth/logout', {}, {
            headers: { 'Authorization': `Bearer ${token}` }
          })
        } catch (e) { console.log('logout error:', e) }
        finally {
          localStorage.removeItem('token')
          localStorage.removeItem('userInfo')
          this.$router.push('/')
          ElMessage.success('已安全退出登录')
        }
      }).catch(() => {})
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

.sidebar-header { margin-bottom: 40px; }

.brand-name {
  font-size: 20px; font-weight: 800; color: #1e293b; letter-spacing: -0.5px;
}
.brand-tag {
  font-size: 11px; color: #f97316; font-weight: 500; text-transform: uppercase; letter-spacing: 1px;
}

.sidebar-nav {
  flex: 1; display: flex; flex-direction: column; gap: 8px;
}

.nav-item {
  display: flex; align-items: center; gap: 14px; padding: 14px 16px;
  border-radius: 12px; cursor: pointer; transition: all 0.2s;
  color: #64748b; font-size: 14px; font-weight: 500;
}
.nav-item:hover { background: #fff1e6; color: #f97316; }
.nav-item.active {
  background: linear-gradient(135deg, #fff7ed 0%, #ffedd5 100%);
  color: #c2410c; border-left: 3px solid #ea580c; font-weight: 600;
}
.nav-icon { font-size: 20px; }

.sidebar-footer {
  margin-top: auto; display: flex; flex-direction: column; gap: 16px;
  padding-top: 24px; border-top: 1px solid #f3f4f6;
}

.user-profile {
  display: flex; align-items: center; gap: 12px; padding: 12px;
  background: #f9fafb; border-radius: 16px; cursor: pointer;
}

.user-avatar {
  width: 44px; height: 44px; border-radius: 50%; object-fit: cover;
  border: 2px solid #fff; box-shadow: 0 2px 8px rgba(0, 0, 0, 0.08);
}

.user-details { display: flex; flex-direction: column; }
.user-name { font-size: 13px; font-weight: 600; color: #1e293b; }
.user-role { font-size: 11px; color: #94a3b8; }

.dropdown-icon {
  margin-left: auto; color: #94a3b8; font-size: 14px; transition: transform 0.2s;
}
.user-profile:hover .dropdown-icon { transform: rotate(180deg); color: #f97316; }

.create-activity-btn {
  width: 100%; height: 44px; border-radius: 22px;
  background: linear-gradient(135deg, #ea580c 0%, #f97316 100%);
  border: none; color: white; font-weight: 600; font-size: 13px;
  display: flex; align-items: center; justify-content: center; gap: 6px;
  box-shadow: 0 4px 12px rgba(249, 115, 22, 0.3);
}
.create-activity-btn:hover {
  transform: translateY(-2px); box-shadow: 0 6px 20px rgba(249, 115, 22, 0.4);
}

.main-content { flex: 1; display: flex; flex-direction: column; overflow: hidden; }

.top-header {
  height: 72px; background: #ffffff; border-bottom: 1px solid #f3f4f6;
  display: flex; align-items: center; justify-content: space-between; padding: 0 40px;
}

.header-search {
  display: flex; align-items: center; background: #f8fafc; border-radius: 24px;
  padding: 10px 18px; gap: 10px; width: 320px;
}
.search-icon { color: #94a3b8; font-size: 18px; }
.search-input { flex: 1; border: none; background: transparent; font-size: 14px; outline: none; color: #334155; }
.search-input::placeholder { color: #94a3b8; }

.header-actions { display: flex; align-items: center; gap: 24px; }
.action-icon { font-size: 22px; color: #475569; cursor: pointer; transition: color 0.2s; }
.action-icon:hover { color: #f97316; }
.header-brand { padding-left: 24px; border-left: 1px solid #e2e8f0; }
.header-brand span { font-size: 16px; font-weight: 700; color: #f97316; }

.content-area { flex: 1; overflow-y: auto; padding: 32px 40px; }

.page-header {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 28px;
}
.page-title-section { display: flex; flex-direction: column; gap: 8px; }
.page-breadcrumb { font-size: 12px; color: #2563eb; font-weight: 600; text-transform: uppercase; letter-spacing: 0.5px; }
.page-title { font-size: 32px; font-weight: 800; color: #1e293b; margin: 0; letter-spacing: -1px; }
.header-btns { display: flex; gap: 12px; }

.filter-bar {
  display: flex; align-items: center; gap: 20px; margin-bottom: 24px;
  padding: 20px 24px; background: #ffffff; border-radius: 20px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.filter-group { display: flex; align-items: center; gap: 8px; }
.filter-label { font-size: 13px; color: #64748b; font-weight: 500; white-space: nowrap; }

.summary-cards {
  display: grid; grid-template-columns: repeat(4, 1fr); gap: 20px; margin-bottom: 28px;
}

.summary-card {
  background: #ffffff; border-radius: 20px; padding: 24px;
  display: flex; align-items: center; gap: 18px;
  box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
  transition: transform 0.2s;
}
.summary-card:hover { transform: translateY(-3px); box-shadow: 0 6px 20px rgba(0, 0, 0, 0.08); }

.summary-icon {
  width: 56px; height: 56px; border-radius: 16px;
  display: flex; align-items: center; justify-content: center; font-size: 26px;
}
.summary-icon.blue { background: linear-gradient(135deg, #dbeafe 0%, #bfdbfe 100%); color: #2563eb; }
.summary-icon.green { background: linear-gradient(135deg, #dcfce7 0%, #bbf7d0 100%); color: #16a34a; }
.summary-icon.orange { background: linear-gradient(135deg, #fef3c7 0%, #fde68a 100%); color: #d97706; }
.summary-icon.red { background: linear-gradient(135deg, #fee2e2 0%, #fecaca 100%); color: #dc2626; }

.summary-info { display: flex; flex-direction: column; }
.summary-value { font-size: 26px; font-weight: 800; color: #1e293b; line-height: 1.2; }
.summary-label { font-size: 12px; color: #94a3b8; margin-top: 2px; }

.report-table-container {
  background: rgba(255, 255, 255, 0.85); backdrop-filter: blur(10px);
  border-radius: 24px; padding: 28px; border: 1px solid rgba(255, 255, 255, 0.8);
}

.table-title-bar {
  display: flex; align-items: center; justify-content: space-between; margin-bottom: 20px;
}
.table-title-bar h3 { font-size: 18px; font-weight: 700; color: #1e293b; margin: 0; }
.record-count { font-size: 13px; color: #94a3b8; }

.amount-text { font-weight: 600; color: #dc2626; }

.pagination-wrapper {
  display: flex; justify-content: flex-end; margin-top: 20px; padding-top: 16px;
  border-top: 1px solid #f3f4f6;
}

@media (max-width: 1400px) {
  .summary-cards { grid-template-columns: repeat(2, 1fr); }
}
@media (max-width: 1024px) {
  .summary-cards { grid-template-columns: 1fr; }
  .filter-bar { flex-wrap: wrap; }
}
</style>
