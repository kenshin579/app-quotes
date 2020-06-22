import {createAction, handleActions} from 'redux-actions';
import {fromJS, List, Map} from 'immutable';
import * as api from "utils/api";
import {pender} from "redux-pender";
import {QUOTE_PAGE_INDEX, QUOTE_PAGE_SIZE} from "../../constants";

// action types
const GET_QUOTE_LIST = 'quote/GET_QUOTE_LIST';
const CREATE_QUOTE = 'quote/CREATE_QUOTE';
const SELECT_QUOTE = 'quote/SELECT_QUOTE';
const INIT_QUOTE_SELECTION = 'quote/INIT_QUOTE_SELECTION';
const GET_QUOTE = 'quote/GET_QUOTE';
const DELETE_QUOTE = 'quote/DELETE_QUOTE';
const EDIT_QUOTE = 'quote/EDIT_QUOTE';
const MOVE_QUOTE = 'quote/MOVE_QUOTE';

// action creators
export const getQuoteList = createAction(GET_QUOTE_LIST, api.getQuoteList);
export const createQuote = createAction(CREATE_QUOTE, api.createQuote);
export const getQuote = createAction(GET_QUOTE, api.getQuote);
export const selectQuote = createAction(SELECT_QUOTE);
export const initQuoteSelection = createAction(INIT_QUOTE_SELECTION);
export const editQuote = createAction(EDIT_QUOTE, api.editQuote);
export const deleteQuotes = createAction(DELETE_QUOTE, api.deleteQuotes);
export const moveQuotes = createAction(MOVE_QUOTE, api.moveQuotes);

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
        return state.set('selectedRowKeys', fromJS(action.payload));
    },
    [INIT_QUOTE_SELECTION]: (state, action) => {
        return state.set('INIT_QUOTE_SELECTION', List());
    },
    ...pender({
        type: GET_QUOTE_LIST,
        onSuccess: (state, action) => {
            const data = action.payload;
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
            console.log('successfully created a quote :: action.payload', action.payload);
        }
    }),
    ...pender({
        type: GET_QUOTE,
        onSuccess: (state, action) => {
            console.log('successfully getting a quote :: action.payload', action.payload);
        }
    }),
    ...pender({
        type: GET_QUOTE,
        onSuccess: (state, action) => {
            console.log('successfully edited a quote :: action.payload', action.payload);
        }
    }),
    ...pender({
        type: MOVE_QUOTE,
        onSuccess: (state, action) => {
            console.log('successfully moved quotes :: action.payload', action.payload);
        }
    })
}, initialState)
