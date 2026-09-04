<template>
  <div class="page page-5">
    <div class="page-content">
      <div class="section-header">
        <div class="section-icon">🚀</div>
        <div>
          <h1 class="section-title">未来驿站 - 文化的共创</h1>
          <p class="section-subtitle">汇聚旅程收获，创造属于你的丝路信物</p>
        </div>
      </div>
      
      <div class="story-content">
        <p>恭喜你抵达旅程的终点——未来驿站！这里是传统与创新交汇的圣地，数智人"丝路"邀请你进行终极的文化共创。</p>
        <p>"现在，请将你一路的收获——历史的厚重、时光的印记、古今交响的旋律——融汇在一起，创造出属于你的丝路信物。"</p>
        <p>在这里，你可以运用AI文创生成技术，将沿途获得的文化元素与现代创意结合，设计独一无二的数字艺术品。</p>
      </div>
      
      <div class="content-section">
        <h3 class="section-title">文创生成工坊</h3>
        <div class="creation-studio">
          <div class="creation-item">
            <img src="https://images.unsplash.com/photo-1541961017774-22349e4a1262?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="敦煌元素" class="creation-img">
            <h4>敦煌纹样+现代设计</h4>
            <p>将飞天壁画元素融入现代产品设计</p>
          </div>
          <div class="creation-item">
            <img src="https://images.unsplash.com/photo-1580537659466-0a9bfa916a54?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="长安色彩" class="creation-img">
            <h4>长安色彩+数字艺术</h4>
            <p>提取唐代色彩 palette 创作数字画作</p>
          </div>
          <div class="creation-item">
            <img src="https://images.unsplash.com/photo-1493225457124-a3eb161ffa5f?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="音乐可视化" class="creation-img">
            <h4>丝路旋律+视觉艺术</h4>
            <p>将生成的音乐转化为视觉艺术作品</p>
          </div>
        </div>
      </div>
      
      <div class="completion-message">
        <h3>🎉 丝路之旅圆满结束！</h3>
        <p>你成功完成了从长安到未来驿站的完整文化体验，见证了传统与现代在数字时代的完美融合！</p>
        <button class="btn restart-btn" @click="$emit('go-to-page', 0)">重新开始旅程</button>
      </div>
    </div>
    <div class="page-number">5</div>
  </div>
</template>

<script setup>
defineEmits(['go-to-page'])
</script>

<style scoped>
.page.page-5{
  background-image: url('@/assets/images/future-bg.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  /* 半透明黑色遮罩（可调整 alpha 值控制暗度，范围 0~1） */
  background-color: rgba(0, 0, 0, 0.5); 
  /* 混合模式：让背景图与黑色「正片叠底」，自然暗化 */
  background-blend-mode: multiply; 
  min-height: 100vh; /* 确保背景占满屏幕高度 */
}
.creation-studio {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(280px, 1fr));
  gap: 25px;
  margin-top: 25px;
}

.creation-item {
  background: rgba(196, 176, 121, 0.6);
  border-radius: 12px;
  overflow: hidden;
  transition: all 0.3s ease;
  border: 1px solid rgba(255, 255, 255, 0.1);
}

.creation-item:hover {
  transform: translateY(-8px);
  box-shadow: 0 12px 25px rgba(0, 0, 0, 0.4);
  border-color: rgba(6, 214, 160, 0.3);
}

.creation-img {
  width: 100%;
  height: 200px;
  object-fit: cover;
  border-bottom: 1px solid rgba(45, 55, 72, 0.5);
}

.creation-item h4 {
  padding: 20px 20px 10px;
  color: #ffd166;
  font-size: 1.2rem;
}

.creation-item p {
  padding: 0 20px 20px;
  color: #f0f1f1;
  line-height: 1.5;
}

.completion-message {
  text-align: center;
 background: rgba(196, 176, 121, 0.6);
  padding: 40px;
  border-radius: 15px;
  margin-top: 40px;
  border: 2px solid rgba(6, 214, 160, 0.3);
}

.completion-message h3 {
  font-size: 2rem;
  margin-bottom: 20px;
  color: #ffd166;
}

.restart-btn {
  background: linear-gradient(135deg, #06d6a0, #118ab2);
  color: #0d1b2a;
  padding: 15px 30px;
  font-size: 1.1rem;
  border: none;
  border-radius: 8px;
  cursor: pointer;
  transition: all 0.3s ease;
  margin-top: 20px;
}

.restart-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(6, 214, 160, 0.4);
}
</style>