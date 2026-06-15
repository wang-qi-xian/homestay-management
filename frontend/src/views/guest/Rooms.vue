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
    </nav>
    <main class="main-content">
      <h2>浏览民宿</h2>
      <div v-if="rooms.length === 0" class="empty">暂无可预订的民宿</div>
      <div class="room-grid">
        <div v-for="room in rooms" :key="room.id" class="room-card">
          <div class="room-img">
            <img v-if="room.imageUrl" :src="room.imageUrl" alt="" />
            <div v-else class="img-placeholder">民宿图片</div>
          </div>
          <div class="room-info">
            <h3>{{ room.title }}</h3>
            <p class="address">{{ room.address }}</p>
            <p class="desc">{{ room.description }}</p>
            <div class="meta">
              <span class="type">{{ room.type }}</span>
              <span class="capacity">{{ room.capacity }}人</span>
            </div>
            <div class="bottom">
              <span class="price">¥{{ room.price }}/晚</span>
              <button class="btn-book" @click="openBook(room)">立即预订</button>
            </div>
          </div>
        </div>
      </div>

      <!-- 预订弹窗 -->
      <div v-if="showModal" class="modal-overlay" @click.self="showModal = false">
        <div class="modal">
          <h3>预订 - {{ selectedRoom?.title }}</h3>
          <div v-if="bookError" class="error">{{ bookError }}</div>
          <div v-if="bookSuccess" class="success">{{ bookSuccess }}</div>
          <div class="form-group">
            <label>入住日期</label>
            <input v-model="bookForm.checkIn" type="date" required />
          </div>
          <div class="form-group">
            <label>离店日期</label>
            <input v-model="bookForm.checkOut" type="date" required />
          </div>
          <div class="modal-actions">
            <button class="btn-cancel" @click="showModal = false">取消</button>
            <button class="btn-primary" @click="handleBook">确认预订</button>
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
const selectedRoom = ref(null)
const bookForm = ref({ checkIn: '', checkOut: '' })
const bookError = ref('')
const bookSuccess = ref('')

const loadRooms = async () => {
  const res = await api.get('/rooms/public')
  if (res.code === 200) rooms.value = res.data
}

const openBook = (room) => {
  selectedRoom.value = room
  bookForm.value = { checkIn: '', checkOut: '' }
  bookError.value = ''
  bookSuccess.value = ''
  showModal.value = true
}

const handleBook = async () => {
  bookError.value = ''
  bookSuccess.value = ''
  try {
    const res = await api.post('/orders', {
      roomId: selectedRoom.value.id,
      checkIn: bookForm.value.checkIn,
      checkOut: bookForm.value.checkOut
    })
    if (res.code === 200) {
      bookSuccess.value = '预订成功！'
      setTimeout(() => { showModal.value = false; loadRooms() }, 1000)
    } else {
      bookError.value = res.message
    }
  } catch (e) {
    bookError.value = '预订失败'
  }
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
.nav-brand { font-size: 18px; font-weight: bold; color: #667eea; }
.nav-links a { margin-left: 24px; color: #555; text-decoration: none; font-size: 14px; }
.nav-links a:hover, .nav-links a.router-link-active { color: #667eea; }
.main-content { padding: 24px; }
h2 { margin-bottom: 20px; color: #333; }
.empty { text-align: center; color: #999; padding: 60px 0; }
.room-grid { display: grid; grid-template-columns: repeat(auto-fill, minmax(320px, 1fr)); gap: 20px; }
.room-card { background: #fff; border-radius: 12px; overflow: hidden; box-shadow: 0 2px 12px rgba(0,0,0,0.08); }
.room-img { height: 180px; overflow: hidden; }
.room-img img { width: 100%; height: 100%; object-fit: cover; }
.img-placeholder { width: 100%; height: 100%; display: flex; align-items: center; justify-content: center; background: #f0f0f0; color: #999; font-size: 16px; }
.room-info { padding: 16px; }
.room-info h3 { margin: 0 0 8px; font-size: 18px; color: #333; }
.address { color: #888; font-size: 13px; margin: 4px 0; }
.desc { color: #666; font-size: 14px; margin: 8px 0; display: -webkit-box; -webkit-line-clamp: 2; -webkit-box-orient: vertical; overflow: hidden; }
.meta { display: flex; gap: 12px; margin: 8px 0; }
.meta span { font-size: 12px; padding: 2px 8px; border-radius: 4px; background: #f0f2ff; color: #667eea; }
.bottom { display: flex; justify-content: space-between; align-items: center; margin-top: 12px; }
.price { font-size: 20px; font-weight: bold; color: #e74c3c; }
.btn-book { padding: 8px 20px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; border: none; border-radius: 6px; cursor: pointer; font-size: 14px; }
.btn-book:hover { opacity: 0.9; }
.modal-overlay { position: fixed; top: 0; left: 0; right: 0; bottom: 0; background: rgba(0,0,0,0.5); display: flex; align-items: center; justify-content: center; z-index: 100; }
.modal { background: #fff; padding: 32px; border-radius: 12px; width: 400px; }
.modal h3 { margin: 0 0 20px; }
.form-group { margin-bottom: 16px; }
.form-group label { display: block; margin-bottom: 6px; color: #555; font-size: 14px; }
.form-group input { width: 100%; padding: 10px 12px; border: 1px solid #ddd; border-radius: 8px; font-size: 14px; box-sizing: border-box; }
.modal-actions { display: flex; justify-content: flex-end; gap: 12px; margin-top: 20px; }
.btn-cancel { padding: 10px 20px; border: 1px solid #ddd; border-radius: 8px; background: #fff; cursor: pointer; }
.btn-primary { padding: 10px 20px; background: linear-gradient(135deg, #667eea, #764ba2); color: #fff; border: none; border-radius: 8px; cursor: pointer; }
.error { color: #e74c3c; margin-bottom: 12px; font-size: 14px; }
.success { color: #27ae60; margin-bottom: 12px; font-size: 14px; }
</style>
