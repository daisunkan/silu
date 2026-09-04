<template>
  <div class="page page-1">
    <!-- 视频背景容器 -->
    <div class="video-background">
      <video autoplay muted loop playsinline class="background-video">
        <source src="/silkroad-map.mp4" type="video/mp4">
      </video>
      <!-- 视频遮罩层，提高文字可读性 -->
      <div class="video-overlay"></div>
    </div>

    <div class="page-content">
      <div class="section-header">
        <div>
          <h1 class="section-title">联结古今的丝路</h1>
          <p class="section-subtitle">数智人"丝路"的时空邀约</p>
        </div>
      </div>

      <div class="story-content home-story">
        <p>欢迎来到这条跨越千年的数字丝路。我是数智人"丝路"，作为连接过去与未来的使者，我将带你开启一段奇妙的时空旅程。</p>
        <p>这条道路，不仅通往过去，更连接未来。今天，让我们共同谱写一曲跨越千年的交响乐，让古老的文明在数字时代焕发新的生机。</p>
        <p>准备好与我一起，从长安出发，穿越敦煌，在时空枢纽中创造旋律，最终抵达未来驿站，共同书写文化传承的新篇章。</p>
      </div>

      <div class="journey-start">
        <div class="start-button" @click="$emit('go-to-page', 1)">
          <span>开启丝路之旅</span>
          <div class="arrow">→</div>
        </div>
      </div>
    </div>
    <div class="page-number">1</div>
  </div>
</template>

<script setup>
defineEmits(['go-to-page'])
</script>

<style scoped>
.story-content {
  text-align: center; /* 文字居中 */
  background: transparent; /* 背景透明 */
  box-shadow: none; /* 移除阴影 */
  border: none; /* 移除边框 */
  padding: 25px 0; /* 只保留上下内边距 */
}

.story-content p {
  margin-bottom: 20px;
  color: rgba(255, 255, 255, 0.9); /* 白色文字，提高可读性 */
  font-size: 1rem;
  line-height: 1.7;
}
.page {
  position: relative;
  min-height: 100vh;
  overflow: hidden;
}

.video-background {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  z-index: -1;
  /* 确保视频在内容后面 */
  overflow: hidden;
}

.background-video {
  width: 100%;
  height: 100%;
  object-fit: cover;
  /* 确保视频覆盖整个区域并保持比例 */
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  background: rgba(13, 27, 42, 0.4);
  /* 半透明遮罩，提高文字可读性 */
}

.page-content {
  position: relative;
  /* 确保内容在视频上方 */
  z-index: 1;
}

.journey-start {
  text-align: center;
  margin-top: 50px;
}

.start-button {
  display: inline-flex;
  align-items: center;
  gap: 15px;
  padding: 20px 40px;
  background: linear-gradient(140deg, #D4AF37, #F5CA0D);
  color: #0d1b2a;
  border: none;
  border-radius: 50px;
  font-size: 1.3rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  box-shadow: 0 8px 25px rgba(255, 232, 55, 0.3);
}

.start-button:hover {
  transform: translateY(-5px) scale(1.05);
  box-shadow: 0 15px 35px rgba(255, 235, 84, 0.3);
}

.arrow {
  font-size: 1.5rem;
  transition: transform 0.3s ease;
}

.start-button:hover .arrow {
  transform: translateX(5px);
}
/* 响应式调整 */
@media (max-width: 768px) {
  .background-video {
    object-position: center;
    /* 在移动设备上居中视频 */
  }
}
</style>