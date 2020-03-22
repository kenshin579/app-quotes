import React, {Component} from 'react';
import {Link} from "react-router-dom";
import styles from './AppHeader.scss';
import classNames from 'classnames/bind';
import {Layout, Menu} from 'antd';

const cx = classNames.bind(styles);
const Header = Layout.Header;

class AppHeader extends Component {
    render() {
        let menuItems = [
            <Menu.Item key="/login">
                <Link to="/login">Login</Link>
            </Menu.Item>,
            <Menu.Item key="/signup">
                <Link to="/signup">Signup</Link>
            </Menu.Item>
        ];

        return (
            <Layout>
                <Header className={cx('app-header')}>
                    <div className={cx('container')}>
                        <div className={cx('app-title')}>
                            <Link to="/">Best Quotes</Link>
                        </div>
                        <Menu
                            className={cx('app-menu')}
                            mode="horizontal"
                            style={{lineHeight: '64px'}}>
                            {menuItems}
                        </Menu>
                    </div>
                </Header>
            </Layout>
        );
    }
}

export default AppHeader;