import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as quoteActions from 'store/modules/quote';
import {withRouter} from "react-router-dom";
import QuoteDeleteModal from "components/user/modal/QuoteDeleteModal";


class QuoteCreateModalContainer extends Component {
    handleCancel = () => {
        const {BaseActions} = this.props;
        BaseActions.hideModal('quoteDelete');
    };

    handleDelete = async () => {
        const {BaseActions, QuoteActions, selectedRowKeys, pagination, currentUser, location, history} = this.props;
        BaseActions.hideModal('quoteDelete');

        let folderId = location.pathname.substring(location.pathname.lastIndexOf('/') + 1);

        try {
            const response = await QuoteActions.deleteQuote(selectedRowKeys);
            await QuoteActions.getQuoteList(folderId, pagination);
            history.push(`/users/${currentUser}/quotes/folders/${folderId}`);
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {visible} = this.props;

        return (
            <QuoteDeleteModal visible={visible}
                              onDelete={this.handleDelete}
                              onCancel={this.handleCancel}/>
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'quoteDelete']),
        currentUser: state.base.getIn(['user', 'username']),
        pagination: {
            current: state.quote.getIn(['pagination', 'page']),
            pageSize: state.quote.getIn(['pagination', 'size']),
            total: state.quote.getIn(['pagination', 'totalElements']),
        },
        selectedRowKeys: state.quote.get('selectedRowKeys').toJS()
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        QuoteActions: bindActionCreators(quoteActions, dispatch)
    })
)(withRouter(QuoteCreateModalContainer));
