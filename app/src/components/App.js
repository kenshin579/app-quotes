import React from 'react';
import {Route, Switch} from 'react-router-dom';
import {LoginPage, MainPage, NotFoundPage, ProfilePage, SignUpPage} from 'pages';

const App = () => {
    return (
        <div>
            <Switch>
                <Route exact path="/" component={MainPage}/>
                <Route path="/signup" component={SignUpPage}/>
                <Route path="/login" component={LoginPage}/>
                <Route path="/users/:username" component={ProfilePage}/>
                <Route component={NotFoundPage}/>
            </Switch>
        </div>
    );
};

export default App;
