<script setup>
// 在 Vue 3.3+ 中，defineProps 和 defineEmits 是编译器宏，不需要导入
// 直接使用即可，也不需要赋值给变量

defineProps({
  steps: {
    type: Array,
    required: true
  },
  currentStep: {
    type: Number,
    required: true
  }
})

const emit = defineEmits(['step-click'])
</script>

<template>
  <div class="story-progress-container">
    <div class="story-progress">
      <div 
        v-for="(step, index) in steps" 
        :key="index" 
        class="progress-step"
        @click="emit('step-click', index)"
      >
        <div 
          class="step-circle"
          :class="{ 
            active: step.active, 
            completed: step.completed 
          }"
        >
          {{ step.number }}
        </div>
        <div 
          class="step-title"
          :class="{ active: step.active }"
        >
          {{ step.title }}
        </div>
      </div>
    </div>
  </div>
</template>

<style scoped>
.story-progress-container {
  position: fixed;
  top: 80px;
  left: 0;
  right: 0;
  z-index: 99;
  background: rgba(13, 27, 42, 0.9);
  backdrop-filter: blur(10px);
  padding: 15px 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.story-progress {
  display: flex;
  justify-content: space-between;
  max-width: 800px;
  margin: 0 auto;
  position: relative;
  padding: 0 20px;
}

.story-progress::before {
  content: '';
  position: absolute;
  top: 25px;
  left: 50px;
  right: 50px;
  height: 3px;
  background: rgba(45, 55, 72, 0.6);
  z-index: 1;
}

.progress-step {
  text-align: center;
  position: relative;
  z-index: 2;
  flex: 1;
  cursor: pointer;
  transition: all 0.3s ease;
}

.progress-step:hover {
  transform: translateY(-2px);
}

.step-circle {
  width: 50px;
  height: 50px;
  border-radius: 50%;
  background: rgba(45, 55, 72, 0.8);
  margin: 0 auto 12px;
  display: flex;
  align-items: center;
  justify-content: center;
  font-weight: bold;
  font-size: 1.2rem;
  color: #a0aec0;
  border: 3px solid rgba(45, 55, 72, 0.8);
  transition: all 0.3s ease;
  position: relative;
  z-index: 2;
}

.step-circle.active {
  background: linear-gradient(135deg, #06d6a0, #118ab2);
  color: #0d1b2a;
  border-color: #06d6a0;
  box-shadow: 0 0 20px rgba(6, 214, 160, 0.5);
  transform: scale(1.1);
}

.step-circle.completed {
  background: linear-gradient(135deg, #118ab2, #073b4c);
  color: #ffffff;
  border-color: #118ab2;
}

.step-title {
  font-size: 0.9rem;
  color: #a0aec0;
  font-weight: 500;
  transition: all 0.3s ease;
}

.step-title.active {
  color: #06d6a0;
  font-weight: bold;
}

/* 响应式设计 */
@media (max-width: 768px) {
  .story-progress {
    flex-direction: column;
    align-items: center;
    gap: 15px;
  }
  
  .story-progress::before {
    display: none;
  }
  
  .progress-step {
    display: flex;
    align-items: center;
    gap: 15px;
    width: 100%;
    max-width: 250px;
  }
  
  .step-circle {
    margin: 0;
    flex-shrink: 0;
  }
  
  .step-title {
    text-align: left;
    flex: 1;
  }
}
</style>