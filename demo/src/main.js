import { createApp } from 'vue'
import App from './App.vue'

// 动态创建 link 标签添加 Font Awesome
const link = document.createElement('link')
link.rel = 'stylesheet'
link.href = 'https://cdnjs.cloudflare.com/ajax/libs/font-awesome/5.15.4/css/all.min.css'
link.integrity = 'sha512-1ycn6IcaQQ40/MKBW2W4Rhis/DbILU74C1vSrLJxCq57o941Ym01SwNsOMqvEBFlcgUa6xLiPY/NS5R+E6ztJQ=='
link.crossOrigin = 'anonymous'
link.referrerPolicy = 'no-referrer'
document.head.appendChild(link)

createApp(App).mount('#app')