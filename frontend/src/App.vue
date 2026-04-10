<template>
  <div class="app-container" v-if="isLoggedIn">
    <el-container style="height: 100vh;">
      <el-header height="64px" class="header">
        <div class="header-left">
          <el-icon class="logo-icon"><Promotion /></el-icon>
          <h1>社区团购秒杀后台管理系统</h1>
        </div>
        <div class="header-right">
          <span class="user-info">
            <el-icon><User /></el-icon>
            {{ userInfo?.name || '管理员' }}
          </span>
          <el-button type="danger" size="small" @click="handleLogout">
            <el-icon><SwitchButton /></el-icon>
            退出登录
          </el-button>
        </div>
      </el-header>
      <el-container>
        <el-aside width="240px" class="aside">
          <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical"
            router
            background-color="#304156"
            text-color="#bfcbd9"
            active-text-color="#409EFF"
          >
            <el-menu-item index="/">
              <el-icon><HomeFilled /></el-icon>
              <span>数据首页</span>
            </el-menu-item>
            <el-menu-item index="/users">
              <el-icon><UserFilled /></el-icon>
              <span>用户管理</span>
            </el-menu-item>
            <el-menu-item index="/products">
              <el-icon><GoodsFilled /></el-icon>
              <span>商品管理</span>
            </el-menu-item>
            <el-menu-item index="/seckill-activities">
              <el-icon><Timer /></el-icon>
              <span>秒杀活动</span>
            </el-menu-item>
            <el-menu-item index="/orders">
              <el-icon><Document /></el-icon>
              <span>订单管理</span>
            </el-menu-item>
          </el-menu>
        </el-aside>
        <el-main class="main">
          <router-view />
        </el-main>
      </el-container>
    </el-container>
  </div>
  <router-view v-else />
</template>

<script>
import { HomeFilled, UserFilled, GoodsFilled, Timer, Document, Promotion, User, SwitchButton } from '@element-plus/icons-vue'
import { ElMessage, ElMessageBox } from 'element-plus'
import axios from 'axios'

export default {
  name: 'App',
  components: {
    HomeFilled,
    UserFilled,
    GoodsFilled,
    Timer,
    Document,
    Promotion,
    User,
    SwitchButton
  },
  data() {
    return {
      activeMenu: '/',
      userInfo: null
    }
  },
  computed: {
    isLoggedIn() {
      return !!localStorage.getItem('token')
    }
  },
  watch: {
    $route: {
      handler(to) {
        this.activeMenu = to.path
      },
      immediate: true
    }
  },
  created() {
    this.loadUserInfo()
  },
  methods: {
    loadUserInfo() {
      const userInfoStr = localStorage.getItem('userInfo')
      if (userInfoStr) {
        this.userInfo = JSON.parse(userInfoStr)
      }
    },
    handleLogout() {
      ElMessageBox.confirm('确定要退出登录吗？', '提示', {
        confirmButtonText: '确定',
        cancelButtonText: '取消',
        type: 'warning'
      }).then(() => {
        localStorage.removeItem('token')
        localStorage.removeItem('userInfo')
        this.userInfo = null
        ElMessage.success('退出登录成功')
        this.$router.push('/login')
      }).catch(() => {})
    }
  }
}
</script>

<style scoped>
.app-container {
  height: 100vh;
  overflow: hidden;
}

.header {
  background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
  color: white;
  display: flex;
  align-items: center;
  justify-content: space-between;
  padding: 0 24px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.15);
}

.header-left {
  display: flex;
  align-items: center;
  gap: 12px;
}

.logo-icon {
  font-size: 32px;
}

.header h1 {
  margin: 0;
  font-size: 20px;
  font-weight: 500;
}

.header-right {
  display: flex;
  align-items: center;
  gap: 16px;
}

.user-info {
  display: flex;
  align-items: center;
  gap: 6px;
  font-size: 14px;
}

.aside {
  background-color: #304156;
  border-right: none;
}

.el-menu-vertical {
  border-right: none;
}

.main {
  background: linear-gradient(135deg, #f5f7fa 0%, #e8ecf1 100%);
  padding: 24px;
  overflow-y: auto;
}
</style>