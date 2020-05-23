import React from 'react';
import styles from './StatisticList.scss';
import classNames from 'classnames/bind';
import {Row} from 'antd';
import StatisticItem from "../StatisticItem";
import _ from 'lodash';
import {FOLDER_STAT_INFO_NAME_MAPPING} from "../../../../constants";

const cx = classNames.bind(styles);

const StatisticList = ({statisticInfo}) => {
    const statisticView = [];

    _(statisticInfo).forEach((value, key) => {
        statisticView.push(
            <StatisticItem key={key}
                           title={FOLDER_STAT_INFO_NAME_MAPPING[key]}
                           data={value}/>
        )
    });

    return (
        <Row style={{marginTop: '20px'}} gutter={8}>
            {statisticView}
        </Row>
    );
};
export default StatisticList;