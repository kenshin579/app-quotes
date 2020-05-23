import React from 'react';
import styles from './MyQuoteMain.scss';
import classNames from 'classnames/bind';
import {Layout, Menu} from "antd";
import {FolderOutlined, UserOutlined, HomeOutlined, SettingOutlined} from '@ant-design/icons';
import StatisticList from "../../list/StatisticList";
import FolderList from "../../list/FolderList";
import _ from "lodash";
import {Link} from "react-router-dom";

const cx = classNames.bind(styles);

const MyQuoteMain = ({folderStatInfo, folders, dropDownMenuNames, onMenuClick}) => {
    const {Header, Content} = Layout;

    let folderView = [];
    _(folders).forEach(folderInfo => {
        folderView.push(
            <Menu.Item key={folderInfo.folderId}>
                <FolderOutlined/>
                <Link to={`/quotes/${folderInfo.folderId}`}>{folderInfo.folderName}</Link>
            </Menu.Item>
        )
    });

    return (
        <Layout className={cx("layout-background")}>
            <Header className={cx("stat-header")}>
                <StatisticList statisticInfo={folderStatInfo}/>
            </Header>
            <Content style={{margin: '16px'}}>
                <div className={cx('content-title')}>내 폴더</div>
                <FolderList folders={folders}
                            dropDownMenuNames={dropDownMenuNames}
                            onMenuClick={onMenuClick}/>
            </Content>

            {/*<Content style={{margin: '16px'}}>*/}
            {/*    <div className={cx('content-title')}>최근 활동</div>*/}
            {/*    /!*<QuoteTimelineList activityList={activityList}/>*!/*/}
            {/*</Content>*/}
        </Layout>
    );
};

export default MyQuoteMain;