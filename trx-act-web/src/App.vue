<template>
  <div id="app">
    <keep-alive>
      <router-view class="transitionBody"></router-view>
    </keep-alive>
  </div>
</template>
<script>

export default {

};
</script>
<style lang="scss">
#app {
  font-family: Avenir, Helvetica, Arial, sans-serif;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  text-align: center;
  color: #2c3e50;
}

#nav {
  padding: 30px;

  a {
    font-weight: bold;
    color: #2c3e50;

    &.router-link-exact-active {
      color: #42b983;
    }
  }
}

.transitionBody {
  transition: all 0.4s;
  width: 100%;
}
.transitionLeft-enter,
.transitionLeft-leave-active {
  -webkit-transform: translate(-100%, 0);
  transform: translate(-100%, 0);
}
.transitionLeft-leave,
.transitionLeft-enter-active {
  -webkit-transform: translate(100%, 0);
  transform: translate(100%, 0);
}

.transitionRight-enter,
.transitionRight-leave-active {
  -webkit-transform: translate(100%, 0);
  transform: translate(100%, 0);
}
.transitionRight-leave,
.transitionRight-enter-active {
  -webkit-transform: translate(-100%, 0);
  transform: translate(-100%, 0);
}
</style>
