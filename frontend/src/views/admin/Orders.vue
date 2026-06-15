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
      <h2>订单管理</h2>
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th><th>游客ID</th><th>房间ID</th><th>房东ID</th><th>入住</th><th>离店</th><th>总价</th><th>状态</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="order in orders" :key="order.id">
            <td>{{ order.id }}</td>
            <td>{{ order.guestId }}</td>
            <td>{{ order.roomId }}</td>
            <td>{{ order.hostId }}</td>
            <td>{{ order.checkIn }}</td>
            <td>{{ order.checkOut }}</td>
            <td>¥{{ order.totalPrice }}</td>
            <td :class="statusClass(order.status)">{{ statusText(order.status) }}</td>
          </tr>
        </tbody>
      </table>
    </main>
  </div>
</template>

<script setup>
import { ref, onMounted } from 'vue'
import { useRouter } from 'vue-router'
import api from '../../api'

const router = useRouter()
const orders = ref([])

const statusText = (s) => ({ PENDING: '待确认', CONFIRMED: '已确认', CANCELLED: '已取消', COMPLETED: '已完成' }[s] || s)
const statusClass = (s) => ({ PENDING: 'pending', CONFIRMED: 'confirmed', CANCELLED: 'cancelled', COMPLETED: 'completed' }[s] || '')

const loadOrders = async () => {
  const res = await api.get('/orders/admin')
  if (res.code === 200) orders.value = res.data
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(loadOrders)
</script>

<style scoped>
.layout { min-height: 100vh; background: #f5f6fa; }
.navbar { display: flex; align-items: center; justify-content: space-between; padding: 0 24px; height: 60px; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
.nav-brand { font-size: 18px; font-weight: bold; color: #e74c3c; }
.nav-links a { margin-left: 24px; color: #555; text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #e74c3c; }
.main-content { padding: 24px; }
h2 { margin-bottom: 20px; color: #333; }
.data-table { width: 100%; border-collapse: collapse; background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.data-table th, .data-table td { padding: 12px 16px; text-align: left; border-bottom: 1px solid #f0f0f0; font-size: 14px; }
.data-table th { background: #f8f9fa; color: #555; font-weight: 600; }
.pending { color: #856404; }
.confirmed { color: #155724; }
.cancelled { color: #721c24; }
.completed { color: #004085; }
</style>
