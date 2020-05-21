import React from 'react';
import styles from './StatisticList.scss';
import classNames from 'classnames/bind';
import {Col, Divider, Layout, Row} from 'antd';
import StatisticItem from "../StatisticItem";

const cx = classNames.bind(styles);
const Header = Layout.Header;

const StatisticList = ({statisticInfo}) => {
    const statisticView = [];

    Object.keys(statisticInfo).forEach((key) => {
        statisticView.push(
            <StatisticItem key={key} title={key} data={statisticInfo[key]}/>
        )
    });

    return (
        <Row style={{marginTop: '20px'}} gutter={8}>
            {statisticView}
        </Row>
    );
};
export default StatisticList;