import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Login',
    component: () => import('../views/Login.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/dashboard',
    name: 'Home',
    component: () => import('../views/Home.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/login',
    name: 'MobileLogin',
    component: () => import('../views/MobileLogin.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/mobile/register',
    name: 'MobileRegister',
    component: () => import('../views/MobileRegister.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/mobile',
    name: 'MobileHome',
    component: () => import('../views/MobileHome.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/category',
    name: 'MobileCategory',
    component: () => import('../views/MobileCategory.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/cart',
    name: 'MobileCart',
    component: () => import('../views/MobileCart.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/profile',
    name: 'MobileProfile',
    component: () => import('../views/MobileProfile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/seckill-detail',
    name: 'SeckillDetail',
    component: () => import('../views/SeckillDetail.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/seckill-success',
    name: 'SeckillSuccess',
    component: () => import('../views/SeckillSuccess.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/pay-success',
    name: 'MobilePaySuccess',
    component: () => import('../views/MobilePaySuccess.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/address',
    name: 'MobileAddress',
    component: () => import('../views/MobileAddress.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/coupon',
    name: 'MobileCoupon',
    component: () => import('../views/MobileCoupon.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/favorites',
    name: 'MobileFavorites',
    component: () => import('../views/MobileFavorites.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/history',
    name: 'MobileHistory',
    component: () => import('../views/MobileHistory.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/service',
    name: 'MobileService',
    component: () => import('../views/MobileService.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/mobile/help',
    name: 'MobileHelp',
    component: () => import('../views/MobileHelp.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/mobile/about',
    name: 'MobileAbout',
    component: () => import('../views/MobileAbout.vue'),
    meta: { requiresAuth: false }
  },
  {
    path: '/mobile/order-pending',
    name: 'MobileOrderPending',
    component: () => import('../views/MobileOrderPending.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/order-shipping',
    name: 'MobileOrderShipping',
    component: () => import('../views/MobileOrderShipping.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/order-received',
    name: 'MobileOrderReceived',
    component: () => import('../views/MobileOrderReceived.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/order-review',
    name: 'MobileOrderReview',
    component: () => import('../views/MobileOrderReview.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/mobile/after-sale',
    name: 'MobileAfterSale',
    component: () => import('../views/MobileAfterSale.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/users',
    name: 'UserList',
    component: () => import('../views/UserList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/products',
    name: 'ProductList',
    component: () => import('../views/ProductList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/seckill-activities',
    name: 'SeckillActivityList',
    component: () => import('../views/SeckillActivityList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/orders',
    name: 'OrderList',
    component: () => import('../views/OrderList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/data-report',
    name: 'DataReport',
    component: () => import('../views/DataReport.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/system-settings',
    name: 'SystemSettings',
    component: () => import('../views/SystemSettings.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/db-manager',
    name: 'DbManager',
    component: () => import('../views/DbManager.vue'),
    meta: { requiresAuth: true }
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const token = localStorage.getItem('token')
  
  if (to.meta.requiresAuth && !token) {
    if (to.path.startsWith('/mobile')) {
      next('/mobile/login')
    } else {
      next('/')
    }
  } else if (to.path === '/' && token) {
    next('/dashboard')
  } else if (to.path === '/mobile/login' && token) {
    next('/mobile')
  } else {
    next()
  }
})

export default router
