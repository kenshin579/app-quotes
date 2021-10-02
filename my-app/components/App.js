import React, {Component} from 'react';
import {Route, Switch, withRouter} from 'react-router-dom';
import {
    LoginPage,
    MainPage,
    MyQuoteMainPage,
    MyQuotePage,
    NotFoundPage,
    SettingPage,
    SignUpPage,
    TestPage
} from 'pages';
import BaseContainer from 'containers/common/BaseContainer';
import AuthenticatedRoute from 'components/common/AuthenticatedRoute';
import {connect} from "react-redux";
import {bindActionCreators} from "redux";
import * as baseActions from "store/modules/base";
import LoadingIndicator from "components/common/LoadingIndicator";

class App extends Component {
    componentDidMount() {
        this.getCurrentUser();
    }

    getCurrentUser = async () => {
        const {BaseActions} = this.props;

        try {
            const response = await BaseActions.getCurrentUser();
            console.log('response', response);
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {authenticated, loading} = this.props;

        if (loading) {
            return <LoadingIndicator tip="Loading..."/>;
        }

        return (
            <>
                <Switch>
                    <Route exact path="/" component={MainPage}/>
                    <Route path="/signup" component={SignUpPage}/>
                    <Route path="/login" component={LoginPage}/>
                    {authenticated && (
                        <Switch>
                            <AuthenticatedRoute exact path="/users/:username/quotes"
                                                authenticated={authenticated}
                                                component={MyQuoteMainPage}/>
                            <AuthenticatedRoute path="/users/:username/quotes/folders/:folderId"
                                                authenticated={authenticated}
                                                component={MyQuotePage}/>
                            <AuthenticatedRoute path="/users/:username/settings"
                                                authenticated={authenticated}
                                                component={SettingPage}/>
                        </Switch>
                    )}
                    <Route path="/test" component={TestPage}/>
                    <Route component={NotFoundPage}/>
                </Switch>
                <BaseContainer/>
            </>
        );
    }
}

export default connect(
    (state) => ({
        authenticated: state.base.get('authenticated'),
        loading: state.pender.pending['base/GET_CURRENT_USER']
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch)
    })
)(withRouter(App));