import {createAction, handleActions} from 'redux-actions';
import {fromJS, List, Map} from 'immutable';
import * as api from "../../utils/api";
import {pender} from "redux-pender";
import {QUOTE_PAGE_INDEX, QUOTE_PAGE_SIZE, QUOTE_TODAY_INDEX, QUOTE_TODAY_SIZE} from "../../constants";

// action types
const GET_RANDOM_LIST = 'main/GET_RANDOM_LIST';

// action creators
export const getRandomList = createAction(GET_RANDOM_LIST, api.getRandomList);

// initial state
const initialState = Map({
    quotes: List(),
    pagination: Map({
        page: QUOTE_TODAY_INDEX,
        size: QUOTE_TODAY_SIZE,
        totalElements: 0,
        totalPages: 0,
        last: false
    }),
});

// reducer
export default handleActions({
    ...pender({
        type: GET_RANDOM_LIST,
        onSuccess: (state, action) => {
            const data = action.payload;
            console.log('quote module :: data', data);
            return state.set('quotes', fromJS(data.content))
                .setIn(['pagination', 'page'], parseInt(data.page, 10))
                .setIn(['pagination', 'size'], parseInt(data.size, 10))
                .setIn(['pagination', 'totalElements'], parseInt(data.totalElements, 10))
                .setIn(['pagination', 'totalPages'], parseInt(data.totalPages, 10))
                .setIn(['pagination', 'last'], data.last);
        }
    })
}, initialState)
