import vue from 'vue'
import vuex from 'vuex'
import state from './states.js'
import mutations from './mutations.js'
import actions from './actions.js'
import getters from './getters'
import user from './modules/user'
vue.use(vuex);
export default new vuex.Store({
  state,
  mutations,
  getters,
  actions,
  modules:{
    user
  }
})