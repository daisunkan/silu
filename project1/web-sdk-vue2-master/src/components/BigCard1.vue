<template>
    <div class="big-card-container">
        <!-- 大卡片主体 -->
        <div class="big-card">
            <h2 class="big-card-title">丝路之"开辟"阶段（汉代）</h2>
            <div class="cards-grid">
                <!-- 3x3 SpecialCard 网格 -->
                <SpecialCard class="card-item" :imagePath="'card11.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片1</h3>
                            <p>这是第一个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>张骞出使西域</h3>
                            <p>汉武帝派遣张骞出使西域，开辟了通往西方的道路，司马迁称此行为"凿空"之举</p>
                            <button class="action-btn" @click="fillChatInput('请详细介绍一下张骞出使西域的历史背景和意义')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card12.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片2</h3>
                            <p>这是第二个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>西域都护府设立</h3>
                            <p>汉代设立西域都护府，总理西域事务，标志着丝绸之路正式纳入国家管理层面，图中展示的是新疆地区曾经设立设立西域都护府的奎玉克协海尔古城和卓尔库特古城</p>
                            <button class="action-btn" @click="fillChatInput('请详细介绍一下西域都护府的设立背景和作用')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card13.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片3</h3>
                            <p>这是第三个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>甘英出使大秦</h3>
                            <p>班超派遣甘英出使大秦（罗马），虽未抵达但却到了西亚的"条支"（今伊拉克），传播了中国文化</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下史料甘英出使大秦中人物甘英的个人生平')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card14.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片4</h3>
                            <p>这是第四个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>佛教传入中国</h3>
                            <p>丝绸之路成为佛教传入中国的通道，佛教音乐、雕塑、绘画等影响了中国的文化艺术</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下佛教是如何传入中国的')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card15.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片5</h3>
                            <p>这是第五个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>楼兰古国</h3>
                            <p>作为丝绸之路要冲的楼兰国，后改名为鄯善，是东西方文化交流的重要节点</p>
                            <button class="action-btn" @click="fillChatInput('讲解一下楼兰古国的历史发展')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card16.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片6</h3>
                            <p>这是第六个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>汉代丝绸</h3>
                            <p>中国丝绸通过丝绸之路运抵西域与罗马，成为东西方贸易的重要商品，图中展示的是国家一级文物——素纱襌衣</p>
                            <button class="action-btn" @click="fillChatInput('国家一级文物——素纱襌衣的历史是怎样的')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card17.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片7</h3>
                            <p>这是第七个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>玉门关与阳关</h3>
                            <p>汉代在敦煌以南设阳关、以北设玉门关，成为丝绸之路的重要关口</p>
                            <button class="action-btn" @click="fillChatInput('玉门关和阳关是什么？它们的历史是怎样的？')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card18.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片8</h3>
                            <p>这是第八个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>班超恢复丝路</h3>
                            <p>东汉班超重新打通因政局动荡而中断的丝绸之路，被封为"定远侯"</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下班超的生平和丝路恢复的历史')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card19.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片9</h3>
                            <p>这是第九个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>过所制度</h3>
                            <p>汉代建立的对外"过所"制度，管理出入中外人员，标志着丝绸之路管理的完善,图中展示的是吐鲁番出土的过所文书</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下过所制度的历史背景和作用')">了解更多</button>
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