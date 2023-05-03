<template class="dark">
    <div class="sign">
        <div class="head">
            <img alt="logo" src="/logo_dark.svg" width="90px">
            <h1>DrawVictory Team</h1>
            <p>加入官网，向我们反馈，参与到我们未来的活动中去。</p>
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
                <img :src='verificationImg' alt="验证码" class='code'>
                <button id='bto' @click="register">确定</button>
                <p id="verification_close" @click="onVerify=false">×</p>
                <a @click="verificationImg='/api/users/verify?'+new Date().getMilliseconds()">看不清？换一张</a>
            </div>
        </transition>
        <transition name="el-fade-in">
            <div v-show="isLogin" class="login_frame frame">
                <p>登录</p>
                <div class="input_box">
                    <input v-model.trim="user.username" required>
                    <span>账户名称</span>
                </div>
                <div class="input_box">
                    <input v-model.trim="user.password" required type="password">
                    <span>账户密码</span>
                </div>
                <button class="login sign_button">登录</button>
                <a @click="isLogin=!isLogin;cleanInfo();router.replace('/register')">没有账户？立即注册</a>
            </div>
        </transition>
        <transition name="el-fade-in">
            <div v-show="!isLogin" class="reg_frame frame">
                <p @click="cleanInfo">注册</p>
                <div class="input_box">
                    <input v-model.trim="user.username" autocomplete="off" required @blur="checkUsername()">
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
                    <input v-model.trim="user.email" required @blur="checkEmail">
                    <span>邮箱地址</span>
                    <p v-show="tips.email" class="tip">邮箱格式错误，请确保您的邮箱为真实邮箱</p>
                </div>
                <div class="input_box">
                    <input v-model.trim="user.password" required type="password" @blur="checkPassword">
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
                    <input v-model.trim="password" required type="password" @blur="checkPwdIsSame"
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
import {reactive, ref} from 'vue';
import {Code, RegUser, SpringObject} from '../api/model'
import axios from "../api/request";
import {ElLoading, ElMessage, ElNotification} from 'element-plus'
import {QuestionFilled} from "@element-plus/icons-vue";
import {useRoute, useRouter} from "vue-router";

const isLogin = ref(useRoute().name === 'login');//注册界面or登录界面
const user = reactive<RegUser>({username: "", password: "", email: ""});//提交的用户对象
const password = ref('');//重复输入的密码
const verificationCode = ref('');//验证码
const tips = reactive({username: false, password: false, email: false, diffPwd: false});//各个错误提示是否显示
const onVerify = ref(false);//是否打开验证码界面
const verificationImg = ref('/api/users/verify');//验证码图片url
const router = useRouter();

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
        tips[key] = false;
    }
    for (let key in user) {
        user[key] = '';
    }
    password.value = '';
    verificationCode.value = '';
}

function verify(): boolean {
    console.log(user)
    if (checkUsername() && checkEmail() && checkPassword() && checkPwdIsSame()) {
        onVerify.value = true;
        return true;
    }
    return false;
}

async function register() {
    if (verificationCode.value.length !== 4) {
        ElMessage.warning('请正确输入验证码');
        return;
    }
    const loading = ElLoading.service({
        lock: true,
        text: '注册中...',
        spinner: 'el-icon-loading',
        background: 'rgba(0, 0, 0, 0.7)'
    });
    const {data: resp} = await axios.post<SpringObject<null>>('/api/users/register/' + verificationCode.value, user);
    loading.close();
    if (resp.code === Code.REGISTER_OK) {
        console.log('test')
        cleanInfo();
        onVerify.value = false;
        ElNotification({
            title: '注册成功',
            message: resp.msg,
            type: 'success',
        });
    } else if (resp.code === Code.REGISTER_VERIFICATION_CODE_ERR) {
        verificationCode.value = '';
        verificationImg.value = '/api/users/verify?' + new Date().getMilliseconds();
        ElNotification({
            title: '注册失败',
            message: resp.msg,
            type: 'warning',
        });
    } else {
        verificationCode.value = '';
        verificationImg.value = '/api/users/verify?' + new Date().getMilliseconds();
        ElNotification({
            title: '注册失败 ' + (resp.code || 'NO STATUS CODE'),
            message: resp.msg || '未知错误<br>请稍后重试或尝试联系管理员',
            type: 'error',
        });
    }
}
</script>

<style scoped>
@import 'src/assets/styles/sign.css';
</style>