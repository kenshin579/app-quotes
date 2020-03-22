import React from 'react';
import Header from "../AppHeader";

const PageTemplate = ({children}) => (
    <div>
        <Header/>
        <main>
            {/*{children}*/}
        </main>
    </div>
);

export default PageTemplate;
