import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as quoteActions from 'store/modules/quote';
import {withRouter} from "react-router-dom";
import QuoteEditModal from "../../components/user/modal/QuoteEditModal";


class QuoteEditModalContainer extends Component {
    handleCancel = () => {
        const {BaseActions} = this.props;
        BaseActions.hideModal('quoteEdit');
    };

    handleEdit = async () => {
        const {BaseActions, QuoteActions, selectedRowKeys, pagination, currentUser, location, history} = this.props;
        BaseActions.hideModal('quoteEdit');

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
        const {visible, quotes, selectedRowKeys} = this.props;

        const selectedQuotes = quotes.filter(it => selectedRowKeys.includes(it.quoteId));

        return (
            <QuoteEditModal visible={visible}
                            quote={selectedQuotes.length > 0 ? selectedQuotes[0] : []}
                            onEdit={this.handleEdit}
                            onCancel={this.handleCancel}/>
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'quoteEdit']),
        currentUser: state.base.getIn(['user', 'username']),
        quotes: state.quote.get('quotes').toJS(),
        selectedRowKeys: state.quote.get('selectedRowKeys').toJS()
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        QuoteActions: bindActionCreators(quoteActions, dispatch)
    })
)(withRouter(QuoteEditModalContainer));
