<template>
  <div class="layout">
    <nav class="navbar">
      <div class="nav-brand">民宿管理平台 - 管理端</div>
      <div class="nav-links">
        <router-link to="/admin">首页</router-link>
        <router-link to="/admin/rooms">民宿管理</router-link>
        <router-link to="/admin/orders">订单管理</router-link>
        <router-link to="/admin/users">用户管理</router-link>
        <a href="#" @click.prevent="logout">退出</a>
      </div>
    </nav>
    <main class="main-content">
      <div class="welcome">
        <h1>管理控制台</h1>
        <p>全局管理民宿平台数据</p>
      </div>
      <div class="stats">
        <div class="stat-card" style="border-top: 4px solid #e74c3c;">
          <div class="stat-num">{{ stats.rooms }}</div>
          <div class="stat-label">民宿总数</div>
        </div>
        <div class="stat-card" style="border-top: 4px solid #3498db;">
          <div class="stat-num">{{ stats.orders }}</div>
          <div class="stat-label">订单总数</div>
        </div>
        <div class="stat-card" style="border-top: 4px solid #27ae60;">
          <div class="stat-num">{{ stats.users }}</div>
          <div class="stat-label">用户总数</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../../api'

const router = useRouter()
const stats = ref({ rooms: 0, orders: 0, users: 0 })

const loadStats = async () => {
  const [roomsRes, ordersRes, usersRes] = await Promise.all([
    api.get('/rooms/admin'),
    api.get('/orders/admin'),
    api.get('/users/admin')
  ])
  if (roomsRes.code === 200) stats.value.rooms = roomsRes.data.length
  if (ordersRes.code === 200) stats.value.orders = ordersRes.data.length
  if (usersRes.code === 200) stats.value.users = usersRes.data.length
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(loadStats)
</script>

<style scoped>
.layout { min-height: 100vh; background: #f5f6fa; }
.navbar { display: flex; align-items: center; justify-content: space-between; padding: 0 24px; height: 60px; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
.nav-brand { font-size: 18px; font-weight: bold; color: #e74c3c; }
.nav-links a { margin-left: 24px; color: #555; text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #e74c3c; }
.main-content { padding: 40px 24px; }
.welcome { text-align: center; padding: 40px 20px; }
.welcome h1 { font-size: 28px; color: #333; margin-bottom: 8px; }
.welcome p { color: #888; font-size: 16px; }
.stats { display: flex; gap: 24px; justify-content: center; margin-top: 32px; }
.stat-card { background: #fff; padding: 32px 48px; border-radius: 12px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); text-align: center; }
.stat-num { font-size: 36px; font-weight: bold; color: #333; }
.stat-label { font-size: 14px; color: #888; margin-top: 8px; }
</style>
