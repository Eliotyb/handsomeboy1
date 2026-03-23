<template>
  <div class="home-container">
    <el-card class="welcome-card">
      <template #header>
        <div class="card-header">
          <span>欢迎使用社区团购秒杀后台管理系统</span>
        </div>
      </template>
      <div class="welcome-content">
        <p>本系统基于Spring Boot + Vue + MySQL + Redis技术栈开发，提供以下功能：</p>
        <ul>
          <li>用户管理：添加、编辑、删除用户信息</li>
          <li>商品管理：添加、编辑、删除商品信息</li>
          <li>秒杀活动：创建、管理秒杀活动</li>
          <li>订单管理：查看、处理订单信息</li>
        </ul>
      </div>
    </el-card>
  </div>
</template>

<script>
export default {
  name: 'Home'
}
</script>

<style scoped>
.home-container {
  padding: 20px;
}

.welcome-card {
  margin-bottom: 20px;
}

.card-header {
  display: flex;
  justify-content: space-between;
  align-items: center;
}

.welcome-content {
  padding: 20px;
}

.welcome-content ul {
  margin-top: 10px;
  padding-left: 20px;
}

.welcome-content li {
  margin-bottom: 10px;
}
</style>