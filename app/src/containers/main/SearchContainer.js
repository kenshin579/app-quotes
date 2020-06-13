import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import SearchQuote from "components/main/SearchQuote";

//todo: 현재는 사용하지 않음
class SearchContainer extends Component {

    render() {
        return (
            <SearchQuote totalQuote={23524}/>
        )
    }
}

export default connect(
    (state) => ({
        authenticated: state.base.get('authenticated')
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch)
    })
)(SearchContainer);
