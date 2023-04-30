// import VueCropper from 'vue-cropper';
// import 'vue-cropper/dist/index.css'

import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import {createApp} from "vue";
import Root from "./router/Root.vue";
import router from "./router/router";

createApp(Root).use(ElementPlus).use(router).mount('#app');