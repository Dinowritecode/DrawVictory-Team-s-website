<template>
  <div class='msg'>{{ msg }}<br><span v-show="isSuccess">接下来可完成账户初始化操作</span></div>
  <button class='btn' @click="starting=true" v-show="isSuccess">START</button>
  <transition name="el-fade-in">
    <div id="back" v-if="starting"></div>
  </transition>
  <transition name="el-fade-in">
    <div id="setting" v-if="starting">
      <p @click="starting=false">×</p>

    </div>
  </transition>
</template>

<script setup lang="ts">
import {ref} from 'vue';
import axios from "../api/request";
import {Code, SpringObject} from "../api/model";

const msg = ref('激活中..');
const isSuccess = ref(false);//是否激活成功
const starting = ref(false)//是否打开starting界面
const token: string = location.search.substring(1);

if (token === '')
  msg.value = '请勿使用空激活码';
else active();

async function active() {
  const {data: resp} = await axios.get<SpringObject<null>>('/api/users/active/' + token);
  msg.value = resp.msg;
  if (resp.code === Code.ACTIVE_OK) {
    isSuccess.value = true;
  }
}
</script>

<style>
* {
  padding: 0;
  margin: 0;
  border: 0;
}

body {
  background-color: #24292d;
}

.btn {
  position: absolute;
  background-color: #24292d;
  color: #ff7675;
  border: 1px solid #ff7675;
  padding: 6px 10px;
  border-radius: 5px;
  letter-spacing: 1px;
  outline: none;
  height: 35px;
  width: 165px;
  top: 58%;
  right: 0;
  left: 0;
  margin: auto;
  cursor: pointer;
}

.btn:hover {
  background-color: #ff7675;
  color: #fff;
  transition: color 0.15s ease-in-out;
}

#back {
  width: 100%;
  height: 100%;
  background-color: rgba(0, 0, 0, 0.3);
  position: absolute;
  top: 0;
  left: 0;
  z-index: 10;
}

.msg {
  width: 260px;
  color: white;
  font-size: 20px;
  position: absolute;
  left: 0;
  top: 49%;
  right: 0;
  bottom: 0;
  margin: auto;
  text-align: center;
}

#setting {
  position: absolute;
  width: 600px;
  height: 450px;
  margin: auto;
  left: 0;
  right: 0;
  top: 0;
  bottom: 0;
  background-color: white;
  z-index: 11;
  border-radius: 8px;
}

#setting > p {
  position: absolute;
  right: 10px;
  top: -12px;
  height: 0;
  width: 20px;
  font-size: 40px;
  font-weight: 500;
  color: rgba(0, 0, 0, 0.3);
  cursor: pointer;
  z-index: 2;
}
</style>