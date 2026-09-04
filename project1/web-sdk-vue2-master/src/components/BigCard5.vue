<template>
    <div class="big-card-container">
        <!-- 大卡片主体 -->
        <div class="big-card">
            <h2 class="big-card-title">丝路之"新生"阶段（近现代）</h2>
            <div class="cards-grid">
                <!-- 3x3 SpecialCard 网格 -->
                <SpecialCard class="card-item" :imagePath="'card51.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片1</h3>
                            <p>这是第一个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>一带一路倡议</h3>
                            <p>中国提出"一带一路"倡议，重塑丝绸之路经济带，推动欧亚空间深度合作</p>
                            <button class="action-btn" @click="fillChatInput('讲解一下我国“一带一路”倡议的发展')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card52.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片2</h3>
                            <p>这是第二个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝绸之路申遗</h3>
                            <p>中国与中亚国家联合推动丝绸之路申报世界文化遗产，保护丝路历史遗迹</p>
                            <button class="action-btn" @click="fillChatInput('讲解一下丝绸之路申遗的相关故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card53.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片3</h3>
                            <p>这是第三个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>欧亚大陆桥</h3>
                            <p>现代铁路网络构建新的欧亚大陆桥，被誉为现代丝绸之路</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下现代欧亚大陆桥的相关故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card54.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片4</h3>
                            <p>这是第四个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝路旅游热潮</h3>
                            <p>丝绸之路成为重要旅游线路，促进沿线国家的文化交流与经济发展</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路相关的热门旅游景点')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card55.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片5</h3>
                            <p>这是第五个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>敦煌学成立</h3>
                            <p>敦煌文献的发现促使"敦煌学"形成，成为国际显学</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下敦煌学的相关发展历程')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card56.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片6</h3>
                            <p>这是第六个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>数字丝路建设</h3>
                            <p>利用现代信息技术构建数字丝绸之路，促进沿线国家信息互联互通。</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下数字丝绸之路建设的相关内容')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card57.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片7</h3>
                            <p>这是第七个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝绸之路研究</h3>
                            <p>各国学者对丝绸之路进行多学科研究，挖掘其历史价值与当代意义</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下各国对丝绸之路研究的相关内容')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card58.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片8</h3>
                            <p>这是第八个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>文化交流复兴</h3>
                            <p>沿线国家通过艺术节、展览、学术交流等形式复兴丝路文化交流</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路文化交流复兴的相关故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card59.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片9</h3>
                            <p>这是第九个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>中新互联互通</h3>
                            <p>中国与新加坡等国的"国际陆海贸易新通道"成为海上丝绸之路新枢纽</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下中新互联互通的相关内容')">了解更多</button>
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