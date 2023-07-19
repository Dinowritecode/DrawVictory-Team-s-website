import {Response, User, UserType} from "../api/model";
import jwtDecode from "jwt-decode";
import axios from "../api/request";
import {getLocalToken, removeLocalToken} from "../api/user";

export type State =
    {
        user: UserType
    };

const state: State =
    {
        user: new User(),
    };

init();

async function getUser(): Promise<UserType> {
    if (sessionStorage.getItem('state')) {
        return JSON.parse(sessionStorage.getItem('state') as string);
    }
    if (getLocalToken()) {
        const token = jwtDecode<{ sub: string, exp: number }>(getLocalToken() as string);
        if (new Date(token.exp * 1000) < new Date()) {
            removeLocalToken();
            return new User();
        }
        const user: { uid: bigint } = JSON.parse(token.sub);
        const {data: resp} = await axios.get<Response<UserType>>(`/api/users/${user.uid}`);
        return resp.data as UserType;
    } else return new User();
}

function init(): void {
    getUser().then(user => state.user = user);
}

export default state
