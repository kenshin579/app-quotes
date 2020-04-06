import React from 'react';
import {Route, Switch} from 'react-router-dom';
import {LoginPage, MainPage, MyQuote, NotFoundPage, SettingPage, SignUpPage} from 'pages';

const App = () => {
    return (
        <div>
            <Switch>
                <Route exact path="/" component={MainPage}/>
                <Route path="/signup" component={SignUpPage}/>
                <Route path="/login" component={LoginPage}/>
                <Route path="/quotes" component={MyQuote}/>
                <Route path="/settings" component={SettingPage}/>
                <Route component={NotFoundPage}/>
            </Switch>
        </div>
    );
};

export default App;
