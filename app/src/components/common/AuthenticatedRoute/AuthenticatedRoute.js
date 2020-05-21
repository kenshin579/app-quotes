import React from 'react';
import {Redirect, Route, withRouter} from "react-router-dom";
import {connect} from "react-redux";

const AuthenticatedRoute = ({component: ComponentToRender, authenticated, ...rest}) => {
    console.log('[FRANK] authenticated : ', authenticated);
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

export default connect(
    (state) => ({
        authenticated: state.base.get('authenticated')
    }),
    null
)(withRouter(AuthenticatedRoute));
