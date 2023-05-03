import axios from "axios";
import {Code, Response} from "./model";

const _axios = axios.create({});
_axios.interceptors.response.use(
    config => {
        return config
    },
    error => {
        const response: Response = {
            code: Code.UNKNOWN_ERR,
            data: null,
            msg: '请重试'
        }
        return response;
    }
)

export default _axios;