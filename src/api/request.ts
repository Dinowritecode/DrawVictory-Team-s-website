import axios from "axios";
import {useRouter} from "vue-router";
import {ElMessage} from "element-plus";
import {Code} from "./model";

const _axios = axios.create({
})
_axios.interceptors.response.use(
    response => {
        const token: string = response.headers['authorization']
        if (token && token.startsWith('Bearer '))
            localStorage.setItem('token', token.substring(7))
        return response;
    },
    error => {
        switch (error.response.status) {
            case 403:
                if (error.response.data.code === Code.AUTH_ANONYMOUS_ERR) {
                    useRouter().push('/login').then(() => {
                        ElMessage.warning("您尚未登录，已为您跳转到登录页面");
                        return Promise.resolve(error.response)
                    })
                }
                return Promise.resolve(error.response);
            case 404:
                ElMessage.error("该资源不存在，若有疑问请联系管理员");
                return Promise.reject(error);
            case 500:
                ElMessage.error('服务器内部错误，若有疑问请联系管理员');
                return Promise.reject(error);
        }
        return Promise.reject(error);
    }
)
_axios.interceptors.request.use(
    config => {
        const token = localStorage.getItem('token');
        if (token) config.headers.Authorization = `Bearer ${token}`;
        return config;
    }
)

export default _axios;