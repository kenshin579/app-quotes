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
                <Link to="/login">로그인</Link>
            </Menu.Item>,
            <Menu.Item key="/signup">
                <Link to="/signup">회원가입</Link>
            </Menu.Item>
        ];

        return (
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
        );
    }
}

export default AppHeader;