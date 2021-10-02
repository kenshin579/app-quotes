import React from 'react';
import styles from './ActivityList.scss';
import classNames from 'classnames/bind';
import {Col, Divider, Layout, Row} from 'antd';
import StatisticItem from "../StatisticItem";

const cx = classNames.bind(styles);
const Header = Layout.Header;

//todo: 현재는 사용하지 않음
const ActivityList = ({activityList}) => {
    const statisticView = [];

    // Object.keys(statisticInfo).forEach((key) => {
    //     statisticView.push(
    //         <StatisticItem key={key} title={key} data={statisticInfo[key]}/>
    //     )
    // });

    return (
        <Row>
            <div className={cx('date')}>2019년 6월 14일</div>


        </Row>
    );
};
export default ActivityList;