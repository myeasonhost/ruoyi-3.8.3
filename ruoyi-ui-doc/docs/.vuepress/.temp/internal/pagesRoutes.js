import { Vuepress } from '@vuepress/client/lib/components/Vuepress'

const routeItems = [
  ["v-8daa1a0e","/",{"title":"MBPAY"},["/index.html","/README.md"]],
  ["v-744497ce","/api/",{"title":"欢迎MBPAY 👏"},["/api/index.html","/api/README.md"]],
  ["v-7446a652","/faq/",{"title":"常见问题"},["/faq/index.html","/faq/index.md"]],
  ["v-1473bf53","/demo/",{"title":"接入工具"},["/demo/index.html","/demo/index.md"]],
  ["v-2b83938c","/upgrade/",{"title":"升级说明"},["/upgrade/index.html","/upgrade/index.md"]],
  ["v-8e5fdd0a","/api/account/info.html",{"title":"账户余额查询"},["/api/account/info","/api/account/index.md"]],
  ["v-1edd3861","/api/address/add.html",{"title":"收款地址配置"},["/api/address/add","/api/address/add.md"]],
  ["v-41aee69b","/api/address/generate.html",{"title":"收款地址生成"},["/api/address/generate","/api/address/generate.md"]],
  ["v-d565aa0e","/api/address/generate_add.html",{"title":"收款地址生成 & 替换"},["/api/address/generate_add","/api/address/generate_add.md"]],
  ["v-f07c4b4c","/api/address/query.html",{"title":"收款地址查询"},["/api/address/query","/api/address/query.md"]],
  ["v-74692357","/api/intro/convention.html",{"title":"1.1 接口约定"},["/api/intro/convention","/api/intro/convention.md"]],
  ["v-77af57d4","/api/intro/safety.html",{"title":"1.2 安全说明"},["/api/intro/safety","/api/intro/safety.md"]],
  ["v-b3bca560","/api/dpai/create.html",{"title":"3.1 订单创建"},["/api/dpai/create","/api/dpai/create.md"]],
  ["v-5bae9f23","/api/dpai/notify.html",{"title":"3.2 订单回调"},["/api/dpai/notify","/api/dpai/notify.md"]],
  ["v-4565c556","/api/transaction/cashier.html",{"title":"2.4 订单收银台"},["/api/transaction/cashier","/api/transaction/cashier.md"]],
  ["v-5dd2301e","/api/transaction/create.html",{"title":"2.1 订单创建"},["/api/transaction/create","/api/transaction/create.md"]],
  ["v-135f21f1","/api/transaction/notify.html",{"title":"2.2 订单回调"},["/api/transaction/notify","/api/transaction/notify.md"]],
  ["v-49e73630","/api/transaction/query.html",{"title":"2.3 订单查询"},["/api/transaction/query","/api/transaction/query.md"]],
  ["v-3706649a","/404.html",{"title":""},["/404"]],
]

export const pagesRoutes = routeItems.reduce(
  (result, [name, path, meta, redirects]) => {
    result.push(
      {
        name,
        path,
        component: Vuepress,
        meta,
      },
      ...redirects.map((item) => ({
        path: item,
        redirect: path,
      }))
    )
    return result
  },
  [
    {
      name: "404",
      path: "/:catchAll(.*)",
      component: Vuepress,
    }
  ]
)
