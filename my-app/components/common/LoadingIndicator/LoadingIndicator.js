import React from 'react';
import styles from './LoadingIndicator.scss';
import classNames from 'classnames/bind';
import {Spin} from 'antd';

const cx = classNames.bind(styles);

const LoadingIndicator = ({tip}) => {
    return (
        <Spin tip={tip} style={{display: 'block', textAlign: 'center', marginTop: 30}}/>
    );
};


export default LoadingIndicator;