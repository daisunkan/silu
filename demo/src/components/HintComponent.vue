<template>
  <div class="hint">使用鼠标滚轮或键盘左右键翻页</div>
</template>

<style scoped>
.hint {
  position: fixed;
  bottom: 2rem;
  left: 50%;
  transform: translateX(-50%);
  background: rgba(0, 0, 0, 0.5);
  padding: 0.5rem 1rem;
  border-radius: 20px;
  font-size: 0.9rem;
  backdrop-filter: blur(5px);
  animation: pulse 2s infinite;
}

@keyframes pulse {
  0% { opacity: 0.7; }
  50% { opacity: 1; }
  100% { opacity: 0.7; }
}
</style>