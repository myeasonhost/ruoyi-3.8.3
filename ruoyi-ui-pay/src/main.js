import Vue from 'vue'
import 'babel-polyfill'

import App from './App.vue'
import router from './router'
import store from './vuexStore'
import http from './http'
import Element from 'element-ui'
import 'element-ui/lib/theme-chalk/index.css'

Vue.config.productionTip = false
Vue.prototype.$http = http
Vue.use(Element, {
  size: 'medium' // set element-ui default size
})

window.requestedTime = 2
new Vue({
  router,
  store,
  render: h => h(App)
}).$mount('#app')
