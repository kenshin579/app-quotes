import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as folderActions from 'store/modules/folder';
import {withRouter} from "react-router-dom";
import FolderCreateModal from "components/user/modal/FolderCreateModal";


class FolderCreateModalContainer extends Component {
    handleCancel = () => {
        const {BaseActions} = this.props;
        BaseActions.hideModal('folderCreate');
    };

    handleCreate = async (values) => {
        const {BaseActions, FolderActions} = this.props;
        BaseActions.hideModal('folderCreate');

        try {
            const response = await FolderActions.createFolder(values.folderName);
            console.log('response', response);
            await FolderActions.getFolderList();
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {visible} = this.props;

        return (
            <FolderCreateModal visible={visible}
                               onCreate={this.handleCreate}
                               onCancel={this.handleCancel}/>
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'folderCreate']),
        currentUser: state.base.getIn(['user', 'username']),
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        FolderActions: bindActionCreators(folderActions, dispatch)
    })
)(withRouter(FolderCreateModalContainer));
