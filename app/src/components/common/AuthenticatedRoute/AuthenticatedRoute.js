import React from 'react';
import {Redirect, Route} from "react-router-dom";

const AuthenticatedRoute = ({component: ComponentToRender, authenticated, ...rest}) => {

    return (
        <Route
            {...rest}
            render={props =>
                authenticated ? (
                    <ComponentToRender {...rest} {...props} />
                ) : (
                    <Redirect
                        to={{
                            pathname: '/login',
                            state: {from: props.location}
                        }}
                    />
                )
            }
        />
    );
};

export default AuthenticatedRoute;