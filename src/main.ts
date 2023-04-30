/*
import {createApp} from 'vue'
import ElementPlus from 'element-plus'
import VueRouter from 'vue-router'
// import VueCropper from 'vue-cropper';
// import 'vue-cropper/dist/index.css'
import 'element-plus/dist/index.css'
import App from './views/Sign.vue'

const app = createApp(App);
app.use(ElementPlus);
// app.use(VueCropper);
app.mount('#app');*/

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import {createApp} from "vue";
import Root from "./router/Root.vue";
import router from "./router/router";

createApp(Root).use(ElementPlus).use(router).mount('#app');