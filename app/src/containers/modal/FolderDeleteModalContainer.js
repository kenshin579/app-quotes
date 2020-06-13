import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as folderActions from 'store/modules/folder';
import {withRouter} from "react-router-dom";
import DeleteModal from "components/user/modal/DeleteModal";
import {DELETE_MODAL_TYPE} from "../../constants";


class FolderDeleteModalContainer extends Component {
    handleCancel = () => {
        const {BaseActions} = this.props;
        BaseActions.hideModal('folderDelete');
    };

    handleDelete = async () => {
        const {BaseActions, FolderActions, folderId} = this.props;
        BaseActions.hideModal('folderDelete');

        try {
            const response = await FolderActions.deleteFolder(folderId);
            console.log('response', response);
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {visible} = this.props;

        return (
            <DeleteModal visible={visible}
                         deleteModalType={DELETE_MODAL_TYPE.FOLDER}
                         onDelete={this.handleDelete}
                         onCancel={this.handleCancel}/>
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'folderDelete']),
        currentUser: state.base.getIn(['user', 'username']),
        folderId: state.base.getIn(['folderModal', 'folderId']),
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        FolderActions: bindActionCreators(folderActions, dispatch)
    })
)(withRouter(FolderDeleteModalContainer));
