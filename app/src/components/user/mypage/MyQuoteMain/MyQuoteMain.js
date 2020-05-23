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

const MyQuoteMain = () => {
    const {Header, Content} = Layout;

    let statisticInfo = {
        // 'numOfAllQuotes': 101,
        // 'numOfLikes': 55
        '모든 명언': 101,
        '좋아요': 55
    };

    let folderList = [
        {
            'folderId': 1,
            'folderName': 'My 명언',
            'numOfQuotes': 51
        },
        {
            'folderId': 2,
            'folderName': '영어 명언',
            'numOfQuotes': 20
        },
        {
            'folderId': 3,
            'folderName': '책 : 검사내전',
            'numOfQuotes': 27
        },
    ];


    function handleMenuClick(e) {
        // message.info('Click on menu item.');
        console.log('click', e);
    }

    const menu = (
        <Menu onClick={handleMenuClick}>
            <Menu.Item key="1" icon={<UserOutlined />}>
                1st menu item
            </Menu.Item>
            <Menu.Item key="2" icon={<UserOutlined />}>
                2nd menu item
            </Menu.Item>
            <Menu.Item key="3" icon={<UserOutlined />}>
                3rd item
            </Menu.Item>
        </Menu>
    );


    let folderView = [];
    _(folderList).forEach(folderInfo => {
        folderView.push(
            <Menu.Item key={folderInfo.folderId}>
                <FolderOutlined/>
                <Link to={`/quotes/${folderInfo.folderId}`}>{folderInfo.folderName}</Link>
            </Menu.Item>
        )
    });


    //todo : 최근 활동은 나중에 작업하는 걸로 함
    let activityList = [
        {
            '20190614': [
                {
                    'activityType': 'like'
                }
            ]
        }
    ];

    return (
        <Layout className={cx("layout-background")}>
            <Header className={cx("stat-header")}>
                <StatisticList statisticInfo={statisticInfo}/>
            </Header>
            <Content style={{margin: '16px'}}>
                <div className={cx('content-title')}>내 폴더</div>
                <FolderList folderList={folderList}/>
            </Content>

            {/*<Content style={{margin: '16px'}}>*/}
            {/*    <div className={cx('content-title')}>최근 활동</div>*/}
            {/*    /!*<QuoteTimelineList activityList={activityList}/>*!/*/}
            {/*</Content>*/}
        </Layout>
    );
};

export default MyQuoteMain;