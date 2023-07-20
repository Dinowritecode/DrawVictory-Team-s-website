import {createPinia} from "pinia";
import piniaPluginPersistedstate from 'pinia-plugin-persistedstate'
import useUserStore from "./userStore";

const pinia = createPinia();
pinia.use(piniaPluginPersistedstate);

export default pinia;
export {useUserStore};