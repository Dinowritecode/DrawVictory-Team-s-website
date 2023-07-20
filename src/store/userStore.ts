import {defineStore} from "pinia";
import {Response, User, UserType} from "../api/model";
import axios from "../api/request";
import jwtDecode from "jwt-decode";
import {getLocalToken, removeLocalToken} from "../api/user";

async function getUser(): Promise<UserType> {
    if (getLocalToken()) {
        const token = jwtDecode<{ sub: string, exp: number }>(getLocalToken() as string);
        if (new Date(token.exp * 1000) < new Date()) {
            removeLocalToken();
            return new User();
        }
        const user: { uid: bigint } = JSON.parse(token.sub);
        const {data: resp} = await axios.get<Response<UserType>>(`/api/users/${user.uid}`);
        return resp.data as UserType;
    } else return {username: 'fuck'};
}

interface UserState {
    user: UserType,
    login: boolean
}

const useUserStore = defineStore('user', {
    state: (): UserState => ({
        user: new User(),
        login: false
    }),
    actions: {
        async fetch() {
            this.user = await getUser();
            this.login = !!this.user.uid
        }
    },
    persist: {key: 'user_store_cache', storage: sessionStorage}
})

export default useUserStore;