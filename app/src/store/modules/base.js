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
const UPDATE_FOLDER_MODAL_INFO = 'base/UPDATE_FOLDER_MODAL_INFO';

// action creators
export const showModal = createAction(SHOW_MODAL);
export const hideModal = createAction(HIDE_MODAL);
export const signup = createAction(SIGNUP, api.signup);
export const login = createAction(LOGIN, api.login);
export const logout = createAction(LOGOUT, api.logout);
export const getCurrentUser = createAction(GET_CURRENT_USER, api.getCurrentUser);
export const updateFolderModalInfo = createAction(UPDATE_FOLDER_MODAL_INFO);

// initial state
const initialState = Map({
    modal: Map({
        quoteCreate: false,
        quoteDelete: false,
        quoteEdit: false,
        quoteMove: false,
        folderCreate: false,
        folderRename: false,
        folderDelete: false,
    }),
    folderModal: Map({
       folderId: ''
    }),
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
    [UPDATE_FOLDER_MODAL_INFO]: (state, action) => {
        const {payload: folderId} = action;
        return state.setIn(['folderModal', 'folderId'], folderId);
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
            return state.set('authenticated', false)
                .set('user', Map({}));
        }
    }),
    ...pender({
        type: GET_CURRENT_USER,
        onSuccess: (state, action) => {
            const {id, username, name} = action.payload;
            return state.setIn(['user', 'username'], username)
                .setIn(['user', 'name'], name)
                .set('authenticated', true);
        }
    }),
}, initialState)
