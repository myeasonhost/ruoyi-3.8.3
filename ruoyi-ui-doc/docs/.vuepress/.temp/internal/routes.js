export const redirects = JSON.parse("{}")

export const routes = Object.fromEntries([
  ["/", { loader: () => import(/* webpackChunkName: "index.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/index.html.js"), meta: {"title":"MBPAY"} }],
  ["/api/", { loader: () => import(/* webpackChunkName: "index.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/index.html.js"), meta: {"title":"欢迎MBPAY 👏"} }],
  ["/faq/", { loader: () => import(/* webpackChunkName: "index.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/faq/index.html.js"), meta: {"title":"常见问题"} }],
  ["/upgrade/", { loader: () => import(/* webpackChunkName: "index.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/upgrade/index.html.js"), meta: {"title":"升级说明"} }],
  ["/api/account/info.html", { loader: () => import(/* webpackChunkName: "info.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/account/info.html.js"), meta: {"title":"账户余额查询"} }],
  ["/api/address/add.html", { loader: () => import(/* webpackChunkName: "add.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/address/add.html.js"), meta: {"title":"收款地址配置"} }],
  ["/api/address/generate.html", { loader: () => import(/* webpackChunkName: "generate.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/address/generate.html.js"), meta: {"title":"收款地址生成"} }],
  ["/api/address/generate_add.html", { loader: () => import(/* webpackChunkName: "generate_add.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/address/generate_add.html.js"), meta: {"title":"收款地址生成 & 替换"} }],
  ["/api/address/query.html", { loader: () => import(/* webpackChunkName: "query.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/address/query.html.js"), meta: {"title":"收款地址查询"} }],
  ["/api/dpai/create.html", { loader: () => import(/* webpackChunkName: "create.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/dpai/create.html.js"), meta: {"title":"3.1 订单创建"} }],
  ["/api/dpai/notify.html", { loader: () => import(/* webpackChunkName: "notify.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/dpai/notify.html.js"), meta: {"title":"3.2 订单回调"} }],
  ["/api/intro/convention.html", { loader: () => import(/* webpackChunkName: "convention.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/intro/convention.html.js"), meta: {"title":"1.1 接口约定"} }],
  ["/api/intro/safety.html", { loader: () => import(/* webpackChunkName: "safety.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/intro/safety.html.js"), meta: {"title":"1.2 安全说明"} }],
  ["/api/transaction/cashier.html", { loader: () => import(/* webpackChunkName: "cashier.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/transaction/cashier.html.js"), meta: {"title":"2.4 订单收银台"} }],
  ["/api/transaction/create.html", { loader: () => import(/* webpackChunkName: "create.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/transaction/create.html.js"), meta: {"title":"2.1 订单创建"} }],
  ["/api/transaction/notify.html", { loader: () => import(/* webpackChunkName: "notify.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/transaction/notify.html.js"), meta: {"title":"2.2 订单回调"} }],
  ["/api/transaction/query.html", { loader: () => import(/* webpackChunkName: "query.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/transaction/query.html.js"), meta: {"title":"2.3 订单查询"} }],
  ["/404.html", { loader: () => import(/* webpackChunkName: "404.html" */"D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/404.html.js"), meta: {"title":""} }],
]);

if (import.meta.webpackHot) {
  import.meta.webpackHot.accept()
  if (__VUE_HMR_RUNTIME__.updateRoutes) {
    __VUE_HMR_RUNTIME__.updateRoutes(routes)
  }
  if (__VUE_HMR_RUNTIME__.updateRedirects) {
    __VUE_HMR_RUNTIME__.updateRedirects(redirects)
  }
}

if (import.meta.hot) {
  import.meta.hot.accept(({ routes, redirects }) => {
    __VUE_HMR_RUNTIME__.updateRoutes(routes)
    __VUE_HMR_RUNTIME__.updateRedirects(redirects)
  })
}
