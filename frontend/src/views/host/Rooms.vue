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
      <div class="page-header">
        <h2>我的民宿</h2>
        <button class="btn-add" @click="openAdd">添加民宿</button>
      </div>
      <div v-if="rooms.length === 0" class="empty">暂无民宿，点击上方按钮添加</div>
      <div class="room-list">
        <div v-for="room in rooms" :key="room.id" class="room-card">
          <div class="room-img">
            <img v-if="room.imageUrl" :src="room.imageUrl" alt="" />
            <div v-else class="img-placeholder">民宿图片</div>
          </div>
          <div class="room-info">
            <h3>{{ room.title }}</h3>
            <p>{{ room.address }}</p>
            <div class="meta">
              <span>{{ room.type }}</span>
              <span>{{ room.capacity }}人</span>
              <span :class="room.available ? 'on' : 'off'">{{ room.available ? '上架中' : '已下架' }}</span>
            </div>
            <p class="price">¥{{ room.price }}/晚</p>
            <div class="actions">
              <button class="btn-edit" @click="openEdit(room)">编辑</button>
              <button class="btn-toggle" @click="toggleRoom(room)">{{ room.available ? '下架' : '上架' }}</button>
              <button class="btn-delete" @click="deleteRoom(room.id)">删除</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 添加/编辑弹窗 -->
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal">
          <h3>{{ isEdit ? '编辑民宿' : '添加民宿' }}</h3>
          <div v-if="error" class="error">{{ error }}</div>
          <div class="form-group">
            <label>标题</label>
            <input v-model="form.title" type="text" required />
          </div>
          <div class="form-group">
            <label>地址</label>
            <input v-model="form.address" type="text" />
          </div>
          <div class="form-group">
            <label>描述</label>
            <textarea v-model="form.description" rows="3"></textarea>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>价格（/晚）</label>
              <input v-model.number="form.price" type="number" step="0.01" />
            </div>
            <div class="form-group">
              <label>类型</label>
              <input v-model="form.type" type="text" placeholder="如：公寓、别墅" />
            </div>
          </div>
          <div class="form-row">
            <div class="form-group">
              <label>容纳人数</label>
              <input v-model.number="form.capacity" type="number" />
            </div>
            <div class="form-group">
              <label>图片URL</label>
              <input v-model="form.imageUrl" type="text" />
            </div>
          </div>
          <div class="modal-actions">
            <button class="btn-cancel" @click="showModal = false">取消</button>
            <button class="btn-primary" @click="handleSubmit">{{ isEdit ? '保存' : '添加' }}</button>
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
const rooms = ref([])
const showModal = ref(false)
const isEdit = ref(false)
const editId = ref(null)
const form = ref({ title: '', address: '', description: '', price: null, type: '', capacity: null, imageUrl: '' })
const error = ref('')

const defaultForm = () => ({ title: '', address: '', description: '', price: null, type: '', capacity: null, imageUrl: '' })

const loadRooms = async () => {
  const res = await api.get('/rooms/host')
  if (res.code === 200) rooms.value = res.data
}

const openAdd = () => {
  isEdit.value = false
  editId.value = null
  form.value = defaultForm()
  error.value = ''
  showModal.value = true
}

const openEdit = (room) => {
  isEdit.value = true
  editId.value = room.id
  form.value = { title: room.title, address: room.address, description: room.description, price: room.price, type: room.type, capacity: room.capacity, imageUrl: room.imageUrl }
  error.value = ''
  showModal.value = true
}

const handleSubmit = async () => {
  error.value = ''
  try {
    let res
    if (isEdit.value) {
      res = await api.put(`/rooms/${editId.value}`, form.value)
    } else {
      res = await api.post('/rooms', form.value)
    }
    if (res.code === 200) {
      showModal.value = false
      loadRooms()
    } else {
      error.value = res.message
    }
  } catch (e) {
    error.value = '操作失败'
  }
}

const toggleRoom = async (room) => {
  await api.put(`/rooms/${room.id}`, { available: !room.available })
  loadRooms()
}

const deleteRoom = async (id) => {
  if (!confirm('确定删除该民宿？')) return
  await api.delete(`/rooms/${id}`)
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
.nav-brand { font-size: 18px; font-weight: bold; color: #27ae60; }
.nav-links a { margin-left: 24px; color: #555; text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #27ae60; }
.main-content { padding: 24px; }
.page-header { display: flex; justify-content: space-between; align-items: center; margin-bottom: 20px; }
h2 { margin: 0; color: #333; }
.btn-add { padding: 10px 20px; background: #27ae60; color: #fff; border: none; border-radius: 8px; cursor: pointer; font-size: 14px; }
.empty { text-align: center; color: #999; padding: 60px 0; }
.room-list { display: flex; flex-direction: column; gap: 16px; }
.room-card { display: flex; background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 8px rgba(0,0,0,0.06); }
.room-img { width: 200px; min-height: 150px; flex-shrink: 0; }
.room-img img { width: 100%; height: 100%; object-fit: cover; }
.img-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background: #f0f0f0; color: #999; }
.room-info { padding: 16px; flex: 1; }
.room-info h3 { margin: 0 0 8px; }
.room-info p { color: #666; font-size: 14px; margin: 4px 0; }
.meta { display: flex; gap: 8px; margin: 8px 0; }
.meta span { font-size: 12px; padding: 2px 8px; border-radius: 4px; background: #f0f2ff; color: #667eea; }
.meta .on { background: #d4edda; color: #155724; }
.meta .off { background: #f8d7da; color: #721c24; }
.price { font-weight: bold; color: #e74c3c; font-size: 16px; }
.actions { margin-top: 12px; display: flex; gap: 8px; }
.btn-edit { padding: 6px 14px; border: 1px solid #667eea; color: #667eea; background: #fff; border-radius: 6px; cursor: pointer; font-size: 13px; }
.btn-toggle { padding: 6px 14px; border: 1px solid #f39c12; color: #f39c12; background: #fff; border-radius: 6px; cursor: pointer; font-size: 13px; }
.btn-delete { padding: 6px 14px; border: 1px solid #e74c3c; color: #e74c3c; background: #fff; border-radius: 6px; cursor: pointer; font-size: 13px; }
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: #fff; padding: 32px; border-radius: 12px; width: 500px; max-height: 80vh; overflow-y: auto; }
.modal h3 { margin: 0 0 20px; }
.form-group { margin-bottom: 14px; }
.form-group label { display: block; margin-bottom: 6px; color: #555; font-size: 14px; }
.form-group input, .form-group textarea { width: 100%; padding: 8px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; box-sizing: border-box; }
.form-row { display: flex; gap: 16px; }
.form-row .form-group { flex: 1; }
.modal-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 20px; }
.btn-cancel { padding: 10px 20px; border: 1px solid #ddd; border-radius: 8px; background: #fff; cursor: pointer; }
.btn-primary { padding: 10px 20px; background: #27ae60; color: #fff; border: none; border-radius: 8px; cursor: pointer; }
.error { color: #e74c3c; margin-bottom: 12px; font-size: 14px; }
</style>
