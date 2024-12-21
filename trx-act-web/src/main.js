import Vue from "vue";
import App from "./App";
import router from "./router/index.js";
import store from "./store/index";
import Vant from "vant";
import vlipboard from "vue-clipboard2";
import vueCount from "vue-count-to";
import "vant/lib/index.css";

Vue.use(Vant);
Vue.use(vlipboard);
Vue.use(vueCount);


Vue.config.productionTip = false;

new Vue({
  router,
  store,
  render: h => h(App)
}).$mount("#app");

