import React from 'react';
import styles from './SearchQuote.scss';
import classNames from 'classnames/bind';
import {Input} from 'antd';
import {convertNumberWithComma} from "utils/numberformat";

const cx = classNames.bind(styles);
const {Search} = Input;

const SearchQuote = ({totalQuote}) => {
    return (
        <div className={cx('search-container')}>
            <h1>명언검색</h1>
            <Search
                className={cx('search-input')}
                placeholder="input search text"
                onSearch={value => console.log(value)}
                enterButton/>
            <p style={{fontSize: 'x-small'}}>명언 수 {convertNumberWithComma(totalQuote)}건</p>
        </div>
    );
};
export default SearchQuote;