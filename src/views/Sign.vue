<template class="dark">
  <div class="sign">
    <div class="head">
      <img alt="logo" src="/logo_dark.svg" width="90px">
      <h1>DrawVictory Team</h1>
      <p>加入官网，向我们反馈，参与到我们未来的活动中去</p>
    </div>
    <transition name="el-fade-in">
      <div v-if="onVerify" id="mask"></div>
    </transition>
    <transition name="el-fade-in">
      <div v-if="onVerify" id="verification_frame" @keydown.esc="onVerify=false">
        <h1>|人机验证</h1>
        <p id='verification_desc'>
          为什么要进行人机验证？我们只是想确认您是否是机器人，避免向服务器发送重复的请求</p>
        <input v-model.trim="verificationCode" placeholder='请输入验证码'
               @keydown.enter="register">
        <img :src='verificationImg' alt="正在加载中...">
        <button id='bto' @click="register">确定</button>
        <p id="verification_close" @click="onVerify=false">×</p>
        <a @click="changeVerificationImg">看不清？换一张</a>
      </div>
    </transition>
    <transition name="el-fade-in">
      <div v-show="isLogin" class="login_frame frame">
        <p>登录</p>
        <div class="input_box">
          <input v-model.trim="user.username" autocomplete="username" required>
          <span>账户名称</span>
        </div>
        <div class="input_box">
          <input v-model.trim="user.password" autocomplete="current-password" required type="password"
                 @keydown.enter="login">
          <span>账户密码</span>
        </div>
        <el-checkbox text-color="#ff7675">保持登录状态</el-checkbox>
        <button class="login sign_button" @click="login">登录</button>
        <a @click="isLogin=!isLogin;cleanInfo();router.replace('/register')">没有账户？立即注册</a>
      </div>
    </transition>
    <transition name="el-fade-in">
      <div v-show="!isLogin" class="reg_frame frame">
        <p @click="cleanInfo">注册</p>
        <div class="input_box">
          <input v-model.trim="user.username" autocomplete="username" required @blur="checkUsername()">
          <span>账户名称</span>
          <p v-show="tips.username" class="tip">用户名格式错误
            <el-tooltip
              class="box-item"
              content="用户名只允许包含数字，大小写字母，下划线和连词线，且长度在4-16位之间"
              effect="dark"
              placement="right"
            >
              <el-icon color="grey">
                <QuestionFilled/>
              </el-icon>
            </el-tooltip>
          </p>
        </div>
        <div class="input_box">
          <input v-model.trim="user.email" autocomplete="email" required @blur="checkEmail">
          <span>邮箱地址</span>
          <p v-show="tips.email" class="tip">邮箱格式错误，请确保您的邮箱为真实邮箱</p>
        </div>
        <div class="input_box">
          <input v-model.trim="user.password" autocomplete="new-password" required type="password"
                 @blur="checkPassword">
          <span>注册密码</span>
          <p v-show="tips.password" class="tip">密码格式错误
            <el-tooltip
              class="box-item"
              content="密码需包含字母，符号或者数字中至少两项，且长度在6-16位之间"
              effect="dark"
              placement="right"
            >
              <el-icon color="grey">
                <QuestionFilled/>
              </el-icon>
            </el-tooltip>
          </p>
        </div>
        <div class="input_box">
          <input v-model.trim="confirmPassword" required type="password" @blur="checkPwdIsSame"
                 @keydown.enter="verify">
          <span>确认密码</span>
          <p v-show="tips.diffPwd" class="tip">两次密码不一致</p>
        </div>
        <button class="reg sign_button" @click="verify">注册</button>
        <a @click="isLogin=!isLogin;cleanInfo();router.replace('/login')">已有账户？立即登录</a>
      </div>
    </transition>
  </div>
</template>

<script lang="ts" setup>
import {Response} from '../api/model'
import axios from "../api/request";
import {ElMessage, ElNotification} from 'element-plus'
import {QuestionFilled} from "@element-plus/icons-vue";
import {useRoute, useRouter} from "vue-router";
import NProgress from 'nprogress';

const isLogin = ref(useRoute().name === 'login');//注册界面or登录界面
const user = reactive<{ username: string, password: string, email: string }>(
  {username: "", password: "", email: ""});//提交的用户对象
const confirmPassword = ref('');//重复输入的密码
const verificationCode = ref('');//验证码
const verificationId = ref('');//验证id
const verificationImg = ref('');//验证码图片
const tips = reactive({username: false, password: false, email: false, diffPwd: false});//各个错误提示是否显示
const onVerify = ref(false);//是否打开验证码界面
const router = useRouter();

function changeVerificationImg(): void {
  axios.get('/api/users/verify?' + new Date().getMilliseconds(), {responseType: 'blob'}).then(
    resp => {
      verificationId.value = resp.headers['verification-id'];
      verificationImg.value = URL.createObjectURL(new Blob([resp.data], {type: 'image/png'}));
    }
  )
}

function checkUsername(): boolean {
  if (!/^[\w-]{4,16}$/.test(user.username)) {
    tips.username = true;
    return false
  }
  tips.username = false;
  return true
}

function checkPassword(): boolean {
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
  tips.email = false;
  return true
}

function checkPwdIsSame(): boolean {
  if (confirmPassword.value === '' || user.password !== confirmPassword.value) {
    tips.diffPwd = true;
    return false
  }
  tips.diffPwd = false;
  return true
}

function cleanInfo(): void {
  for (let key in tips) {
    tips[key] = false;
  }
  for (let key in user) {
    user[key] = '';
  }
  confirmPassword.value = '';
  verificationCode.value = '';
}

function verify(): boolean {
  if (+checkUsername() & +checkEmail() & +checkPassword() & +checkPwdIsSame()) {
    onVerify.value = true;
    changeVerificationImg();
    return true;
  }
  return false;
}

async function register() {
  if (verificationCode.value.length !== 4) {
    ElMessage.warning('请正确输入验证码');
    return;
  }
  NProgress.start();
  const {data: resp} = await axios.post<Response>('/api/users/register', user, {
    params: {
      verificationCode: verificationCode.value,
      verificationId: verificationId.value
    }
  });
  NProgress.done();
  if (resp.success) {
    onVerify.value = false;
    ElNotification({
      title: '注册成功',
      message: resp.msg,
      type: 'success',
    });
  } else {
    verificationCode.value = '';
    onVerify.value = false;
    ElNotification({
      title: '注册失败',
      message: resp.msg,
      type: 'warning',
    });
  }
}

async function login() {
  if (user.username == '' || user.password == '') {
    ElMessage.warning("用户名或密码不能为空");
    return;
  }
  NProgress.start();
  const {data: resp} = await axios.post<Response>('/api/auth/login', user);
  NProgress.done();
  if (resp.success) {
    router.push('/').then(() => ElMessage.success(resp.msg));
  } else {
    ElMessage.warning(resp.msg);
  }
}
</script>

<style lang="less" scoped>
@import '@/assets/styles/sign.less' screen and (min-width: 768px);
</style>