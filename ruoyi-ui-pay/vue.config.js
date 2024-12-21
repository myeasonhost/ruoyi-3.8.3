var os = require('os'); var ip = ''; var ifaces = os.networkInterfaces() // 获取本机ip
const env = 'test' // 测试环境
// const env = 'prePro' // 预生产环境
// const env = 'pro' // 生产环境

let v1 = 'http://192.168.0.81:8080/api'
let v2 = 'http://192.168.0.81:8080/api'
if (env === 'prePro') {
} else if (env === 'pro') {
}

out:
for (var i in ifaces) {
  for (var j in ifaces[i]) {
    var val = ifaces[i][j]
    if (val.family === 'IPv4' && val.address !== '192.168.0.81') {
      ip = val.address
      break out
    }
  }
}
module.exports = {
  // baseUrl: './',
  // assetsDir: 'static',
  productionSourceMap: false,
  css: { extract: false },
  devServer: {
    host: ip,
    proxy: {
      '/api': {
        target: v1,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      },
      '/api': {
        target: v2,
        changeOrigin: true,
        pathRewrite: {
          '^/api': ''
        }
      }
    },
    overlay: {
      warnings: true,
      errors: true
    }
  },
  lintOnSave: true,
  configureWebpack (config) {
    //生产环境取消 console.log
    if (process.env.NODE_ENV === 'production') {
      // config.optimization.minimizer[0].options.terserOptions.compress.drop_console = true
    }
  }
}
