import {createAction, handleActions} from 'redux-actions';
import {Map} from 'immutable';
import * as api from "../../utils/api";
import {pender} from "redux-pender";

// action types
const SHOW_MODAL = 'base/SHOW_MODAL';
const HIDE_MODAL = 'base/HIDE_MODAL';
const LOGIN = 'base/LOGIN';
const LOGOUT = 'base/LOGOUT';

// action creators
export const showModal = createAction(SHOW_MODAL);
export const hideModal = createAction(HIDE_MODAL);
export const login = createAction(LOGIN, api.login);
export const logout = createAction(LOGOUT, api.logout);

// initial state
const initialState = Map({
    modal: Map({
    }),
    authenticated: false,
    user: Map({
        name : '',
        username: '',
        email: ''
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
}, initialState)
