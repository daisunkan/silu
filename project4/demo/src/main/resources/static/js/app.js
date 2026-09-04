import ApiService from './api.js';

const { createApp, ref, onMounted, nextTick } = Vue;

const ImageMaskEditor = {
    setup() {
        // 响应式数据
        const canvas = ref(null);
        const canvasWidth = ref(800);
        const canvasHeight = ref(600);
        
        const baseImage = ref(null);
        const maskImage = ref(null);
        const isDrawing = ref(false);
        const isErasing = ref(false);
        const brushSize = ref(20);
        const lastX = ref(0);
        const lastY = ref(0);
        const hasUserDrawing = ref(false);
        const originalImageData = ref(null);
        const originalImageFile = ref(null);
        
        const prompt = ref('');
        const selectedSize = ref('1024*1024');
        const selectedStyle = ref('<auto>');
        const imageCount = ref(1);
        const statusMessage = ref('请选择一张图片开始风格迁移');
        
        const showPreview = ref(false);
        const previewImageSrc = ref('');
        const generatedImageUrls = ref([]);
        const isGenerating = ref(false);
        const currentPreviewIndex = ref(0);
        
        // 可用风格选项
        const availableStyles = ref([
            { value: '<auto>', label: '自动' },
            { value: '<3d cartoon>', label: '3D卡通' },
            { value: '<anime>', label: '动画' },
            { value: '<oil painting>', label: '油画' },
            { value: '<watercolor>', label: '水彩' },
            { value: '<sketch>', label: '素描' },
            { value: '<chinese painting>', label: '中国画' },
            { value: '<flat illustration>', label: '扁平插画' }
        ]);
        
        let ctx = null;
        let tempCanvas = null;
        let tempCtx = null;

        // 创建圆形鼠标指针
        const createCircleCursor = (size, color, isEraser = false) => {
            const canvas = document.createElement('canvas');
            canvas.width = size + 10;
            canvas.height = size + 10;
            const ctx = canvas.getContext('2d');
            
            // 绘制外圈
            ctx.beginPath();
            ctx.arc((size + 10) / 2, (size + 10) / 2, size / 2 + 2, 0, Math.PI * 2);
            ctx.strokeStyle = isEraser ? '#ff4444' : '#333333';
            ctx.lineWidth = 2;
            ctx.stroke();
            
            // 绘制内圈
            ctx.beginPath();
            ctx.arc((size + 10) / 2, (size + 10) / 2, size / 2, 0, Math.PI * 2);
            ctx.strokeStyle = color;
            ctx.lineWidth = 2;
            ctx.stroke();
            
            if (isEraser) {
                // 橡皮擦添加X标记
                ctx.beginPath();
                ctx.moveTo((size + 10) / 2 - size / 4, (size + 10) / 2 - size / 4);
                ctx.lineTo((size + 10) / 2 + size / 4, (size + 10) / 2 + size / 4);
                ctx.moveTo((size + 10) / 2 + size / 4, (size + 10) / 2 - size / 4);
                ctx.lineTo((size + 10) / 2 - size / 4, (size + 10) / 2 + size / 4);
                ctx.strokeStyle = '#ff4444';
                ctx.lineWidth = 2;
                ctx.stroke();
            }
            
            return `url(${canvas.toDataURL()}) ${(size + 10) / 2} ${(size + 10) / 2}, auto`;
        };

        // 更新鼠标指针
        const updateCursor = () => {
            if (!canvas.value) return;
            
            const size = brushSize.value;
            if (isErasing.value) {
                canvas.value.style.cursor = createCircleCursor(size, '#ff4444', true);
            } else {
                canvas.value.style.cursor = createCircleCursor(size, '#333333', false);
            }
        };

        // Bresenham 直线算法
        const drawLineOnMask = (x0, y0, x1, y1, isErasing) => {
            const maskData = maskImage.value.data;
            const radius = brushSize.value / 2;
            
            const dx = Math.abs(x1 - x0);
            const dy = Math.abs(y1 - y0);
            const sx = (x0 < x1) ? 1 : -1;
            const sy = (y0 < y1) ? 1 : -1;
            let err = dx - dy;
            
            let x = x0;
            let y = y0;
            
            while (true) {
                // 在当前位置绘制圆形
                const startX = Math.max(0, Math.floor(x - radius));
                const endX = Math.min(canvasWidth.value, Math.ceil(x + radius));
                const startY = Math.max(0, Math.floor(y - radius));
                const endY = Math.min(canvasHeight.value, Math.ceil(y + radius));
                const radiusSq = radius * radius;
                
                for (let py = startY; py < endY; py++) {
                    for (let px = startX; px < endX; px++) {
                        const dx = px - x;
                        const dy = py - y;
                        const distanceSq = dx * dx + dy * dy;
                        
                        if (distanceSq <= radiusSq) {
                            const index = (py * maskImage.value.width + px) * 4;
                            if (isErasing) {
                                // 橡皮擦：设置alpha为0（透明）
                                maskData[index + 3] = 0;
                            } else {
                                // 画笔：设置为黑色不透明（用户看到的是黑色涂抹）
                                maskData[index] = 0;     // R
                                maskData[index + 1] = 0; // G
                                maskData[index + 2] = 0; // B
                                maskData[index + 3] = 255; // A (完全不透明)
                            }
                        }
                    }
                }
                
                if (x === x1 && y === y1) break;
                
                const e2 = 2 * err;
                if (e2 > -dy) {
                    err -= dy;
                    x += sx;
                }
                if (e2 < dx) {
                    err += dx;
                    y += sy;
                }
            }
            
            // 更新显示
            updateCanvasDisplay();
        };

        // 更新画布显示
        const updateCanvasDisplay = () => {
            if (!originalImageData.value) return;
            
            // 先绘制原始图像
            ctx.putImageData(originalImageData.value, 0, 0);
            
            // 然后绘制黑色标记（使用合成模式）
            ctx.globalCompositeOperation = 'source-over';
            const displayCanvas = document.createElement('canvas');
            displayCanvas.width = canvasWidth.value;
            displayCanvas.height = canvasHeight.value;
            const displayCtx = displayCanvas.getContext('2d');
            
            // 创建黑色标记图像
            const markImageData = displayCtx.createImageData(canvasWidth.value, canvasHeight.value);
            for (let i = 0; i < maskImage.value.data.length; i += 4) {
                if (maskImage.value.data[i + 3] > 0) { // 如果有标记
                    markImageData.data[i] = 0;     // R
                    markImageData.data[i + 1] = 0; // G
                    markImageData.data[i + 2] = 0; // B
                    markImageData.data[i + 3] = 128; // A (半透明)
                }
            }
            
            displayCtx.putImageData(markImageData, 0, 0);
            ctx.drawImage(displayCanvas, 0, 0);
        };

        // 初始化画布
        const initCanvas = () => {
            if (!canvas.value) return;
            
            ctx = canvas.value.getContext('2d');
            tempCanvas = document.createElement('canvas');
            tempCtx = tempCanvas.getContext('2d');
            
            // 设置画布背景为白色
            ctx.fillStyle = 'white';
            ctx.fillRect(0, 0, canvasWidth.value, canvasHeight.value);
            
            // 初始化遮罩图像（完全透明）
            maskImage.value = ctx.createImageData(canvasWidth.value, canvasHeight.value);
            for (let i = 0; i < maskImage.value.data.length; i += 4) {
                maskImage.value.data[i] = 0;
                maskImage.value.data[i + 1] = 0;
                maskImage.value.data[i + 2] = 0;
                maskImage.value.data[i + 3] = 0;
            }
            
            // 重置用户绘制标记
            hasUserDrawing.value = false;
            
            // 更新鼠标指针
            updateCursor();
        };

        // 处理文件上传
        const handleFileUpload = (event) => {
            const file = event.target.files[0];
            if (!file) return;
            
            loadImageToCanvas(file);
        };

        // 加载图片到画布 - 使用原始尺寸，不进行调整
        const loadImageToCanvas = (file) => {
            originalImageFile.value = file;
            const reader = new FileReader();
            
            reader.onload = (e) => {
                const img = new Image();
                img.onload = () => {
                    // 使用原始图片尺寸，不进行调整
                    const width = img.width;
                    const height = img.height;
                    
                    // 设置画布尺寸为图片原始尺寸
                    canvasWidth.value = width;
                    canvasHeight.value = height;
                    
                    nextTick(() => {
                        // 重新初始化画布
                        initCanvas();
                        
                        // 绘制图像到画布（使用原始尺寸）
                        ctx.drawImage(img, 0, 0, width, height);
                        
                        // 保存原始图片数据
                        originalImageData.value = ctx.getImageData(0, 0, canvasWidth.value, canvasHeight.value);
                        
                        // 保存基础图片
                        baseImage.value = ctx.getImageData(0, 0, canvasWidth.value, canvasHeight.value);
                        
                        statusMessage.value = `已加载图片: ${file.name} (原始尺寸${width}x${height})`;
                    });
                };
                img.src = e.target.result;
            };
            
            reader.readAsDataURL(file);
        };

        // 获取画布坐标
        const getCanvasCoordinates = (e) => {
            const rect = canvas.value.getBoundingClientRect();
            let x, y;
            
            if (e.type.includes('touch')) {
                x = e.touches[0].clientX - rect.left;
                y = e.touches[0].clientY - rect.top;
            } else {
                x = e.clientX - rect.left;
                y = e.clientY - rect.top;
            }
            
            // 确保坐标在画布范围内
            x = Math.max(0, Math.min(x, canvasWidth.value));
            y = Math.max(0, Math.min(y, canvasHeight.value));
            
            return { x, y };
        };

        // 开始绘制
        const startDrawing = (e) => {
            if (!baseImage.value) return;
            
            e.preventDefault();
            isDrawing.value = true;
            hasUserDrawing.value = true;
            
            const { x, y } = getCanvasCoordinates(e);
            lastX.value = x;
            lastY.value = y;
            
            drawOnImage(x, y);
        };

        // 绘制到图像上
        const drawOnImage = (x, y) => {
            const radius = brushSize.value / 2;
            const radiusSq = radius * radius;
            
            // 计算受影响的像素范围
            const startX = Math.max(0, Math.floor(x - radius));
            const endX = Math.min(canvasWidth.value, Math.ceil(x + radius));
            const startY = Math.max(0, Math.floor(y - radius));
            const endY = Math.min(canvasHeight.value, Math.ceil(y + radius));
            
            for (let py = startY; py < endY; py++) {
                for (let px = startX; px < endX; px++) {
                    const dx = px - x;
                    const dy = py - y;
                    const distanceSq = dx * dx + dy * dy;
                    
                    // 如果像素在圆形区域内
                    if (distanceSq <= radiusSq) {
                        const index = (py * maskImage.value.width + px) * 4;
                        if (isErasing.value) {
                            // 橡皮擦：设置alpha为0（透明）
                            maskImage.value.data[index + 3] = 0;
                        } else {
                            // 画笔：设置为黑色不透明（用户看到的是黑色涂抹）
                            maskImage.value.data[index] = 0;     // R
                            maskImage.value.data[index + 1] = 0; // G
                            maskImage.value.data[index + 2] = 0; // B
                            maskImage.value.data[index + 3] = 255; // A (完全不透明)
                        }
                    }
                }
            }
            
            // 更新显示
            updateCanvasDisplay();
        };

        // 绘制线条
        const drawLine = (startX, startY, endX, endY) => {
            drawLineOnMask(startX, startY, endX, endY, isErasing.value);
        };

        // 绘制过程
        const draw = (e) => {
            if (isDrawing.value && baseImage.value) {
                const { x, y } = getCanvasCoordinates(e);
                drawLine(lastX.value, lastY.value, x, y);
                lastX.value = x;
                lastY.value = y;
            }
        };

        // 停止绘制
        const stopDrawing = () => {
            isDrawing.value = false;
        };

        // 设置画笔模式
        const setBrushMode = () => {
            isErasing.value = false;
            updateCursor();
        };

        // 设置橡皮擦模式
        const setEraserMode = () => {
            isErasing.value = true;
            updateCursor();
        };

        // 预览效果
        const previewResult = () => {
            if (!baseImage.value) {
                alert('请先加载一张图片');
                return;
            }
            
            // 创建预览图片：背景为白色，标记区域为黑色
            const previewCanvas = document.createElement('canvas');
            previewCanvas.width = canvasWidth.value;
            previewCanvas.height = canvasHeight.value;
            const previewCtx = previewCanvas.getContext('2d');
            
            // 设置背景为白色
            previewCtx.fillStyle = 'white';
            previewCtx.fillRect(0, 0, previewCanvas.width, previewCanvas.height);
            
            // 将遮罩标记的区域设置为黑色
            if (hasUserDrawing.value) {
                for (let y = 0; y < maskImage.value.height; y++) {
                    for (let x = 0; x < maskImage.value.width; x++) {
                        const index = (y * maskImage.value.width + x) * 4;
                        const alpha = maskImage.value.data[index + 3];
                        if (alpha > 0) {
                            previewCtx.fillStyle = '#000000'; // 黑色
                            previewCtx.fillRect(x, y, 1, 1);
                        }
                    }
                }
            }
            
            // 显示预览
            previewImageSrc.value = previewCanvas.toDataURL('image/png');
            showPreview.value = true;
        };

        // 清除所有标记
        const clearMask = () => {
            if (!baseImage.value) {
                alert('请先加载一张图片');
                return;
            }
            
            if (confirm('确定要清除所有标记吗？')) {
                // 重置显示图片为原图
                ctx.putImageData(originalImageData.value, 0, 0);
                
                // 重置遮罩为完全透明
                maskImage.value = ctx.createImageData(canvasWidth.value, canvasHeight.value);
                for (let i = 0; i < maskImage.value.data.length; i += 4) {
                    maskImage.value.data[i + 3] = 0;
                }
                
                // 重置用户绘制标记
                hasUserDrawing.value = false;
                
                statusMessage.value = '已清除所有标记';
            }
        };

        // 保存当前状态
        const saveCurrentState = () => {
            if (!baseImage.value) {
                alert('请先加载一张图片');
                return;
            }
            
            // 创建白底黑色涂抹图
            const saveCanvas = document.createElement('canvas');
            saveCanvas.width = canvasWidth.value;
            saveCanvas.height = canvasHeight.value;
            const saveCtx = saveCanvas.getContext('2d');
            
            // 设置背景为白色
            saveCtx.fillStyle = 'white';
            saveCtx.fillRect(0, 0, saveCanvas.width, saveCanvas.height);
            
            // 将遮罩标记的区域设置为黑色
            if (hasUserDrawing.value) {
                for (let y = 0; y < maskImage.value.height; y++) {
                    for (let x = 0; x < maskImage.value.width; x++) {
                        const index = (y * maskImage.value.width + x) * 4;
                        const alpha = maskImage.value.data[index + 3];
                        if (alpha > 0) {
                            saveCtx.fillStyle = '#000000'; // 黑色
                            saveCtx.fillRect(x, y, 1, 1);
                        }
                    }
                }
            }
            
            // 创建下载链接
            const dataURL = saveCanvas.toDataURL('image/png');
            const link = document.createElement('a');
            link.download = `mask-${new Date().getTime()}.png`;
            link.href = dataURL;
            link.click();
            
            statusMessage.value = '当前状态已保存';
        };

        // 创建掩码图像文件 - 白底黑色标记（后端会转换为黑底白标记）
        const createMaskImageFile = () => {
            return new Promise((resolve) => {
                const maskCanvas = document.createElement('canvas');
                maskCanvas.width = canvasWidth.value;
                maskCanvas.height = canvasHeight.value;
                const maskCtx = maskCanvas.getContext('2d');
                
                // 创建白底背景
                maskCtx.fillStyle = 'white';
                maskCtx.fillRect(0, 0, maskCanvas.width, maskCanvas.height);
                
                // 将用户涂抹的区域绘制为黑色
                if (hasUserDrawing.value) {
                    const tempCanvas = document.createElement('canvas');
                    tempCanvas.width = canvasWidth.value;
                    tempCanvas.height = canvasHeight.value;
                    const tempCtx = tempCanvas.getContext('2d');
                    tempCtx.putImageData(maskImage.value, 0, 0);
                    
                    const imageData = tempCtx.getImageData(0, 0, tempCanvas.width, tempCanvas.height);
                    const data = imageData.data;
                    
                    for (let i = 0; i < data.length; i += 4) {
                        const r = data[i];
                        const g = data[i + 1];
                        const b = data[i + 2];
                        const a = data[i + 3];
                        
                        if (a > 0) {
                            // 设置为黑色 (RGB: 0, 0, 0)
                            data[i] = 0;     // R
                            data[i + 1] = 0; // G
                            data[i + 2] = 0; // B
                            data[i + 3] = 255; // A (完全不透明)
                        }
                    }
                    
                    tempCtx.putImageData(imageData, 0, 0);
                    maskCtx.drawImage(tempCanvas, 0, 0);
                }
                
                maskCanvas.toBlob((blob) => {
                    resolve(new File([blob], 'mask.png', { type: 'image/png' }));
                }, 'image/png');
            });
        };

        // 验证提示词长度
        const validatePrompt = (text) => {
            if (text.length > 75) {
                alert('提示词不能超过75个字符，已自动截断');
                return text.substring(0, 75);
            }
            return text;
        };

        // 生成图片
        const generateImage = async () => {
            if (!baseImage.value) {
                alert('请先加载一张图片');
                return;
            }
            
            if (!hasUserDrawing.value) {
                alert('请先使用画笔进行标记');
                return;
            }
            
            if (!prompt.value.trim()) {
                alert('请输入风格迁移的描述');
                return;
            }
            
            try {
                isGenerating.value = true;
                statusMessage.value = '正在进行风格迁移，请稍候...';
                
                // 验证并截断提示词
                const validatedPrompt = validatePrompt(prompt.value.trim());
                
                // 创建掩码图像文件（白底黑色标记）
                const maskImageFile = await createMaskImageFile();
                
                // 创建FormData
                const formData = new FormData();
                formData.append('prompt', validatedPrompt);
                formData.append('baseImageFile', originalImageFile.value);
                formData.append('maskImageFile', maskImageFile);
                formData.append('size', selectedSize.value);
                formData.append('style', selectedStyle.value);
                formData.append('n', imageCount.value.toString());
                
                // 发送请求到后端
                const result = await ApiService.generateImage(formData);
                
                if (result.imageUrls && result.imageUrls.length > 0) {
                    generatedImageUrls.value = result.imageUrls;
                    currentPreviewIndex.value = 0;
                    previewImageSrc.value = result.imageUrls[0]; // 显示第一张图片
                    showPreview.value = true;
                    statusMessage.value = `风格迁移成功！共生成${result.imageUrls.length}张图片`;
                } else {
                    throw new Error('风格迁移失败：未返回图片URL');
                }
            } catch (error) {
                console.error('风格迁移失败:', error);
                alert('风格迁移失败: ' + error.message);
                statusMessage.value = '风格迁移失败，请重试';
            } finally {
                isGenerating.value = false;
            }
        };

        // 关闭预览
        const closePreview = () => {
            showPreview.value = false;
            previewImageSrc.value = '';
            generatedImageUrls.value = [];
            currentPreviewIndex.value = 0;
        };

        // 下载图片
        const downloadImage = (url, index = 0) => {
            if (url) {
                const link = document.createElement('a');
                link.href = url;
                link.download = `generated-image-${index + 1}.png`;
                link.click();
            }
        };

        // 切换预览图片
        const switchPreviewImage = (index) => {
            if (generatedImageUrls.value[index]) {
                currentPreviewIndex.value = index;
                previewImageSrc.value = generatedImageUrls.value[index];
            }
        };

        // 下载全部图片
        const downloadAllImages = () => {
            generatedImageUrls.value.forEach((url, index) => {
                setTimeout(() => {
                    downloadImage(url, index);
                }, index * 100); // 稍微延迟避免浏览器阻止多个下载
            });
        };

        // 监听画笔大小变化，更新鼠标指针
        const onBrushSizeChange = () => {
            updateCursor();
        };

        // 组件挂载时初始化
        onMounted(() => {
            initCanvas();
            
            // 测试后端连接
            ApiService.healthCheck().then(data => {
                console.log('后端服务状态:', data);
                statusMessage.value = '后端服务连接正常，请选择图片开始风格迁移';
            }).catch(error => {
                console.error('后端连接失败:', error);
                statusMessage.value = '后端服务连接失败，请检查服务状态';
            });
        });

        return {
            // Refs
            canvas,
            canvasWidth,
            canvasHeight,
            prompt,
            selectedSize,
            selectedStyle,
            imageCount,
            availableStyles,
            statusMessage,
            brushSize,
            isErasing,
            showPreview,
            previewImageSrc,
            isGenerating,
            generatedImageUrls,
            currentPreviewIndex,
            
            // Methods
            handleFileUpload,
            startDrawing,
            draw,
            stopDrawing,
            setBrushMode,
            setEraserMode,
            previewResult,
            clearMask,
            saveCurrentState,
            generateImage,
            closePreview,
            downloadImage,
            switchPreviewImage,
            downloadAllImages,
            onBrushSizeChange
        };
    },
    template: `
        <div class="container">
            <header>
                <h1>AI文创风格迁移系统</h1>
            </header>
            
            <div class="main-content">
                <div class="editor-area">
                    <div class="image-panel">
                        <div class="canvas-container">
                            <canvas 
                                ref="canvas" 
                                :width="canvasWidth" 
                                :height="canvasHeight"
                                @mousedown="startDrawing"
                                @mousemove="draw"
                                @mouseup="stopDrawing"
                                @mouseout="stopDrawing"
                                @touchstart="startDrawing"
                                @touchmove="draw"
                                @touchend="stopDrawing"
                            ></canvas>
                        </div>
                        
                        <div class="file-upload">
                            <input 
                                type="file" 
                                id="fileInput" 
                                class="file-input" 
                                accept="image/*"
                                @change="handleFileUpload"
                            >
                            <label for="fileInput" class="file-label">
                                <i class="fas fa-upload"></i> 选择基础图片
                            </label>
                            <div class="status-info">
                                {{ statusMessage }}
                            </div>
                        </div>
                    </div>
                    
                    <div class="controls-panel">
                        <div class="control-group">
                            <h3>风格迁移设置</h3>
                            <textarea 
                                v-model="prompt" 
                                class="prompt-input" 
                                placeholder="请输入风格迁移的描述（最多75个字符）..."
                                maxlength="75"
                            ></textarea>
                            <div class="slider-container">
                                <label>图像尺寸:</label>
                                <select v-model="selectedSize" class="form-select">
                                    <option value="1024*1024">1024x1024</option>
                                    <option value="720*1280">720x1280</option>
                                    <option value="1280*720">1280x720</option>
                                </select>
                            </div>
                        </div>
                        
                        <div class="control-group">
                            <h3>高级设置</h3>
                            <div class="slider-container">
                                <label>艺术风格:</label>
                                <select v-model="selectedStyle" class="form-select">
                                    <option v-for="style in availableStyles" :value="style.value">
                                        {{ style.label }}
                                    </option>
                                </select>
                            </div>
                            <div class="slider-container">
                                <label>生成数量:</label>
                                <input type="range" v-model.number="imageCount" min="1" max="4" class="form-range">
                                <span class="count-display">{{ imageCount }}张</span>
                            </div>
                        </div>
                        
                        <div class="control-group">
                            <h3>编辑工具</h3>
                            <div class="tool-buttons">
                                <button 
                                    :class="['btn', 'tool-btn', { active: !isErasing }]"
                                    @click="setBrushMode"
                                >
                                    <i class="fas fa-paint-brush"></i> 画笔
                                </button>
                                <button 
                                    :class="['btn', 'tool-btn', { active: isErasing }]"
                                    @click="setEraserMode"
                                >
                                    <i class="fas fa-eraser"></i> 橡皮擦
                                </button>
                            </div>
                        </div>
                        
                        <div class="control-group">
                            <h3>画笔设置</h3>
                            <div class="slider-container">
                                <label>画笔大小:</label>
                                <input 
                                    type="range" 
                                    v-model.number="brushSize" 
                                    min="5" 
                                    max="50"
                                    @input="onBrushSizeChange"
                                    class="form-range"
                                >
                                <span class="size-display">{{ brushSize }}px</span>
                                <div 
                                    class="brush-preview" 
                                    :style="{
                                        width: brushSize + 'px',
                                        height: brushSize + 'px',
                                        background: isErasing ? '#ff4444' : '#333333'
                                    }"
                                ></div>
                            </div>
                        </div>
                        
                        <div class="control-group action-buttons">
                            <h3>操作</h3>
                            <button class="btn btn-block action-btn" @click="previewResult">
                                <i class="fas fa-eye"></i> 预览效果
                            </button>
                            <button class="btn btn-block action-btn" @click="saveCurrentState">
                                <i class="fas fa-save"></i> 保存当前状态
                            </button>
                            <button class="btn btn-block action-btn" @click="clearMask">
                                <i class="fas fa-trash"></i> 清除所有
                            </button>
                            <button 
                                class="btn btn-block generate-btn" 
                                @click="generateImage"
                                :disabled="isGenerating"
                            >
                                <i class="fas fa-magic"></i> 
                                {{ isGenerating ? '风格迁移中...' : '开始风格迁移' }}
                            </button>
                        </div>
                        
                        <div class="instructions">
                            <h4>使用说明:</h4>
                            <ul>
                                <li>1. 选择一张基础图片</li>
                                <li>2. 使用画笔在需要风格迁移的区域涂抹</li>
                                <li>3. 使用橡皮擦可以擦除已标记的区域</li>
                                <li>4. 调整画笔大小以适应不同区域</li>
                                <li>5. 输入风格迁移的描述（最多75字符）</li>
                                <li>6. 选择艺术风格和生成数量</li>
                                <li>7. 点击"开始风格迁移"进行AI风格迁移</li>
                            </ul>
                        </div>
                    </div>
                </div>
            </div>
            
            <!-- 预览模态框 -->
            <div v-if="showPreview" class="preview-modal" @click="closePreview">
                <div class="preview-content" @click.stop>
                    <h3>风格迁移结果预览</h3>
                    <div v-if="generatedImageUrls.length > 1" class="image-navigation">
                        <button 
                            v-for="(url, index) in generatedImageUrls" 
                            :key="index"
                            :class="['nav-btn', { active: currentPreviewIndex === index }]"
                            @click="switchPreviewImage(index)"
                        >
                            图片 {{ index + 1 }}
                        </button>
                    </div>
                    <div class="preview-image-container">
                        <img :src="previewImageSrc" alt="预览图片" class="preview-image">
                    </div>
                    <div class="preview-actions">
                        <button class="btn preview-action-btn" @click="closePreview">
                            <i class="fas fa-times"></i> 关闭预览
                        </button>
                        <button 
                            class="btn preview-action-btn download-btn" 
                            @click="downloadImage(previewImageSrc, currentPreviewIndex)"
                        >
                            <i class="fas fa-download"></i> 下载当前图片
                        </button>
                        <button 
                            v-if="generatedImageUrls.length > 1"
                            class="btn preview-action-btn download-all-btn" 
                            @click="downloadAllImages"
                        >
                            <i class="fas fa-download"></i> 下载全部图片
                        </button>
                    </div>
                    <div class="expiry-notice">
                        <i class="fas fa-info-circle"></i> 图片链接有效期为24小时，请及时保存
                    </div>
                </div>
            </div>
            
            <!-- 加载模态框 -->
            <div v-if="isGenerating" class="loading">
                <div class="loading-spinner"></div>
                <p>AI正在进行风格迁移，请稍候...</p>
                <p class="loading-subtitle">这可能需要几十秒时间，请耐心等待</p>
            </div>
        </div>
    `
};

// 创建并挂载Vue应用
createApp(ImageMaskEditor).mount('#app');