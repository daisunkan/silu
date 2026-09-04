<template>
  <div 
    class="special-card-container"
    :class="{ 'is-expanded': isExpanded }"
  >
    <!-- 主卡片按钮 -->
    <div 
      class="card-button"
      @mouseenter="expandCard"
      @mouseleave="startCollapseTimer"
    >
      <!-- 卡片内容 -->
      <img 
        v-if="imageUrl || imagePath" 
        :src="getImageUrl" 
        alt="Card Image"
        class="card-image"
      >
      <div v-else class="default-content">
        <slot name="button-content"></slot>
      </div>
    </div>

    <!-- 展开的卡片 - 使用 transition 组件包裹 -->
    <transition
      name="card-expand"
      @enter="onExpandEnter"
    >
      <div 
        class="expanded-card"
        v-show="isExpanded"
        @mouseenter="cancelCollapse"
        @mouseleave="collapseCard"
      >
        <!-- 展开卡片内容 -->
        <slot name="expanded-content"></slot>
      </div>
    </transition>

    <!-- 图片查看模态框 -->
    <transition name="fade">
      <div 
        v-if="showImageModal"
        class="image-modal"
        @click.self="showImageModal = false"
      >
        <div class="image-modal-content">
          <span 
            class="close-btn"
            @click="showImageModal = false"
          >×</span>
          <img
            :src="getImageUrl"
            class="modal-image"
            ref="modalImage"
            @wheel="handleZoom"
          >
        </div>
      </div>
    </transition>
  </div>
</template>

<script>
export default {
  name: 'SpecialCard',
  props: {
    // 支持外部 URL
    imageUrl: {
      type: String,
      default: ''
    },
    // 支持本地图片路径
    imagePath: {
      type: String,
      default: ''
    }
  },
  data() {
    return {
      isExpanded: false,
      collapseTimer: null,
      expandedPosition: {
        top: -100,
        left: '50%',
        transform: 'translateX(-50%)'
      },
      showImageModal: false,
      scale: 1
    }
  },
  computed: {
    getImageUrl() {
      // 如果提供了 imagePath，使用 require 加载本地图片
      if (this.imagePath) {
        try {
          return require(`@/assets/${this.imagePath}`);
        } catch (e) {
          console.warn('本地图片加载失败:', e);
          return '';
        }
      }
      // 否则使用外部 URL
      return this.imageUrl;
    }
  },
  methods: {
    expandCard() {
      clearTimeout(this.collapseTimer)
      this.calculateExpandedPosition()
      this.isExpanded = true
    },
    startCollapseTimer() {
      this.collapseTimer = setTimeout(() => {
        this.isExpanded = false
      }, 300)
    },
    cancelCollapse() {
      clearTimeout(this.collapseTimer)
    },
    collapseCard() {
      this.isExpanded = false
    },
    onExpandEnter(el) {
      // 确保元素在动画开始前有正确的初始状态
      el.style.display = 'block'
      // 应用计算后的位置
      el.style.top = `${this.expandedPosition.top}px`
      el.style.left = this.expandedPosition.left
      el.style.transform = this.expandedPosition.transform
    },

    mounted() {
      window.addEventListener('resize', this.handleWindowResize)
    },
    beforeDestroy() {
      window.removeEventListener('resize', this.handleWindowResize)
    },
    handleWindowResize() {
      if (this.isExpanded) {
        this.calculateExpandedPosition()
      }
    },
    calculateExpandedPosition() {
      const cardRect = this.$el.getBoundingClientRect()
      const expandedWidth = 220 // 展开卡片宽度
      const expandedHeight = 200 // 估计展开卡片高度
      const viewportWidth = window.innerWidth
      const viewportHeight = window.innerHeight
      const viewportPadding = 10 // 视口边距

      // 计算水平位置
      let left = '50%'
      let transform = 'translateX(-50%)'
      
      // 检查右侧是否会超出
      if (cardRect.right + expandedWidth/2 > viewportWidth - viewportPadding) {
        left = 'auto'
        transform = 'none'
        this.expandedPosition.left = `${viewportWidth - expandedWidth - viewportPadding}px`
      } 
      // 检查左侧是否会超出
      else if (cardRect.left - expandedWidth/2 < viewportPadding) {
        left = 'auto'
        transform = 'none'
        this.expandedPosition.left = `${viewportPadding}px`
      } else {
        this.expandedPosition.left = left
      }

      this.expandedPosition.transform = transform

      // 计算垂直位置
      // 默认向上展开
      let top = -100
      
      // 检查顶部是否会超出
      if (cardRect.top - expandedHeight < viewportPadding) {
        // 改为向下展开
        top = cardRect.height + 20
      }
      
      this.expandedPosition.top = top
    },
    handleZoom(e) {
      e.preventDefault()
      const delta = e.deltaY > 0 ? -0.1 : 0.1
      this.scale = Math.min(Math.max(0.5, this.scale + delta), 3)
      this.$refs.modalImage.style.transform = `scale(${this.scale})`
    }
  }
}
</script>

<style scoped>
.special-card-container {
  position: relative;
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.card-button {
  width: 160px;
  height: 160px;
  border-radius: 20px;
  background: #ffffff;
  box-shadow: 0 4px 12px rgba(0, 0, 0, 0.15);
  display: flex;
  justify-content: center;
  align-items: center;
  cursor: pointer;
  transition: all 0.3s cubic-bezier(0.25, 0.8, 0.25, 1);
  position: relative;
  z-index: 1;
  overflow: hidden;
}

.card-button:hover {
  transform: translateY(-5px) scale(1.03);
  box-shadow: 0 8px 16px rgba(0, 0, 0, 0.2);
}

.card-image {
  width: 100%;
  height: 100%;
  object-fit: cover;
  border-radius: 20px;
}

.default-content {
  width: 100%;
  height: 100%;
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  padding: 10px;
  box-sizing: border-box;
}

.expanded-card {
  position: absolute;
  width: 220px;
  padding: 16px;
  z-index: 2;
  overflow: hidden;
  /* 轻微模糊效果 */
  backdrop-filter: blur(8px);
  /* 透明边框 */
  border: 1px solid rgba(255, 255, 255, 0.6);
  /* 清澈透明背景 */
  background: rgba(255, 255, 255, 0.591);
  border-radius: 16px;
  /* 简洁阴影 */
  box-shadow: 
      0 4px 16px rgba(0, 0, 0, 0.1),
      0 0 0 1px rgba(255, 255, 255, 0.4);
}

/* 展开动画 */
.card-expand-enter-active,
.card-expand-leave-active {
  transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.card-expand-enter-from {
  opacity: 0;
  transform: translateX(-50%) translateY(15px) scale(0.85);
  top: -80px;
}

.card-expand-enter-to {
  opacity: 1;
  transform: translateY(0) scale(1);
}

.card-expand-leave-from {
  opacity: 1;
  transform: translateX(-50%) translateY(0) scale(1);
  top: -100px;
}

.card-expand-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(10px) scale(0.9);
  top: -80px;
}
</style>