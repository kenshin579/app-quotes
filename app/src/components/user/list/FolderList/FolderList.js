import React from 'react';
import styles from './FolderList.scss';
import classNames from 'classnames/bind';
import {Col, Dropdown, Layout, Menu, Row} from 'antd';
import _ from 'lodash';
import FolderItem from "../FolderItem";
import {UserOutlined, DownOutlined, FolderOutlined, DeleteOutlined} from "@ant-design/icons";

const cx = classNames.bind(styles);

const FolderList = ({folders, dropDownMenuNames, onMenuClick}) => {
    const folderView = [];
    const menuItems = [];


    const dropMenu = (
        <Menu onClick={onMenuClick}>
           {menuItems}
        </Menu>
    );

    _(dropDownMenuNames).forEach((value, key) => {
        menuItems.push(
            <Menu.Item key={key}>
                {value}
            </Menu.Item>
        )
    });

    _(folders).forEach(folder => {
        folderView.push(
            <FolderItem key={folder.folderName}
                        folderName={folder.folderName}
                        numOfQuotes={folder.numOfQuotes}
                        dropMenu={dropMenu}/>
        )
    });

    return (
        <Row style={{marginTop: '5px'}} gutter={8}>
            {folderView}
        </Row>
    );
};
export default FolderList;