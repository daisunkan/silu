<template>
  <div class="page page-3">
    <div class="page-content">
      <div class="section-header">
        <div class="section-icon">🕌</div>
        <div>
          <h1 class="section-title">敦煌 - 时光的修复师</h1>
          <p class="section-subtitle">修复历史的碎片，重建与过去对话的桥梁</p>
        </div>
      </div>
      
      <div class="story-content">
        <p>现在我们来到敦煌，这座沙漠中的艺术宝库。岁月的侵蚀让许多珍贵的文物面临损坏的危险，而你将成为"时光修复师"。</p>
        <p>数智人"丝路"引导你亲手修复一件破损的文物。在这个过程中，你将深刻体会到：我们修复的不仅是历史的碎片，更是与过去对话的桥梁。</p>
        <p>每一道纹路、每一片色彩都承载着古人的智慧与情感。通过AI修复技术，我们让这些沉默的文物重新诉说它们的故事。</p>
      </div>
      
      <div class="content-section">
        <h3 class="section-title">文物修复过程</h3>
        <div class="image-gallery">
          <div class="image-card">
            <img src="https://images.unsplash.com/photo-1576086213369-97a306d36557?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="破损壁画" class="card-img">
            <div class="card-content">
              <h4 class="card-title">破损壁画</h4>
              <p class="card-desc">敦煌壁画因岁月侵蚀而出现的剥落和褪色</p>
            </div>
          </div>
          <div class="image-card">
            <img src="https://images.unsplash.com/photo-1580537659466-0a9bfa916a54?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="AI修复" class="card-img">
            <div class="card-content">
              <h4 class="card-title">AI智能修复</h4>
              <p class="card-desc">基于深度学习算法重建缺失部分</p>
            </div>
          </div>
          <div class="image-card">
            <img src="https://images.unsplash.com/photo-1541961017774-22349e4a1262?ixlib=rb-1.2.1&auto=format&fit=crop&w=500&q=80" alt="修复完成" class="card-img">
            <div class="card-content">
              <h4 class="card-title">修复成果</h4>
              <p class="card-desc">恢复原有色彩的敦煌壁画艺术</p>
            </div>
          </div>
        </div>
      </div>
    </div>
    <div class="page-number">3</div>
  </div>
</template>

<script setup>
// 这个页面不需要额外的逻辑
</script>

<style scoped>
.page.page-3 {
  /* 设置背景图片，假设本地图片放在 src/assets 目录下 */
  background-image: url('@/assets/images/duhuang-bg.jpg');
  /* 让背景图片覆盖整个容器 */
  background-size: cover;
  /* 背景图片居中显示 */
  background-position: center;
  /* 防止背景图片重复 */
  background-repeat: no-repeat;
  /* 如果需要固定背景不随滚动移动，可添加以下属性 */
  /* background-attachment: fixed; */
  /* 可以根据需要添加背景透明度 */
  /* background-color: rgba(255, 255, 255, 0.8); */
}
</style>