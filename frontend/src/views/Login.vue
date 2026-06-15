<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>民宿预定管理平台</h2>
      <h3>用户登录</h3>
      <div v-if="error" class="error">{{ error }}</div>
      <form @submit.prevent="handleLogin">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" required placeholder="请输入用户名" />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" required placeholder="请输入密码" />
        </div>
        <button type="submit" class="btn-primary">登录</button>
      </form>
      <p class="link-text">还没有账号？<router-link to="/register">立即注册</router-link></p>
      <div class="test-accounts">
        <p>测试账号：</p>
        <p>游客：guest / 123456</p>
        <p>房东：host / 123456</p>
        <p>管理员：admin / 123456</p>
      </div>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'

const router = useRouter()
const form = ref({ username: '', password: '' })
const error = ref('')

const handleLogin = async () => {
  error.value = ''
  try {
    const res = await api.post('/auth/login', form.value)
    if (res.code === 200) {
      localStorage.setItem('token', res.data.token)
      localStorage.setItem('user', JSON.stringify(res.data))
      const roleRoutes = { GUEST: '/guest', HOST: '/host', ADMIN: '/admin' }
      router.push(roleRoutes[res.data.role] || '/guest')
    } else {
      error.value = res.message
    }
  } catch (e) {
    error.value = '登录失败，请检查网络'
  }
}
</script>

<style scoped>
.auth-container { display: flex; justify-content: center; align-items: center; min-height: 100vh; background: linear-gradient(135deg, #667eea 0%, #764ba2 100%); }
.auth-card { background: #fff; padding: 40px; border-radius: 12px; box-shadow: 0 20px 60px rgba(0,0,0,0.2); width: 420px; }
.auth-card h2 { text-align: center; color: #333; margin-bottom: 8px; font-size: 22px; }
.auth-card h3 { text-align: center; color: #666; margin-bottom: 24px; font-size: 16px; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; margin-bottom: 6px; color: #555; font-size: 14px; }
.form-group input { width: 100%; padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; box-sizing: border-box; }
.form-group input:focus { outline: none; border-color: #667eea; }
.btn-primary { width: 100%; padding: 12px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; border: none; border-radius: 8px; font-size: 16px; cursor: pointer; }
.btn-primary:hover { opacity: 0.9; }
.error { color: #e74c3c; text-align: center; margin-bottom: 12px; font-size: 14px; }
.link-text { text-align: center; margin-top: 16px; color: #666; font-size: 14px; }
.link-text a { color: #667eea; text-decoration: none; }
.test-accounts { margin-top: 20px; padding: 12px; background: #f8f9fa; border-radius: 8px; font-size: 13px; color: #666; }
.test-accounts p { margin: 4px 0; }
</style>
