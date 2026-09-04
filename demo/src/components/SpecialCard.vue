<template>
  <div class="special-card-container" :class="{ 'is-expanded': isExpanded }">
    <!-- 主卡片按钮 -->
    <div class="card-button" @mouseenter="expandCard" @mouseleave="startCollapseTimer" @click="handleCardClick">
      <!-- 卡片内容 -->
      <img v-if="imageUrl || imagePath" :src="getImageUrl" alt="Card Image" class="card-image">
      <div v-else class="default-content">
        <slot name="button-content"></slot>
      </div>
    </div>

    <!-- 展开的卡片 - 使用 transition 组件包裹 -->
    <transition name="card-expand" @enter="onExpandEnter">
      <div v-show="isExpanded" class="expanded-card" @mouseenter="cancelCollapse" @mouseleave="collapseCard">
        <!-- 展开卡片内容 -->
        <slot name="expanded-content"></slot>
      </div>
    </transition>

    <!-- 图片查看模态框 -->
    <transition name="fade">
      <div v-if="showImageModal" class="image-modal" @click.self="showImageModal = false">
        <div class="image-modal-content">
          <span class="close-btn" @click="showImageModal = false">×</span>
           <img
            ref="modalImage"
            class="modal-image"
            :src="getImageUrl"
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
  // 明确声明组件发出的事件
  emits: {
    'card-click': null
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
      if (this.imagePath) {
        try {
          return require(`@/assets/images/${this.imagePath}`);
        } catch (e) {
          console.warn('本地图片加载失败:', e);
          return '';
        }
      }
      return this.imageUrl;
    }
  },
  // 生命周期钩子应该在 methods 之前
  mounted() {
    window.addEventListener('resize', this.handleWindowResize)
  },
  beforeUnmount() {  // 使用 beforeUnmount 替代 beforeDestroy
    window.removeEventListener('resize', this.handleWindowResize)
  },
  methods: {
    handleCardClick() {
      this.$emit('card-click')
    },
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
      el.style.display = 'block'
      el.style.top = `${this.expandedPosition.top}px`
      el.style.left = this.expandedPosition.left
      el.style.transform = this.expandedPosition.transform
      
      console.log('展开卡片位置:', this.expandedPosition)
    },
    calculateExpandedPosition() {
      const cardRect = this.$el.getBoundingClientRect()
      const expandedWidth = 280
      const expandedHeight = 200
      const viewportWidth = window.innerWidth
      const viewportHeight = window.innerHeight
      const viewportPadding = 10

      console.log('卡片位置:', cardRect)
      console.log('视口尺寸:', viewportWidth, viewportHeight)

      // 计算水平位置 - 改进版本
      let left = '50%'
      let transform = 'translateX(-50%)'
      let finalLeft = cardRect.left + (cardRect.width / 2)

      // 检查右侧是否会超出
      if (finalLeft + expandedWidth / 2 > viewportWidth - viewportPadding) {
        left = 'auto'
        transform = 'none'
        this.expandedPosition.left = `${viewportWidth - expandedWidth - viewportPadding}px`
      }
      // 检查左侧是否会超出
      else if (finalLeft - expandedWidth / 2 < viewportPadding) {
        left = 'auto'
        transform = 'none'
        this.expandedPosition.left = `${viewportPadding}px`
      } else {
        this.expandedPosition.left = left
      }

      this.expandedPosition.transform = transform

      // 改进的垂直位置计算
      let top = -expandedHeight - 10 // 默认向上展开，留出间距

      // 检查顶部空间是否足够
      const spaceAbove = cardRect.top - viewportPadding
      const spaceBelow = viewportHeight - cardRect.bottom - viewportPadding
      
      console.log('顶部空间:', spaceAbove, '底部空间:', spaceBelow)

      // 如果顶部空间不足但底部空间足够，改为向下展开
      if (spaceAbove < expandedHeight && spaceBelow >= expandedHeight) {
        top = cardRect.height + 10
      }
      // 如果上下空间都不足，选择空间更大的方向
      else if (spaceAbove < expandedHeight && spaceBelow < expandedHeight) {
        top = spaceAbove >= spaceBelow ? -expandedHeight - 10 : cardRect.height + 10
      }

      this.expandedPosition.top = top
      console.log('最终位置 - top:', top, 'left:', this.expandedPosition.left)
    },
    handleZoom(e) {
      e.preventDefault()
      const delta = e.deltaY > 0 ? -0.1 : 0.1
      this.scale = Math.min(Math.max(0.5, this.scale + delta), 3)
      this.$refs.modalImage.style.transform = `scale(${this.scale})`
    },
    handleWindowResize() {
      if (this.isExpanded) {
        this.calculateExpandedPosition()
      }
    }
  }
}
</script>

<style scoped>
.special-card-container {
  position: relative;
  width: 100%; /* 确保容器占满网格单元格 */
  height: 100%;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
}

.card-button {
  width: 100%; /* 改为百分比宽度 */
  height: auto; /* 高度自适应 */
  aspect-ratio: 1/1; /* 保持正方形比例 */
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
  transition: transform 0.3s ease;
}

.card-button:hover .card-image {
  transform: scale(1.05);
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
  width: 280px;
  padding: 20px;
  z-index: 1000; /* 提高 z-index 确保在最上层 */
  overflow: visible; /* 改为 visible 避免被裁剪 */
  backdrop-filter: blur(8px);
  border: 1px solid rgba(255, 255, 255, 0.6);
  background: rgba(255, 255, 255, 0.85); /* 提高背景不透明度 */
  border-radius: 16px;
  box-shadow:
    0 4px 16px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.4);
  /* 防止内容被裁剪 */
  box-sizing: border-box;
}

/* 展开动画 */
.card-expand-enter-active,
.card-expand-leave-active {
  transition: all 0.4s cubic-bezier(0.68, -0.55, 0.265, 1.55);
}

.card-expand-enter-from {
  opacity: 0;
  transform: translateX(-50%) translateY(15px) scale(0.85);
}

.card-expand-enter-to {
  opacity: 1;
  transform: translateY(0) scale(1);
}

.card-expand-leave-from {
  opacity: 1;
  transform: translateX(-50%) translateY(0) scale(1);
}

.card-expand-leave-to {
  opacity: 0;
  transform: translateX(-50%) translateY(10px) scale(0.9);
}
</style>