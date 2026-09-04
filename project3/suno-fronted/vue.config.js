const { defineConfig } = require('@vue/cli-service')
const path = require('path')

module.exports = defineConfig({
  transpileDependencies: true,
  configureWebpack: {
    resolve: {
      alias: {
        '@': path.resolve(__dirname, 'src')
      }
    }
  },
  devServer: {
    port: 8083,
    open: true,
    proxy: {
      '/api': {
        target: 'http://localhost:8086',
        changeOrigin: true
      }
    }
  },
  lintOnSave: false  // 添加这行
})