<template>
  <div class="layout">
    <nav class="navbar">
      <div class="nav-brand">民宿预定平台</div>
      <div class="nav-links">
        <router-link to="/guest">首页</router-link>
        <router-link to="/guest/rooms">浏览民宿</router-link>
        <router-link to="/guest/orders">我的订单</router-link>
        <a href="#" @click.prevent="logout">退出</a>
      </div>
      <div class="nav-user">欢迎，{{ user?.nickname || user?.username }}</div>
    </nav>
    <main class="main-content">
      <div class="welcome">
        <h1>欢迎来到民宿预定平台</h1>
        <p>发现独特的住宿体验，让旅途更有温度</p>
        <router-link to="/guest/rooms" class="btn-primary">浏览民宿</router-link>
      </div>
    </main>
  </div>
</template>

<script setup>
import { computed } from 'vue'
import { useRouter } from 'vue-router'

const router = useRouter()
const user = computed(() => JSON.parse(localStorage.getItem('user') || 'null'))

const logout = () => {
  localStorage.removeItem('token')
  localStorage.removeItem('user')
  router.push('/login')
}
</script>

<style scoped>
.layout { min-height: 100vh; background: #f5f6fa; }
.navbar { display: flex; align-items: center; justify-content: space-between; padding: 0 24px; height: 60px; background: #fff; box-shadow: 0 2px 8px rgba(0,0,0,0.08); }
.nav-brand { font-size: 18px; font-weight: bold; color: #667eea; }
.nav-links a { margin-left: 24px; color: #555; text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #667eea; }
.nav-user { font-size: 14px; color: #888; }
.main-content { padding: 40px 24px; }
.welcome { text-align: center; padding: 80px 20px; }
.welcome h1 { font-size: 32px; color: #333; margin-bottom: 12px; }
.welcome p { color: #888; font-size: 16px; margin-bottom: 32px; }
.btn-primary { display: inline-block; padding: 12px 32px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; text-decoration: none; border-radius: 8px; font-size: 16px; }
.btn-primary:hover { opacity: 0.9; }
</style>
