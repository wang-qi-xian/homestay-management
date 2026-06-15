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
    </nav>
    <main class="main-content">
      <h2>订单管理</h2>
      <div v-if="orders.length === 0" class="empty">暂无订单</div>
      <div class="order-list">
        <div v-for="order in orders" :key="order.id" class="order-card">
          <div class="order-header">
            <span class="order-id">订单号：{{ order.id }}</span>
            <span :class="['status', statusClass(order.status)]">{{ statusText(order.status) }}</span>
          </div>
          <div class="order-body">
            <p>房间ID：{{ order.roomId }} | 游客ID：{{ order.guestId }}</p>
            <p>入住：{{ order.checkIn }} ~ 离店：{{ order.checkOut }}</p>
            <p class="total">总价：¥{{ order.totalPrice }}</p>
          </div>
          <div class="order-actions">
            <button v-if="order.status === 'PENDING'" class="btn-confirm" @click="confirmOrder(order.id)">确认</button>
            <button v-if="order.status === 'CONFIRMED'" class="btn-complete" @click="completeOrder(order.id)">完成</button>
            <button v-if="order.status === 'PENDING' || order.status === 'CONFIRMED'" class="btn-cancel" @click="cancelOrder(order.id)">取消</button>
          </div>
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
const orders = ref([])

const statusText = (s) => ({ PENDING: '待确认', CONFIRMED: '已确认', CANCELLED: '已取消', COMPLETED: '已完成' }[s] || s)
const statusClass = (s) => ({ PENDING: 'pending', CONFIRMED: 'confirmed', CANCELLED: 'cancelled', COMPLETED: 'completed' }[s] || '')

const loadOrders = async () => {
  const res = await api.get('/orders/host')
  if (res.code === 200) orders.value = res.data
}

const confirmOrder = async (id) => {
  const res = await api.put(`/orders/${id}/confirm`)
  if (res.code === 200) loadOrders()
  else alert(res.message)
}

const completeOrder = async (id) => {
  const res = await api.put(`/orders/${id}/complete`)
  if (res.code === 200) loadOrders()
  else alert(res.message)
}

const cancelOrder = async (id) => {
  if (!confirm('确定取消该订单？')) return
  const res = await api.put(`/orders/${id}/cancel`)
  if (res.code === 200) loadOrders()
  else alert(res.message)
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
.nav-brand { font-size: 18px; font-weight: bold; color: #27ae60; }
.nav-links a { margin-left: 24px; color: #555; text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #27ae60; }
.main-content { padding: 24px; }
h2 { margin-bottom: 20px; color: #333; }
.empty { text-align: center; color: #999; padding: 60px 0; }
.order-list { display: flex; flex-direction: column; gap: 16px; }
.order-card { background: #fff; border-radius: 12px; padding: 20px; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.order-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 12px; }
.order-id { font-weight: bold; color: #333; }
.status { padding: 4px 12px; border-radius: 4px; font-size: 13px; }
.pending { background: #fff3cd; color: #856404; }
.confirmed { background: #d4edda; color: #155724; }
.cancelled { background: #f8d7da; color: #721c24; }
.completed { background: #cce5ff; color: #004085; }
.order-body p { margin: 4px 0; color: #555; font-size: 14px; }
.total { font-weight: bold; color: #e74c3c; font-size: 16px; }
.order-actions { margin-top: 12px; display: flex; gap: 8px; }
.btn-confirm { padding: 6px 16px; border: none; background: #27ae60; color: #fff; border-radius: 6px; cursor: pointer; font-size: 13px; }
.btn-complete { padding: 6px 16px; border: none; background: #3498db; color: #fff; border-radius: 6px; cursor: pointer; font-size: 13px; }
.btn-cancel { padding: 6px 16px; border: 1px solid #e74c3c; color: #e74c3c; background: #fff; border-radius: 6px; cursor: pointer; font-size: 13px; }
</style>
