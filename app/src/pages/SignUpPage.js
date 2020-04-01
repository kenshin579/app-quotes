import React from 'react';
import PageTemplate from "../components/common/PageTemplate";
import SignUpContainer from "../containers/user/signup/SignUpContainer";

const SignUpPage = () => {
    return (
        <PageTemplate>
            <SignUpContainer/>
        </PageTemplate>
    );
};

export default SignUpPage;