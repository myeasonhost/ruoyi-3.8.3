const state = {
   useName: "sam"
 };
 const mutations = {
   adduseNam(state){
      state.useName="adduseName"
      console.log(state.useName)
   }
 };
 const actions = {
   AadduseNam({commit}){
      commit("adduseNam")
   }
 };
 const getters = {
   usergetters(state){
      return state.useName+"usergetters"
   }
 };
  
 // 不要忘记把state, mutations等暴露出去。
 export default {
   namespaced: true,
   state,
   mutations,
   actions,
   getters
 }