import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as folderActions from 'store/modules/folder';
import {withRouter} from "react-router-dom";
import LeftSidebar from "../../components/user/LeftSidebar";

class LeftSidebarContainer extends Component {
    componentDidMount() {
        this.loadData();
    }

    loadData = async () => {
        const {FolderActions} = this.props;
        try {
            const response = await FolderActions.getFolderList();
            console.log('[FRANK] response', response);
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {folders, loading, currentUser, authenticated, location} = this.props;
        console.log('LeftSideBarContainer :: location', location);

        if (loading) {
            return null;
        }

        return (
            <LeftSidebar folders={folders}
                         location={location}
                         authenticated={authenticated}
                         currentUser={currentUser}/>
        )
    }
}

export default connect(
    (state) => ({
        authenticated: state.base.get('authenticated'),
        currentUser: state.base.getIn(['user', 'username']),
        folders: state.folder.get('folders').toJS(),
        loading: state.pender.pending['folder/GET_FOLDER_LIST']
    }),
    (dispatch) => ({
        FolderActions: bindActionCreators(folderActions, dispatch)
    })
)(withRouter(LeftSidebarContainer));
