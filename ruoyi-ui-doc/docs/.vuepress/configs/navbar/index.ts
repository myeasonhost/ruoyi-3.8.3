import type { NavbarConfig } from '@vuepress/theme-default'
import { default as appConfig } from '../app';

export const zh: NavbarConfig = [
  {
    text: '接入文档',
    link: '/api/',
  },
  {
    text: '常见问题',
    link: '/faq/'
  },
  {
    text: '商户登录',
    link: appConfig.links.system
  }
]
