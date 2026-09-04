<template>
  <div id="app">
    <!-- 启动界面 -->
    <div class="start-container" v-if="!started">
      <h1 class="logo-title">凿空问对</h1>
      <h2 class="logo-subtitle">丝路藏经洞：与AI数字人对话千年丝路</h2>
      <div class="button-group">
        <button @click="startAvatar('demo1')">形象1-苏 轼</button>
        <button @click="startAvatar('demo2')">形象2-风 屿</button>
      </div>
      <!-- 视频背景组件 - 始终显示在最底层 -->
      <VideoBackground ref="videoBackground" />
    </div>

    <!-- 主内容区域 -->
    <div class="main-container" v-if="started">
      <!-- Avatar组件区域 - 占2/3宽度 -->
      <div class="avatar-section">
        <!-- BigCard组件容器 -->
        <div class="big-cards-container">
          <transition name="slide">
            <BigCard1 v-show="currentCard === 1" />
          </transition>
          <transition name="slide">
            <BigCard2 v-show="currentCard === 2" />
          </transition>
          <transition name="slide">
            <BigCard3 v-show="currentCard === 3" />
          </transition>
          <transition name="slide">
            <BigCard4 v-show="currentCard === 4" />
          </transition>
          <transition name="slide">
            <BigCard5 v-show="currentCard === 5" />
          </transition>
          <transition name="slide">
            <BigCard6 v-show="currentCard === 6" />
          </transition>
        </div>
        <!-- 左侧导航栏 -->
        <div class="side-nav" :class="{ 'show': showSideNav }" @mouseenter="showSideNav = true"
          @mouseleave="showSideNav = false">
          <div class="nav-item" @click="switchCard(1)" :class="{ active: currentCard === 1 }">开辟</div>
          <div class="nav-item" @click="switchCard(2)" :class="{ active: currentCard === 2 }">鼎盛</div>
          <div class="nav-item" @click="switchCard(3)" :class="{ active: currentCard === 3 }">转折</div>
          <div class="nav-item" @click="switchCard(4)" :class="{ active: currentCard === 4 }">沉淀</div>
          <div class="nav-item" @click="switchCard(5)" :class="{ active: currentCard === 5 }">新生</div>
          <div class="nav-item" @click="switchCard(6)" :class="{ active: currentCard === 6 }">文物</div>
        </div>
        <component :is="currentAvatar" ref="avatar" :key="avatarKey" @switch-avatar="handleSwitchAvatar"></component>
      </div>

      <!-- 右侧聊天面板 - 占1/3宽度 -->
      <div class="right-panel">
        <div class="chat-header">
          <h3>博文雅叙</h3>
          <div class="chat-actions">
            <!--
            <button @click="clearConversation" class="action-btn">清除对话</button>
            -->
            <button @click="newConversation" class="action-btn">新对话</button>
          </div>
        </div>

        <div class="messages-container">
          <div v-for="(message, index) in messages" :key="index"
            :class="['message', message.isUser ? 'user-message' : 'ai-message']">
            {{ message.text }}
          </div>
        </div>

        <div class="input-area">
          <input v-model="inputText" @keyup.enter="sendMessage" class="chat-input" placeholder="输入消息或点击麦克风说话...">
          <button class="voice-btn" :class="{ recording: isRecording }" @click="toggleRecording" type="button">
            {{ isRecording ? '●' : '🎤' }}
          </button>
          <button class="send-btn" @click="sendMessage">发送</button>
        </div>
      </div>
    </div>
    <!-- 3D模型查看器 -->
    <ModelViewer :show-modal="showModelModal" :model-path="currentModel" @close="hideModel" />
  </div>
</template>

<script>
import { EventBus } from './event-bus.js'
import avatarDemo1 from './views/avatardemo.vue'
import avatarDemo2 from './views/avatardemo2.vue'
import BigCard1 from './components/BigCard1.vue'
import BigCard2 from './components/BigCard2.vue'
import BigCard3 from './components/BigCard3.vue'
import BigCard4 from './components/BigCard4.vue'
import BigCard5 from './components/BigCard5.vue'
import BigCard6 from './components/BigCard6.vue'
import VideoBackground from './components/VideoBackground.vue'
import ModelViewer from './components/ModelViewer.vue'  // 添加这行导入

export default {
  name: 'App',
  components: {
    avatarDemo1,
    avatarDemo2,
    BigCard1,
    BigCard2,
    BigCard3,
    BigCard4,
    BigCard5,
    BigCard6,
    VideoBackground,
    ModelViewer  // 添加这行
  },
  data() {
    return {
      currentCard: 1,
      started: false,
      currentAvatar: 'avatarDemo1',
      avatarKey: 0,
      messages: [],
      inputText: '',
      sessionId: Date.now().toString(), // 生成唯一sessionId
      showSideNav: false,
      navItems: ['开辟', '鼎盛', '转折', '沉淀', '新生', '文物'],
      showModelModal: false,  // 添加这个
      currentModel: '',        // 添加这个

      isRecording: false,
      recognition: null,
      browserSupported: true,
      interimTranscript: '', // 实时识别结果

    }
  },
  methods: {
    switchCard(cardNumber) {
      this.currentCard = cardNumber
    },
    handleSwitchAvatar(targetAvatar) {
      this.startAvatar(targetAvatar);
    },
    async startAvatar(avatarType) {
      // 如果已经启动，先停止当前avatar
      if (this.started && this.$refs.avatar) {
        await this.$refs.avatar.stop();
      }

      this.currentAvatar = avatarType === 'demo1' ? 'avatarDemo1' : 'avatarDemo2';
      this.avatarKey++; // 强制重新创建组件实例
      this.started = true;

      // 确保组件已加载
      this.$nextTick(() => {
        const avatar = this.$refs.avatar;

        // 按顺序执行初始化流程
        try {
          avatar.initSDK();
          avatar.createRecoder();
          avatar.setSDKEvenet();
          avatar.setPlayerEvenet();
          avatar.SetApiInfo2();
          avatar.SetGlobalParams();
          avatar.start();
        } catch (error) {
          console.error('初始化失败:', error);
          this.started = false;
        }
      });
    },

    stopAll() {
      if (this.$refs.avatar) {
        this.$refs.avatar.stop();
      }
      this.started = false;
    },

    quickAction(action) {
      switch (action) {
        case 'pause':
          if (this.$refs.avatar && this.$refs.avatar.interrupt) {
            this.$refs.avatar.interrupt();
          }
          break;
        case 'reset':
          // 重置逻辑
          break;
        case 'settings':
          // 打开设置
          break;
      }
    },



    async clearConversation() {
      try {
        // 打断当前朗诵
        if (this.$refs.avatar && this.$refs.avatar.interrupt) {
          this.$refs.avatar.interrupt();
        }

        // 调用后端清除接口
        await fetch('http://localhost:8085/api/clear', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          },
          body: JSON.stringify({
            sessionId: this.sessionId
          }),
          credentials: 'same-origin'
        });

        // 清除前端消息
        this.messages = [];

      } catch (error) {
        console.error('清除对话出错:', error);
      }
    },

    newConversation() {
      try {
        // 打断当前朗诵
        if (this.$refs.avatar && this.$refs.avatar.interrupt) {
          this.$refs.avatar.interrupt();
        }

        // 生成新的sessionId
        this.sessionId = Date.now().toString();
        // 清除消息
        this.messages = [];
      } catch (error) {
        console.error('新建对话出错:', error);
      }
    },

    async sendMessage() {
      if (!this.inputText.trim()) return;

      // 添加用户消息
      const userMessage = {
        text: this.inputText,
        isUser: true,
        timestamp: new Date()
      };
      this.messages.push(userMessage);

      // 清空输入框
      this.inputText = '';

      try {
        // 调用后端API
        const response = await fetch('http://localhost:8085/api/chat', {
          method: 'POST',
          headers: {
            'Content-Type': 'application/json',
            'Accept': 'application/json'
          },
          body: JSON.stringify({
            message: userMessage.text,
            sessionId: this.sessionId
          }),
          credentials: 'same-origin'
        });

        if (!response.ok) {
          throw new Error('API请求失败');
        }

        const data = await response.json();

        // 添加AI回复
        const aiMessage = {
          text: data.response,
          isUser: false,
          timestamp: new Date()
        };
        this.messages.push(aiMessage);

        // 调用数字人朗读（仅在模型有输出时）
        if (aiMessage.text && aiMessage.text.trim()) {
          try {
            // 确保avatar组件已加载
            if (!this.$refs.avatar) {
              throw new Error('数字人组件未加载');
            }

            // 设置textarea内容
            this.$refs.avatar.textarea = aiMessage.text;

            // 等待Vue更新DOM
            await this.$nextTick();

            // 调用文本驱动
            if (this.$refs.avatar.writeText) {
              await this.$refs.avatar.writeText();
            }

            // 清空textarea等待下一轮
            this.$refs.avatar.textarea = '';

          } catch (error) {
            console.error('调用朗读功能出错:', error);
          }
        }

      } catch (error) {
        console.error('调用API出错:', error);
        this.messages.push({
          text: '抱歉，获取回复时出错: ' + error.message,
          isUser: false,
          timestamp: new Date()
        });
      }

      // 滚动到底部
      this.$nextTick(() => {
        const container = this.$el.querySelector('.messages-container');
        container.scrollTop = container.scrollHeight;
      });
    },

    // 添加3D模型相关方法
    showModel(modelName) {
      this.currentModel = modelName;
      this.showModelModal = true;
    },

    hideModel() {
      this.showModelModal = false;
      this.currentModel = '';
    },

    // 添加填充聊天输入框的方法
    fillChatInput(text) {
      this.inputText = text
      // 可选：自动聚焦到输入框
      this.$nextTick(() => {
        const input = this.$el.querySelector('.chat-input')
        if (input) {
          input.focus()
        }
      })
    },

    initSpeechRecognition() {
      const SpeechRecognition = window.SpeechRecognition || window.webkitSpeechRecognition;

      if (!SpeechRecognition) {
        console.error('浏览器不支持语音识别');
        this.browserSupported = false;
        return;
      }

      this.recognition = new SpeechRecognition();
      this.recognition.continuous = true;
      this.recognition.interimResults = true;
      this.recognition.lang = 'zh-CN';

      this.recognition.onstart = () => {
        this.isRecording = true;
      };

      this.recognition.onresult = (event) => {
        let finalTranscript = '';
        let interimTranscript = '';

        for (let i = event.resultIndex; i < event.results.length; i++) {
          const transcript = event.results[i][0].transcript;
          if (event.results[i].isFinal) {
            finalTranscript += transcript;
          } else {
            interimTranscript += transcript;
          }
        }

        // 实时更新输入框内容
        if (finalTranscript) {
          this.inputText = finalTranscript;
        } else if (interimTranscript) {
          this.inputText = interimTranscript;
        }
      };

      this.recognition.onend = () => {
        this.isRecording = false;
      };

      this.recognition.onerror = (event) => {
        this.isRecording = false;
        console.error('语音识别错误:', event.error);
      };
    },

    toggleRecording() {
      if (!this.browserSupported) {
        alert('您的浏览器不支持语音识别功能');
        return;
      }

      if (this.isRecording) {
        this.recognition.stop();
      } else {
        if (!this.recognition) {
          this.initSpeechRecognition();
        }
        this.recognition.start();
      }
    },

  },
  mounted() {
    this.initSpeechRecognition();
    // 其他现有的初始化代码（如果有的话）
  },
  created() {
    // 监听填充聊天输入框的事件
    EventBus.$on('fill-chat-input', this.fillChatInput)
    // 监听显示3D模型的事件
    EventBus.$on('show-3d-model', this.showModel)
  },
  beforeDestroy() {
    // 移除事件监听
    EventBus.$off('fill-chat-input', this.fillChatInput)
    EventBus.$off('show-3d-model', this.showModel)
  }
}

</script>

<style>
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  color: #2c3e50;
  height: 100vh;
  width: 100vw;
  overflow: hidden;
}

/* 启动界面样式保持不变 */
.start-container {
  display: flex;
  flex-direction: column;
  justify-content: center;
  align-items: center;
  height: 100%;
  margin-top: -100px;
  text-align: center;
}

.logo-title {
  font-size: 3.5rem;
  margin-bottom: 1rem;
  color: #001a58;
}

.logo-subtitle {
  font-size: 1.2rem;
  color: #0062ad;
  margin-bottom: 2rem;
  font-weight: normal;
}

.button-group {
  display: flex;
  gap: 20px;
}

button {
  position: relative;
  padding: 10px 40px;
  margin: 0px 10px 10px 0px;
  float: left;
  border-radius: 3px;
  font-size: 20px;
  color: #FFF;
  text-decoration: none;
  background-color: #2ecc97;
  border: none;
  border-bottom: 5px solid #27ae95;
  text-shadow: 0px -2px #27ae78;
  -webkit-transition: all 0.1s;
  transition: all 0.1s;
}

button:hover,
button:active {
  -webkit-transform: translate(0px, 5px);
  -ms-transform: translate(0px, 5px);
  transform: translate(0px, 5px);
  border-bottom: 1px solid #2ecc71;
}

button:first-child {
  background-color: #409EFF;
}

button:last-child {
  background-color: #67C23A;
}

button:last-child:hover {
  background-color: #85ce61;
}

/* 主容器样式 */
.main-container {
  display: flex;
  width: 99%;
  height: 98vh;
  background: #f5f5f5;
}

/* Avatar区域 - 2/3宽度 */
.avatar-section {
  flex: 2;
  /* 占据2/3 */
  height: 100%;
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  position: relative;
  background: linear-gradient(220.55deg, #FFF6EB 0%, #DFD1C5 100%);
}

.side-nav {
  position: absolute;
  left: -150px;
  /* 初始位置 */
  top: 40px;
  width: 160px;
  height: 37%;
  background: linear-gradient(220.55deg, #FFF6EB 0%, #DFD1C5 100%);
  box-shadow: 2px 0 5px rgba(0, 0, 0, 0.1);
  transition: all 0.3s ease;
  z-index: 30;
  padding: 15px 0;
  display: flex;
  flex-direction: column;
  border-radius: 10px;
  /* 轻微模糊效果 */
  backdrop-filter: blur(8px);
  /* 透明边框 */
  border: 1px solid rgba(255, 255, 255, 0.6);
  /* 清澈透明背景 */
  background: rgba(255, 255, 255, 0.591);
  /* 简洁阴影 */
  box-shadow:
    0 4px 16px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.4);
}

.side-nav.show {
  left: 0;
}

.nav-item {
  padding: 12px 15px;
  margin: 4px 0;
  cursor: pointer;
  transition: all 0.3s ease;
  color: #333;
  border-left: 3px solid transparent;
  font-size: 0.9rem;
}

.nav-item:hover,
.nav-item.active {
  background-color: #f5f5f5;
  border-left: 4px solid #409EFF;
  color: #409EFF;
}

.slide-enter-active,
.slide-leave-active {
  transition: all 0.3s ease;
}

.slide-enter,
.slide-leave-to {
  opacity: 0;
  transform: translateX(-20px);
}

/* 右侧面板 - 1/3宽度 */
.right-panel {
  flex: 1;
  height: 100%;
  display: flex;
  flex-direction: column;
  position: relative;
  background: none;
}

.right-panel::before {
  content: "";
  position: absolute;
  top: 120px;
  left: 0;
  right: 0;
  bottom: 78px;
  background-image: url('./assets/background.png');
  background-size: cover;
  opacity: 0.3;
  z-index: 0;
}

.messages-container {
  background-color: transparent;
  position: relative;
  z-index: 1;
  /* 轻微模糊效果 */
  backdrop-filter: blur(1px);
  /* 透明边框 */
  border: 1px solid rgba(255, 255, 255, 0.6);
  /* 清澈透明背景 */
  background: rgba(255, 255, 255, 0.159);
  /* 简洁阴影 */
  box-shadow:
    0 4px 16px rgba(0, 0, 0, 0.1),
    0 0 0 1px rgba(255, 255, 255, 0.4);
}

.message {
  opacity: 1 !important;
}

.fade-enter-active,
.fade-leave-active {
  transition: opacity 0.3s ease;
}

.fade-enter,
.fade-leave-to {
  opacity: 0;
}



.chat-header {
  padding: 15px 20px;
  background: linear-gradient(220.55deg, #FFF6EB 0%, #DFD1C5 100%);
}

.chat-header h3 {
  margin: 0;
  color: #333;
}

.messages-container {
  flex: 1;
  padding: 20px;
  overflow-y: auto;
  display: flex;
  flex-direction: column;
}

.message {
  margin-bottom: 15px;
  max-width: 80%;
  padding: 12px 16px;
  border-radius: 18px;
  line-height: 1.4;
  font-size: 0.95rem;
}

.user-message {
  align-self: flex-end;
  background-color: #409EFF;
  color: white;
  border-bottom-right-radius: 4px;
}

.ai-message {
  align-self: flex-start;
  background-color: #f0f0f0;
  color: #333;
  border-bottom-left-radius: 4px;
}

.voice-btn {
  margin-left: 8px;
  padding: 8px 12px;
  background-color: #f0f0f0;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  cursor: pointer;
  transition: all 0.3s;
  font-size: 16px;
}

.voice-btn:hover {
  background-color: #e4e7ed;
}

.voice-btn.recording {
  background-color: #ff4d4f;
  color: white;
  animation: pulse 1.5s infinite;
}

@keyframes pulse {
  0% {
    box-shadow: 0 0 0 0 rgba(255, 77, 79, 0.7);
  }

  70% {
    box-shadow: 0 0 0 6px rgba(255, 77, 79, 0);
  }

  100% {
    box-shadow: 0 0 0 0 rgba(255, 77, 79, 0);
  }
}

.input-area {
  display: flex;
  align-items: center;
  padding: 15px;
  border-top: 1px solid #eaeaea;
  background: linear-gradient(220.55deg, #FFF6EB 0%, #DFD1C5 100%);
}

.chat-input {
  flex: 1;
  padding: 12px 15px;
  border: 1px solid #dcdfe6;
  border-radius: 4px;
  outline: none;
  transition: border-color 0.3s;
  font-size: 0.95rem;
}

.chat-input:focus {
  border-color: #409EFF;
}

.send-btn {
  margin-left: 10px;
  padding: 0 20px;
  background-color: #409EFF;
  color: white;
  border: none;
  border-radius: 4px;
  cursor: pointer;
  transition: background-color 0.3s;
}

.send-btn:hover {
  background-color: #66b1ff;
}
</style>