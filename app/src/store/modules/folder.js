import {createAction, handleActions} from 'redux-actions';
import {fromJS, List, Map} from 'immutable';
import * as api from "../../utils/api";
import {pender} from "redux-pender";

// action types
const GET_FOLDER_LIST = 'folder/GET_FOLDER_LIST';
const CREATE_FOLDER = 'folder/CREATE_FOLDER';
const RENAME_FOLDER = 'folder/RENAME_FOLDER';
const DELETE_FOLDER = 'folder/DELETE_FOLDER';

// action creators
export const getFolderList = createAction(GET_FOLDER_LIST, api.getFolderList);
export const createFolder = createAction(CREATE_FOLDER, api.createFolder);
export const renameFolder = createAction(RENAME_FOLDER, api.renameFolder);
export const deleteFolder = createAction(DELETE_FOLDER, api.deleteFolder);

// initial state
const initialState = Map({
    folders: List(),
    folderStatInfo: Map({
        totalNumOfQuotes: 0,
        totalNumOfLikes: 0
    })
});

// reducer
export default handleActions({
    ...pender({
        type: GET_FOLDER_LIST,
        onSuccess: (state, action) => {
            const {folderList, folderStatInfo} = action.payload;
            return state.set('folders', fromJS(folderList))
                .set('folderStatInfo', fromJS(folderStatInfo));
        }
    }),
    ...pender({
        type: CREATE_FOLDER,
        onSuccess: (state, action) => {
            console.log('successfully created a folder :: action.payload', action.payload);
        }
    }),
    ...pender({
        type: RENAME_FOLDER,
        onSuccess: (state, action) => {
            console.log('successfully renamed a folder :: action.payload', action.payload);
        }
    }),
    ...pender({
        type: DELETE_FOLDER,
        onSuccess: (state, action) => {
            console.log('successfully deleted a folder :: action.payload', action.payload);
        }
    })
}, initialState)
