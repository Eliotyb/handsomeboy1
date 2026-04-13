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
