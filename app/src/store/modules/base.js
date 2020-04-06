import {createAction, handleActions} from 'redux-actions';
import {Map} from 'immutable';
import * as api from "../../utils/api";
import {pender} from "redux-pender";

// action types
const SHOW_MODAL = 'base/SHOW_MODAL';
const HIDE_MODAL = 'base/HIDE_MODAL';
const SIGNUP = 'base/SIGNUP';
const LOGIN = 'base/LOGIN';
const LOGOUT = 'base/LOGOUT';
const GET_CURRENT_USER = 'base/GET_CURRENT_USER';

// action creators
export const showModal = createAction(SHOW_MODAL);
export const hideModal = createAction(HIDE_MODAL);
export const signup = createAction(SIGNUP, api.signup);
export const login = createAction(LOGIN, api.login);
export const logout = createAction(LOGOUT, api.logout);
export const getCurrentUser = createAction(GET_CURRENT_USER, api.getCurrentUser);

// initial state
const initialState = Map({
    modal: Map({}),
    authenticated: false,
    user: Map({
        name: '',
        username: ''
    })
});

// reducer
export default handleActions({
    [SHOW_MODAL]: (state, action) => {
        const {payload: modalName} = action;
        return state.setIn(['modal', modalName], true);
    },
    [HIDE_MODAL]: (state, action) => {
        const {payload: modalName} = action;
        return state.setIn(['modal', modalName], false);
    },
    ...pender({
        type: LOGIN,
        onSuccess: (state, action) => {  // 로그인 성공 시
            return state.set('authenticated', true);
        }
    }),
    ...pender({
        type: LOGOUT,
        onSuccess: (state, action) => {
            console.log('action.payload', action.payload);
            return state.set('authenticated', false)
                .set('user', Map({}));
        }
    }),
    ...pender({
        type: GET_CURRENT_USER,
        onSuccess: (state, action) => {
            const {id, username, name} = action.payload;
            return state.setIn(['user', 'username'], username)
                .setIn(['user', 'name'], name);
        }
    }),
}, initialState)
