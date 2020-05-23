import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as folderActions from 'store/modules/folder';
import {withRouter} from "react-router-dom";
import LeftSidebar from "../../components/user/LeftSidebar";
import * as baseActions from "../../store/modules/base";

class LeftSidebarContainer extends Component {
    componentDidMount() {
        this.loadData();
    }

    loadData = async () => {
        const {FolderActions} = this.props;
        try {
            const response = await FolderActions.getFolderList();
            console.log('response', response);
        } catch (e) {
            console.error(e);
        }
    };

    handleCreateModal = () => {
        const {BaseActions} = this.props;
        BaseActions.showModal('folderCreate');
    };

    render() {
        const {folders, loading, currentUser, authenticated, location} = this.props;
        const {handleCreateModal} = this;

        if (loading) {
            return null;
        }

        return (
            <LeftSidebar folders={folders}
                         location={location}
                         authenticated={authenticated}
                         currentUser={currentUser}
                         onCreateModal={handleCreateModal}/>
        )
    }
}

export default connect(
    (state) => ({
        visible: state.base.getIn(['modal', 'folderCreate']),
        authenticated: state.base.get('authenticated'),
        currentUser: state.base.getIn(['user', 'username']),
        folders: state.folder.get('folders').toJS(),
        loading: state.pender.pending['folder/GET_FOLDER_LIST']
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        FolderActions: bindActionCreators(folderActions, dispatch)
    })
)(withRouter(LeftSidebarContainer));
