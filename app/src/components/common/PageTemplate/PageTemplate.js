import React from 'react';
import styles from './PageTemplate.scss';
import {Layout} from "antd";
import classNames from 'classnames/bind';
import AppHeaderContainer from "../../../containers/common/AppHeaderContainer";

const {Content} = Layout;
const cx = classNames.bind(styles);

const PageTemplate = ({children}) => (
    <Layout>
        <AppHeaderContainer/>
        <Content>
            {children}
        </Content>
    </Layout>
);

export default PageTemplate;
