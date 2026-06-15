import { createRouter, createWebHistory } from 'vue-router'

const routes = [
  { path: '/', redirect: '/login' },
  { path: '/login', component: () => import('../views/Login.vue') },
  { path: '/register', component: () => import('../views/Register.vue') },
  // 游客
  { path: '/guest', component: () => import('../views/guest/Home.vue'), meta: { role: 'GUEST' } },
  { path: '/guest/rooms', component: () => import('../views/guest/Rooms.vue'), meta: { role: 'GUEST' } },
  { path: '/guest/orders', component: () => import('../views/guest/Orders.vue'), meta: { role: 'GUEST' } },
  // 房东
  { path: '/host', component: () => import('../views/host/Home.vue'), meta: { role: 'HOST' } },
  { path: '/host/rooms', component: () => import('../views/host/Rooms.vue'), meta: { role: 'HOST' } },
  { path: '/host/orders', component: () => import('../views/host/Orders.vue'), meta: { role: 'HOST' } },
  // 管理员
  { path: '/admin', component: () => import('../views/admin/Home.vue'), meta: { role: 'ADMIN' } },
  { path: '/admin/rooms', component: () => import('../views/admin/Rooms.vue'), meta: { role: 'ADMIN' } },
  { path: '/admin/orders', component: () => import('../views/admin/Orders.vue'), meta: { role: 'ADMIN' } },
  { path: '/admin/users', component: () => import('../views/admin/Users.vue'), meta: { role: 'ADMIN' } },
]

const router = createRouter({
  history: createWebHistory(),
  routes
})

router.beforeEach((to, from, next) => {
  const user = JSON.parse(localStorage.getItem('user') || 'null')
  if (to.meta.role && (!user || user.role !== to.meta.role)) {
    next('/login')
  } else {
    next()
  }
})

export default router
