import React from 'react';
import styles from './RandomList.scss';
import classNames from 'classnames/bind';
import _ from "lodash";
import QuoteItem from "../QuoteItem";

const cx = classNames.bind(styles);

//todo : 현재 사용하지 않음
const RandomList = ({initLoading, loadMoreButton, numOfVisible, quotes}) => {
    const quoteView = [];

    _(quotes).forEach(quoteInfo => {
        quoteView.push(
            <QuoteItem quoteText={quoteInfo.quoteText}
                       authorName={quoteInfo.authorName}/>
        )
    });

    return (
        <div className={cx('random-container')}>
            {quoteView}
            {loadMoreButton}
        </div>
        // <List loading={initLoading}
        //       itemLayout="horizontal"
        //       loadMore={loadMoreButton}
        //       dataSource={quotes}
        // />

    );
};
export default RandomList;