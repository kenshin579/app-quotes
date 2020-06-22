import React from 'react';
import {Link} from "react-router-dom";
import styles from './AppHeader.scss';
import classNames from 'classnames/bind';
import {Dropdown, Layout, Menu} from 'antd';
import {DownOutlined, UserOutlined} from '@ant-design/icons';

const cx = classNames.bind(styles);
const Header = Layout.Header;

const AppHeader = ({authenticated, currentUser, onClick}) => {
    let headerMenuItems;

    if (authenticated) {
        headerMenuItems = [
            <Menu.Item key="/profile" className={cx('profile-menu')}>
                <ProfileDropdownMenu
                    currentUser={currentUser}
                    onClick={onClick}
                />
            </Menu.Item>
        ];
    } else {
        headerMenuItems = [
            <Menu.Item key="/login">
                <Link to="/login">로그인</Link>
            </Menu.Item>,
            <Menu.Item key="/signup">
                <Link to="/signup">회원가입</Link>
            </Menu.Item>
        ];
    }

    return (
        <Header className={cx('app-header')}>
            <div className={cx('app-title')}>
                <Link to="/">Best Quotes</Link>
            </div>
            <Menu
                className={cx('app-menu')}
                mode="horizontal">
                {headerMenuItems}
            </Menu>
        </Header>
    );
};

const ProfileDropdownMenu = ({currentUser, onClick}) => {
    const dropDownMenu = (
        <Menu  className={cx('profile-dropdown-menu')} onClick={onClick}>
            <Menu.Item key="myquote">
                <Link to={`/users/${currentUser}/quotes`}>내 명언</Link>
            </Menu.Item>
            <Menu.Item key="profile">
                <Link to={`/users/${currentUser}/settings`}>설정</Link>
            </Menu.Item>
            <Menu.Item key="logout" className="dropdown-item">
                로그아웃
            </Menu.Item>
        </Menu>
    );

    return (
        <Dropdown overlay={dropDownMenu}>
            <a className={cx('ant-dropdown-link')} onClick={e => e.preventDefault()}>
                <UserOutlined/>{currentUser}<DownOutlined/>
            </a>
        </Dropdown>
    );
};

export default AppHeader;