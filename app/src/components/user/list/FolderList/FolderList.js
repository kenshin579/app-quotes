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

    _(folders).forEach(folder => {
        folderView.push(
            <FolderItem key={folder.folderId}
                        folderId={folder.folderId}
                        dropDownMenuNames={dropDownMenuNames}
                        folderName={folder.folderName}
                        numOfQuotes={folder.numOfQuotes}
                        onMenuClick={onMenuClick}/>
        )
    });

    return (
        <Row style={{marginTop: '5px'}} gutter={8}>
            {folderView}
        </Row>
    );
};
export default FolderList;