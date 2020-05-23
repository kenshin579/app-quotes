import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as quoteActions from 'store/modules/quote';
import * as baseActions from 'store/modules/base';
import MyQuote from "components/user/mypage/MyQuote";


class MyQuoteContainer extends Component {
    componentDidMount() {
        console.log('MyQuoteContainer componentDidMount');
        const {pagination} = this.props;
        this.loadData(pagination);
    }

    componentDidUpdate(prevProps, prevState) {
        console.log('MyQuoteContainer componentDidUpdate :: prevProps', prevProps);
        const {pagination} = this.props;

        if (prevProps.folderId !== this.props.folderId
            || prevProps.pagination.total !== this.props.pagination.total) {
            this.loadData(pagination);
        }
    }

    loadData = async (pagination) => {
        const {QuoteActions, folderId} = this.props;

        try {
            const response = await QuoteActions.getQuoteList(folderId, pagination);
            console.log('response', response);
        } catch (e) {
            console.error(e);
        }
    };

    handleTableChange = (pagination) => {
        console.log('handleTableChange :: pagination', pagination);
        this.loadData(pagination);
    };

    handleCreateModal = () => {
        const {BaseActions} = this.props;
        BaseActions.showModal('quoteCreate');
    };

    handleDeleteModal = () => {
        const {BaseActions} = this.props;
        BaseActions.showModal('quoteDelete');
    };

    handleSelectChange = selectedRowKeys => {
        console.log('MyQuoteContainer :: selectedRowKeys changed: ', selectedRowKeys);
        const {QuoteActions} = this.props;
        QuoteActions.selectQuote(selectedRowKeys);
    };

    render() {
        const {quotes, pagination, selectedRowKeys, loading} = this.props;
        const {handleTableChange, handleCreateModal, handleDeleteModal} = this;
        const rowSelection = {
            selectedRowKeys,
            onChange: this.handleSelectChange,
        };

        if (loading) {
            return null;
        }

        return (
            <MyQuote
                quotes={quotes}
                rowSelection={rowSelection}
                pagination={pagination}
                onCreateModal={handleCreateModal}
                onDeleteModal={handleDeleteModal}
                onTableChange={handleTableChange}
            />
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'quoteCreate']),
        quotes: state.quote.get('quotes').toJS(),
        pagination: {
            current: state.quote.getIn(['pagination', 'page']),
            pageSize: state.quote.getIn(['pagination', 'size']),
            total: state.quote.getIn(['pagination', 'totalElements']),
        },
        selectedRowKeys : state.quote.get('selectedRowKeys').toJS(),
        loading: state.pender.pending['quote/GET_QUOTE_LIST']
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        QuoteActions: bindActionCreators(quoteActions, dispatch),
    })
)(MyQuoteContainer);
