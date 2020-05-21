import {createAction, handleActions} from 'redux-actions';
import {fromJS, List, Map} from 'immutable';
import * as api from "../../utils/api";
import {pender} from "redux-pender";

// action types
const GET_FOLDER_LIST = 'folder/GET_FOLDER_LIST';

// action creators
export const getFolderList = createAction(GET_FOLDER_LIST, api.getFolderList);

// initial state
const initialState = Map({
    folders: List()
});

// reducer
export default handleActions({
    ...pender({
        type: GET_FOLDER_LIST,
        onSuccess: (state, action) => {
            return state.set('folders', fromJS(action.payload));
        }
    })
}, initialState)
