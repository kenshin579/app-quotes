import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import SignUp from "../../../components/user/SignUp";
import * as baseActions from 'store/modules/base';

class SignUpContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            name: '',
            username: '',
            email: '',
            password: ''
        }
    }

    handleSubmit = (event) => {
        event.preventDefault();

        const signupRequest = {
            name: this.state.name.value,
            email: this.state.email.value,
            username: this.state.username.value,
            password: this.state.password.value
        };

        // signup(signupRequest)
        //     .then(response => {
        //         notification.success({
        //             message: 'Polling App',
        //             description: "Thank you! You're successfully registered. Please Profile to continue!",
        //         });
        //         this.props.history.push("/login");
        //     }).catch(error => {
        //     notification.error({
        //         message: 'Polling App',
        //         description: error.message || 'Sorry! Something went wrong. Please try again!'
        //     });
        // });
    };

    render() {
        return (
            <div>
                <SignUp/>
            </div>
        )
    }
}

export default connect(
    (state) => ({
        logged: state.base.get('logged')
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch)
    })
)(SignUpContainer);
