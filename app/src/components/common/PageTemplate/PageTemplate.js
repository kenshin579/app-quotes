import React from 'react';
import styles from './PageTemplate.scss';
import AppHeader from "../AppHeader";
import {Layout} from "antd";
import classNames from 'classnames/bind';

const {Content} = Layout;
const cx = classNames.bind(styles);

const PageTemplate = ({children}) => (
    <div>
        <Layout>
            <AppHeader/>
            <Content className={cx('page-template-content')}>
                <div className={cx('container')}>
                    {children}
                </div>
            </Content>
        </Layout>
    </div>
);

export default PageTemplate;
