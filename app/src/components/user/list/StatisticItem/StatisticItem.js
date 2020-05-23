import React from 'react';
import styles from './StatisticItem.scss';
import classNames from 'classnames/bind';
import {Col, Divider} from 'antd';

const cx = classNames.bind(styles);

const StatisticItem = ({title, data}) => {
    return (
        <>
            <Divider type='vertical' style={{marginLeft: '30px', color: '#b6b7ab', width: '2px', height: '70px', }}>
            </Divider>
            <Col span={2}>
                <div className={cx('stat-title')}>{title}</div>
                <div className={cx('stat-data')}>{data}</div>
            </Col>
        </>
    );
};
export default StatisticItem;