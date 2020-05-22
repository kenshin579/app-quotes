import React from 'react';
import PageTemplate from "../components/common/PageTemplate";
import TodayQuoteListContainer from "../containers/main/TodayQuoteListContainer";
import SearchContainer from "../containers/main/SearchContainer";

const MainPage = () => {
    return (
        <PageTemplate>
            {/*<SearchContainer/>*/}
            <TodayQuoteListContainer/>
        </PageTemplate>
    );
};

export default MainPage;