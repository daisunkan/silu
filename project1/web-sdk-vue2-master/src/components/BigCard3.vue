<template>
    <div class="big-card-container">
        <!-- 大卡片主体 -->
        <div class="big-card">
            <h2 class="big-card-title">丝路之"转折"阶段（宋元）</h2>
            <div class="cards-grid">
                <!-- 3x3 SpecialCard 网格 -->
                <SpecialCard class="card-item" :imagePath="'card31.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片1</h3>
                            <p>这是第一个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>蒙古帝国西征</h3>
                            <p>成吉思汗与其子孙沿丝绸之路西征，用低税率鼓励欧亚贸易，促进东西方融合</p>
                            <button class="action-btn" @click="fillChatInput('讲解成吉思汗与其子孙沿丝绸之路西征的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card32.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片2</h3>
                            <p>这是第二个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>海上丝路兴起</h3>
                            <p>宋代航海技术提高，海上陶瓷之路逐渐取代陆上丝路的地位</p>
                            <button class="action-btn" @click="fillChatInput('讲解海上丝路兴起的历史原因')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card33.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片3</h3>
                            <p>这是第三个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>马可·波罗游记</h3>
                            <p>威尼斯商人马可·波罗经丝路来到中国，其游记激发欧洲对东方的向往</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下马可·波罗游记的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card34.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片4</h3>
                            <p>这是第四个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>伊斯兰教传入新疆</h3>
                            <p>10世纪初，伊斯兰教传入新疆喀什地区，16世纪时取代佛教成为天山以南主要宗教</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下伊斯兰教传入新疆的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card35.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片5</h3>
                            <p>这是第五个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝路上的疾病传播</h3>
                            <p>14世纪里海地区黑死病暴发并通过丝路西传至欧洲，导致欧洲损失1/3人口</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路上疾病传播的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card36.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片6</h3>
                            <p>这是第六个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>明朝闭关政策</h3>
                            <p>1539年，明王朝关闭嘉峪关，实施"闭关绝贡"政策，丝绸之路趋向低谷</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下明朝闭关政策的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card37.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片7</h3>
                            <p>这是第七个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>奥斯曼帝国崛起</h3>
                            <p>1453年拜占庭帝国灭亡，奥斯曼帝国阻断亚欧联系，促使欧洲寻找新航路</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下奥斯曼帝国崛起的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card38.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片8</h3>
                            <p>这是第八个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>青花瓷的传播</h3>
                            <p>元代青花瓷通过丝绸之路传入中亚、西亚，融合了伊斯兰文化元素</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下青花瓷传播的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card39.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片9</h3>
                            <p>这是第九个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>驿站系统完善</h3>
                            <p>元朝建立完善的驿站系统，保障了丝绸之路的通信与交通。</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下元朝驿站系统完善的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>
            </div>
        </div>
    </div>
</template>

<script>
import SpecialCard from './SpecialCard.vue'
import { EventBus } from '../event-bus.js'

export default {
    name: 'BigCard',
    components: {
        SpecialCard
    },
    methods: {
        fillChatInput(text) {
            // 触发全局事件，传递预设文本
            EventBus.$emit('fill-chat-input', text)
        }
    }
}
</script>

<style scoped>
.big-card-container {
    /* 位置调整说明：
     - 调整 left 属性可以改变卡片在水平方向的位置
     - 调整 top 属性可以改变卡片在垂直方向的位置
     - 调整 width 属性可以改变卡片的宽度（当前设置为屏幕宽度的1/3）
     - 调整 height 属性可以改变卡片的高度
     - 调整 z-index 属性可以改变卡片的层级
  */
    position: fixed;
    top: 50px;
    width: 40vw;
    /* 占据屏幕1/3宽度 */
    height: 100vh;
    z-index: 20;
}

.big-card {
    width: 100%;
    height: 100%;
}

.big-card-title {
    text-align: center;
    margin-bottom: 30px;
    color: #333;
    font-size: 24px;
    font-weight: 600;
    flex-shrink: 0; 
}


.cards-grid {
    display: grid;
    grid-template-columns: repeat(3, 1fr);
    grid-template-rows: repeat(3, 1fr);
    gap: 15px;
    row-gap: 80px;    /* 上下间距 */
    column-gap: 15px; /* 左右间距 */
    justify-items: center;
    align-items: center;
}

.card-item {
    width: 100%;
    height: 100%;
    min-height: 140px;
}

/* 响应式调整 */
@media (max-width: 1200px) {
    .big-card-container {
        width: 40vw;
    }
}

@media (max-width: 768px) {
    .big-card-container {
        width: 50vw;
    }

    .cards-grid {
        grid-template-columns: repeat(2, 1fr);
        grid-template-rows: repeat(5, 1fr);
    }
}

@media (max-width: 480px) {
    .big-card-container {
        width: 100vw;
        position: relative;
        height: auto;
    }

    .cards-grid {
        grid-template-columns: 1fr;
        grid-template-rows: repeat(9, 1fr);
    }
}
</style>