import React, {useRef} from 'react';
import styles from './QuoteTimelineList.scss';
import classNames from 'classnames/bind';
import {Button, Carousel} from "antd";
import {LeftCircleOutlined, RightCircleOutlined} from '@ant-design/icons';
import _ from "lodash";

const cx = classNames.bind(styles);

const QuoteTimelineList = ({quotes, loadMoreButton}) => {
    const quoteView = [];

    // _(quotes).forEach(quoteInfo => {
    //     quoteView.push(
    //         <div key={quoteInfo.quoteId} className={cx('quote-item')}>
    //             <blockquote>
    //                 <p>{quoteInfo.quoteText}</p>
    //                 <small>{quoteInfo.authorName}</small>
    //             </blockquote>
    //         </div>
    //     )
    // });

    return (
        <div className={cx('timeline-container')}>
            {quoteView}
            {loadMoreButton}
        </div>

    );
};
export default QuoteTimelineList;