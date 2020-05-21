import React from 'react';
import PageTemplate from "../components/common/PageTemplate";
import MyPageMainContainer from "../containers/user/MyQuoteMainContainer";
import UserTemplate from "../components/user/UserTemplate";

const MyQuoteMainPage = ({match}) => {
    const {id} = match.params;
    console.log('id', id);

    return (
        <UserTemplate>
            <MyPageMainContainer/>
        </UserTemplate>
    );
};

export default MyQuoteMainPage;