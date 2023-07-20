export default [
    {
        path: '/',
        name: '主页',
        meta: {title: 'DrawVictory Team｜绘赢'},
        alias: '/index',
        component: () => import('@/views/Index.vue')
    },
    {
        path: '/404',
        name: '404',
        meta: {title: '404'},
        component: () => import('@/views/NotFound.vue')
    },
    {
        path: '/register',
        name: 'register',
        meta: {title: '注册'},
        alias: '/signup',
        component: () => import('@/views/Sign.vue')
    },
    {
        path: '/login',
        name: 'login',
        meta: {title: '登录'},
        alias: '/signin',
        component: () => import('@/views/Sign.vue')
    },
    {
        path: '/activate',
        name: 'activate',
        meta: {title: '激活'},
        component: () => import('@/views/Activate.vue')
    },
    {
        path: '/test',
        name: 'test',
        meta: {
            title: 'test',
            requiresAuth: true,
            roles: ['admin']
        },
        component: () => import('@/views/Activate.vue')
    },
    {
        path: '/:pathMatcher(.*)*',
        redirect: '/404'
    }
];
