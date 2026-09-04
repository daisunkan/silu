// vue.config.js
const { defineConfig } = require('@vue/cli-service')

module.exports = defineConfig({
  transpileDependencies: true,
  
  // 配置构建选项
  configureWebpack: {
    performance: {
      maxAssetSize: 5000000, // 将警告阈值提高到1MB
      maxEntrypointSize: 5000000
    }
  },
  
  // 链式配置Webpack
  chainWebpack: config => {
    // 优化音乐文件处理
    config.module
      .rule('media')
      .test(/\.(mp3|wav|m4a|ogg)$/)
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'media/[name].[hash:8].[ext]'
      })
  }
})