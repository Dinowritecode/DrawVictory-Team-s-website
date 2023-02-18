<template>
  <div class="head">
    <img src="/logo.svg" width="90px" alt="logo">
    <h1>DrawVictory Team</h1>
    <p>加入官网，向我们反馈，参与到我们未来的活动中去。</p>
  </div>
  <div id="login_frame" v-show="!isReg">
    <p>登录</p>
    <div id="input_box">
      <input type="text" required v-model.trim="user.username">
      <span>账户名称</span>
    </div>
    <div id="input_box">
      <input type="password" required v-model.trim="user.password">
      <span>账户密码</span>
    </div>
    <button class="login sign_button">登录</button>
    <a @click="isReg=!isReg;cleanInfo()">没有账户？立即注册</a>
  </div>
  <div id="reg_frame" v-show="isReg">
    <p @click="cleanInfo">注册</p>
    <div id="input_box">
      <input type="text" required v-model.trim="user.username" @blur="checkUsername()">
      <span>账户名称</span>
      <p class="tip" v-show="tips.username">用户名格式错误
        <img class="help" src="/help.svg" alt="帮助"
             @click="ElMessage('用户名只允许包含数字,大小写字母,下划线和连词线,且长度在4-16位之间')">
      </p>
    </div>
    <div id="input_box">
      <input type="text" required v-model.trim="user.email" @blur="checkEmail">
      <span>邮箱地址</span>
      <p class="tip" v-show="tips.email">邮箱格式错误，请确保您的邮箱为真实邮箱</p>
    </div>
    <div id="input_box">
      <input type="password" required v-model.trim="user.password" @blur="checkPwd">
      <span>注册密码</span>
      <p class="tip" v-show="tips.password">密码格式错误
        <img class="help" src="/help.svg" alt="帮助"
             @click="ElMessage('密码需包含字母,符号或者数字中至少两项,且长度在6-16位之间')">
      </p>
    </div>
    <div id="input_box">
      <input type="password" required v-model.trim="password" @blur="checkPwdIsSame">
      <span>确认密码</span>
      <p class="tip" v-show="tips.diffPwd">两次密码不一致</p>
    </div>
    <button class="reg sign_button">注册</button>
    <a @click="isReg=!isReg;cleanInfo()">已有账户？立即登录</a>
  </div>
</template>

<script setup lang="ts">
import {reactive, ref} from 'vue';
import {RegUser} from '../api/module'
import axios from "../api/request";
import {ElMessage} from 'element-plus'
import {useDark} from '@vueuse/core'
import 'element-plus/theme-chalk/dark/css-vars.css'

useDark();
const isReg = ref(false);//注册界面or登录界面
const user = reactive<RegUser>({username: "", password: "", email: ""});//提交的用户对象
const password = ref('');//重复输入的密码
const verCode = ref('');//验证码
const tips = reactive(
    {username: false, password: false, email: false, diffPwd: false});//控制错误提示是否显示

function checkUsername(): boolean {
  if (!/^\d{15,17}$/.test(user.username)) {
    tips.username = true;
    return false
  }
  tips.username = false;
  return true
}

function checkPwd(): boolean {
  if (!/^(?!\d+$)(?!^[a-zA-Z]+$)(?!^[_#@]+$).{6,16}$/.test(user.password)) {
    tips.password = true;
    return false
  }
  tips.password = false;
  return true
}

function checkEmail(): boolean {
  if (!/^[a-zA-Z0-9_.-]+@[a-zA-Z0-9-]+(\.[a-zA-Z0-9-]+)*\.[a-zA-Z0-9]{2,6}$/.test(user.email)) {
    tips.email = true;
    return false
  }
  tips.password = false;
  return true
}

function checkPwdIsSame(): boolean {
  if (password.value === '' || user.password !== password.value) {
    tips.diffPwd = true;
    return false
  }
  tips.diffPwd = false;
  return true
}

function cleanInfo(): void {
  for (let key in tips) {
    tips[key] = '';
  }
  for (let key in user) {
    console.log(user[key])
    user[key] = '';
    console.log(user[key])
  }
  password.value = '';
  verCode.value = '';
}

function register(): void {
  if (checkUsername() || checkEmail() || checkPwd() || checkPwdIsSame() || verCode.value === '')
    return
}
</script>

<style>
@import '../assets/css/sign.css';
</style>