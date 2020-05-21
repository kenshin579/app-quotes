import React from 'react';
import PageTemplate from "../components/common/PageTemplate";
import RandomListContainer from "../containers/main/RandomListContainer";
import SearchContainer from "../containers/main/SearchContainer";

const MainPage = () => {
    return (
        <PageTemplate>
            {/*<SearchContainer/>*/}
            <RandomListContainer/>
        </PageTemplate>
    );
};

export default MainPage;