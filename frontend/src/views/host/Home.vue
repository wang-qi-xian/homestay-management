<template>
  <div class="layout">
    <nav class="navbar">
      <div class="nav-brand">民宿管理平台 - 房东端</div>
      <div class="nav-links">
        <router-link to="/host">首页</router-link>
        <router-link to="/host/rooms">我的民宿</router-link>
        <router-link to="/host/orders">订单管理</router-link>
        <a href="#" @click.prevent="logout">退出</a>
      </div>
      <div class="nav-user">欢迎，{{ user?.nickname || user?.username }}</div>
    </nav>
    <main class="main-content">
      <div class="welcome">
        <h1>房东管理中心</h1>
        <p>管理您的民宿和订单</p>
      </div>
      <div class="stats">
        <div class="stat-card">
          <div class="stat-num">{{ stats.rooms }}</div>
          <div class="stat-label">我的民宿</div>
        </div>
        <div class="stat-card">
          <div class="stat-num">{{ stats.pendingOrders }}</div>
          <div class="stat-label">待处理订单</div>
        </div>
        <div class="stat-card">
          <div class="stat-num">{{ stats.totalOrders }}</div>
          <div class="stat-label">总订单数</div>
        </div>
      </div>
    </main>
  </div>
</template>

<script setup>
import { ref, computed, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../../api'

const router = useRouter()
const user = computed(() => JSON.parse(localStorage.getItem('user') || 'null'))
const stats = ref({ rooms: 0, pendingOrders: 0, totalOrders: 0 })

const loadStats = async () => {
  const [roomsRes, ordersRes] = await Promise.all([
    api.get('/rooms/host'),
    api.get('/orders/host')
  ])
  if (roomsRes.code === 200) stats.value.rooms = roomsRes.data.length
  if (ordersRes.code === 200) {
    stats.value.totalOrders = ordersRes.data.length
    stats.value.pendingOrders = ordersRes.data.filter(o => o.status === 'PENDING').length
  }
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
.nav-brand { font-size: 18px; font-weight: bold; color: #27ae60; }
.nav-links a { margin-left: 24px; color: #555; text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #27ae60; }
.nav-user { font-size: 14px; color: #888; }
.main-content { padding: 40px 24px; }
.welcome { text-align: center; padding: 40px 20px; }
.welcome h1 { font-size: 28px; color: #333; margin-bottom: 8px; }
.welcome p { color: #888; font-size: 16px; }
.stats { display: flex; gap: 24px; justify-content: center; margin-top: 32px; }
.stat-card { background: #fff; padding: 32px 48px; border-radius: 12px; box-shadow: 0 2px 12px rgba(0,0,0,0.06); text-align: center; }
.stat-num { font-size: 36px; font-weight: bold; color: #27ae60; }
.stat-label { font-size: 14px; color: #888; margin-top: 8px; }
</style>
