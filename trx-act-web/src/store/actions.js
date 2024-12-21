export default {//在action中可以进行异步操作。
    dispatch1({commit,dispatch}){
      commit("ADD_BOOK1")
      dispatch("dispatch2")
    },
    dispatch2({commit}){
      commit("ADD_BOOK2")
    }
 }