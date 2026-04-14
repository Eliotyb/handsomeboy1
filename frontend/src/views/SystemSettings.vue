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
        <div class="nav-item" :class="{ active: activeNav === 'users' }" @click="$router.push('/users')">
          <el-icon class="nav-icon"><UserFilled /></el-icon>
          <span>用户管理</span>
        </div>
        <div class="nav-item" :class="{ active: activeNav === 'settings' }" @click="activeNav = 'settings'">
          <el-icon class="nav-icon"><Setting /></el-icon>
          <span>系统设置</span>
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
          <input type="text" placeholder="搜索设置项..." class="search-input" />
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
            <div class="page-breadcrumb">系统管理 / 设置</div>
            <h1 class="page-title">系统设置</h1>
          </div>
        </div>

        <el-tabs v-model="activeTab" class="settings-tabs" type="border-card">
          <el-tab-pane label="基本设置" name="basic">
            <div class="settings-section">
              <h3 class="section-title">站点信息</h3>
              <div class="setting-item">
                <label class="setting-label">站点名称</label>
                <el-input v-model="basicForm.siteName" placeholder="请输入站点名称" style="width: 360px;" />
              </div>
              <div class="setting-item">
                <label class="setting-label">站点描述</label>
                <el-input v-model="basicForm.siteDesc" type="textarea" :rows="3" placeholder="请输入站点描述" style="width: 480px;" />
              </div>
              <div class="setting-item">
                <label class="setting-label">联系邮箱</label>
                <el-input v-model="basicForm.email" placeholder="请输入联系邮箱" style="width: 360px;" />
              </div>
              <div class="setting-item">
                <label class="setting-label">客服电话</label>
                <el-input v-model="basicForm.phone" placeholder="请输入客服电话" style="width: 240px;" />
              </div>
              <div class="setting-item">
                <label class="setting-label">ICP备案号</label>
                <el-input v-model="basicForm.icp" placeholder="请输入ICP备案号" style="width: 320px;" />
              </div>
            </div>

            <div class="settings-section">
              <h3 class="section-title">SEO设置</h3>
              <div class="setting-item">
                <label class="setting-label">页面标题</label>
                <el-input v-model="basicForm.seoTitle" placeholder="请输入页面标题" style="width: 480px;" />
              </div>
              <div class="setting-item">
                <label class="setting-label">关键词</label>
                <el-select v-model="basicForm.seoKeywords" multiple filterable allow-create
                  default-first-option placeholder="输入关键词后回车添加" style="width: 480px;">
                </el-select>
              </div>
              <div class="setting-item">
                <label class="setting-label">页面描述</label>
                <el-input v-model="basicForm.seoDesc" type="textarea" :rows="2" placeholder="请输入页面描述" style="width: 480px;" />
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="秒杀配置" name="seckill">
            <div class="settings-section">
              <h3 class="section-title">秒杀规则</h3>
              <div class="setting-item">
                <label class="setting-label">每日最大活动数</label>
                <el-input-number v-model="seckillForm.maxDailyActivities" :min="1" :max="50" />
                <span class="setting-hint">限制每天可创建的秒杀活动数量</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">单个活动最大商品数</label>
                <el-input-number v-model="seckillForm.maxProductsPerActivity" :min="1" :max="100" />
                <span class="setting-hint">每个秒杀活动最多包含的商品数量</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">默认活动时长（小时）</label>
                <el-input-number v-model="seckillForm.defaultDurationHours" :min="1" :max="168" />
                <span class="setting-hint">新建活动时的默认持续时长</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">预热时间（分钟）</label>
                <el-input-number v-model="seckillForm.preheatMinutes" :min="0" :max="1440" />
                <span class="setting-hint">活动开始前多久显示预告信息</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">每人限购数量</label>
                <el-input-number v-model="seckillForm.perUserLimit" :min="1" :max="99" />
                <span class="setting-hint">每个用户在单个活动中最多购买的数量</span>
              </div>
            </div>

            <div class="settings-section">
              <h3 class="section-title">库存与价格</h3>
              <div class="setting-item">
                <label class="setting-label">最低折扣比例（%）</label>
                <el-slider v-model="seckillForm.minDiscountPercent" :min="10" :max="90" show-input :show-input-controls="false" input-size="small" style="width: 400px;" />
              </div>
              <div class="setting-item">
                <label class="setting-label">库存预警阈值</label>
                <el-input-number v-model="seckillForm.stockWarningThreshold" :min="1" :max="1000" />
                <span class="setting-hint">低于此数量时触发库存预警提醒</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">允许超卖</label>
                <el-switch v-model="seckillForm.allowOversell" active-text="开启" inactive-text="关闭" />
                <span class="setting-hint">开启后允许订单量超过实际库存（需后续补货）</span>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="通知设置" name="notify">
            <div class="settings-section">
              <h3 class="section-title">消息推送</h3>
              <div class="setting-item">
                <label class="setting-label">活动开始提醒</label>
                <el-switch v-model="notifyForm.activityStartNotify" />
                <span class="setting-hint">秒杀活动开始时向关注用户推送消息</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">订单状态变更</label>
                <el-switch v-model="notifyForm.orderStatusNotify" />
                <span class="setting-hint">订单支付、发货、完成等状态变化时通知用户</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">库存预警通知</label>
                <el-switch v-model="notifyForm.stockWarnNotify" />
                <span class="setting-hint">商品库存低于预警值时通知管理员</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">新用户注册通知</label>
                <el-switch v-model="notifyForm.newUserNotify" />
                <span class="setting-hint">有新用户注册时通知管理员</span>
              </div>
            </div>

            <div class="settings-section">
              <h3 class="section-title">短信/邮件配置</h3>
              <div class="setting-item">
                <label class="setting-label">短信服务商</label>
                <el-select v-model="notifyForm.smsProvider" placeholder="选择短信服务商" style="width: 280px;">
                  <el-option label="阿里云短信" value="aliyun" />
                  <el-option label="腾讯云短信" value="tencent" />
                  <el-option label="未启用" value="none" />
                </el-select>
              </div>
              <div class="setting-item">
                <label class="setting-label">邮件SMTP服务器</label>
                <el-input v-model="notifyForm.smtpHost" placeholder="smtp.example.com" style="width: 320px;" />
              </div>
              <div class="setting-item">
                <label class="setting-label">邮件发送地址</label>
                <el-input v-model="notifyForm.smtpFrom" placeholder="noreply@example.com" style="width: 320px;" />
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="安全设置" name="security">
            <div class="settings-section">
              <h3 class="section-title">登录安全</h3>
              <div class="setting-item">
                <label class="setting-label">Token过期时间（分钟）</label>
                <el-input-number v-model="securityForm.tokenExpireMinutes" :min="30" :max="1440" :step="30" />
                <span class="setting-hint">用户登录Token的有效时长</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">密码最小长度</label>
                <el-input-number v-model="securityForm.minPasswordLength" :min="6" :max="32" />
              </div>
              <div class="setting-item">
                <label class="setting-label">登录失败锁定次数</label>
                <el-input-number v-model="securityForm.maxLoginFailures" :min="3" :max="20" />
                <span class="setting-hint">连续失败多少次后临时锁定账户</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">验证码开关</label>
                <el-switch v-model="securityForm.captchaEnabled" active-text="开启" inactive-text="关闭" />
              </div>
            </div>

            <div class="settings-section">
              <h3 class="section-title">接口安全</h3>
              <div class="setting-item">
                <label class="setting-label">API限流（请求/分钟）</label>
                <el-input-number v-model="securityForm.rateLimitPerMinute" :min="30" :max="10000" :step="10" />
                <span class="setting-hint">单IP每分钟最大API请求次数</span>
              </div>
              <div class="setting-item">
                <label class="setting-label">防刷机制</label>
                <el-switch v-model="securityForm.antiScrapingEnabled" active-text="开启" inactive-text="关闭" />
                <span class="setting-hint">检测异常高频请求并自动拦截</span>
              </div>
            </div>
          </el-tab-pane>

          <el-tab-pane label="关于系统" name="about">
            <div class="about-container">
              <div class="about-logo">
                <div class="logo-circle">P</div>
                <h2>Pulse Velocity 秒杀系统</h2>
                <p class="version">版本号：v2.1.0</p>
              </div>
              <el-divider />
              <div class="about-info">
                <div class="info-row">
                  <span class="info-label">系统名称</span>
                  <span class="info-value">社团团购秒杀后台管理系统</span>
                </div>
                <div class="info-row">
                  <span class="info-label">技术架构</span>
                  <span class="info-value">Spring Boot 2.7 + Vue 3 + Element Plus + MySQL + Redis</span>
                </div>
                <div class="info-row">
                  <span class="info-label">前端框架</span>
                  <span class="info-value">Vue 3.5 + Vite 6 + Element Plus 2.8</span>
                </div>
                <div class="info-row">
                  <span class="info-label">后端框架</span>
                  <span class="info-value">Spring Boot 2.7.15 + MyBatis Plus 3.5</span>
                </div>
                <div class="info-row">
                  <span class="info-label">数据库</span>
                  <span class="info-value">MySQL 8.0 (Docker)</span>
                </div>
                <div class="info-row">
                  <span class="info-label">缓存服务</span>
                  <span class="info-value">Redis (Docker)</span>
                </div>
                <div class="info-row">
                  <span class="info-label">构建时间</span>
                  <span class="info-value">{{ buildTime }}</span>
                </div>
              </div>
              <el-divider />
              <div class="about-footer">
                <p>&copy; 2024 Pulse Velocity Community Groupon System. All rights reserved.</p>
              </div>
            </div>
          </el-tab-pane>
        </el-tabs>

        <div class="save-bar">
          <el-button @click="resetSettings">重置</el-button>
          <el-button type="primary" @click="saveSettings" :loading="saving">
            <el-icon><Check /></el-icon>保存设置
          </el-button>
        </div>
      </div>
    </main>
  </div>
</template>

<script>
import {
  Search, Bell, Setting, Plus, Grid, Lightning, UserFilled,
  Box, Document, ArrowDown, SwitchButton, Check
} from '@element-plus/icons-vue'
import axios from 'axios'
import { ElMessage, ElMessageBox } from 'element-plus'

export default {
  name: 'SystemSettings',
  components: {
    Search, Bell, Setting, Plus, Grid, Lightning, UserFilled,
    Box, Document, ArrowDown, SwitchButton, Check
  },
  data() {
    return {
      activeNav: 'settings',
      activeTab: 'basic',
      saving: false,
      avatarUrl: 'https://trae-api-cn.mchost.guru/api/ide/v1/text_to_image?prompt=professional%20avatar%20portrait%2C%20formal%20photo%2C%20business%20person%2C%20square%20image&image_size=square',
      buildTime: new Date().toLocaleString(),
      basicForm: {
        siteName: '社团团购秒杀平台',
        siteDesc: '专业的社团团购秒杀管理系统，为校园用户提供优质优惠商品',
        email: 'admin@pulsecity.com',
        phone: '400-888-9999',
        icp: '京ICP备XXXXXXXX号',
        seoTitle: '社团团购秒杀平台 - 校园优选 品质生活',
        seoKeywords: ['社团团购', '秒杀', '校园优惠', '学生专享'],
        seoDesc: '社团团购秒杀平台，为高校师生提供高品质商品的限时秒杀服务'
      },
      seckillForm: {
        maxDailyActivities: 10,
        maxProductsPerActivity: 20,
        defaultDurationHours: 24,
        preheatMinutes: 30,
        perUserLimit: 3,
        minDiscountPercent: 50,
        stockWarningThreshold: 10,
        allowOversell: false
      },
      notifyForm: {
        activityStartNotify: true,
        orderStatusNotify: true,
        stockWarnNotify: true,
        newUserNotify: false,
        smsProvider: 'aliyun',
        smtpHost: '',
        smtpFrom: ''
      },
      securityForm: {
        tokenExpireMinutes: 120,
        minPasswordLength: 8,
        maxLoginFailures: 5,
        captchaEnabled: true,
        rateLimitPerMinute: 120,
        antiScrapingEnabled: true
      }
    }
  },
  methods: {
    saveSettings() {
      this.saving = true
      setTimeout(() => {
        this.saving = false
        ElMessage.success('系统设置保存成功！')
        localStorage.setItem('systemSettings', JSON.stringify({
          basic: this.basicForm,
          seckill: this.seckillForm,
          notify: this.notifyForm,
          security: this.securityForm
        }))
      }, 800)
    },
    resetSettings() {
      ElMessageBox.confirm('确定要重置所有设置为默认值吗？', '确认重置', {
        confirmButtonText: '确定重置',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        Object.assign(this.$data.basicForm, {
          siteName: '社团团购秒杀平台',
          siteDesc: '专业的社团团购秒杀管理系统',
          email: 'admin@pulsecity.com',
          phone: '400-888-9999',
          icp: '',
          seoTitle: '社团团购秒杀平台',
          seoKeywords: ['社团团购', '秒杀'],
          seoDesc: ''
        })
        ElMessage.info('已重置为默认设置')
      }).catch(() => {})
    },
    handleUserCommand(command) {
      switch (command) {
        case 'profile':
          ElMessage.info('个人中心功能开发中')
          break
        case 'settings':
          ElMessage.info('当前已在系统设置页')
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

.settings-tabs {
  border-radius: 20px; overflow: hidden; box-shadow: 0 2px 12px rgba(0, 0, 0, 0.04);
}
.settings-tabs :deep(.el-tabs__header) {
  background: #fafafa;
}
.settings-tabs :deep(.el-tabs__item) {
  font-size: 14px; font-weight: 500; padding: 0 24px; height: 48px; line-height: 48px;
}
.settings-tabs :deep(.el-tabs__item.is-active) {
  color: #ea580c; font-weight: 700;
}
.settings-tabs :deep(.el-tabs__active-bar) {
  background: #ea580c; height: 3px;
}

.settings-section {
  padding: 24px 0;
  border-bottom: 1px solid #f3f4f6;
}
.settings-section:last-child { border-bottom: none; }

.section-title {
  font-size: 15px; font-weight: 700; color: #1e293b; margin: 0 0 20px 0;
  padding-left: 12px; border-left: 3px solid #f97316;
}

.setting-item {
  display: flex; align-items: center; gap: 16px; margin-bottom: 18px; flex-wrap: wrap;
}

.setting-label {
  width: 140px; font-size: 13px; color: #475569; font-weight: 500; text-align: right; flex-shrink: 0;
}

.setting-hint {
  font-size: 12px; color: #94a3b8; margin-left: 4px;
}

.save-bar {
  display: flex; justify-content: flex-end; gap: 12px; margin-top: 24px;
  padding: 20px 0; border-top: 1px solid #f3f4f6;
}

.about-container {
  max-width: 640px; margin: 0 auto; padding: 40px 20px; text-align: center;
}

.about-logo { margin-bottom: 20px; }

.logo-circle {
  width: 72px; height: 72px; border-radius: 50%;
  background: linear-gradient(135deg, #ea580c 0%, #f97316 100%);
  color: white; font-size: 36px; font-weight: 900; line-height: 72px;
  margin: 0 auto 16px; box-shadow: 0 8px 24px rgba(234, 88, 12, 0.3);
}

.about-logo h2 {
  font-size: 22px; color: #1e293b; margin: 0 0 8px 0;
}
.version {
  font-size: 13px; color: #94a3b8;
}

.about-info { text-align: left; }
.info-row {
  display: flex; padding: 12px 0; border-bottom: 1px dashed #f0f0f0;
}
.info-row:last-of-type { border-bottom: none; }
.info-label {
  width: 120px; font-size: 13px; color: #64748b; font-weight: 500; flex-shrink: 0;
}
.info-value {
  font-size: 13px; color: #334155; word-break: break-all;
}

.about-footer p {
  font-size: 12px; color: #cbd5e1; margin: 0;
}

@media (max-width: 1024px) {
  .setting-label { width: 110px; text-align: left; }
  .setting-item { flex-direction: column; align-items: flex-start; }
}
</style>
