import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as mainActions from 'store/modules/main';
import LoadingIndicator from "../../components/common/LoadingIndicator";
import {Button} from "antd";
import {QUOTE_TODAY_SIZE} from "../../constants";
import QuoteCarousel from "../../components/main/list/QuoteCarousel";
import QuoteTimelineList from "../../components/main/list/QuoteTimelineList";

class RandomListContainer extends Component {

    componentDidMount() {
        const {pagination} = this.props;
        this.loadData(pagination);
    }

    loadData = async (pagination) => {
        const {MainActions} = this.props;
        try {
            const response = await MainActions.getRandomList(pagination);
            console.log('response', response);
        } catch (e) {
            console.error(e);
        }

        this.handleScrollPosition();
    };

    onLoadMore = () => {
        this.setCurrentScrollPosition();
        const {pagination} = this.props;
        pagination.pageSize += QUOTE_TODAY_SIZE;
        this.loadData(pagination);
    };

    handleScrollPosition = () => {
        const scrollPosition = sessionStorage.getItem("scrollPosition");
        if (scrollPosition) {
            window.scrollTo(0, parseInt(scrollPosition));
            sessionStorage.removeItem("scrollPosition");
        }
    };

    setCurrentScrollPosition = e => {
        sessionStorage.setItem("scrollPosition", window.pageYOffset);
    };

    render() {
        const {loading, lastpage, quotes} = this.props;

        if (loading) {
            return <LoadingIndicator tip={'Loading...'}/>
        }

        const loadMoreButton = !lastpage ? (
            <div
                style={{
                    textAlign: 'center',
                    margin: '12px',
                    height: 32,
                    lineHeight: '32px',
                }}>
                <Button shape="round" onClick={this.onLoadMore}>더보기</Button>
            </div>
        ) : null;

        return (
            <div>
                <QuoteCarousel quotes={quotes}/>
                <QuoteTimelineList quotes={quotes}
                                   loadMoreButton={loadMoreButton}/>
            </div>
        )
    }
}

export default connect(
    (state) => ({
        quotes: state.main.get('quotes').toJS(),
        pagination: {
            current: state.main.getIn(['pagination', 'page']),
            pageSize: state.main.getIn(['pagination', 'size']),
            total: state.main.getIn(['pagination', 'totalElements']),
        },
        lastpage: state.main.getIn(['pagination', 'last']),
        loading: state.pender.pending['main/GET_RANDOM_LIST']
    }),
    (dispatch) => ({
        MainActions: bindActionCreators(mainActions, dispatch)
    })
)(RandomListContainer);
