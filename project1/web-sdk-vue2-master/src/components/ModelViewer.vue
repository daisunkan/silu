<template>
    <div v-if="showModal" class="model-modal" @click.self="closeModal">
        <div class="model-modal-content">
            <span class="close-btn" @click="closeModal">×</span>

            <!-- 模型查看器容器 -->
            <div class="model-viewer-container">
                <div ref="modelContainer" class="model-container"></div>

                <!-- 控制面板 -->
                <div class="controls-panel">
                    <button @click="resetView" class="control-btn" title="重置视角">⟲</button>
                    <button @click="toggleAutoRotate" class="control-btn" :title="autoRotate ? '停止旋转' : '自动旋转'">
                        {{ autoRotate ? '⏹' : '🔄' }}
                    </button>
                    <div class="zoom-controls">
                        <button @click="zoomIn" class="control-btn" title="放大">+</button>
                        <button @click="zoomOut" class="control-btn" title="缩小">-</button>
                    </div>
                    <button @click="toggleGrid" class="control-btn" :title="showGrid ? '隐藏网格' : '显示网格'">
                        {{ showGrid ? '📊' : '📈' }}
                    </button>
                </div>

                <!-- 加载状态 -->
                <div v-if="loading" class="loading-overlay">
                    <div class="loading-spinner"></div>
                    <p>加载模型中...</p>
                </div>

                <!-- 错误信息 -->
                <div v-if="error" class="error-overlay">
                    <p>模型加载失败</p>
                    <button @click="retryLoad" class="control-btn">重试</button>
                </div>

                <!-- 操作提示 -->
                <div class="help-tips">
                    <p>🖱️ 鼠标拖拽旋转 | 🔍 滚轮缩放 | 🖱️ 右键平移</p>
                </div>
            </div>
        </div>
    </div>
</template>

<script>
import * as THREE from 'three';
import { GLTFLoader } from 'three/examples/jsm/loaders/GLTFLoader.js';
import { OrbitControls } from 'three/examples/jsm/controls/OrbitControls.js';

export default {
    name: 'ModelViewer',
    props: {
        showModal: {
            type: Boolean,
            default: false
        },
        modelPath: {
            type: String,
            default: ''
        }
    },
    data() {
        return {
            scene: null,
            camera: null,
            renderer: null,
            controls: null,
            loader: null,
            model: null,
            loading: false,
            error: false,
            autoRotate: true,
            showGrid: false, // 默认不显示网格
            gridHelper: null,
            ambientLight: null,
            directionalLight: null,
            hemisphereLight: null,
            isInitialized: false
        };
    },
    mounted() {
        console.log('ModelViewer 组件已挂载');
    },
    watch: {
        showModal(newVal) {
            if (newVal) {
                this.$nextTick(() => {
                    // 确保DOM元素存在
                    if (this.$refs.modelContainer) {
                        this.init3DViewer();
                    } else {
                        console.warn('modelContainer 尚未渲染');
                        // 延迟初始化
                        setTimeout(() => {
                            this.init3DViewer();
                        }, 100);
                    }
                });
            } else {
                this.cleanup();
            }
        },
        modelPath: {
            handler(newPath) {
                if (newPath && this.showModal && this.isInitialized) {
                    // 延迟执行，确保查看器已初始化
                    setTimeout(() => {
                        this.loadModel();
                    }, 200);
                }
            },
            immediate: false // 不要立即执行
        }
    },
    methods: {
        init3DViewer() {
            // 清理之前的场景
            this.cleanup();

            // 创建场景 - 设置为浅灰色背景
            this.scene = new THREE.Scene();
            this.scene.background = new THREE.Color(0x666666); // 更浅的灰色背景

            // 创建相机
            this.camera = new THREE.PerspectiveCamera(
                45, // 更广的视角
                this.$refs.modelContainer.clientWidth / this.$refs.modelContainer.clientHeight,
                0.1,
                1000
            );
            // 更近的初始相机位置
            this.camera.position.set(3, 3, 3);

            // 创建渲染器
            this.renderer = new THREE.WebGLRenderer({
                antialias: true,
                alpha: true
            });
            this.renderer.setSize(
                this.$refs.modelContainer.clientWidth,
                this.$refs.modelContainer.clientHeight
            );
            this.renderer.shadowMap.enabled = true;
            this.renderer.shadowMap.type = THREE.PCFSoftShadowMap;
            this.renderer.physicallyCorrectLights = true;
            this.renderer.toneMapping = THREE.ACESFilmicToneMapping;
            this.renderer.toneMappingExposure = 1;

            // 添加渲染器到DOM
            this.$refs.modelContainer.appendChild(this.renderer.domElement);

            // 添加轨道控制器
            this.controls = new OrbitControls(this.camera, this.renderer.domElement);
            this.controls.enableDamping = true;
            this.controls.dampingFactor = 0.05;
            this.controls.autoRotate = this.autoRotate;
            this.controls.autoRotateSpeed = 1.5;
            this.controls.minDistance = 0.5; // 允许更近距离查看
            this.controls.maxDistance = 50;
            this.controls.enablePan = true;
            this.controls.screenSpacePanning = true;

            // 初始化加载器 - 这必须在调用 loadModel 之前完成
            this.loader = new GLTFLoader();
            console.log('GLTFLoader 初始化完成');

            // 添加光照系统
            this.addLights();

            // 添加参考网格（默认隐藏）
            this.addGrid();

            // 标记为已初始化
            this.isInitialized = true;

            // 延迟加载模型，确保所有组件都已初始化
            setTimeout(() => {
                this.loadModel();
            }, 100);

            // 开始动画循环
            this.animate();

            // 窗口大小改变时调整渲染器
            window.addEventListener('resize', this.handleResize);
        },

        addLights() {
            // 移除旧的光源
            if (this.ambientLight) this.scene.remove(this.ambientLight);
            if (this.directionalLight) this.scene.remove(this.directionalLight);
            if (this.hemisphereLight) this.scene.remove(this.hemisphereLight);

            // 1. 半球光 - 模拟天空和地面的环境光
            this.hemisphereLight = new THREE.HemisphereLight(
                0xffffff, // 天空颜色
                0x444444, // 地面颜色
                0.8       // 强度
            );
            this.hemisphereLight.position.set(0, 20, 0);
            this.scene.add(this.hemisphereLight);

            // 2. 环境光 - 提供基础照明
            this.ambientLight = new THREE.AmbientLight(0x404040, 0.6);
            this.scene.add(this.ambientLight);

            // 3. 主方向光 - 模拟太阳光
            this.directionalLight = new THREE.DirectionalLight(0xffffff, 2.0);
            this.directionalLight.position.set(0, 0, 10);
            this.directionalLight.castShadow = true;

            // 优化阴影质量
            this.directionalLight.shadow.mapSize.width = 2048;
            this.directionalLight.shadow.mapSize.height = 2048;
            this.directionalLight.shadow.camera.near = 0.5;
            this.directionalLight.shadow.camera.far = 50;
            this.directionalLight.shadow.camera.left = -20;
            this.directionalLight.shadow.camera.right = 20;
            this.directionalLight.shadow.camera.top = 20;
            this.directionalLight.shadow.camera.bottom = -20;

            this.scene.add(this.directionalLight);

            // 4. 补光 - 从另一侧提供柔和光照
            const fillLight = new THREE.DirectionalLight(0xffffff, 0.5);
            fillLight.position.set(-10, 10, -10);
            this.scene.add(fillLight);

            // 5. 边缘光 - 突出轮廓
            const rimLight = new THREE.DirectionalLight(0xffffff, 0.4);
            rimLight.position.set(0, 5, -15);
            this.scene.add(rimLight);
        },

        addGrid() {
            // 移除旧的网格
            if (this.gridHelper) {
                this.scene.remove(this.gridHelper);
            }

            // 创建网格辅助器
            this.gridHelper = new THREE.GridHelper(20, 20, 0x888888, 0x444444);
            this.gridHelper.rotation.x = Math.PI / 2;
            this.gridHelper.position.y = -2;
            this.gridHelper.visible = this.showGrid; // 根据设置显示/隐藏
            this.scene.add(this.gridHelper);
        },

        toggleGrid() {
            this.showGrid = !this.showGrid;
            if (this.gridHelper) {
                this.gridHelper.visible = this.showGrid;
            }
        },

        async loadModel() {
            // 检查必要的组件是否已初始化
            if (!this.loader || !this.scene) {
                console.warn('3D查看器尚未初始化完成，延迟加载模型');
                // 延迟一段时间后重试
                setTimeout(() => {
                    if (this.showModal && this.isInitialized) {
                        this.loadModel();
                    }
                }, 100);
                return;
            }

            if (!this.modelPath) {
                console.warn('模型路径为空');
                return;
            }

            this.loading = true;
            this.error = false;

            try {
                // 构建模型路径 - 使用 public 目录路径
                const modelFullPath = `/card6/glb/${this.modelPath}.glb`;
                console.log('加载模型路径:', modelFullPath);

                // 添加更详细的加载进度跟踪
                const gltf = await new Promise((resolve, reject) => {
                    this.loader.load(
                        modelFullPath,
                        (gltf) => {
                            console.log('模型加载成功:', gltf);
                            resolve(gltf);
                        },
                        (progress) => {
                            console.log('加载进度:', progress);
                        },
                        (error) => {
                            console.error('模型加载错误:', error);
                            reject(error);
                        }
                    );
                });

                // 清理之前的模型
                if (this.model) {
                    this.scene.remove(this.model);
                    // 释放材质和几何体
                    this.model.traverse((child) => {
                        if (child.isMesh) {
                            if (child.geometry) child.geometry.dispose();
                            if (child.material) {
                                if (Array.isArray(child.material)) {
                                    child.material.forEach(material => material.dispose());
                                } else {
                                    child.material.dispose();
                                }
                            }
                        }
                    });
                }

                this.model = gltf.scene;

                // 启用模型阴影
                this.model.traverse((child) => {
                    if (child.isMesh) {
                        child.castShadow = true;
                        child.receiveShadow = true;

                        // 增强材质效果
                        if (child.material) {
                            child.material.metalness = 0.1;
                            child.material.roughness = 0.8;
                            child.material.envMapIntensity = 1;
                        }
                    }
                });

                this.scene.add(this.model);

                // 调整模型位置和大小
                this.adjustModel();

                this.loading = false;
            } catch (err) {
                console.error('模型加载失败:', err);
                this.error = true;
                this.loading = false;
            }
        },

        adjustModel() {
            if (!this.model) return;

            // 计算模型的包围盒
            const box = new THREE.Box3().setFromObject(this.model);
            const center = box.getCenter(new THREE.Vector3());
            const size = box.getSize(new THREE.Vector3());

            // 将模型居中并放置在网格上方
            this.model.position.x = -center.x;
            this.model.position.y = -center.y;
            this.model.position.z = -center.z;

            // 调整相机位置以适应模型大小
            const maxDim = Math.max(size.x, size.y, size.z);
            const fov = this.camera.fov * (Math.PI / 180);
            let cameraZ = Math.abs(maxDim / Math.sin(fov / 2));

            // 设置更近的相机距离
            cameraZ = Math.max(cameraZ * 1.2, 2); // 减少倍数，让相机更近
            cameraZ = Math.min(cameraZ, 15); // 减少最大距离

            // 使用更近的视角
            this.camera.position.set(cameraZ, cameraZ * 0.5, cameraZ);
            this.controls.target.set(0, 0, 0);
            this.controls.update();
        },

        animate() {
            requestAnimationFrame(this.animate);

            if (this.controls) {
                this.controls.update();
            }

            if (this.renderer && this.scene && this.camera) {
                this.renderer.render(this.scene, this.camera);
            }
        },

        resetView() {
            if (this.controls && this.camera && this.model) {
                // 重新计算模型大小和位置
                const box = new THREE.Box3().setFromObject(this.model);
                const size = box.getSize(new THREE.Vector3());
                const maxDim = Math.max(size.x, size.y, size.z);

                // 设置更近的初始视角
                const cameraDistance = Math.max(maxDim * 1.5, 3);
                this.camera.position.set(cameraDistance, cameraDistance * 0.5, cameraDistance);
                this.controls.target.set(0, 0, 0);
                this.controls.reset();
            }
        },

        toggleAutoRotate() {
            this.autoRotate = !this.autoRotate;
            if (this.controls) {
                this.controls.autoRotate = this.autoRotate;
            }
        },

        zoomIn() {
            if (this.camera) {
                this.camera.position.multiplyScalar(0.9);
            }
        },

        zoomOut() {
            if (this.camera) {
                this.camera.position.multiplyScalar(1.1);
            }
        },

        handleResize() {
            if (this.camera && this.renderer && this.$refs.modelContainer) {
                const container = this.$refs.modelContainer;
                this.camera.aspect = container.clientWidth / container.clientHeight;
                this.camera.updateProjectionMatrix();
                this.renderer.setSize(container.clientWidth, container.clientHeight);
            }
        },

        retryLoad() {
            this.error = false;
            this.loading = true;
            
            // 延迟重试，确保组件已准备就绪
            setTimeout(() => {
                this.loadModel();
            }, 300);
        },

        closeModal() {
            this.$emit('close');
        },

        cleanup() {
            // 移除事件监听
            window.removeEventListener('resize', this.handleResize);

            // 清理Three.js资源
            if (this.renderer) {
                this.renderer.dispose();
                if (this.$refs.modelContainer && this.renderer.domElement) {
                    try {
                        this.$refs.modelContainer.removeChild(this.renderer.domElement);
                    } catch (e) {
                        console.warn('移除渲染器DOM元素时出错:', e);
                    }
                }
            }

            if (this.controls) {
                this.controls.dispose();
            }

            // 清理模型资源
            if (this.model) {
                this.model.traverse((child) => {
                    if (child.isMesh) {
                        if (child.geometry) child.geometry.dispose();
                        if (child.material) {
                            if (Array.isArray(child.material)) {
                                child.material.forEach(material => material.dispose());
                            } else {
                                child.material.dispose();
                            }
                        }
                    }
                });
            }

            // 重置所有引用
            this.scene = null;
            this.camera = null;
            this.renderer = null;
            this.controls = null;
            this.loader = null;
            this.model = null;
            this.gridHelper = null;
            this.ambientLight = null;
            this.directionalLight = null;
            this.hemisphereLight = null;
            this.isInitialized = false;
        }
    },
    beforeDestroy() {
        this.cleanup();
    }
};
</script>

<style scoped>
.model-modal {
    position: fixed;
    top: 0;
    left: 0;
    width: 100vw;
    height: 100vh;
    background: rgba(0, 0, 0, 0.8);
    display: flex;
    justify-content: center;
    align-items: center;
    z-index: 1000;
}

.model-modal-content {
    position: relative;
    width: 90vw;
    height: 90vh;
    background: #666666;
    /* 更浅的灰色背景 */
    border-radius: 12px;
    overflow: hidden;
    box-shadow: 0 10px 30px rgba(0, 0, 0, 0.5);
}

.close-btn {
    position: absolute;
    top: 15px;
    right: 20px;
    font-size: 30px;
    color: #fff;
    cursor: pointer;
    z-index: 1001;
    background: rgba(0, 0, 0, 0.6);
    width: 40px;
    height: 40px;
    border-radius: 50%;
    display: flex;
    align-items: center;
    justify-content: center;
    transition: all 0.3s ease;
    border: 1px solid rgba(255, 255, 255, 0.3);
}

.close-btn:hover {
    background: rgba(0, 0, 0, 0.8);
    transform: scale(1.1);
}

.model-viewer-container {
    width: 100%;
    height: 100%;
    position: relative;
}

.model-container {
    width: 100%;
    height: 100%;
}

.controls-panel {
    position: absolute;
    bottom: 20px;
    left: 50%;
    transform: translateX(-50%);
    display: flex;
    gap: 8px;
    background: rgba(0, 0, 0, 0.7);
    padding: 12px 20px;
    border-radius: 25px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.control-btn {
    padding: 10px 14px;
    background: rgba(255, 255, 255, 0.1);
    color: white;
    border: 1px solid rgba(255, 255, 255, 0.3);
    border-radius: 8px;
    cursor: pointer;
    font-size: 16px;
    transition: all 0.3s ease;
    min-width: 44px;
}

.control-btn:hover {
    background: rgba(255, 255, 255, 0.2);
    transform: translateY(-2px);
}

.zoom-controls {
    display: flex;
    gap: 5px;
}

.loading-overlay,
.error-overlay {
    position: absolute;
    top: 0;
    left: 0;
    width: 100%;
    height: 100%;
    background: rgba(102, 102, 102, 0.9);
    /* 匹配新的背景色 */
    display: flex;
    flex-direction: column;
    justify-content: center;
    align-items: center;
    z-index: 100;
}

.loading-spinner {
    width: 50px;
    height: 50px;
    border: 5px solid rgba(255, 255, 255, 0.3);
    border-top: 5px solid #ffffff;
    border-radius: 50%;
    animation: spin 1s linear infinite;
    margin-bottom: 15px;
}

@keyframes spin {
    0% {
        transform: rotate(0deg);
    }

    100% {
        transform: rotate(360deg);
    }
}

.error-overlay p {
    color: #ff6b6b;
    margin-bottom: 15px;
    font-size: 18px;
}

.help-tips {
    position: absolute;
    top: 20px;
    left: 50%;
    transform: translateX(-50%);
    background: rgba(0, 0, 0, 0.7);
    color: white;
    padding: 8px 16px;
    border-radius: 20px;
    font-size: 14px;
    backdrop-filter: blur(10px);
    border: 1px solid rgba(255, 255, 255, 0.2);
}

.help-tips p {
    margin: 0;
    opacity: 0.9;
}
</style>