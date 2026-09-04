<template>
    <div class="big-card-container">
        <!-- 大卡片主体 -->
        <div class="big-card">
            <h2 class="big-card-title">丝路之"鼎盛"阶段（魏晋至唐）</h2>
            <div class="cards-grid">
                <!-- 3x3 SpecialCard 网格 -->
                <SpecialCard class="card-item" :imagePath="'card21.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片1</h3>
                            <p>这是第一个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>粟特商人</h3>
                            <p>中亚粟特人沿丝绸之路建立商业网络，形成聚落，是丝路贸易的重要中间商</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下历史中粟特商人的发展历史')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card22.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片2</h3>
                            <p>这是第二个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>唐三彩胡俑</h3>
                            <p>唐代墓葬中出土的深目高鼻、满脸胡须的胡人俑，反映唐代丝路贸易的繁荣</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路中唐三彩是如何发展的')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card23.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片3</h3>
                            <p>这是第三个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>唐帝国疆域扩张</h3>
                            <p>唐代疆域西扩，与阿拉伯帝国并立，丝路沿线文明达到空前的繁荣</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路中唐玄宗是如何发展的')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card24.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片4</h3>
                            <p>这是第四个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>玄奘西行取经</h3>
                            <p>唐代玄奘法师西行印度取经，其著作《大唐西域记》记录了丝路沿线风土人情</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路中玄奘法师是如何发展的')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card25.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片5</h3>
                            <p>这是第五个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>四大帝国并立</h3>
                            <p>汉朝、罗马、贵霜、帕提亚四大帝国通过丝绸之路展开经贸与文化交流</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路中四大帝国是如何发展的')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card26.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片6</h3>
                            <p>这是第六个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>胡商遇盗图</h3>
                            <p>敦煌壁画中的"胡商遇盗图"，描绘了丝路贸易的惊险与困难</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路的敦煌壁画中胡商遇盗图的历史')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card27.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片7</h3>
                            <p>这是第七个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝绸之路上的宗教传播</h3>
                            <p>佛教、祆教、景教、伊斯兰教等通过丝绸之路传播，形成多元宗教共存局面</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下丝绸之路上宗教传播的历史')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card28.webp'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片8</h3>
                            <p>这是第八个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>岳州窑胡俑</h3>
                            <p>南方岳州窑生产的胡人俑，反映胡人在华打工或经商的情况</p>
                            <button class="action-btn" @click="fillChatInput('介绍一下著名文物岳州窑胡俑所展现的历史故事')">了解更多</button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card29.jpg'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片9</h3>
                            <p>这是第九个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>丝绸之路上的女性</h3>
                            <p>粟特妇女信件揭示丝路上女性的生活状况，以及祆教社区对妇女的保护</p>
                            <button class="action-btn" @click="fillChatInput('讲解一下粟特母女信件的故事，即米薇写给母亲和丈夫的信')">了解更多</button>
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