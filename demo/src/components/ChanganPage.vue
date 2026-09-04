<template>
  <div class="page page-2">
    <div class="page-content">
      <div class="section-header">
        <div class="section-icon">🏯</div>
        <div>
          <h1 class="section-title">长安 - 文明的基石</h1>
          <p class="section-subtitle">丝绸之路的起点，开放交融的见证</p>
        </div>
      </div>
      
      <div class="story-content">
        <p>欢迎来到长安，这座千年古都是丝绸之路的起点，也是文明交融的熔炉。在这里，你将了解丝绸之路真正的精神内核。</p>
        <p>数智人"丝路"将为你讲解：这条伟大的道路自古就是吸收、转化并传播各种文化的平台。它不仅仅是商品的流通，更是思想、艺术和技术的交汇。</p>
        <p>这种开放与交融的精神，为我们后续将现代元素融入传统文化奠定了历史合理性。古今交融，本就是丝路精神的延续。</p>
      </div>
      
      <div class="content-section">
        <h3 class="section-title">数智人讲解展示</h3>
        <div class="image-gallery">
          <div class="image-card">
            <img src="https://images.unsplash.com/photo-1541336032412-2048a678540d?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="长安古城" class="card-img">
            <div class="card-content">
              <h4 class="card-title">长安古城复原</h4>
              <p class="card-desc">基于历史资料重建的唐代长安城数字模型</p>
            </div>
          </div>
          <div class="image-card">
            <img src="https://images.unsplash.com/photo-1589994965851-a8f479c573a9?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="丝路商队" class="card-img">
            <div class="card-content">
              <h4 class="card-title">丝路商队</h4>
              <p class="card-desc">往来于东西方的商队，承载着文明交流的使命</p>
            </div>
          </div>
          <div class="image-card">
            <img src="https://images.unsplash.com/photo-1559056199-641a0ac8b55e?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="文化交流" class="card-img">
            <div class="card-content">
              <h4 class="card-title">文化交融</h4>
              <p class="card-desc">不同文明在长安交汇融合，创造新的文化形态</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="page-number">2</div>
  </div>
</template>

<script setup>
// 这个页面不需要额外的逻辑
</script>
<style scoped>
.page.page-2{
  background-image: url('@/assets/images/changan-bg.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  /* 半透明黑色遮罩（可调整 alpha 值控制暗度，范围 0~1） */
  background-color: rgba(0, 0, 0, 0.5); 
  /* 混合模式：让背景图与黑色「正片叠底」，自然暗化 */
  background-blend-mode: multiply; 
  min-height: 100vh; /* 确保背景占满屏幕高度 */
}
</style>