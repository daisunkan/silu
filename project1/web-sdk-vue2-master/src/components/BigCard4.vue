<template>
    <div class="big-card-container">
        <!-- 大卡片主体 -->
        <div class="big-card">
            <h2 class="big-card-title">丝路之"沉淀"阶段（明清）</h2>
            <div class="cards-grid">
                <!-- 3x3 SpecialCard 网格 -->
                <SpecialCard class="card-item" :imagePath="'card41.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片1</h3>
                            <p>这是第一个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>郑和下西洋</h3>
                            <p>明代郑和七次下西洋，通过海上丝绸之路与南亚、西亚、欧洲和北非交流</p>
                            <button class="action-btn" @click="fillChatInput('讲解一下郑和下西洋的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card42.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片2</h3>
                            <p>这是第二个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>茶叶贸易繁荣</h3>
                            <p>中国茶叶通过丝绸之路传入西方，成为东西方贸易的重要商品</p>
                            <button class="action-btn" @click="fillChatInput('讲解一下茶叶贸易通过丝路而繁荣的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card43.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片3</h3>
                            <p>这是第三个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>耶稣会士来华</h3>
                            <p>利玛窦等欧洲传教士经海路来华，成为中西文化交流的桥梁</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下耶稣会士来华的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card44.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片4</h3>
                            <p>这是第四个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>海上陶瓷之路</h3>
                            <p>明代海上丝绸之路以瓷器为主要贸易商品，连接中国与东南亚、南亚、中东</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下明代海上陶瓷之路的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card45.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片5</h3>
                            <p>这是第五个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝绸之路记忆保存</h3>
                            <p>随着丝路贸易衰落，沿线地区通过文献、地图、文物保存丝路记忆</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路上沿线地区对丝路文化的保存的相关故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card46.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片6</h3>
                            <p>这是第六个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>欧洲中国风热潮</h3>
                            <p>17-18世纪欧洲兴起中国风，丝绸、瓷器、茶叶成为欧洲贵族地位象征</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下17-18世纪欧洲中国风热潮的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card47.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片7</h3>
                            <p>这是第七个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>考古发现兴起</h3>
                            <p>20世纪初，斯文·赫定、斯坦因等在楼兰、敦煌等地发现丝路遗迹</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下考古发现丝路遗迹的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card48.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片8</h3>
                            <p>这是第八个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>传统工艺传承</h3>
                            <p>丝绸、陶瓷等传统工艺在特定地区传承，保存了丝路相关技艺</p>
                            <button class="action-btn" @click="fillChatInput('介绍当今一下丝绸陶瓷等传统工艺传承的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card49.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片9</h3>
                            <p>这是第九个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝绸之路命名</h3>
                            <p>1877年德国地理学家李希霍芬将这条古代商路命名为"丝绸之路"</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路命名的历史故事')">了解更多</button>
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