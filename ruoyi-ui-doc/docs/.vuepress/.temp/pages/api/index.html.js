import comp from "D:/eason/ruoyi-3.8.3/ruoyi-ui-doc/docs/.vuepress/.temp/pages/api/index.html.vue"
const data = JSON.parse("{\"path\":\"/api/\",\"title\":\"欢迎MBPAY 👏\",\"lang\":\"zh\",\"frontmatter\":{},\"headers\":[{\"level\":2,\"title\":\"商户后台\",\"slug\":\"商户后台\",\"link\":\"#商户后台\",\"children\":[]},{\"level\":2,\"title\":\"联系我们\",\"slug\":\"联系我们\",\"link\":\"#联系我们\",\"children\":[]}],\"git\":{},\"filePathRelative\":\"api/README.md\"}")
export { comp, data }

if (import.meta.webpackHot) {
  import.meta.webpackHot.accept()
  if (__VUE_HMR_RUNTIME__.updatePageData) {
    __VUE_HMR_RUNTIME__.updatePageData(data)
  }
}

if (import.meta.hot) {
  import.meta.hot.accept(({ data }) => {
    __VUE_HMR_RUNTIME__.updatePageData(data)
  })
}
