import {createRouter, createWebHistory} from 'vue-router';
import {ElMessage} from "element-plus";
import jwtDecode from "jwt-decode";
import {getLocalToken, getUserRole} from "../api/user";
import NProgress from "nprogress";
import routes from "./routes";

const router = createRouter({
    history: createWebHistory(),
    routes: routes
});
//router设置页面标题
router.beforeEach(async (to, from, next) => {
    NProgress.start();
    const token = getLocalToken();
    const roles = to.meta.roles as string[];
    if (to.meta.requiresAuth) { // 需要登录验证
        if (!token || jwtDecode<{ exp: number }>(token).exp * 1000 < Date.now()) { // 用户未经登录
            next('/login');
            ElMessage.warning('您暂未登录或登录已过期，已为您跳转到登录页面');
        } else { // 用户已登录
            if (roles.includes(await getUserRole())) { // 用户角色匹配
                next();
            } else {
                next('/403');
            }
        }
    } else { // 不需要登录验证
        if (to.meta.title)
            document.title = to.meta.title.toString();
        next()
    }
})
router.afterEach(() => {
    NProgress.done();
})
export default router;