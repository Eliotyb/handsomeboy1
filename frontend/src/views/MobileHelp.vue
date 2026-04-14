<template>
  <div class="mobile-container">
    <header class="page-header">
      <div class="header-left" @click="$router.back()">
        <el-icon class="back-icon"><ArrowLeft /></el-icon>
      </div>
      <h1 class="page-title">帮助中心</h1>
      <div class="header-right"></div>
    </header>

    <div class="help-content">
      <div class="search-box">
        <el-input v-model="searchKeyword" placeholder="搜索问题..." prefix-icon="Search" clearable />
      </div>

      <div class="help-categories">
        <div class="category-item" v-for="(category, index) in categories" :key="index"
             @click="viewCategory(category)">
          <el-icon class="category-icon" :style="{ color: category.color }">
            <component :is="category.icon" />
          </el-icon>
          <span class="category-name">{{ category.name }}</span>
          <el-icon class="arrow-right"><ArrowRight /></el-icon>
        </div>
      </div>

      <div class="hot-topics">
        <h3 class="section-title">热门问题</h3>
        <div class="topic-list">
          <div class="topic-item" v-for="(topic, index) in hotTopics" :key="index"
               @click="viewTopic(topic)">
            <el-icon class="topic-icon"><QuestionFilled /></el-icon>
            <span class="topic-text">{{ topic.title }}</span>
            <el-icon class="arrow-icon"><ArrowRight /></el-icon>
          </div>
        </div>
      </div>

      <div class="feedback-section">
        <el-button type="primary" round size="large" @click="showFeedback = true">
          <el-icon><EditPen /></el-icon>
          问题反馈
        </el-button>
      </div>
    </div>

    <el-dialog v-model="showFeedback" title="问题反馈" width="90%">
      <el-form :model="feedbackForm" label-position="top">
        <el-form-item label="问题描述">
          <el-input v-model="feedbackForm.content" type="textarea" :rows="4"
                    placeholder="请详细描述您遇到的问题..." />
        </el-form-item>
        <el-form-item label="联系方式（选填）">
          <el-input v-model="feedbackForm.contact" placeholder="手机号或邮箱" />
        </el-form-item>
      </el-form>
      <template #footer>
        <el-button @click="showFeedback = false">取消</el-button>
        <el-button type="primary" @click="submitFeedback">提交反馈</el-button>
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
import { ArrowLeft, ArrowRight, Search, QuestionFilled, EditPen, ShoppingCart, Goods, House, UserFilled,
         Wallet, Ticket, Setting } from '@element-plus/icons-vue'
import { ElMessage } from 'element-plus'

export default {
  name: 'MobileHelp',
  components: {
    ArrowLeft,
    ArrowRight,
    Search,
    QuestionFilled,
    EditPen,
    ShoppingCart,
    Goods,
    House,
    UserFilled
  },
  data() {
    return {
      searchKeyword: '',
      showFeedback: false,
      feedbackForm: { content: '', contact: '' },
      categories: [
        { name: '账号相关', icon: 'UserFilled', color: '#ff6b6b' },
        { name: '订单问题', icon: 'ShoppingCart', color: '#4caf50' },
        { name: '支付退款', icon: 'Wallet', color: '#ff9800' },
        { name: '优惠券', icon: 'Ticket', color: '#9c27b0' },
        { name: '配送物流', icon: 'Goods', color: '#2196f3' },
        { name: '账户设置', icon: 'Setting', color: '#607d8b' }
      ],
      hotTopics: [
        { id: 1, title: '如何注册新账号？' },
        { id: 2, title: '忘记密码怎么办？' },
        { id: 3, title: '如何修改收货地址？' },
        { id: 4, title: '订单状态说明' },
        { id: 5, title: '退款流程详解' }
      ]
    }
  },
  methods: {
    viewCategory(category) {
      ElMessage.info(`查看${category.name}相关问题`)
    },
    viewTopic(topic) {
      ElMessage.info(`查看：${topic.title}`)
    },
    submitFeedback() {
      if (!this.feedbackForm.content) {
        ElMessage.warning('请输入问题描述')
        return
      }
      ElMessage.success('感谢您的反馈，我们会尽快处理！')
      this.showFeedback = false
      this.feedbackForm = { content: '', contact: '' }
    }
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
  width: 32px;
}

.help-content {
  padding: 16px;
}

.search-box {
  margin-bottom: 16px;
}

.help-categories {
  background: white;
  border-radius: 12px;
  padding: 12px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.category-item {
  display: flex;
  align-items: center;
  padding: 14px 12px;
  border-bottom: 1px solid #f5f5f5;
  cursor: pointer;
  transition: background 0.3s;
}

.category-item:last-child {
  border-bottom: none;
}

.category-item:hover {
  background: #fafafa;
}

.category-icon {
  font-size: 22px;
  margin-right: 12px;
  width: 32px;
  text-align: center;
}

.category-name {
  flex: 1;
  font-size: 15px;
  color: #333;
}

.arrow-right {
  color: #ccc;
  font-size: 16px;
}

.hot-topics {
  background: white;
  border-radius: 12px;
  padding: 20px;
  margin-bottom: 16px;
  box-shadow: 0 2px 8px rgba(0, 0, 0, 0.06);
}

.section-title {
  font-size: 16px;
  font-weight: 600;
  color: #333;
  margin: 0 0 16px;
}

.topic-list {
  display: flex;
  flex-direction: column;
  gap: 12px;
}

.topic-item {
  display: flex;
  align-items: center;
  gap: 10px;
  padding: 12px;
  background: #fafafa;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s;
}

.topic-item:hover {
  background: #fff5f5;
  transform: translateX(4px);
}

.topic-icon {
  color: #ff6b6b;
  font-size: 18px;
}

.topic-text {
  flex: 1;
  font-size: 14px;
  color: #333;
}

.arrow-icon {
  color: #ccc;
  font-size: 14px;
}

.feedback-section {
  text-align: center;
  padding: 20px 0;
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
