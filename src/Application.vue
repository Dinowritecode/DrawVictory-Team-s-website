<script lang="ts" setup>
import {useStore} from "vuex";
import {useRoute} from "vue-router";

const state = useStore().state
// window.addEventListener('unload', () => sessionStorage.setItem('state', JSON.stringify(state)))
</script>

<template>
  <div class="root">
    <el-header class="header"><!--网页导航栏-->
      <img class="logo" src="/logo_dark.svg" alt="logo">
      <p class="name">DrawVictory Team</p>
      <p class="name2">绘赢</p>
      <el-menu mode="horizontal" class="menu" router text-color="#FF7675" background-color="#24292d"
               :default-active="useRoute().path">
        <el-menu-item class="items" index="/">首页</el-menu-item>
        <el-menu-item class="items" index="/404">介绍</el-menu-item>
        <el-menu-item class="login-register" index="/register">登录/注册</el-menu-item>
      </el-menu>
    </el-header>
    <div class="router-view">
      <router-view></router-view>
    </div>
  </div>
</template>

<style scoped lang="less">
.root {
  position: absolute;
  height: 100%;
  width: 100%;
  display: flex;
  flex-direction: column;
}

.header {
  top: 0;
  width: 100%;
  height: 50px;
  background-color: #24292d;
  z-index: 100;
}

.router-view {
  width: 100%;
  height: 100%;
  overflow: auto;
}

.logo {
  position: absolute;
  z-index: 100;
  width: 38px;
  top: 6px;
  user-select: none;
}

.name {
  position: absolute;
  color: #ffffff;
  user-select: none;
  font-size: 15px;
  font-weight: 600;
  left: 68px;
  top: 6px;
  margin: 0;
}

.name2 {
  position: absolute;
  color: #ffffff;
  user-select: none;
  font-size: 14px;
  font-weight: 600;
  left: 68px;
  top: 25px;
  margin: 0;
}

.menu {
  position: absolute;
  height: 50px;
  right: 0;
  background-color: #24292d;
  left: 250px;
  border: none;
}

.items {
  height: 50px;
  color: white;
  user-select: none;
  text-align: center;
  box-sizing: border-box;
  border: 0;
}

.items:hover {
  border-bottom: 3px solid #ff7675;
  animation: all 0.2s ease;
}

.login-register {
  background-color: #ff7675;
  color: white;
  position: absolute;
  width: 100px;
  height: 50px;
  right: 0;
  border: none;
  align-self: flex-end;
}

::-webkit-scrollbar {
  width: 6px;
}

::-webkit-scrollbar-thumb:hover {
  cursor: pointer;
  background-color: #727272;
}

::-webkit-scrollbar-thumb {
  background-color: #434343;
  -webkit-box-shadow: inset 0 0 6px rgba(0, 0, 0, .3);
  border-radius: 10px;
  transition: all .2s ease-in-out;
}

::-webkit-scrollbar-track {
  background-color: rgba(0, 0, 0, .9)
}
</style>
