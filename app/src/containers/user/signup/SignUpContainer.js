import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import SignUp from "../../../components/user/SignUp";
import * as baseActions from 'store/modules/base';
import {notification} from "antd";
import {withRouter} from "react-router-dom";

class SignUpContainer extends Component {
    handleFinish = async (values) => {
        const {BaseActions, history} = this.props;
        console.log('values', values);

        try {
            const response = await BaseActions.signup(values);
            console.log('response', response);
            history.push('/login');
        } catch (err) {
            console.error('err', err);
            notification.error({
                message: 'Quote App',
                description: '서버 오류가 발생했습니다. 다시 시도해주세요',
            });
        }
    };

    render() {
        return (
            <SignUp onFinish={this.handleFinish}/>
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
)(withRouter(SignUpContainer));
