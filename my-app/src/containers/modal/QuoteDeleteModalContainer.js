import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as quoteActions from 'store/modules/quote';
import {withRouter} from "react-router-dom";
import DeleteModal from "components/user/modal/DeleteModal";
import {DELETE_MODAL_TYPE} from "../../constants";


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
            const response = await QuoteActions.deleteQuotes(selectedRowKeys);

            await QuoteActions.getQuoteList(folderId, pagination);
            history.push(`/users/${currentUser}/quotes/folders/${folderId}`);
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {visible} = this.props;

        return (
            <DeleteModal visible={visible}
                         deleteModalType={DELETE_MODAL_TYPE.QUOTE}
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
