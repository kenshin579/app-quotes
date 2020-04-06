import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import {withRouter} from "react-router-dom";
import Profile from "../../../components/user/Profile";


class ProfileContainer extends Component {

    render() {
        return (
            <Profile/>
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
)(withRouter(ProfileContainer));
