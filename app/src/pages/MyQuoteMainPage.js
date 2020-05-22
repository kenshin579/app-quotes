import React from 'react';
import MyQuoteMainContainer from "../containers/user/MyQuoteMainContainer";
import UserTemplate from "../components/user/UserTemplate";

const MyQuoteMainPage = ({match}) => {
    const {id} = match.params;
    console.log('id', id);

    return (
        <UserTemplate>
            <MyQuoteMainContainer/>
        </UserTemplate>
    );
};

export default MyQuoteMainPage;