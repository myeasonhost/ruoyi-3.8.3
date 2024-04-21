import type { SidebarConfig } from '@vuepress/theme-default'

export const zh: SidebarConfig = {
  '/api/': [
    {
      text: '接入文档',
      children: [
        '/api/README.md',
        {
          text: '1）接口说明',
          children: [
            '/api/intro/convention.md',
            '/api/intro/safety.md',
          ]
        }, {
          text: '2）代收订单',
          children: [
            '/api/transaction/create.md',
            '/api/transaction/notify.md',
            '/api/transaction/query.md',
            '/api/transaction/cashier.md'
          ]
        }, {
          text: '3）代付订单',
          children: [
            '/api/dpai/create.md',
            '/api/dpai/notify.md',
          ]
        }
      ],
    },
  ],
  '/faq/': [
    '/faq/index.md'
  ],
  '/upgrade/': [
    '/upgrade/index.md'
  ]
}
