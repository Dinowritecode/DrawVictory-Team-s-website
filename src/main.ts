import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import {createApp} from "vue";
import App from "./Application.vue";
import router from "./router";
import {createDeviceDetector} from 'next-vue-device-detector';
import store from "./store";
import 'nprogress/nprogress.css'

createApp(App)
    .use(ElementPlus)
    .use(router)
    .use(createDeviceDetector())
    .use(store)
    .mount('#app');