<template>
  <div class='p1' id='msg' @click="console.log('click')">{{ msg }}</div>
  <button class='btn' @click="starting=true" v-show="flag">START</button>
  <div id="back" v-show="starting"></div>
  <div id="setting" v-show="starting">
    <p @click="starting=false">×</p>
    <el-upload
        class="upload"
        drag
        action=""
        :before-upload="beforeAvatarUpload"
        :on-change="onChangeUpload"
    >
      <el-icon class="el-icon--upload">
        <upload-filled/>
      </el-icon>
      <div class="el-upload__text">
        将文件拖到此处，或<em>点击上传</em>
      </div>
      <template #tip>
        <div class="el-upload__tip">
          只能上传jpg/png文件，且不超过500kb
        </div>
      </template>
    </el-upload>
    <div class='box'></div>
  </div>
</template>

<script setup lang="ts">
import {computed, ref} from 'vue';
import axios from "../api/request";
import {ElLoading, ElMessage} from "element-plus";
import {useRequest} from 'vue-request'
import {AxiosRespObject, Code, SpringObjectError, User} from "../api/module";
// noinspection TypeScriptCheckImport
import {UploadFilled} from '@element-plus/icons-vue'

const msg = ref('');
const flag = ref(false);//是否激活成功
const starting = ref(false)//是否打开starting界面

const token: string = location.search.substring(1);
if (token === '')
  msg.value = '请勿使用空激活码';
else active();

function active() {
  const loading = ElLoading.service({//加载动画
    lock: true,
    text: '正在激活...',
    spinner: 'el-icon-loading',
    background: 'rgba(0, 0, 0, 0.3)'
  });
  const {data} = useRequest<AxiosRespObject<User>>(() => axios.get('/api/users/active/' + token))
  const resp = computed(() => data.value?.data || SpringObjectError)
  loading.close();
  msg.value = resp.value.msg;
  if (resp.value?.code === Code.ACTIVE_OK)
    flag.value = true;
}

function onChangeUpload(file: File) {

}

function beforeAvatarUpload(file: File) {
  const isJPG = file.type === 'image/jpeg';
  const isPNG = file.type === 'image/png';
  const isLt500K = file.size >> 30 < 500;

  if (!isJPG && !isPNG) {
    ElMessage.error('上传头像图片只能是 JPG/PNG 格式!');
  }
  if (!isLt500K) {
    ElMessage.error('上传头像图片大小不能超过 500KB!');
  }
  return (isJPG || isPNG) && isLt500K;
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

#msg {
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

.upload {
  margin: 3% auto auto;
  width: 80%;
}

.el-upload__tip {
  text-align: center;
}
</style>