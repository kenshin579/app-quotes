import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as quoteActions from "../../store/modules/quote";
import * as folderActions from 'store/modules/folder';
import {withRouter} from "react-router-dom";
import QuoteMoveModal from "../../components/user/modal/QuoteMoveModal";


class QuoteMoveModalContainer extends Component {
    handleCancel = () => {
        const {BaseActions} = this.props;
        BaseActions.hideModal('quoteMove');
    };

    handleMove = async (values) => {
        const {BaseActions, QuoteActions, selectedRowKeys} = this.props;
        BaseActions.hideModal('quoteMove');

        console.log('selectedRowKeys', selectedRowKeys);

        try {
            const response = await QuoteActions.moveQuotes(values.folderId, selectedRowKeys);
            console.log('response', response);
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {visible, folders} = this.props;

        return (
            <QuoteMoveModal visible={visible}
                            folders={folders}
                            onMove={this.handleMove}
                            onCancel={this.handleCancel}
            />
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'quoteMove']),
        currentUser: state.base.getIn(['user', 'username']),
        folders: state.folder.get('folders').toJS(),
        selectedRowKeys: state.quote.get('selectedRowKeys').toJS(),
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        QuoteActions: bindActionCreators(quoteActions, dispatch),
        FolderActions: bindActionCreators(folderActions, dispatch),
    })
)(withRouter(QuoteMoveModalContainer));
