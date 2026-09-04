<!-- PageNavigation.vue -->
<script setup>
import { inject, ref } from 'vue'

defineProps({
  currentPage: {
    type: Number,
    required: true
  },
  totalPages: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['go-to-page'])

// 安全地注入音频控制
const audioControls = inject('audioControls', {
  isMusicPlaying: ref(false),
  toggleMusic: () => {
    console.warn('audioControls not provided, using default')
  }
})

const { isMusicPlaying, toggleMusic } = audioControls
</script>

<template>
  <div class="nav-header">
    <div class="nav-container">
      <div 
        class="nav-button music-button"
        @click="toggleMusic"
      >
        {{ isMusicPlaying ? '🔊' : '🔇' }}
      </div>
      <div 
        v-for="i in totalPages" 
        :key="i" 
        :class="['nav-button', { active: currentPage === i-1 }]"
        @click="emit('go-to-page', i-1)"
      >
        {{ ['序幕', '长安基石', '时光修复', '丝路旋律', '文化共创'][i-1] }}
      </div>
    </div>
  </div>
</template>

<style scoped>
.nav-header {
  position: fixed;
  top: 0;
  left: 0;
  right: 0;
  height: 60px;
  background: rgba(0, 0, 0, 0.2);
  backdrop-filter: blur(10px);
  -webkit-backdrop-filter: blur(10px);
  z-index: 100;
  display: flex;
  justify-content: flex-end;
  align-items: center;
  padding: 0 2rem;
  box-shadow: 0 2px 10px rgba(0, 0, 0, 0.1);
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.nav-container {
  display: flex;
  gap: 1rem;
}

.nav-button {
  min-width: 80px;
  height: 36px;
  padding: 0 1rem;
  background: rgba(255, 255, 255, 0.1);
  color: white;
  cursor: pointer;
  transition: all 0.3s ease;
  border-radius: 18px;
  display: flex;
  justify-content: center;
  align-items: center;
  font-size: 0.9rem;
  font-weight: 500;
}

.nav-button.active {
  background: linear-gradient(45deg, #ff9a9e, #fad0c4);
  color: #333;
  box-shadow: 0 0 15px rgba(255, 154, 158, 0.5);
  transform: scale(1.05);
}

.nav-button:hover {
  background: rgba(255, 255, 255, 0.2);
}
</style>