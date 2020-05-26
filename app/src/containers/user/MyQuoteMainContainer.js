import React, {Component} from 'react';
import {connect} from 'react-redux';
import {bindActionCreators} from "redux";
import * as baseActions from 'store/modules/base';
import * as folderActions from 'store/modules/folder';
import MyQuoteMain from "../../components/user/mypage/MyQuoteMain";


class MyQuoteMainContainer extends Component {
    constructor(props) {
        super(props);
        this.state = {
            dropDownMenuNames: {
                folderRename: '폴더 이름 변경',
                folderDelete: '폴더 삭제'
            }
        }
    }

    componentDidMount() {
        this.loadData();
    }

    componentDidUpdate(prevProps, prevState) {
        if (prevProps.folderStatInfo.totalNumOfQuotes !== this.props.folderStatInfo.totalNumOfQuotes) {
            this.loadData();
        }
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


    handleMenuClick = async (folderId, e) => {
        console.log('e.key', e.key);
        console.log('folderId', folderId);
        const {BaseActions} = this.props;
        BaseActions.updateFolderModalInfo(folderId);

        try {
            switch (e.key) {
                case "folderRename" :
                    BaseActions.showModal('folderRename');
                    break;
                case "folderDelete":
                    BaseActions.showModal('folderDelete');
                    break;
            }
        } catch (e) {
            console.error(e);
        }
    };

    render() {
        const {authenticated, folderStatInfo, folders} = this.props;
        const {dropDownMenuNames} = this.state;
        const {handleMenuClick} = this;

        return (
            <MyQuoteMain authenticated={authenticated}
                         folderStatInfo={folderStatInfo}
                         folders={folders}
                         dropDownMenuNames={dropDownMenuNames}
                         onMenuClick={handleMenuClick}/>
        )
    }
}

export default connect(
    (state) => ({
        authenticated: state.base.get('authenticated'),
        folders: state.folder.get('folders').toJS(),
        folderStatInfo: state.folder.get('folderStatInfo').toJS()
    }),
    (dispatch) => ({
        BaseActions: bindActionCreators(baseActions, dispatch),
        FolderActions: bindActionCreators(folderActions, dispatch)
    })
)(MyQuoteMainContainer);
