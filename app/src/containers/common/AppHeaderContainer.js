import React, {Component} from 'react';
import {connect} from 'react-redux';
import AppHeader from "../../components/common/AppHeader";
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import {withRouter} from "react-router-dom";
import {ACCESS_TOKEN} from "../../constants";

class AppHeaderContainer extends Component {
    handleClickProfileMenu = async ({key}) => {
        const {BaseActions, isAuthenticated, history} = this.props;

        if (key === 'logout' && isAuthenticated) {
            try {
                await BaseActions.logout();
            } catch (err) {
                console.error('err', err);
            }
            localStorage.removeItem(ACCESS_TOKEN);
            history.push('/');
        }
    };

    render() {
        const {handleClickProfileMenu} = this;
        const {isAuthenticated, currentUser} = this.props;

        return (
            <AppHeader
                isAuthenticated={isAuthenticated}
                currentUser={currentUser}
                onClick={handleClickProfileMenu}
            />
        )
    }
}

export default connect(
    (state) => ({
        isAuthenticated: state.base.get('authenticated'),
        currentUser: state.base.get('user').toJS()
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch)
    })
)(withRouter(AppHeaderContainer));
