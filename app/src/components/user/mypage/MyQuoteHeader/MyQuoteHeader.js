import React from 'react';
import styles from './MyQuoteHeader.scss';
import classNames from 'classnames/bind';
import {Button, Layout} from "antd";
import {DeleteOutlined, PlusOutlined, ArrowRightOutlined} from '@ant-design/icons';

const cx = classNames.bind(styles);

const MyQuoteHeader = ({hasSelected, onCreateModal, onDeleteModal, onMoveModal}) => {
    const {Header, Content} = Layout;

    return (
        <Header className={cx('toolbar')}>
            <Button type="link" icon={<PlusOutlined/>} onClick={onCreateModal}>새 명언 추가</Button>
            {hasSelected ? <Button type="link" icon={<DeleteOutlined/>} onClick={onDeleteModal}>명언 삭제</Button> : null}
            {hasSelected ? <Button type="link" icon={<ArrowRightOutlined/>} onClick={onMoveModal}>명언 이동</Button> : null}
        </Header>
    );
};

export default MyQuoteHeader;