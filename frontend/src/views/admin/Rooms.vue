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
      <h2>民宿管理</h2>
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th><th>标题</th><th>地址</th><th>价格</th><th>类型</th><th>容量</th><th>状态</th><th>房东ID</th><th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="room in rooms" :key="room.id">
            <td>{{ room.id }}</td>
            <td>{{ room.title }}</td>
            <td>{{ room.address }}</td>
            <td>¥{{ room.price }}</td>
            <td>{{ room.type }}</td>
            <td>{{ room.capacity }}人</td>
            <td :class="room.available ? 'on' : 'off'">{{ room.available ? '上架' : '下架' }}</td>
            <td>{{ room.hostId }}</td>
            <td>
              <button class="btn-delete" @click="deleteRoom(room.id)">删除</button>
            </td>
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
const rooms = ref([])

const loadRooms = async () => {
  const res = await api.get('/rooms/admin')
  if (res.code === 200) rooms.value = res.data
}

const deleteRoom = async (id) => {
  if (!confirm('确定删除？')) return
  await api.delete(`/rooms/admin/${id}`)
  loadRooms()
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(loadRooms)
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
.on { color: #27ae60; }
.off { color: #e74c3c; }
.btn-delete { padding: 4px 12px; border: 1px solid #e74c3c; color: #e74c3c; background: #fff; border-radius: 4px; cursor: pointer; font-size: 12px; }
.btn-delete:hover { background: #e74c3c; color: #fff; }
</style>
