import React from 'react';
import styles from './FolderList.scss';
import classNames from 'classnames/bind';
import {Layout, Row} from 'antd';
import _ from 'lodash';
import FolderItem from "../FolderItem";

const cx = classNames.bind(styles);
const Header = Layout.Header;

const FolderList = ({folderList}) => {
    const folderView = [];

    _(folderList).forEach(folderInfo => {
        folderView.push(
            <FolderItem key={folderInfo.folderName}
                        folderName={folderInfo.folderName}
                        numOfQuotes={folderInfo.numOfQuotes}/>
        )
    });

    return (
        <Row style={{marginTop: '5px'}} gutter={8}>
            {folderView}
        </Row>
    );
};
export default FolderList;