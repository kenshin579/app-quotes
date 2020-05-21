import React from 'react';
import MyQuoteContainer from "../containers/user/MyQuoteContainer";
import UserTemplate from "../components/user/UserTemplate";

const MyQuotePage = ({match}) => {
    const {folderId} = match.params;
    console.log('folderId', folderId);

    return (
        <UserTemplate>
            <MyQuoteContainer folderId={folderId}/>
        </UserTemplate>
    );
};

export default MyQuotePage;