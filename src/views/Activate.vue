<template>
  <div class="activate">
    <div class='msg'>{{ msg }}<br><span v-show="isSuccess">接下来可完成账户初始化操作</span></div>
    <button v-show="isSuccess" class='btn' @click="starting=true">START</button>
    <transition name="el-fade-in">
      <div v-if="starting" id="back"></div>
    </transition>
    <transition name="el-fade-in">
      <div v-if="starting" id="setting">
        <p @click="starting=false">×</p>
        <el-button @click="avatarConfigVisible=true">上传头像</el-button>
        <CustomCropper
          :visible.sync="avatarConfigVisible"
          @closeAvatarDialog="closeAvatarDialog" @cancel="avatarConfigVisible=false"
        ></CustomCropper>
      </div>
    </transition>
  </div>
</template>

<script lang="ts" setup>
import axios from "../api/request";
import {Code, Response} from "../api/model";
import CustomCropper from "../components/CustomCropper.vue";

const msg = ref('激活中..');
// const isSuccess = ref(false);//是否激活成功
const isSuccess = ref(true);//是否激活成功
const starting = ref(false)//是否打开starting界面
const avatarConfigVisible = ref(false);
const token: string = location.search.substring(1);

if (token === '')
  msg.value = '请勿使用空激活码';
else activate();

async function activate() {
  const {data: resp} = await axios.post<Response>('/api/users/activate/' + token);
  msg.value = resp.msg ?? '激活失败';
  if (resp.code === Code.ACTIVATE_OK) {
    isSuccess.value = true;
  }
}

function closeAvatarDialog(data) {//data为裁剪后图片的blob数据
  console.log(data)
  avatarConfigVisible.value = false;
}
</script>

<style scoped>
* {
  padding: 0;
  margin: 0;
  border: 0;
}

.activate {
  height: 100%;
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
  overflow: hidden;
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
