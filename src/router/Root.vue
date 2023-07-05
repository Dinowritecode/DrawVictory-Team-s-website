<script lang="ts" setup>
</script>

<template>
  <el-header class="header"><!--网页导航栏-->
    <img class="logo" src="/logo_dark.svg" alt="logo">
    <p class="name">DrawVictory Team</p>
    <p class="name2">绘赢</p>
    <el-menu mode="horizontal" class="menu" router text-color="red">
      <router-link to="/" class="link">
      <el-menu-item class="items">首页</el-menu-item>
      </router-link>
      <router-link to="/" class="link">
      <el-menu-item class="items">介绍</el-menu-item>
      </router-link>
    </el-menu>
    <el-menu class="login-button" background-color="#ff7675" router>
      <router-link to="/login" class="login-leader">
      <el-menu-item class="login-register">登录/注册</el-menu-item>
      </router-link>
    </el-menu>
  </el-header>
    <div class="root">
        <router-view></router-view>
    </div>
</template>

<style scoped>
.header{
    position: fixed;
    width: 100%;
    height: 50px;
    background-color: #24292d;
    z-index: 100;
}
.logo{
    position: absolute;
    z-index: 100;
    width: 38px;
    top: 6px;
    user-select: none;
}
.name{
    position: absolute;
    color: #ffffff;
    user-select: none;
    font-size: 15px;
    font-weight: 600;
    left: 68px;
    top: 6px;
    margin: 0;
}
.name2{
    position: absolute;
    color: #ffffff;
    user-select: none;
    font-size: 14px;
    font-weight: 600;
    left: 68px;
    top: 25px;
    margin: 0;
}
.menu{
    position: absolute;
    height: 50px;
    width: 500px;
    background-color: #24292d;
    left: 250px;
    border: none;
}
.link{
    text-decoration: none;
}
.items{
    height: 50px;
    color: white;
    user-select: none;
    text-align: center;
}
.login-button{
    position: absolute;
    width: 100px;
    height: 50px;
    right: 0;
    border: none;
}
.login-leader{
    text-decoration: none;
}
.login-register{
    height: 50px;
}
</style>