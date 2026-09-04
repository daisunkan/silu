const { defineConfig } = require('@vue/cli-service')
module.exports = defineConfig({
  transpileDependencies: true,
  lintOnSave: false,
  devServer:{
    port:8081
  },
  chainWebpack: config => {
    config.module
      .rule('glb')
      .test(/\.glb$/)
      .use('file-loader')
      .loader('file-loader')
      .options({
        name: 'assets/[name].[hash:8].[ext]'
      })
      .end()
  },
})
