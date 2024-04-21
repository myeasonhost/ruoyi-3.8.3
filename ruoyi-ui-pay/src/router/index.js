import Vue from 'vue'
import Router from 'vue-router'

Vue.use(Router)
const originalPush = Router.prototype.push
Router.prototype.push = function push (location) {
  return originalPush.call(this, location).catch(err => err)
}

const router = new Router({
  mode: 'history',
  routes: [
    {
      path: '/',
      name: 'home',
      component: () => import('@/pages/Home/index.vue'),
      meta: { title: 'mbpay' }
    }
  ]
})

// 使用钩子函数对路由进行权限跳转
router.beforeEach((to, from, next) => {
  document.title = 'pay'
  next()
})

export default router
