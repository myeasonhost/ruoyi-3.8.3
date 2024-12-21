<!--
 * @Author: your name
 * @Date: 2020-05-01 12:05:05
 * @LastEditTime: 2020-05-09 19:56:55
 * @LastEditors: your name
 * @Description: In User Settings Edit
 * @FilePath: \moban_cli4\src\views\About.vue
 -->
<template>
  <div class="about">
    <h1>This is an about page</h1>
    <button @click="goShuju1">点我</button>
  </div>
</template>
<script>
let time = new Date();
import { mapState, mapGetters, mapMutations, mapActions } from "vuex";
import { ceshi } from "../api/index.js";
export default {
  date() {
    return {
      ceshi: 1
    };
  },
  computed: {
    ...mapState(["date1"]),
    ...mapState("user", ["useName"]),
    ...mapGetters(["getter1", "getter2"]),
    ...mapGetters("user", ["usergetters"]),
    shuju1() {
      return "ok";
    }
  },
  created() {
    console.log("created");
    console.log(this.$global.token);
    // this.changeData()  全局函数方式一
    this.$global_fun.global_funA();
    this.$global_fun.global_funB();
    console.log(this.$global_fun.global_funURL);
  },
  mounted() {
    console.log("mounted");
    console.log(time);
    console.log(Date.parse(time));
    // console.log(moment(time).format("HH:mm:ss"))
  },
  methods: {
    ...mapMutations(["ADD_BOOK"]),
    ...mapMutations("user", ["adduseNam"]),
    ...mapActions(["dispatch1"]),
    ...mapActions("user", ["AadduseNam"]),
    goShuju1() {
      console.log(this.shuju1);
      console.log(this.date1); //用computed接收)
      console.log(this.useName); //用computed接收)
      console.log(this.getter1); //用computed接收)
      console.log(this.getter2); //用computed接收)
      console.log(this.usergetters); //用computed接收)
      this.ADD_BOOK(); //执行Mutations方法
      this.dispatch1(); //执行Actions方法
      this.adduseNam();
      this.AadduseNam();
      this.$Message.info("提示");
      ceshi({}).then(res=> {
        console.log(res);
        console.log(res.status);
        console.log(res.data);
      });
    },
  },
};
</script>
<style lang="scss" scoped></style>
