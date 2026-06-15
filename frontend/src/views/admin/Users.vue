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
      <h2>用户管理</h2>
      <table class="data-table">
        <thead>
          <tr>
            <th>ID</th><th>用户名</th><th>昵称</th><th>手机</th><th>角色</th><th>注册时间</th><th>操作</th>
          </tr>
        </thead>
        <tbody>
          <tr v-for="user in users" :key="user.id">
            <td>{{ user.id }}</td>
            <td>{{ user.username }}</td>
            <td>{{ user.nickname || '-' }}</td>
            <td>{{ user.phone || '-' }}</td>
            <td><span :class="roleClass(user.role)">{{ roleText(user.role) }}</span></td>
            <td>{{ user.createdAt }}</td>
            <td>
              <button v-if="user.role !== 'ADMIN'" class="btn-delete" @click="deleteUser(user.id)">删除</button>
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
const users = ref([])

const roleText = (r) => ({ GUEST: '游客', HOST: '房东', ADMIN: '管理员' }[r] || r)
const roleClass = (r) => ({ GUEST: 'guest', HOST: 'host', ADMIN: 'admin' }[r] || '')

const loadUsers = async () => {
  const res = await api.get('/users/admin')
  if (res.code === 200) users.value = res.data
}

const deleteUser = async (id) => {
  if (!confirm('确定删除该用户？')) return
  await api.delete(`/users/admin/${id}`)
  loadUsers()
}

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}

onMounted(loadUsers)
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
.guest { padding: 2px 8px; border-radius: 4px; background: #d4edda; color: #155724; font-size: 12px; }
.host { padding: 2px 8px; border-radius: 4px; background: #fff3cd; color: #856404; font-size: 12px; }
.admin { padding: 2px 8px; border-radius: 4px; background: #f8d7da; color: #721c24; font-size: 12px; }
.btn-delete { padding: 4px 12px; border: 1px solid #e74c3c; color: #e74c3c; background: #fff; border-radius: 4px; cursor: pointer; font-size: 12px; }
.btn-delete:hover { background: #e74c3c; color: #fff; }
</style>
