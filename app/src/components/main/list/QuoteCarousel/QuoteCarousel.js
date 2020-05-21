import React, {useRef} from 'react';
import styles from './QuoteCarousel.scss';
import classNames from 'classnames/bind';
import {Button, Carousel} from "antd";
import {LeftCircleOutlined, RightCircleOutlined} from '@ant-design/icons';
import _ from "lodash";

const cx = classNames.bind(styles);

const QuoteCarousel = ({quotes}) => {
    const quoteView = [];

    _(quotes).forEach(quoteInfo => {
        quoteView.push(
            <div key={quoteInfo.quoteId} className={cx('quote-item')}>
                <blockquote>
                    <p>{quoteInfo.quoteText}</p>
                    <small>{quoteInfo.authorName}</small>
                </blockquote>
            </div>
        )
    });

    const carouselRef = React.createRef();
    const settings = {
        dots: true,
        infinite: true,
        autoplaySpeed: 10000,
        autoplay: true,
        speed: 500,
        slidesToShow: 1,
        slidesToScroll: 1
    };

    return (
        <div>
            <Carousel ref={carouselRef} {...settings}>
                {quoteView}
            </Carousel>
            <Button type="link"
                    className={cx('custom-slick-arrow')}
                    style={{left: '10px', zIndex: '1'}}
                    icon={<LeftCircleOutlined/>}
                    onClick={() => {
                        carouselRef.current.prev()
                    }}/>
            <Button type="link"
                    className={cx('custom-slick-arrow')}
                    style={{right: '10px'}}
                    icon={<RightCircleOutlined/>}
                    onClick={() => {
                        carouselRef.current.next()
                    }}/>
        </div>

    );
};
export default QuoteCarousel;