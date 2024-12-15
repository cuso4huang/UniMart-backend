import Vue from 'vue'
import VueRouter from 'vue-router'
import TransactionList from '@/views/TransactionList.vue'
import { getToken } from '@/utils/auth'
import UserProfile from '@/views/UserProfile.vue'

Vue.use(VueRouter)

const routes = [
  {
    path: '/',
    name: 'Home',
    component: () => import('../views/Home.vue')
  },
  {
    path: '/login',
    name: 'Login',
    component: () => import('../views/Login.vue')
  },
  {
    path: '/register',
    name: 'Register',
    component: () => import('../views/Register.vue')
  },
  {
    path: '/product/:id',
    name: 'ProductDetail',
    component: () => import('../views/ProductDetail.vue')
  },
  {
    path: '/user/profile',
    name: 'UserProfile',
    component: () => import('@/views/UserProfile.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/transactions',
    name: 'TransactionList',
    component: TransactionList,
    meta: { requiresAuth: true }
  },
  {
    path: '/publish',
    name: 'PublishProduct',
    component: () => import('@/views/PublishProduct.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/my',
    name: 'MyProducts',
    component: () => import('@/views/MyProducts.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/product/edit/:id',
    name: 'ProductEdit',
    component: () => import('../views/ProductEdit.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/transaction/:id',
    name: 'TransactionDetail',
    component: () => import('../views/TransactionDetail.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/profile',
    name: 'Profile',
    component: UserProfile,
    meta: { requiresAuth: true }
  },
  {
    path: '/messages',
    name: 'MessageList',
    component: () => import('@/views/MessageList.vue'),
    meta: { requiresAuth: true }
  },
  {
    path: '/message/:receiverId/:productId?',
    name: 'MessagePanel',
    component: () => import('@/views/MessagePanel.vue'),
    meta: { requiresAuth: true }
  }
]

const router = new VueRouter({
  mode: 'history',
  base: process.env.BASE_URL,
  routes
})

router.beforeEach((to, from, next) => {
  const hasToken = getToken()

  if (to.matched.some(record => record.meta.requiresAuth)) {
    if (!hasToken || hasToken === 'undefined' || hasToken === 'null') {
      next({
        path: '/login',
        query: { redirect: to.fullPath }
      })
    } else {
      next()
    }
  } else {
    next()
  }
})

export default router 