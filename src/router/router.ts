import {createRouter, createWebHistory} from 'vue-router';

const routes = [
    {
        path: '/404',
        meta: {title: '404'},
        component: () => import('../views/NotFound.vue')
    },
    {
        path: '/register',
        name: 'register',
        meta: {title: '注册'},
        component: () => import('../views/Sign.vue')
    },
    {
        path: '/login',
        name: 'login',
        meta: {title: '登录'},
        component: () => import('../views/Sign.vue')
    },
    {
        path: '/active',
        name: 'active',
        meta: {title: '激活'},
        component: () => import('../views/Active.vue')
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
router.beforeEach((to, from, next) => {
    /* 路由发生变化修改页面title */
    if (to.meta.title)
        document.title = to.meta.title.toString();
    next();
})
export default router;