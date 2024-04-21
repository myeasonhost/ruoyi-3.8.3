import comp from "D:/eason/tronapi-doc/docs/.vuepress/.temp/pages/demo/index.html.vue"
const data = JSON.parse("{\"path\":\"/demo/\",\"title\":\"接入工具\",\"lang\":\"zh\",\"frontmatter\":{\"sidebarDepth\":1},\"headers\":[{\"level\":2,\"title\":\"php\",\"slug\":\"php\",\"link\":\"#php\",\"children\":[]},{\"level\":2,\"title\":\"java\",\"slug\":\"java\",\"link\":\"#java\",\"children\":[]},{\"level\":2,\"title\":\"node\",\"slug\":\"node\",\"link\":\"#node\",\"children\":[]}],\"git\":{},\"filePathRelative\":\"demo/index.md\"}")
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
