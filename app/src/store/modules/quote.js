import {createAction, handleActions} from 'redux-actions';
import {fromJS, List, Map} from 'immutable';
import * as api from "../../utils/api";
import {pender} from "redux-pender";
import {QUOTE_PAGE_INDEX, QUOTE_PAGE_SIZE} from "../../constants";

// action types
const GET_QUOTE_LIST = 'quote/GET_QUOTE_LIST';
const CREATE_QUOTE = 'quote/CREATE_QUOTE';
const SELECT_QUOTE = 'quote/SELECT_QUOTE';
const DELETE_QUOTE = 'quote/DELETE_QUOTE';
const EDIT_QUOTE = 'quote/EDIT_QUOTE';

// action creators
export const getQuoteList = createAction(GET_QUOTE_LIST, api.getQuoteList);
export const createQuote = createAction(CREATE_QUOTE, api.createQuote);
export const selectQuote = createAction(SELECT_QUOTE);
export const deleteQuote = createAction(DELETE_QUOTE, api.deleteQuotes);

// initial state
const initialState = Map({
    quotes: List(),
    pagination: Map({
        page: QUOTE_PAGE_INDEX,
        size: QUOTE_PAGE_SIZE,
        totalElements: 0,
        totalPages: 0,
        last: false
    }),
    selectedRowKeys: List()
});

// reducer
export default handleActions({
    [SELECT_QUOTE]: (state, action) => {
        console.log('select quote :: action.payload', action.payload);
        return state.set('selectedRowKeys', fromJS(action.payload));
    },
    ...pender({
        type: GET_QUOTE_LIST,
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
    }),
    ...pender({
        type: CREATE_QUOTE,
        onSuccess: (state, action) => {
            console.log('action.payload', action.payload);
        }
    })
}, initialState)
