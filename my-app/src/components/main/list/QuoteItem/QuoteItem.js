import React from 'react';
import styles from './QuoteItem.scss';
import classNames from 'classnames/bind';
import {Layout} from 'antd';
import {HeartOutlined} from "@ant-design/icons";

const cx = classNames.bind(styles);

//todo : 현재 사용하지 않음
const QuoteItem = ({quoteText, authorName}) => {
    return (
        <div className={cx('quote-card')}>
            <div className={cx('quote-text')}>
                <blockquote className={cx('curly-quotes')}>{quoteText}</blockquote>
            </div>
            <div className={cx('author')}>{authorName}</div>
            <div className={cx('footer')}><HeartOutlined/></div>
        </div>
    );
};
export default QuoteItem;