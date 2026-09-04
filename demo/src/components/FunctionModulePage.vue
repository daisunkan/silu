<template>
  <div class="page page-6">
    <div class="page-content">
      <div class="section-header">
        <div class="section-icon">🛠️</div>
        <div>
          <h1 class="section-title">功能模块中心</h1>
          <p class="section-subtitle">选择您想要体验的具体功能模块</p>
        </div>
      </div>
      
      <div class="story-content">
        <p>欢迎来到功能模块中心！在这里，您可以直接体验四大核心AI技术。每个模块都配备了完整的交互界面，让您能够深入了解和体验现代AI技术在文化传承中的应用。</p>
        <p>请选择您感兴趣的功能模块开始体验：</p>
      </div>
      
      <!-- 功能模块选择界面 -->
      <div class="function-modules">
        <div class="module-grid">
          <!-- 文物修复模块 -->
          <div class="module-card" @click="goToCulturalRepair">
            <div class="module-icon">🔧</div>
            <h3 class="module-title">AI文物修复</h3>
            <p class="module-desc">运用AI技术修复破损文物，还原历史原貌</p>
            <div class="btn">开始使用</div>
          </div>
          
          <!-- AI文创风格迁移模块 -->
          <div class="module-card" @click="goToStyleTransfer">
            <div class="module-icon">🎨</div>
            <h3 class="module-title">AI文创风格迁移</h3>
            <p class="module-desc">将古代艺术风格与现代创意完美融合</p>
            <div class="btn">开始使用</div>
          </div>
          
          <!-- AI数字人模块 -->
          <div class="module-card" @click="goToDigitalHuman">
            <div class="module-icon">👤</div>
            <h3 class="module-title">AI数字人</h3>
            <p class="module-desc">与古代文人进行实时对话，了解历史文化</p>
            <div class="btn">开始使用</div>
          </div>
          
          <!-- 音乐古风迁移模块 -->
          <div class="module-card" @click="goToMusicTransfer">
            <div class="module-icon">🎵</div>
            <h3 class="module-title">音乐古风迁移</h3>
            <p class="module-desc">将现代音乐转化为古风雅韵，重现千年音律</p>
            <div class="btn">开始使用</div>
          </div>
        </div>
      </div>
    </div>
    <div class="page-number">6</div>
  </div>
</template>

<script setup>
// 跳转到文物修复界面
const goToCulturalRepair = () => {
  window.location.href = 'http://localhost:8082/image-edit.html'
}

// 跳转到风格迁移界面
const goToStyleTransfer = () => {
  window.location.href = 'http://localhost:8084'
}

// 跳转到AI数字人界面
const goToDigitalHuman = () => {
  window.location.href = 'http://localhost:8081/#/'
}

// 跳转到音乐古风迁移界面
const goToMusicTransfer = () => {
  window.location.href = 'http://localhost:8083'
}
</script>

<style scoped>
.page.page-6{
  background-image: url('@/assets/images/function-bg.jpg');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  /* 半透明黑色遮罩（可调整 alpha 值控制暗度，范围 0~1） */
  background-color: rgba(0, 0, 0, 0.5); 
  /* 混合模式：让背景图与黑色「正片叠底」，自然暗化 */
  background-blend-mode: multiply; 
  min-height: 100vh; /* 确保背景占满屏幕高度 */
}
.function-modules {
  max-width: 1200px;
  margin: 0 auto;
}

.module-grid {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 25px;
  margin-bottom: 40px;
}

.module-card {
  background: rgba(196, 176, 121, 0.6);
  border-radius: 15px;
  padding: 35px 25px;
  text-align: center;
  transition: all 0.3s ease;
  cursor: pointer;
  border: 2px solid transparent;
  position: relative;
  overflow: hidden;
}

.module-card::before {
  content: '';
  position: absolute;
  top: 0;
  left: -100%;
  width: 100%;
  height: 100%;
  background: linear-gradient(90deg, transparent, rgba(255, 255, 255, 0.1), transparent);
  transition: left 0.5s;
}

.module-card:hover::before {
  left: 100%;
}

.module-card:hover {
  transform: translateY(-10px);
  box-shadow: 0 15px 30px rgba(0, 0, 0, 0.4);
  border-color: rgba(6, 214, 160, 0.3);
}

.module-icon {
  font-size: 3.5rem;
  margin-bottom: 25px;
}

.module-title {
  font-size: 1.6rem;
  margin-bottom: 18px;
  color: #ffd166;
}

.module-desc {
  color: #a0aec0;
  margin-bottom: 25px;
  line-height: 1.6;
}

.btn {
  display: inline-block;
  padding: 12px 24px;
  background: linear-gradient(140deg, #ffb301, #fc7f01);
  color: #0d1b2a;
  border: none;
  border-radius: 8px;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  text-decoration: none;
}

.btn:hover {
  transform: translateY(-2px);
  box-shadow: 0 5px 15px rgba(6, 214, 160, 0.4);
}
</style>