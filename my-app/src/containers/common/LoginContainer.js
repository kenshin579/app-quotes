import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import Login from "components/user/Login";
import {notification} from 'antd';
import {withRouter} from "react-router-dom";
import {ACCESS_TOKEN} from "../../constants";

class LoginContainer extends Component {
    handleFinish = async (values) => {
        const {BaseActions, history} = this.props;

        try {
            const response = await BaseActions.login(values);
            localStorage.setItem(ACCESS_TOKEN, response.accessToken);
            await BaseActions.getCurrentUser();
            history.push('/');

        } catch (err) {
            console.error('err', err);
            //todo: notification 별도의 wrapper 컨포넌트 만들기
            if (err.status === 401) {
                notification.error({
                    message: 'Quote App',
                    description: '아이디나 암호가 잘못 입력되었습니다. 다시 시도해주세요!',
                    placement: 'bottomRight'
                });
            } else {
                notification.error({
                    message: 'Quote App',
                    description: '서버 오류가 발생했습니다. 다시 시도해주세요',
                    placement: 'bottomRight'
                });
            }
        }
    };

    render() {
        return (
            <Login onFinish={this.handleFinish}/>
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
)(withRouter(LoginContainer));
