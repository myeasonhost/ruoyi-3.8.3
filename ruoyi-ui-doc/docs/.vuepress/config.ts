import { navbar, sidebar, appConfig } from "./configs";
import { defineUserConfig } from 'vuepress'
import { viteBundler } from '@vuepress/bundler-vite'
import { defaultTheme } from '@vuepress/theme-default'

export default defineUserConfig({
  base: "/",
  lang: "zh",
  title: appConfig.title,
  description: appConfig.description,
  head: [
    ["link", { rel: "icon", href: appConfig.favicon }],
    ["meta", { name: 'description',  content: appConfig.description }],
    ["meta", { name: 'keywords',  content: appConfig.keywords }]
  ],
  bundler: viteBundler({
    viteOptions: {},
    vuePluginOptions: {},
  }),
  theme: defaultTheme({
    logo: appConfig.logo,
    editLink: false,
    lastUpdated: false,
    docsDir: "docs",
    contributors: false,
    locales: {
      "/": {
        navbar: navbar.zh,
        sidebar: sidebar.zh,
        sidebarDepth: 1,
        tip: "说明",
        warning: "注意",
        danger: "警告",
        backToHome: "返回首页",
      },
    },
  })
});
