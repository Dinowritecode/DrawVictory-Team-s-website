import {createApp} from 'vue'
import ElementPlus from 'element-plus'
// import VueCropper from 'vue-cropper';
// import 'vue-cropper/dist/index.css'
import 'element-plus/dist/index.css'
import App from './views/sign.vue'

const app = createApp(App);
app.use(ElementPlus);
// app.use(VueCropper);
app.mount('#app');