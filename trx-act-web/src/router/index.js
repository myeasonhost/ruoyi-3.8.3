import Vue from "vue";
import VueRouter from "vue-router";
import Home from "../views/Home.vue";

Vue.use(VueRouter);

const routes = [
  {
    path: "/",
    name: "Home",
    component: Home
  },
  {
    path: "/about",
    name: "About",
    meta: { requireLogin: true, role: 0 }, //requireLogin是否需要登陆，role是否支持试玩
    component: () =>
      import("../views/About.vue")
  },
  {
    path: "*",
    redirect: "/"
  }
];

const router = new VueRouter({ mode: "hash",routes});

export default router;
