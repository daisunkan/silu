<template>
    <div class="big-card-container">
        <!-- 大卡片主体 -->
        <div class="big-card">
            <h2 class="big-card-title">丝路文物鉴赏</h2>
            <div class="cards-grid">
                <!-- 3x3 SpecialCard 网格 -->
                <SpecialCard class="card-item" :imagePath="'card6/“父辛”铜斝.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片1</h3>
                            <p>这是第一个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>“父辛”铜斝</h3>
                            <p>商父辛铜斝为商代青铜酒器，现藏甘肃省博物馆。</p>
                            <button class="action-btn" @click="showModel('“父辛”铜斝')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/青铜貂觯.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片2</h3>
                            <p>这是第二个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>青铜貂觯</h3>
                            <p>西周时期文物，现藏于甘肃省博物馆</p>
                            <button class="action-btn" @click="showModel('青铜貂觯')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/释迦牟尼与弥勒佛碑.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片3</h3>
                            <p>这是第三个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>释迦牟尼与弥勒佛碑</h3>
                            <p>六朝时期文物，现藏于美国克利夫兰艺术博物馆</p>
                            <button class="action-btn" @click="showModel('释迦牟尼与弥勒佛碑')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/水烟桶.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片4</h3>
                            <p>这是第四个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>水烟桶</h3>
                            <p>16-17世纪时期文物，现藏于美国克利夫兰艺术博物馆</p>
                            <button class="action-btn" @click="showModel('水烟桶')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/水烟桶2.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片5</h3>
                            <p>这是第五个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>水烟桶2</h3>
                            <p>16-17世纪时期文物，现藏于美国克利夫兰艺术博物馆</p>
                            <button class="action-btn" @click="showModel('水烟桶2')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/铜方鼎.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片6</h3>
                            <p>这是第六个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>铜方鼎</h3>
                            <p>西周时期文物，现藏于甘肃省博物馆</p>
                            <button class="action-btn" @click="showModel('铜方鼎')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/銅尊.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片7</h3>
                            <p>这是第七个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>銅尊</h3>
                            <p>商代时期文物，现藏于香港中文大学文物馆</p>
                            <button class="action-btn" @click="showModel('銅尊')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/镶嵌莲花设计烧瓶.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片8</h3>
                            <p>这是第八个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>镶嵌莲花设计烧瓶</h3>
                            <p>藏品年代未知，现藏于美国克利夫兰艺术博物馆</p>
                            <button class="action-btn" @click="showModel('镶嵌莲花设计烧瓶')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>

                <SpecialCard class="card-item" :imagePath="'card6/耶林杯.png'">
                    <template #button-content>
                        <div class="card-content">
                            <h3>卡片9</h3>
                            <p>这是第九个卡片的内容</p>
                        </div>
                    </template>
                    <template #expanded-content>
                        <div class="expanded-content">
                            <h3>耶林杯</h3>
                            <p>维京时代文物，现藏于丹麦国立博物馆</p>
                            <button class="action-btn" @click="showModel('耶林杯')">
                                查看3D模型
                            </button>
                        </div>
                    </template>
                </SpecialCard>
            </div>
        </div>
    </div>
</template>

<script>
import SpecialCard from './SpecialCard.vue'
import { EventBus } from '../event-bus.js'  // 添加这行导入

export default {
    name: 'BigCard',
    components: {
        SpecialCard
    },
    methods: {
        showModel(modelName) {
            // 触发全局事件，传递模型名称
            EventBus.$emit('show-3d-model', modelName)
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
    row-gap: 80px;
    /* 上下间距 */
    column-gap: 15px;
    /* 左右间距 */
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