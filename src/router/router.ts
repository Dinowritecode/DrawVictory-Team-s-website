import {createRouter, createWebHistory} from 'vue-router';
import {ElMessage} from "element-plus";
import jwtDecode from "jwt-decode";
import {getLocalToken, getUserRole} from "../api/user";

const routes = [
    {
        path: '/404',
        name: '404',
        meta: {title: '404'},
        component: () => import('../views/NotFound.vue')
    },
    {
        path: '/register',
        name: 'register',
        meta: {title: '注册'},
        alias: '/signup',
        component: () => import('../views/Sign.vue')
    },
    {
        path: '/login',
        name: 'login',
        meta: {title: '登录'},
        alias: '/signin',
        component: () => import('../views/Sign.vue')
    },
    {
        path: '/activate',
        name: 'activate',
        meta: {title: '激活'},
        component: () => import('../views/Activate.vue')
    },
    {
        path: '/test',
        name: 'test',
        meta: {
            title: 'test',
            requiresAuth: true,
            roles: ['admin']
        },
        component: () => import('../views/Activate.vue')
    },
    {
        path: '/:pathMatcher(.*)*',
        redirect: '/404'
    }
];
const router = createRouter({
    history: createWebHistory(),
    routes: routes
});
//router设置页面标题
router.beforeEach(async (to, from, next) => {
    const token = getLocalToken();
    const roles = to.meta.roles as string[];
    if (to.meta.requiresAuth) { // 需要登录验证
        if (!token || jwtDecode<{ exp: number }>(token).exp * 1000 < Date.now()) { // 用户未经登录
            ElMessage.warning('您暂未登录或登录已过期，已为您跳转到登录页面');
            next('/login');
        } else { // 用户已登录
            if (roles.includes(await getUserRole())) { // 用户角色不匹配
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
export default router;