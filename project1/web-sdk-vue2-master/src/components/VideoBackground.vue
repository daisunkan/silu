<template>
  <div class="video-background">
    <video 
      ref="videoPlayer"
      :src="currentVideo" 
      :style="videoStyle"
      muted
      autoplay
      @loadedmetadata="onVideoLoaded"
      @ended="playNextVideo"
    >
      您的浏览器不支持视频播放
    </video>
    <div class="video-overlay" :style="overlayStyle"></div>
  </div>
</template>

<script>
export default {
  name: 'VideoBackground',
  data() {
    return {
      videos: [
        // 替换为您的视频路径
        require('@/assets/videos/video1.mp4'),
      ],
      currentIndex: 0,
      // 可调整的模糊参数
      blurAmount: 2,
      // 可调整的透明度
      opacityAmount: 0.5
    }
  },
  computed: {
    currentVideo() {
      return this.videos[this.currentIndex];
    },
    videoStyle() {
      return {
        filter: `blur(${this.blurAmount}px)`,
        opacity: this.opacityAmount
      }
    },
    overlayStyle() {
      return {
        backdropFilter: `blur(${this.blurAmount * 0.5}px)`,
        WebkitBackdropFilter: `blur(${this.blurAmount * 0.5}px)`
      }
    }
  },
  mounted() {
    this.playCurrentVideo();
    // 可选：添加键盘事件来调整参数
    this.setupControls();
  },
  beforeDestroy() {
    if (this.$refs.videoPlayer) {
      this.$refs.videoPlayer.pause();
    }
    // 清理事件监听
    window.removeEventListener('keydown', this.handleKeyPress);
  },
  methods: {
    playCurrentVideo() {
      if (this.$refs.videoPlayer) {
        this.$refs.videoPlayer.load();
        this.$refs.videoPlayer.play().catch(error => {
          console.log('视频播放失败:', error);
        });
      }
    },
    
    playNextVideo() {
      this.currentIndex = (this.currentIndex + 1) % this.videos.length;
      this.$nextTick(() => {
        this.playCurrentVideo();
      });
    },
    
    onVideoLoaded() {
      // 视频加载完成后的回调
      const video = this.$refs.videoPlayer;
      console.log(`视频尺寸: ${video.videoWidth}x${video.videoHeight}`);
    },
    
    setupControls() {
      // 添加键盘控制来调整参数
      window.addEventListener('keydown', this.handleKeyPress);
    },
    
    handleKeyPress(event) {
      // 按键控制模糊程度和透明度
      switch(event.key) {
        case 'ArrowUp':
          this.blurAmount = Math.min(this.blurAmount + 0.5, 20);
          event.preventDefault();
          break;
        case 'ArrowDown':
          this.blurAmount = Math.max(this.blurAmount - 0.5, 0);
          event.preventDefault();
          break;
        case 'ArrowRight':
          this.opacityAmount = Math.min(this.opacityAmount + 0.1, 1);
          event.preventDefault();
          break;
        case 'ArrowLeft':
          this.opacityAmount = Math.max(this.opacityAmount - 0.1, 0.1);
          event.preventDefault();
          break;
        case ' ':
          // 空格键切换视频
          this.playNextVideo();
          event.preventDefault();
          break;
      }
    },
    
    // 外部可调用的方法
    setBlur(amount) {
      this.blurAmount = Math.max(0, Math.min(amount, 20));
    },
    
    setOpacity(amount) {
      this.opacityAmount = Math.max(0.1, Math.min(amount, 1));
    },
    
    skipToVideo(index) {
      if (index >= 0 && index < this.videos.length) {
        this.currentIndex = index;
        this.playCurrentVideo();
      }
    }
  }
}
</script>

<style scoped>
.video-background {
  position: fixed;
  left: 0;
  top: 0;
  width: 100vw;
  height: 100vh;
  overflow: hidden;
  z-index: -1; /* 置于底层 */
}

.video-background video {
  /* 保持视频原始比例，填充整个容器 */
  width: 100%;
  height: 100%;
  object-fit: cover; /* 保持比例填充，可能会裁剪边缘 */
  transition: all 1s ease;
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  pointer-events: none; /* 允许点击穿透 */
}

/* 响应式调整 */
@media (max-aspect-ratio: 16/9) {
  .video-background video {
    width: 100%;
    height: auto;
  }
}

@media (min-aspect-ratio: 16/9) {
  .video-background video {
    width: auto;
    height: 100%;
  }
}
</style>