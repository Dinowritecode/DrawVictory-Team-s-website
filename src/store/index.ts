import {createStore} from 'vuex'
import {Response, User, UserType} from "../api/model";
import jwtDecode from "jwt-decode";
import axios from "../api/request";
import {getLocalToken, removeLocalToken} from "../api/user";

type State =
    {
        user: UserType
    }

const state: State = null != sessionStorage.getItem('state') ?
    JSON.parse(sessionStorage.getItem('state') as string) :
    {
        user: await getUser(),
    }

async function getUser(): Promise<UserType> {
    console.log(123)
    if (getLocalToken()) {
        const token = jwtDecode<{ sub: string , exp:number}>(getLocalToken() as string);
        if (new Date(token.exp * 1000) < new Date()) {
            removeLocalToken();
            return new User();
        }
        const user: { uid: bigint } = JSON.parse(token.sub);
        const {data: resp} = await axios.get<Response<UserType>>(`/api/users/${user.uid}`);
        return resp.data as UserType;
    } else return new User();
}

const getters = {
    getUser(state: State): UserType {
        return state.user;
    }
}

export default createStore({
    state,
    getters
});
