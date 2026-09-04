<template>
  <div class="fullscreen-music-generator">
    <!-- ========== 视频背景 ========== -->
    <div class="video-background">
      <video ref="bg-video" autoplay muted loop playsinline class="bg-video">
        <source src="@/assets/silkroad-background.mp4" type="video/mp4">
        <!-- 备用高质量背景图片 -->
        
      </video>
      <div class="video-overlay"></div>
    </div>
    
    <!-- ========== 功能选择器 ========== -->
    <div class="function-selector">
      <button :class="{ active: activeFunction === 'upload' }" @click="activeFunction = 'upload'">
        <i class="fa fa-upload"></i> 音频风格转换
      </button>
      <button :class="{ active: activeFunction === 'generate' }" @click="activeFunction = 'generate'">
        <i class="fa fa-music"></i> 歌词生成音乐
      </button>
    </div>

    <!-- ========== 内容区域 ========== -->
    <div class="fullscreen-content-area">
      <!-- 音频风格转换功能 -->
      <div v-if="activeFunction === 'upload'" class="function-content upload-content">
        <div class="fullscreen-layout">
          <!-- 左侧主内容区 -->
          <div class="main-panel">
            <div class="panel-header">
              <h1 class="center-title">
                <i class="fa fa-upload"></i>
                音频风格转换
              </h1>
              <p class="center-description">上传音频文件，一键转换为您喜欢的音乐风格</p>
            </div>

            <!-- 上传区域 -->
            <div class="upload-section">
              <div class="upload-area" @click="triggerUpload" @drop="handleDrop" @dragover.prevent @dragenter.prevent>
                <div class="upload-icon">
                  <i class="fa fa-cloud-upload"></i>
                </div>
                <div class="upload-text">
                  <p class="main-text">拖放音频文件到此处，或点击选择</p>
                  <p class="sub-text">支持 MP3、WAV 格式，最大 50MB</p>
                </div>
                <input type="file" ref="fileInput" @change="handleFileSelect" accept="audio/mp3,audio/wav"
                  style="display: none">
              </div>

              <!-- 文件信息显示 -->
              <div v-if="selectedFile" class="file-info-section">
                <div class="file-header">
                  <i class="fa fa-file-audio-o"></i>
                  <span class="file-name">{{ selectedFile.name }}</span>
                  <span class="file-size">({{ formatFileSize(selectedFile.size) }})</span>
                  <button @click="clearFile" class="clear-btn">
                    <i class="fa fa-times"></i>
                  </button>
                </div>
                <div class="progress-container">
                  <div class="progress-bar">
                    <div class="progress" :style="{ width: uploadProgress + '%' }"></div>
                  </div>
                  <span class="progress-text">{{ uploadProgress }}%</span>
                </div>
              </div>
            </div>

            <!-- 转换设置 -->
            <div class="settings-section">
              <h3 class="settings-title">
                <i class="fa fa-cog"></i>
                转换设置
              </h3>

              <div class="setting-group">
                <label class="setting-label">目标风格</label>
                <div class="style-selector">
                  <select v-model="selectedStyle" class="form-select">
                    <option value="traditional Chinese Zheng">🎵 古筝风格</option>
                    <option value="traditional Chinese Pipa">🎵 琵琶风格</option>
                    <option value="traditional Chinese Erhu">🎵 二胡风格</option>
                    <option value="traditional Chinese flute">🎵 笛子风格</option>
                  </select>
                </div>
              </div>

              <button @click="uploadAndConvert" :disabled="!selectedFile || isUploading" class="action-btn primary-btn">
                <i class="fa fa-magic"></i>
                <span v-if="!isUploading">开始风格转换</span>
                <span v-else>
                  <i class="fa fa-spinner fa-spin"></i> 转换中...
                </span>
              </button>

              <div v-if="!selectedFile" class="hint-text">
                <i class="fa fa-info-circle"></i>
                请先选择要转换的音频文件
              </div>
            </div>
          </div>

          <!-- 右侧结果区 -->
          <div class="result-panel">
            <!-- 转换结果 -->
            <div v-if="convertResult" class="result-section">
              <div class="result-header">
                <i class="fa fa-check-circle success-icon"></i>
                <h3>转换成功</h3>
              </div>
              <div class="audio-preview">
                <audio :src="convertResult" controls class="audio-player"></audio>
                <div class="audio-actions">
                  <a :href="convertResult" download class="download-btn">
                    <i class="fa fa-download"></i> 下载音频
                  </a>
                </div>
              </div>
            </div>

            <!-- 空状态提示 -->
            <div v-else class="empty-state">
              <div class="empty-image">
                <div class="music-icon">
                  <i class="fa fa-music"></i>
                </div>
              </div>
              <h3>等待转换</h3>
              <p>转换后的音频将显示在这里</p>
              <div class="feature-tips">
                <div class="tip-item">
                  <i class="fa fa-magic"></i>
                  <span>支持多种音乐风格转换</span>
                </div>
                <div class="tip-item">
                  <i class="fa fa-file-audio-o"></i>
                  <span>MP3、WAV格式</span>
                </div>
                <div class="tip-item">
                  <i class="fa fa-cloud-upload"></i>
                  <span>最大支持50MB文件</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>

      <!-- 歌词生成音乐功能 -->
      <div v-if="activeFunction === 'generate'" class="function-content generate-content">
        <div class="fullscreen-layout">
          <!-- 左侧主内容区 -->
          <div class="main-panel">
            <div class="panel-header">
              <h1 class="center-title">
                <i class="fa fa-music"></i>
                歌词生成音乐
              </h1>
              <p class="center-description">输入歌词内容，AI将为您创作专属音乐</p>
            </div>

            <!-- 歌词输入表单 -->
            <div class="form-section">
              <div class="form-grid">
                <div class="form-group">
                  <label class="form-label">
                    <i class="fa fa-header"></i>
                    歌曲标题
                  </label>
                  <input type="text" v-model="musicData.title" required placeholder="为您的歌曲起个名字..." class="form-input">
                </div>

                <div class="form-group">
                  <label class="form-label">
                    <i class="fa fa-tags"></i>
                    音乐风格
                  </label>
                  <select v-model="musicData.style" class="form-select">
                    <option value="traditional Chinese Zheng">🎵 古筝风格</option>
                    <option value="traditional Chinese Pipa">🎵 琵琶风格</option>
                    <option value="traditional Chinese Erhu">🎵 二胡风格</option>
                    <option value="traditional Chinese flute">🎵 笛子风格</option>
                  </select>
                </div>
              </div>

              <div class="form-group full-width">
                <label class="form-label">
                  <i class="fa fa-file-text"></i>
                  歌词内容
                </label>
                <textarea v-model="musicData.prompt" rows="8" required placeholder="请输入歌词内容，AI将根据歌词创作音乐..."
                  class="form-textarea"></textarea>
              </div>
            </div>

            <!-- 生成按钮 -->
            <div class="action-section">
              <button @click="generateMusic" :disabled="isGenerating" class="action-btn generate-btn">
                <i class="fa fa-bolt"></i>
                <span v-if="!isGenerating">立即生成音乐</span>
                <span v-else>
                  <i class="fa fa-spinner fa-spin"></i> AI创作中...
                </span>
              </button>

              <div v-if="generateStatus" class="status-indicator">
                <div class="status-icon">
                  <i class="fa fa-spinner fa-spin" v-if="generateStatus.includes('中')"></i>
                  <i class="fa fa-check" v-else-if="generateStatus.includes('完成')"></i>
                  <i class="fa fa-info-circle" v-else></i>
                </div>
                <div class="status-content">
                  <h5>生成状态</h5>
                  <p>{{ generateStatus }}</p>
                </div>
              </div>
            </div>
          </div>

          <!-- 右侧结果区 -->
          <div class="result-panel">
            <!-- 生成结果 -->
            <div v-if="generatedAudio" class="result-section">
              <div class="result-header">
                <i class="fa fa-star success-icon"></i>
                <h3>创作完成</h3>
              </div>
              <div class="audio-preview">
                <audio :src="generatedAudio" controls class="audio-player"></audio>
                <div class="audio-actions">
                  <a :href="generatedAudio" download class="download-btn">
                    <i class="fa fa-download"></i> 下载作品
                  </a>
                </div>
              </div>
            </div>

            <!-- 空状态提示 -->
            <div v-else class="empty-state">
              <div class="empty-image">
                <div class="music-icon">
                  <i class="fa fa-headphones"></i>
                </div>
              </div>
              <h3>等待创作</h3>
              <p>生成的音乐作品将显示在这里</p>
              <div class="feature-tips">
                <div class="tip-item">
                  <i class="fa fa-pencil"></i>
                  <span>输入歌词自动生成音乐</span>
                </div>
                <div class="tip-item">
                  <i class="fa fa-sliders"></i>
                  <span>多种音乐风格选择</span>
                </div>
              </div>
            </div>
          </div>
        </div>
      </div>
    </div>
  </div>
</template>

<script>
import { fileService, musicService } from '@/services/suno-api';

export default {
  name: 'FullscreenMusicGenerator',
  data() {
    return {
      // ========== 功能切换 ==========
      activeFunction: 'generate',

      // ========== 音频风格转换数据 ==========
      selectedFile: null,
      selectedStyle: 'traditional Chinese Zheng',
      uploadProgress: 0,
      isUploading: false,
      convertResult: null,

      // ========== 歌词生成音乐数据 ==========
      musicData: {
        title: '',
        style: 'traditional Chinese Zheng',
        prompt: '',
        instrumental: false,
        customMode: true,
        model: 'V4',
        callBackUrl: '/api/music/callback',
        styleWeight: 0.9,
        negativeTags: 'piano, guitar, drums, electronic, synth, bass, western instruments, pop, rock, jazz, orchestra, violin, cello, trumpet, saxophone'
      },
      isGenerating: false,
      generateStatus: '',
      generatedAudio: null
    };
  },
  methods: {
    // ========== 文件上传相关方法 ==========
    triggerUpload() {
      this.$refs.fileInput.click();
    },

    handleFileSelect(event) {
      const files = event.target.files;
      if (files && files.length > 0) {
        this.selectedFile = files[0];
        this.validateFile();
      }
    },

    handleDrop(event) {
      event.preventDefault();
      const files = event.dataTransfer.files;
      if (files && files.length > 0) {
        this.selectedFile = files[0];
        this.validateFile();
      }
    },

    validateFile() {
      if (!this.selectedFile) {
        return false;
      }

      // 检查文件类型
      const validTypes = ['audio/mp3', 'audio/wav', 'audio/mpeg'];
      const fileType = this.selectedFile.type;

      if (!validTypes.includes(fileType)) {
        alert('请选择MP3或WAV格式的音频文件');
        this.clearFile();
        return false;
      }

      // 检查文件大小 (50MB)
      const maxSize = 50 * 1024 * 1024;
      if (this.selectedFile.size > maxSize) {
        alert('文件大小不能超过50MB');
        this.clearFile();
        return false;
      }

      return true;
    },

    formatFileSize(bytes) {
      if (bytes === 0) return '0 Bytes';
      const k = 1024;
      const sizes = ['Bytes', 'KB', 'MB', 'GB'];
      const i = Math.floor(Math.log(bytes) / Math.log(k));
      return parseFloat((bytes / Math.pow(k, i)).toFixed(2)) + ' ' + sizes[i];
    },

    clearFile() {
      this.selectedFile = null;
      this.uploadProgress = 0;
      this.convertResult = null;

      // 重置文件输入
      if (this.$refs.fileInput) {
        this.$refs.fileInput.value = '';
      }
    },

    // ========== 音频风格转换方法 ==========
    async uploadAndConvert() {
      if (!this.validateFile()) {
        alert('请先选择有效的音频文件');
        return;
      }

      this.isUploading = true;
      this.uploadProgress = 0;
      this.convertResult = null;

      try {
        // 模拟上传进度
        const progressInterval = setInterval(() => {
          if (this.uploadProgress < 80) {
            this.uploadProgress += 10;
          }
        }, 300);

        // 1. 上传文件到服务器
        const uploadResponse = await fileService.upload(this.selectedFile);

        if (uploadResponse.data && uploadResponse.data.uploadUrl) {
          // 2. 调用风格转换API
          const convertData = {
            uploadUrl: uploadResponse.data.uploadUrl,
            customMode: true,
            instrumental: true,
            model: 'V4',
            style: this.selectedStyle,
            title: this.selectedFile.name.replace(/\.[^/.]+$/, ""),
            prompt: "",
            callBackUrl: '/api/music/callback',
            styleWeight: 0.9,
            negativeTags: 'piano, guitar, drums, electronic, synth, bass, western instruments, pop, rock, jazz, orchestra, violin, cello, trumpet, saxophone'
          };

          const convertResponse = await musicService.uploadCover(convertData);

          if (convertResponse.data && convertResponse.data.taskId) {
            // 3. 开始轮询任务状态
            const taskId = convertResponse.data.taskId;
            await this.checkConvertStatus(taskId, progressInterval);
          } else {
            throw new Error('转换任务创建失败');
          }
        } else {
          throw new Error('文件上传失败');
        }
      } catch (error) {
        console.error('转换失败:', error);
        this.isUploading = false;
        this.uploadProgress = 0;

        let errorMessage = '转换失败: ';
        if (error.response && error.response.data && error.response.data.message) {
          errorMessage += error.response.data.message;
        } else {
          errorMessage += error.message || '未知错误';
        }

        alert(errorMessage);
      }
    },

    async checkConvertStatus(taskId, progressInterval) {
      try {
        const statusResponse = await musicService.getStatus(taskId);
        const status = statusResponse.data.status;

        if (status === 'completed' || status === 'SUCCESS') {
          // 转换完成
          clearInterval(progressInterval);
          this.uploadProgress = 100;
          this.convertResult = statusResponse.data.audioData[0].audioUrl;
          this.isUploading = false;
        } else if (status === 'failed' || status === 'FAILED') {
          // 转换失败
          clearInterval(progressInterval);
          this.isUploading = false;
          this.uploadProgress = 0;
          alert('转换失败，请尝试使用更短的音频或不同的风格');
        } else {
          // 继续轮询
          setTimeout(() => this.checkConvertStatus(taskId, progressInterval), 3000);

          // 更新进度（模拟）
          if (this.uploadProgress < 95) {
            this.uploadProgress += 5;
          }
        }
      } catch (error) {
        console.error('状态检查失败:', error);
        clearInterval(progressInterval);
        this.isUploading = false;
        this.uploadProgress = 0;
      }
    },

    // ========== 歌词生成音乐方法 ==========
    async generateMusic() {
      // 验证必填字段
      if (!this.musicData.title || !this.musicData.prompt) {
        alert('请填写歌曲标题和歌词内容');
        return;
      }

      this.isGenerating = true;
      this.generateStatus = '提交中...';
      this.generatedAudio = null;

      try {
        const response = await musicService.generate(this.musicData);

        if (response.data && response.data.taskId) {
          this.generateStatus = '已提交，等待处理中...';
          const taskId = response.data.taskId;
          await this.checkGenerateStatus(taskId);
        } else {
          throw new Error('未收到任务ID');
        }
      } catch (error) {
        console.error('生成失败:', error);
        this.isGenerating = false;
        this.generateStatus = '';

        let errorMessage = '生成失败: ';
        if (error.response && error.response.data && error.response.data.message) {
          errorMessage += error.response.data.message;
        } else {
          errorMessage += error.message || '未知错误';
        }

        alert(errorMessage);
      }
    },

    async checkGenerateStatus(taskId) {
      try {
        const response = await musicService.getStatus(taskId);
        const status = response.data.status;

        if (status === 'completed' || status === 'SUCCESS') {
          this.generateStatus = '生成完成!';
          this.generatedAudio = response.data.audioData[0].audioUrl;
          this.isGenerating = false;
        } else if (status === 'failed' || status === 'FAILED') {
          this.generateStatus = '生成失败，请重试';
          this.isGenerating = false;
        } else {
          this.generateStatus = '处理中...';
          setTimeout(() => this.checkGenerateStatus(taskId), 3000);
        }
      } catch (error) {
        console.error('状态检查失败:', error);
        this.isGenerating = false;
        this.generateStatus = '状态检查失败';
      }
    }
  },

  mounted() {
    console.log('全屏音乐生成器已启动');
    // 确保视频播放
    const video = this.$refs.bgVideo;
    if (video) {
      video.play().catch(error => {
        console.log('视频自动播放被阻止:', error);
        // 添加用户交互后播放视频的逻辑
        const playVideo = () => {
          video.play();
          document.removeEventListener('click', playVideo);
        };
        document.addEventListener('click', playVideo);
      });
    }
  }
};
</script>

<style scoped>
/* ========== 视频背景样式 - 优化版本 ========== */
.video-background {
  position: fixed;
  top: 0;
  left: 0;
  width: 100vw;
  height: 100vh;
  z-index: -1;
  overflow: hidden;
}

.bg-video {
  position: absolute;
  top: 50%;
  left: 50%;
  transform: translate(-50%, -50%);
  min-width: 100%;
  min-height: 100%;
  width: auto;
  height: auto;
  object-fit: cover;
  /* 移除滤镜，保持原视频清晰度 */
  /* 提高视频质量设置 */
  image-rendering: -webkit-optimize-contrast;
  image-rendering: crisp-edges;
  /* 添加轻微锐化效果 */
  filter: brightness(1.02) contrast(1.02);
}

.video-overlay {
  position: absolute;
  top: 0;
  left: 0;
  width: 100%;
  height: 100%;
  /* 大幅减少叠加层不透明度，让背景更清晰 */
  background: linear-gradient(
    135deg, 
    rgba(26, 26, 46, 0.2) 0%, 
    rgba(22, 33, 62, 0.15) 50%, 
    rgba(15, 52, 96, 0.1) 100%
  );
  /* 大幅减少模糊效果 */
  backdrop-filter: blur(0.5px);
}

/* ========== 全局样式 ========== */
.fullscreen-music-generator {
  width: 100vw;
  height: 100vh;
  background: transparent;
  color: #fff;
  overflow: hidden;
  display: flex;
  flex-direction: column;
}

/* ========== 确保内容在视频之上 ========== */
.function-selector,
.fullscreen-content-area {
  position: relative;
  z-index: 1;
}

/* ========== 调整内容区域透明度 ========== */
.main-panel {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding: 30px;
  overflow-y: auto;
  /* 大幅降低面板背景不透明度，让背景更可见 */
  background: rgba(255, 255, 255, 0.05);
  backdrop-filter: blur(8px);
  border-radius: 0;
}

.result-panel {
  width: 400px;
  display: flex;
  flex-direction: column;
  padding: 30px;
  /* 大幅降低面板背景不透明度，让背景更可见 */
  background: rgba(255, 255, 255, 0.08);
  border-left: 1px solid rgba(255, 255, 255, 0.1);
  overflow-y: auto;
  backdrop-filter: blur(8px);
}

/* ========== 功能选择器样式 ========== */
.function-selector {
  display: flex;
  background: rgba(255, 255, 255, 0.08);
  padding: 0;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
  backdrop-filter: blur(10px);
}

.function-selector button {
  flex: 1;
  padding: 18px 24px;
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.8);
  cursor: pointer;
  transition: all 0.3s ease;
  font-size: 16px;
  font-weight: 600;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  position: relative;
  overflow: hidden;
}

.function-selector button::before {
  content: '';
  position: absolute;
  bottom: 0;
  left: 50%;
  width: 0;
  height: 3px;
  background: linear-gradient(45deg, #ff9a9e, #fad0c4);
  transition: all 0.3s ease;
  transform: translateX(-50%);
}

.function-selector button.active {
  color: #ffffff;
  background: rgba(255, 255, 255, 0.1);
}

.function-selector button.active::before {
  width: 100%;
}

.function-selector button:hover {
  background: rgba(255, 255, 255, 0.05);
  color: #fff;
}

/* ========== 全屏内容区域 ========== */
.fullscreen-content-area {
  flex: 1;
  padding: 0;
  overflow: hidden;
}

.function-content {
  width: 100%;
  height: 100%;
  padding: 0;
}

.fullscreen-layout {
  display: flex;
  height: 100%;
  width: 100%;
  gap: 0;
}

/* ========== 面板头部样式 ========== */
.panel-header {
  text-align: center;
  margin-bottom: 30px;
  padding-bottom: 20px;
  border-bottom: 1px solid rgba(255, 255, 255, 0.1);
}

.center-title {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 12px;
  margin: 0 0 10px 0;
  color: #f0c81887;
  font-size: 28px;
  font-weight: 700;
  text-align: center;
  width: 100%;
}

.center-description {
  text-align: center;
  margin: 0;
  color: rgba(247, 227, 169, 0.8);
  font-size: 16px;
  line-height: 1.5;
}

/* ========== 上传区域样式 ========== */
.upload-section {
  margin-bottom: 30px;
}

.upload-area {
  border: 2px dashed rgba(255, 255, 255, 0.3);
  border-radius: 16px;
  padding: 60px 20px;
  text-align: center;
  background: rgba(255, 255, 255, 0.03);
  cursor: pointer;
  transition: all 0.3s ease;
}

.upload-area:hover {
  border-color: rgba(255, 255, 255, 0.6);
  background: rgba(255, 255, 255, 0.08);
}

.upload-icon {
  font-size: 64px;
  color: #f5ca0d;
  margin-bottom: 20px;
  opacity: 0.8;
}

.upload-text .main-text {
  font-size: 20px;
  font-weight: 600;
  margin: 0 0 10px 0;
  color: #fff;
}

.upload-text .sub-text {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.7);
  margin: 0;
}

/* ========== 文件信息显示样式 ========== */
.file-info-section {
  margin-top: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  padding: 20px;
}

.file-header {
  display: flex;
  align-items: center;
  margin-bottom: 15px;
  gap: 12px;
}

.file-name {
  flex: 1;
  font-weight: 500;
  color: #fff;
  overflow: hidden;
  text-overflow: ellipsis;
  white-space: nowrap;
  font-size: 16px;
}

.file-size {
  color: rgba(255, 255, 255, 0.6);
  font-size: 14px;
  white-space: nowrap;
}

.progress-container {
  display: flex;
  align-items: center;
  gap: 15px;
}

.progress-bar {
  flex: 1;
  height: 10px;
  background: rgba(255, 255, 255, 0.1);
  border-radius: 5px;
  overflow: hidden;
}

.progress {
  height: 100%;
  background: linear-gradient(45deg, #ff9a9e, #fad0c4);
  transition: width 0.3s ease;
  border-radius: 5px;
}

.progress-text {
  font-size: 16px;
  color: rgba(255, 255, 255, 0.8);
  min-width: 50px;
  text-align: right;
}

/* ========== 设置区域样式 ========== */
.settings-section {
  margin-top: 30px;
}

.settings-title {
  margin: 0 0 20px 0;
  color: #ff9a9e;
  font-size: 22px;
  font-weight: 600;
  display: flex;
  align-items: center;
  gap: 10px;
}

.setting-group {
  margin-bottom: 25px;
}

.setting-label {
  font-weight: 600;
  color: rgba(255, 255, 255, 0.95);
  font-size: 16px;
  margin-bottom: 10px;
  display: block;
}

/* ========== 表单元素样式 ========== */
.form-section {
  margin-bottom: 30px;
}

.form-grid {
  display: grid;
  grid-template-columns: 1fr 1fr;
  gap: 20px;
  margin-bottom: 25px;
}

.form-group {
  margin-bottom: 25px;
}

.form-group.full-width {
  grid-column: 1 / -1;
}

.form-label {
  color: rgba(255, 255, 255, 0.95);
  font-weight: 600;
  margin-bottom: 10px;
  display: block;
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 8px;
}

.form-input,
.form-textarea,
.form-select {
  width: 100%;
  padding: 16px 20px;
  border-radius: 12px;
  background: rgba(255, 255, 255, 0.05);
  border: 2px solid rgba(255, 255, 255, 0.2);
  color: #ffffff;
  font-size: 16px;
  outline: none;
  transition: all 0.3s ease;
  box-sizing: border-box;
}

.form-textarea {
  resize: vertical;
  min-height: 180px;
  line-height: 1.6;
  font-family: inherit;
}

.form-input::placeholder,
.form-textarea::placeholder {
  color: rgba(255, 255, 255, 0.5);
}

.form-input:focus,
.form-textarea:focus,
.form-select:focus {
  border-color: #ff9a9e;
  background: rgba(255, 255, 255, 0.08);
  box-shadow: 0 0 0 3px rgba(255, 154, 158, 0.2);
}

.form-select {
  appearance: none;
  cursor: pointer;
  background-image: url("data:image/svg+xml,%3Csvg xmlns='http://www.w3.org/2000/svg' width='16' height='16' viewBox='0 0 24 24' fill='none' stroke='%23ffffff' stroke-width='3' stroke-linecap='round' stroke-linejoin='round'%3E%3Cpath d='M6 9l6 6 6-6'/%3E%3C/svg%3E");
  background-repeat: no-repeat;
  background-position: right 20px center;
  background-size: 16px;
  padding-right: 50px;
}

.form-select option {
  background: rgba(30, 30, 40, 0.95);
  color: #ffffff;
  padding: 10px;
}

/* ========== 按钮样式 ========== */
.action-section {
  margin-top: 30px;
}

.action-btn {
  width: 100%;
  padding: 18px 24px;
  border: none;
  border-radius: 12px;
  cursor: pointer;
  font-weight: 600;
  font-size: 18px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
}

.primary-btn {
  background: linear-gradient(135deg, #ff9a9e, #fad0c4);
  color: #333;
}

.primary-btn:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(255, 154, 158, 0.4);
}

.generate-btn {
  background: linear-gradient(135deg, #a1c4fd, #c2e9fb);
  color: #333;
}

.generate-btn:hover:not(:disabled) {
  transform: translateY(-3px);
  box-shadow: 0 8px 20px rgba(161, 196, 253, 0.4);
}

.action-btn:disabled {
  opacity: 0.5;
  cursor: not-allowed;
  transform: none;
}

.clear-btn {
  background: transparent;
  border: none;
  color: rgba(255, 255, 255, 0.7);
  cursor: pointer;
  padding: 6px;
  border-radius: 6px;
  transition: all 0.3s ease;
  font-size: 16px;
}

.clear-btn:hover {
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
}

/* ========== 提示文本样式 ========== */
.hint-text {
  margin-top: 20px;
  padding: 15px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 8px;
  color: rgba(255, 255, 255, 0.7);
  font-size: 16px;
  display: flex;
  align-items: center;
  gap: 10px;
}

/* ========== 结果区域样式 ========== */
.result-section {
  background: rgba(255, 255, 255, 0.08);
  border-radius: 16px;
  padding: 25px;
  border-left: 4px solid #4CAF50;
}

.result-header {
  display: flex;
  align-items: center;
  gap: 15px;
  margin-bottom: 20px;
}

.success-icon {
  color: #4CAF50;
  font-size: 24px;
}

.result-header h3 {
  margin: 0;
  color: #4CAF50;
  font-size: 22px;
}

.audio-preview {
  display: flex;
  flex-direction: column;
  gap: 15px;
}

.audio-player {
  width: 100%;
  border-radius: 8px;
}

.audio-actions {
  display: flex;
  gap: 10px;
}

.download-btn {
  flex: 1;
  padding: 12px 18px;
  border-radius: 8px;
  text-decoration: none;
  text-align: center;
  font-size: 16px;
  transition: all 0.3s ease;
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 8px;
  background: rgba(255, 255, 255, 0.1);
  color: #fff;
  border: 1px solid rgba(255, 255, 255, 0.2);
}

.download-btn:hover {
  background: rgba(255, 255, 255, 0.2);
}

/* ========== 空状态样式 ========== */
.empty-state {
  text-align: center;
  padding: 40px 20px;
  color: rgba(255, 255, 255, 0.6);
  display: flex;
  flex-direction: column;
  height: 100%;
  justify-content: center;
  align-items: center;
}

.empty-image {
  margin-bottom: 25px;
}

.music-icon {
  font-size: 80px;
  color: rgba(255, 255, 255, 0.3);
  margin-bottom: 20px;
}

.empty-state h3 {
  margin: 0 0 10px 0;
  color: rgba(255, 255, 255, 0.9);
  font-size: 24px;
  font-weight: 600;
}

.empty-state p {
  margin: 0 0 30px 0;
  font-size: 16px;
  line-height: 1.5;
}

/* ========== 功能提示样式 ========== */
.feature-tips {
  width: 100%;
  max-width: 300px;
}

.tip-item {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 12px 0;
  color: rgba(255, 255, 255, 0.7);
  font-size: 15px;
  transition: all 0.3s ease;
  border-bottom: 1px solid rgba(255, 255, 255, 0.05);
}

.tip-item:last-child {
  border-bottom: none;
}

.tip-item:hover {
  color: rgba(255, 255, 255, 0.9);
  transform: translateX(5px);
}

.tip-item i {
  color: #ff9a9e;
  width: 18px;
  text-align: center;
  font-size: 16px;
}

/* ========== 状态指示器样式 ========== */
.status-indicator {
  display: flex;
  align-items: center;
  gap: 15px;
  padding: 20px;
  background: rgba(255, 255, 255, 0.05);
  border-radius: 12px;
  border-left: 4px solid #ff9a9e;
  margin-top: 20px;
}

.status-icon {
  font-size: 24px;
  color: #ff9a9e;
}

.status-content h5 {
  margin: 0 0 5px 0;
  color: #ff9a9e;
  font-size: 16px;
}

.status-content p {
  margin: 0;
  font-size: 15px;
  color: rgba(255, 255, 255, 0.8);
}

/* ========== 响应式设计 ========== */
@media (max-width: 1024px) {
  .fullscreen-layout {
    flex-direction: column;
  }

  .result-panel {
    width: 100%;
    height: 40%;
    border-left: none;
    border-top: 1px solid rgba(255, 255, 255, 0.1);
  }

  .form-grid {
    grid-template-columns: 1fr;
  }
}

@media (max-width: 768px) {
  .main-panel,
  .result-panel {
    padding: 20px;
  }

  .panel-title {
    font-size: 24px;
  }

  .upload-area {
    padding: 40px 20px;
  }

  .upload-icon {
    font-size: 48px;
  }

  .form-input,
  .form-textarea,
  .form-select {
    padding: 14px 16px;
    font-size: 14px;
  }

  .action-btn {
    padding: 16px 20px;
    font-size: 16px;
  }
}
</style>