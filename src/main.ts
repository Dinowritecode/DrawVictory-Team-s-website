import ElementPlus from 'element-plus';
import 'element-plus/dist/index.css';
import {createApp} from "vue";
import App from "./Application.vue";
import router from "./router";
import {createDeviceDetector} from 'next-vue-device-detector';
import 'nprogress/nprogress.css'
import pinia, {useUserStore} from './store';

createApp(App)
    .use(ElementPlus, {size: 'default'})
    .use(router)
    .use(createDeviceDetector())
    .use(pinia)
    .mount('#app');
if (!sessionStorage.getItem('user_store_cache'))
    useUserStore().fetch().then();