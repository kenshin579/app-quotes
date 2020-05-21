import React from 'react';
import {Route, Switch} from 'react-router-dom';
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
import BaseContainer from '../containers/common/BaseContainer';
import AuthenticatedRoute from "./common/AuthenticatedRoute";

const App = () => {
    return (
        <>
            <Switch>
                <Route exact path="/" component={MainPage}/>
                <Route path="/signup" component={SignUpPage}/>
                <Route path="/login" component={LoginPage}/>
                <AuthenticatedRoute exact path="/users/:username/quotes" component={MyQuoteMainPage}/>
                <AuthenticatedRoute path="/users/:username/quotes/folders/:folderId" component={MyQuotePage}/>
                <AuthenticatedRoute path="/users/:username/settings" component={SettingPage}/>
                <Route path="/test" component={TestPage}/>
                <Route component={NotFoundPage}/>
            </Switch>
            <BaseContainer/>
        </>
    );
};

export default App;