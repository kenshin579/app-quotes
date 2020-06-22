import React from 'react';
import styles from './QuoteTimelineList.scss';
import classNames from 'classnames/bind';
import _ from "lodash";

const cx = classNames.bind(styles);

const QuoteTimelineList = ({quotes, loadMoreButton}) => {
    const quoteView = [];

    _(quotes).forEach(quoteInfo => {
        quoteView.push(
            <div key={quoteInfo.quoteId}>
                <blockquote className={cx('otro-blockquote')}>
                    {quoteInfo.quoteText}
                    <small>{quoteInfo.authorName}</small>
                </blockquote>
            </div>
        )
    });

    return (
        <div className={cx('timeline-container')}>
            {quoteView}
            {loadMoreButton}
        </div>

    );
};
export default QuoteTimelineList;