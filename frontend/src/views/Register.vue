<template>
  <div class="auth-container">
    <div class="auth-card">
      <h2>民宿预定管理平台</h2>
      <h3>用户注册</h3>
      <div v-if="error" class="error">{{ error }}</div>
      <div v-if="success" class="success">{{ success }}</div>
      <form @submit.prevent="handleRegister">
        <div class="form-group">
          <label>用户名</label>
          <input v-model="form.username" type="text" required placeholder="请输入用户名" />
        </div>
        <div class="form-group">
          <label>密码</label>
          <input v-model="form.password" type="password" required placeholder="请输入密码" />
        </div>
        <div class="form-group">
          <label>昵称</label>
          <input v-model="form.nickname" type="text" placeholder="请输入昵称" />
        </div>
        <div class="form-group">
          <label>手机号</label>
          <input v-model="form.phone" type="text" placeholder="请输入手机号" />
        </div>
        <div class="form-group">
          <label>角色</label>
          <select v-model="form.role" required>
            <option value="GUEST">游客</option>
            <option value="HOST">房东</option>
          </select>
        </div>
        <button type="submit" class="btn-primary">注册</button>
      </form>
      <p class="link-text">已有账号？<router-link to="/login">立即登录</router-link></p>
    </div>
  </div>
</template>

<script setup>
import { ref } from 'vue'
import { useRouter } from 'vue-router'
import api from '../api'

const router = useRouter()
const form = ref({ username: '', password: '', nickname: '', phone: '', role: 'GUEST' })
const error = ref('')
const success = ref('')

const handleRegister = async () => {
  error.value = ''
  success.value = ''
  try {
    const res = await api.post('/auth/register', form.value)
    if (res.code === 200) {
      success.value = '注册成功，即将跳转登录页'
      setTimeout(() => router.push('/login'), 1500)
    } else {
      error.value = res.message
    }
  } catch (e) {
    error.value = '注册失败，请检查网络'
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
.form-group input, .form-group select { width: 100%; padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; box-sizing: border-box; }
.form-group input:focus, .form-group select:focus { outline: none; border-color: #667eea; }
.btn-primary { width: 100%; padding: 12px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; border: none; border-radius: 8px; font-size: 16px; cursor: pointer; }
.btn-primary:hover { opacity: 0.9; }
.error { color: #e74c3c; text-align: center; margin-bottom: 12px; font-size: 14px; }
.success { color: #27ae60; text-align: center; margin-bottom: 12px; font-size: 14px; }
.link-text { text-align: center; margin-top: 16px; color: #666; font-size: 14px; }
.link-text a { color: #667eea; text-decoration: none; }
</style>
