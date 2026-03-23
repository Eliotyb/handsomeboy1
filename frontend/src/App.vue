<template>
  <div class="app-container">
    <el-container style="height: 100vh;">
      <el-header height="60px" class="header">
        <h1>社区团购秒杀后台管理系统</h1>
      </el-header>
      <el-container>
        <el-aside width="200px" class="aside">
          <el-menu
            :default-active="activeMenu"
            class="el-menu-vertical-demo"
            router
          >
            <el-menu-item index="/">
              <el-icon><HomeFilled /></el-icon>
              <span>首页</span>
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
              <el-icon><TimerFilled /></el-icon>
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
</template>

<script>
import { HomeFilled, UserFilled, GoodsFilled, TimerFilled, Document } from '@element-plus/icons-vue'

export default {
  name: 'App',
  components: {
    HomeFilled,
    UserFilled,
    GoodsFilled,
    TimerFilled,
    Document
  },
  data() {
    return {
      activeMenu: '/'
    }
  },
  watch: {
    $route: {
      handler(to) {
        this.activeMenu = to.path
      },
      immediate: true
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
  background-color: #409EFF;
  color: white;
  display: flex;
  align-items: center;
  padding-left: 20px;
}

.header h1 {
  margin: 0;
  font-size: 20px;
}

.aside {
  background-color: #f5f7fa;
  border-right: 1px solid #e4e7ed;
}

.main {
  background-color: #f0f2f5;
  padding: 20px;
  overflow-y: auto;
}
</style>