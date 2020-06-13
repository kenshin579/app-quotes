import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as folderActions from 'store/modules/folder';
import {withRouter} from "react-router-dom";
import FolderRenameModal from "components/user/modal/FolderRenameModal";


class FolderRenameModalContainer extends Component {
    handleCancel = () => {
        const {BaseActions} = this.props;
        BaseActions.hideModal('folderRename');
    };

    handleRename = async (values) => {
        const {BaseActions, FolderActions, folderId} = this.props;
        BaseActions.hideModal('folderRename');

        console.log('values', values);
        try {
            const response = await FolderActions.renameFolder(folderId, values.folderName);
            console.log('response', response);
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {visible} = this.props;

        return (
            <FolderRenameModal visible={visible}
                               onRename={this.handleRename}
                               onCancel={this.handleCancel}/>
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'folderRename']),
        currentUser: state.base.getIn(['user', 'username']),
        folderId: state.base.getIn(['folderModal', 'folderId']),
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        FolderActions: bindActionCreators(folderActions, dispatch)
    })
)(withRouter(FolderRenameModalContainer));
