import React from 'react';
import styles from './UserTemplate.scss';
import {Layout} from "antd";
import classNames from 'classnames/bind';
import AppHeaderContainer from "containers/common/AppHeaderContainer";
import SidebarContainer from "containers/user/LeftSidebarContainer";

const cx = classNames.bind(styles);

const UserTemplate = ({children}) => (
    <Layout>
        <AppHeaderContainer/>
        <Layout>
            <SidebarContainer />
            {children}
        </Layout>
    </Layout>
);

export default UserTemplate;
