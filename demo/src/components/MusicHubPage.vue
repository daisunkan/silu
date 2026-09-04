<template>
  <div class="page page-4">
    <div class="page-content">
      <div class="section-header">
        <div>
          <h1 class="section-title">时空枢纽 - 丝路的旋律</h1>
          <p class="section-subtitle">让古今音符在数字时空中共鸣</p>
        </div>
      </div>

      <div class="story-content">
        <p>欢迎来到时空枢纽，这里是连接所有时代的音乐圣殿。数智人"丝路"在这里进行概念的升华："交流从未停止。"</p>
        <p>古代的商队带来了异域的音符，今天的我们，同样可以将这个时代的旋律，注入古老的乐器之中。这就是数字时代的文化交融。</p>
        <p>上传现代音频被解释为"当代声音的丝路化"，是文化交融在数字时代的体现。输入楚辞生成音乐则是"让古老的诗词，搭乘AI的翅膀，重新飞回当代人的耳中"。</p>
      </div>

      <div class="content-section">
        <!-- 修改这部分：将标题和按钮放在同一行 -->
        <div class="section-header-with-button">
          <h3 class="section-title">音乐交流融合体验</h3>
          
          <!-- 融合音乐播放器移到标题旁边 -->
          <div class="fusion-player">
            <button 
              :class="{ 'playing': isFusionPlaying }"
              class="fusion-play-btn"
              @click="toggleFusionMusic"
            >
              <span class="btn-icon">{{ isFusionPlaying ? '❚❚' : '▶' }}</span>
              <span class="btn-text">播放融合音乐</span>
              <div 
                  v-if="isFusionPlaying" 
                  class="fusion-wave" 
                  >
                <span></span><span></span><span></span><span></span><span></span>
              </div>
            </button>
          </div>
        </div>

        <!-- 乐器卡片容器 -->
        <div class="instrument-cards">
          <!-- 原有的乐器卡片保持不变 -->
          <SpecialCard 
            class="instrument-card" imagePath="guzheng.png"
            :class="{ 'active': currentInstrument === 'guzheng' }" @card-click="playInstrument('guzheng')">
            <template #button-content>
              <div class="instrument-info">
                <h4>古筝</h4>
                <p>东方弹拨乐器的代表</p>
              </div>
            </template>

            <template #expanded-content>
              <div class="expanded-info">
                <h4>古筝</h4>
                <p>中国传统弹拨乐器，已有两千五百多年历史。音色柔美典雅，表现力极强，既能奏出悠扬婉转的旋律，也能演绎气势恢宏的乐章，曾随丝绸之路传递中华乐韵。</p>
                <div v-if="currentInstrument === 'guzheng'" class="music-indicator">
                  <div class="sound-wave">
                    <span></span><span></span><span></span><span></span>
                  </div>
                  <span>播放中...</span>
                </div>
              </div>
            </template>
          </SpecialCard>

          <SpecialCard 
            class="instrument-card" 
            :class="{ 'active': currentInstrument === 'dizi' }"
            image-path="dizi.png" @card-click="playInstrument('dizi')">
            <template #button-content>
              <div class="instrument-info">
                <h4>笛子</h4>
                <p>中国古老吹奏乐器</p>
              </div>
            </template>

            <template #expanded-content>
              <div class="expanded-info">
                <h4>笛子</h4>
                <p>中国传统吹管乐器，音色清脆悦耳，富有穿透力。常用于独奏、合奏，能够表达细腻的情感和悠远的意境。</p>
                <div v-if="currentInstrument === 'dizi'" class="music-indicator">
                  <div class="sound-wave">
                    <span></span><span></span><span></span><span></span>
                  </div>
                  <span>播放中...</span>
                </div>
              </div>
            </template>
          </SpecialCard>

          <SpecialCard 
            class="instrument-card"
            :class="{ 'active': currentInstrument === 'violin' }"
            image-path="violin.png"
            @card-click="playInstrument('violin')">
            <template #button-content>
              <div class="instrument-info">
                <h4>提琴</h4>
                <p>西方弦乐代表</p>
              </div>
            </template>

            <template #expanded-content>
              <div class="expanded-info">
                <h4>提琴</h4>
                <p>西方古典音乐中最重要的弦乐器之一，音域宽广，音色优美，表现力极强，被誉为"乐器皇后"。</p>
                <div v-if="currentInstrument === 'violin'" class="music-indicator">
                  <div class="sound-wave">
                    <span></span><span></span><span></span><span></span>
                  </div>
                  <span>播放中...</span>
                </div>
              </div>
            </template>
          </SpecialCard>

          <SpecialCard 
            class="instrument-card"
            :class="{ 'active': currentInstrument === 'drum' }"
            image-path="drum.png"
            @card-click="playInstrument('drum')">
            <template #button-content>
              <div class="instrument-info">
                <h4>西域鼓组</h4>
                <p>节奏的脉搏</p>
              </div>
            </template>

            <template #expanded-content>
              <div class="expanded-info">
                <h4>西域鼓组</h4>
                <p>源自丝绸之路的打击乐器组合，节奏鲜明有力，充满异域风情，是连接东西方音乐文化的重要桥梁。</p>
                <div v-if="currentInstrument === 'drum'" class="music-indicator">
                  <div class="sound-wave">
                    <span></span><span></span><span></span><span></span>
                  </div>
                  <span>播放中...</span>
                </div>
              </div>
            </template>
          </SpecialCard>
        </div>
      </div>
    </div>
    <div class="page-number">4</div>
  </div>
</template>

<script setup>
import SpecialCard from './SpecialCard.vue'
import { ref, onUnmounted } from 'vue'

const currentInstrument = ref(null)
const isFusionPlaying = ref(false)

// 使用 public 目录的音频文件
const audioInstances = {
  guzheng: new Audio('/audio/guzheng.m4a'),
  dizi: new Audio('/audio/dizi.m4a'),
  violin: new Audio('/audio/violin.m4a'),
  drum: new Audio('/audio/drum.m4a'),
  fusion: new Audio('/audio/fusion.m4a')
}

// 初始化音频
Object.entries(audioInstances).forEach(([name, audio]) => {
  audio.loop = true
  audio.volume = 0.7
  audio.preload = 'auto'
  
  audio.addEventListener('error', (e) => {
    console.error(`音频加载失败: ${name}`, e)
  })
})

const playInstrument = async (instrument) => {
  if (currentInstrument.value === instrument) {
    audioInstances[instrument].pause()
    audioInstances[instrument].currentTime = 0
    currentInstrument.value = null
    return
  }

  stopAllAudio()

  try {
    await audioInstances[instrument].play()
    currentInstrument.value = instrument
  } catch (error) {
    console.error('播放失败:', error)
  }
}

const toggleFusionMusic = async () => {
  if (isFusionPlaying.value) {
    audioInstances.fusion.pause()
    audioInstances.fusion.currentTime = 0
  } else {
    stopAllAudio()
    try {
      await audioInstances.fusion.play()
    } catch (error) {
      console.error('融合音乐播放失败:', error)
    }
  }
  isFusionPlaying.value = !isFusionPlaying.value
}

const stopAllAudio = () => {
  Object.values(audioInstances).forEach(audio => {
    audio.pause()
    audio.currentTime = 0
  })
  currentInstrument.value = null
}

onUnmounted(() => {
  stopAllAudio()
})
</script>

<style scoped>
.page.page-4 {
  background-image: url('@/assets/images/music-bg.png');
  background-size: cover;
  background-position: center;
  background-repeat: no-repeat;
  /* 半透明黑色遮罩（可调整 alpha 值控制暗度，范围 0~1） */
  background-color: rgba(0, 0, 0, 0.5);
  /* 混合模式：让背景图与黑色「正片叠底」，自然暗化 */
  background-blend-mode: multiply;
  min-height: 100vh;
  position: relative;
  display: flex;
  flex-direction: column;
}

.page-content {
  flex: 1;
  display: flex;
  flex-direction: column;
  padding-bottom: 30px; /* 为页码留出空间 */
}
/* 新增：标题和按钮并排布局的样式 */
.section-header-with-button {
  display: flex;
  justify-content: space-between;
  align-items: center;
  margin-bottom: 30px;
  flex-wrap: wrap; /* 在小屏幕上换行 */
  gap: 20px;
}

.section-header-with-button .section-title {
  margin: 0; /* 移除默认的 margin */
  color: #ffd166;
}

.instrument-cards {
  display: grid;
  grid-template-columns: repeat(auto-fit, minmax(20px, 1fr)); /* 改为自适应列数 */
  gap: 25px;
  margin: 30px 0;
  width: 100%; /* 确保占满父容器 */
}

.instrument-card {
  width: 100%; /* 卡片宽度自适应 */
  transition: all 0.3s ease;
}

.instrument-card.active {
  transform: scale(1.05);
  box-shadow: 0 0 20px rgba(255, 209, 102, 0.5);
}

.instrument-info {
  text-align: center;
  color: white;
  padding: 10px;
}

.instrument-info h4 {
  color: #ffd166;
  margin-bottom: 8px;
  font-size: 1.3rem;
}

.instrument-info p {
  color: #ebf0f8;
  font-size: 0.9rem;
  opacity: 0.8;
}

.expanded-info {
  color: #0d1b2a;
  text-align: center;
}

.expanded-info h4 {
  color: #D4AF37;
  margin-bottom: 10px;
  font-size: 1.3rem;
}

.expanded-info p {
  line-height: 1.5;
  margin-bottom: 15px;
  font-size: 1rem;
}

.music-indicator {
  display: flex;
  align-items: center;
  justify-content: center;
  gap: 10px;
  margin-top: 10px;
  padding: 8px 12px;
  background: rgba(214, 197, 6, 0.1);
  border-radius: 20px;
  font-size: 0.8rem;
  color: #D4AF37;
}

.sound-wave {
  display: flex;
  align-items: center;
  gap: 3px;
  height: 20px;
}

.sound-wave span {
  display: inline-block;
  width: 3px;
  background: #D4AF37;
  border-radius: 2px;
  animation: soundWave 1.2s infinite ease-in-out;
}

.sound-wave span:nth-child(1) {
  animation-delay: 0s;
  height: 8px;
}

.sound-wave span:nth-child(2) {
  animation-delay: 0.2s;
  height: 12px;
}

.sound-wave span:nth-child(3) {
  animation-delay: 0.4s;
  height: 16px;
}

.sound-wave span:nth-child(4) {
  animation-delay: 0.6s;
  height: 12px;
}

@keyframes soundWave {
  0%, 100% {
    transform: scaleY(1);
  }
  50% {
    transform: scaleY(0.3);
  }
}

.fusion-player {
  display: flex;
  margin-top: 0; /* 移除上边距，因为现在和标题在同一行 */
}

.fusion-play-btn {
  display: flex;
  align-items: center;
  gap: 12px;
  padding: 15px 30px;
  background: linear-gradient(140deg, #D4AF37, #F5CA0D);
  color: white;
  border: none;
  border-radius: 50px;
  font-size: 1.1rem;
  font-weight: bold;
  cursor: pointer;
  transition: all 0.3s ease;
  position: relative;
  overflow: hidden;
  z-index: 10;
}

.fusion-play-btn:hover {
  transform: translateY(-3px);
  box-shadow: 0 10px 25px rgba(214, 193, 6, 0.4);
}

.fusion-play-btn.playing {
  background: linear-gradient(140deg, #D4AF37, #F5CA0D);
}

.btn-icon {
  font-size: 1.2rem;
}

.fusion-wave {
  display: flex;
  align-items: center;
  gap: 2px;
  margin-left: 5px;
}

.fusion-wave span {
  display: inline-block;
  width: 2px;
  background: white;
  border-radius: 1px;
  animation: fusionWave 1.5s infinite ease-in-out;
}

.fusion-wave span:nth-child(1) {
  animation-delay: 0s;
  height: 6px;
}

.fusion-wave span:nth-child(2) {
  animation-delay: 0.1s;
  height: 10px;
}

.fusion-wave span:nth-child(3) {
  animation-delay: 0.2s;
  height: 14px;
}

.fusion-wave span:nth-child(4) {
  animation-delay: 0.3s;
  height: 10px;
}

.fusion-wave span:nth-child(5) {
  animation-delay: 0.4s;
  height: 6px;
}

@keyframes fusionWave {
  0%, 100% {
    transform: scaleY(1);
  }
  50% {
    transform: scaleY(0.3);
  }
}

/* 响应式设计优化 */
@media (max-width: 1200px) {
  .instrument-cards {
    grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
    gap: 20px;
  }
}

@media (max-width: 768px) {
  .instrument-cards {
    grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
    gap: 15px;
  }
}

@media (max-width: 480px) {
  .instrument-cards {
    grid-template-columns: 1fr; /* 小屏幕上单列显示 */
    gap: 15px;
  }
}
</style>