import {createStore} from 'vuex'
import {UserType} from "../api/model";
import state, {State} from './state'

const getters = {
    getUser(state: State): UserType {
        return state.user;
    }
}

export default createStore({
    state,
    getters
});
