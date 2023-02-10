import axios from "axios";
import {Response} from "./module";

const _axios = axios.create({});
_axios.interceptors.response.use(
    config => {
        return config
    },
    error => {
        const response: Response<null> = {
            code: 9999,
            data: null,
            msg: '请重试'
        }
        return response;
    }
)

export default _axios;