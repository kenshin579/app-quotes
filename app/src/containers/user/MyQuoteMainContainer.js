import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import MyQuoteMain from "../../components/user/mypage/MyQuoteMain";


class MyQuoteMainContainer extends Component {

    render() {
        const {authenticated} = this.props;

        return (
            <MyQuoteMain authenticated={authenticated}/>
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
)(MyQuoteMainContainer);
