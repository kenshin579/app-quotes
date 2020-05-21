import React, {Component} from 'react';
import styles from './CarouselWrapper.scss';
import classNames from 'classnames/bind';
import {Spin} from 'antd';

const cx = classNames.bind(styles);

class CarouselWrapper extends Component {

    render() {
        return (
            <Spin tip={tip} style={{display: 'block', textAlign: 'center', marginTop: 30}}/>
        );
    }
}

export default CarouselWrapper;