import React from 'react';
import styles from './FolderItem.scss';
import classNames from 'classnames/bind';
import {Col, Dropdown, Menu} from 'antd';
import {FolderOutlined} from '@ant-design/icons';
import _ from "lodash";

const cx = classNames.bind(styles);
const style = {
    background: '#fff',
    margin: '5px 20px',
    padding: '0px 10px',
    boxShadow: '2px 2px 6px 0 rgba(0,0,0,0.1)'
};

const FolderItem = ({folderId, dropDownMenuNames, folderName, numOfQuotes, onMenuClick}) => {
    const menuItems = [];

    const dropMenu = (
        <Menu onClick={(e) => onMenuClick(folderId, e)}>
            {menuItems}
        </Menu>
    );

    _(dropDownMenuNames).forEach((menuTitle, menuKey) => {
        menuItems.push(
            <Menu.Item key={menuKey}>
                {menuTitle}
            </Menu.Item>
        )
    });

    return (
        <Col style={style} span={6} className={cx('folder-item')}>
            <div className={cx('num-quotes')}>{numOfQuotes} 명언</div>
            <div className={cx('folder')}><FolderOutlined/> {folderName}</div>
            <Dropdown overlay={dropMenu}
                      trigger={['click']}>
                <div className={cx('vertical-dots')}
                     onClick={e => e.preventDefault()}></div>
            </Dropdown>
        </Col>
    );
};
export default FolderItem;