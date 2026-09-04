<script setup>
import { ref, onMounted, onUnmounted, provide } from 'vue'
import PageNavigation from './components/PageNavigation.vue'
import HintComponent from './components/HintComponent.vue'
import HomePage from './components/HomePage.vue'
import ChanganPage from './components/ChanganPage.vue'
import DunhuangPage from './components/DunhuangPage.vue'
import MusicHubPage from './components/MusicHubPage.vue'
import FutureStationPage from './components/FutureStationPage.vue'
import FunctionModulePage from './components/FunctionModulePage.vue'

const currentPage = ref(0)
const totalPages = 6 // 增加到6个页面
const isMusicPlaying = ref(false)

// 提供音频控制给子组件
const audioControls = {
  isMusicPlaying,
  toggleMusic: () => {
    isMusicPlaying.value = !isMusicPlaying.value
  }
}

provide('audioControls', audioControls)

const goToPage = (page) => {
  if (page >= 0 && page < totalPages) {
    currentPage.value = page
  }
}

const nextPage = () => {
  if (currentPage.value < totalPages - 1) {
    currentPage.value++
  }
}

const prevPage = () => {
  if (currentPage.value > 0) {
    currentPage.value--
  }
}

// 滚动和键盘控制逻辑
let wheelTimeout = null
let wheelDelta = 0
// 滚动阈值
const threshold = 400

const handleWheel = (e) => {
  wheelDelta += e.deltaY
  
  if (wheelTimeout) clearTimeout(wheelTimeout)
  
  wheelTimeout = setTimeout(() => {
    if (Math.abs(wheelDelta) > threshold) {
      if (wheelDelta > 0) {
        nextPage()
      } else {
        prevPage()
      }
    }
    wheelDelta = 0
  }, 100)
}

const handleKeyDown = (e) => {
  switch(e.key) {
    case 'ArrowRight':
      nextPage()
      break
    case 'ArrowLeft':
      prevPage()
      break
  }
}

onMounted(() => {
  window.addEventListener('wheel', handleWheel)
  window.addEventListener('keydown', handleKeyDown)
})

onUnmounted(() => {
  window.removeEventListener('wheel', handleWheel)
  window.removeEventListener('keydown', handleKeyDown)
})
</script>

<template>
  <div id="app">
    <PageNavigation 
      :currentPage="currentPage" 
      :totalPages="totalPages"
      @go-to-page="goToPage"
    />
    
    <div class="container" :style="{ transform: `translateX(-${currentPage * 100}vw)` }">
      <HomePage @go-to-page="goToPage" />
      <ChanganPage />
      <DunhuangPage />
      <MusicHubPage />
      <FutureStationPage @go-to-page="goToPage" />
      <FunctionModulePage />
    </div>
    
    <HintComponent />
  </div>
</template>

<style>
/* 全局样式保持不变 */
#app {
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  background: linear-gradient(135deg, #0d1b2a, #1b263b);
  color: rgb(212, 212, 212);
  font-family: 'Arial', sans-serif;
}

.container {
  display: flex;
  width: 600vw; /* 更新为600vw以适应6个页面 */
  height: 100vh;
  transition: transform 0.8s cubic-bezier(0.25, 0.46, 0.45, 0.94);
}

.page {
  width: 100vw;
  height: 100vh;
  display: flex;
  flex-direction: column;
  position: relative;
}

.page-content {
  flex: 1;
  padding: 2rem;
  overflow-y: auto;
  margin-top: 60px; /* 为导航栏留出空间 */
}

.page-number {
  position: absolute;
  bottom: 2rem;
  right: 2rem;
  font-size: 1.5rem;
  opacity: 0.3;
}

/* 通用内容样式 */
.section-header {
  display: flex;
  align-items: center;
  margin-bottom: 30px;
  text-align: left;
}

.section-icon {
  width: 70px;
  height: 70px;
  background: linear-gradient(135deg, #ff8c00, #e6be0e);
  border-radius: 50%;
  display: flex;
  align-items: center;
  justify-content: center;
  margin-right: 25px;
  font-size: 1.8rem;
  box-shadow: 0 4px 15px rgba(6, 214, 160, 0.3);
}

.section-title {
  font-size: 2.2rem;
  margin-bottom: 10px;
  background: linear-gradient(45deg, #ffd166, #06d6a0);
  -webkit-background-clip: text;
  -webkit-text-fill-color: transparent;
}

.section-subtitle {
  font-size: 1.3rem;
  color: #a0aec0;
  font-weight: 300;
}

.story-content {
  font-size: 1.2rem;
  margin-bottom: 35px;
  line-height: 1.8;
  background: rgba(196, 176, 121, 0.6);
  padding: 25px;
  border-radius: 12px;
  border-left: 5px solid #ffffff;
  box-shadow: 0 4px 15px rgba(0, 0, 0, 0.2);
}

.content-section {
  background: rgba(196, 176, 121, 0.6);
  border-radius: 12px;
  padding: 30px;
  margin-bottom: 30px;
  box-shadow: 0 4px 20px rgba(0, 0, 0, 0.3);
}

.content-section .section-title {
  font-size: 1.6rem;
  border-left: 4px solid #ffd166;
  padding-left: 15px;
  margin-bottom: 20px;
}

.image-gallery {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 25px;
  margin-top: 25px;
}

.image-card {
  background: rgba(13, 27, 42, 0.8);
  border-radius: 10px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.image-card:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.4);
  border-color: rgba(6, 214, 160, 0.3);
}

.card-img {
  width: 100%;
  height: 220px;
  object-fit: cover;
  border-bottom: 1px solid rgba(110, 130, 164, 0.5);
}

.card-content {
  padding: 20px;
  background: rgba(196, 176, 121, 0.6);
}

.card-title {
  font-size: 1.2rem;
  margin-bottom: 12px;
  color: #ffd166;
}

.card-desc {
  font-size: 0.95rem;
  color: #a0aec0;
  line-height: 1.5;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .section-header {
    flex-direction: column;
    text-align: center;
  }
  
  .section-icon {
    margin-right: 0;
    margin-bottom: 20px;
  }
  
  .section-title {
    font-size: 1.8rem;
  }
  
  .section-subtitle {
    font-size: 1.1rem;
  }
  
  .page-content {
    margin-top: 50px;
  }
}
</style>