import React from 'react';
import styles from './LeftSidebar.scss';
import classNames from 'classnames/bind';
import {Link} from 'react-router-dom';
import {Layout, Menu, Sider} from 'antd';
import _ from "lodash";
import {FolderOutlined, HomeOutlined, SettingOutlined} from '@ant-design/icons';

const cx = classNames.bind(styles);

const LeftSidebar = ({folders, location, authenticated, currentUser}) => {
    const {Sider} = Layout;

    let folderView = [];
    if (authenticated) {
        _(folders).forEach(folder => {
            folderView.push(
                <Menu.Item key={`/users/${currentUser}/quotes/folders/${folder.folderId}`}>
                    <FolderOutlined/>
                    <Link to={`/users/${currentUser}/quotes/folders/${folder.folderId}`}>{folder.folderName}</Link>
                </Menu.Item>
            )
        });
    }

    return (
        <Sider className={cx('sidebar')}>
            <Menu defaultSelectedKeys={[location.pathname]} mode="inline">
                <Menu.Item key={`/users/${currentUser}/quotes`}>
                    <HomeOutlined/>
                    <Link to={`/users/${currentUser}/quotes`}/>
                    <span>내 명언 모음</span>
                </Menu.Item>
                <Menu.ItemGroup title="내 폴더">
                    {folderView}
                    <div style={{border: '0.5px solid lightgray'}}></div>
                    <Menu.Item key={`/users/${currentUser}/settings`}>
                        <SettingOutlined/>
                        <Link to={`/users/${currentUser}/settings`}/>
                        <span>설정</span>
                    </Menu.Item>
                </Menu.ItemGroup>
            </Menu>
        </Sider>
    );
};

export default LeftSidebar;