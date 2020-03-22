import React from 'react';
import PageTemplate from "../components/common/PageTemplate";
import SearchPage from "./SearchPage";
import ListTodayPage from "./ListTodayPage";

const MainPage = () => {
    return (
        <PageTemplate>
            <SearchPage/>
            <ListTodayPage/>
        </PageTemplate>
    );
};

export default MainPage;