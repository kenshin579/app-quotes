import React from 'react';
import PageTemplate from "../components/common/PageTemplate";
import LoginContainer from "../containers/user/login/LoginContainer";

const LoginPage = () => {
    return (
        <PageTemplate>
            <LoginContainer/>
        </PageTemplate>
    );
};

export default LoginPage;