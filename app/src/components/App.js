import React from 'react';
import {Route, Switch} from 'react-router-dom';
import {MainPage, SearchPage, SignUpPage, LoginPage, ProfilePage, NotFoundPage} from 'pages';

const App = () => {
    return (
        <div>
            QuoteApps
            <Switch>
                <Route exact path="/" component={MainPage}/>
                <Route path="/search" component={SearchPage}/>
                <Route path="/signup" component={SignUpPage}/>
                <Route path="/login" component={LoginPage}/>
                <Route path="/profiles/:id" component={ProfilePage}/>
                <Route component={NotFoundPage}/>
            </Switch>
        </div>
    );
};

export default App;
