import React from 'react';
import {Link} from "react-router-dom";
import styles from './AppHeader.scss';
import classNames from 'classnames/bind';
import {Dropdown, Layout, Menu} from 'antd';
import {DownOutlined, UserOutlined} from '@ant-design/icons';

const cx = classNames.bind(styles);
const Header = Layout.Header;

const AppHeader = ({isAuthenticated, currentUser, onClick}) => {
    console.log('isAuthenticated2', isAuthenticated);
    console.log('currentUser2', currentUser);

    let headerMenuItems;

    if (isAuthenticated) {
        headerMenuItems = [
            <Menu.Item key="/profile" className="profile-menu">
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
            <div className={cx('container')}>
                <div className={cx('app-title')}>
                    <Link to="/">Best Quotes</Link>
                </div>
                <Menu
                    className={cx('app-menu')}
                    mode="horizontal"
                    style={{lineHeight: '64px'}}>
                    {headerMenuItems}
                </Menu>
            </div>
        </Header>
    );
};

const ProfileDropdownMenu = ({currentUser, onClick}) => {
    const dropDownMenu = (
        <Menu onClick={onClick} className="profile-dropdown-menu">
            <Menu.Item key="myquote" className="dropdown-item">
                <Link to={`/quotes`}>내 명언</Link>
            </Menu.Item>
            <Menu.Item key="profile" className="dropdown-item">
                <Link to='/settings'>설정</Link>
            </Menu.Item>
            <Menu.Item key="logout" className="dropdown-item">
                로그아웃
            </Menu.Item>
        </Menu>
    );

    return (
        <Dropdown overlay={dropDownMenu}>
            <a className="ant-dropdown-link" onClick={e => e.preventDefault()}>
                <UserOutlined/>{currentUser.username}<DownOutlined/>
            </a>
        </Dropdown>
    );
};

export default AppHeader;