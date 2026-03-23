import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/users',
    name: 'UserList',
    component: () => import('../views/UserList.vue')
  },
  {
    path: '/products',
    name: 'ProductList',
    component: () => import('../views/ProductList.vue')
  },
  {
    path: '/seckill-activities',
    name: 'SeckillActivityList',
    component: () => import('../views/SeckillActivityList.vue')
  },
  {
    path: '/orders',
    name: 'OrderList',
    component: () => import('../views/OrderList.vue')
  }
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

export default router