import React from 'react';
import styles from './MyQuoteHeader.scss';
import classNames from 'classnames/bind';
import {Button, Layout} from "antd";
import {DeleteOutlined, PlusOutlined, ArrowRightOutlined, EditOutlined} from '@ant-design/icons';

const cx = classNames.bind(styles);

const MyQuoteHeader = ({selectedSize, onCreateModal, onEditModal, onDeleteModal, onMoveModal}) => {
    const {Header, Content} = Layout;

    const hasSelected = selectedSize > 0;

    return (
        <Header className={cx('toolbar')}>
            <Button type="link" icon={<PlusOutlined/>} onClick={onCreateModal}>새 명언 추가</Button>
            {selectedSize === 1 ?
                <Button type="link" icon={<EditOutlined/>} onClick={onEditModal}>명언 편집</Button> : null}
            {hasSelected ? <Button type="link" icon={<DeleteOutlined/>} onClick={onDeleteModal}>명언 삭제</Button> : null}
            {hasSelected ? <Button type="link" icon={<ArrowRightOutlined/>} onClick={onMoveModal}>명언 이동</Button> : null}
        </Header>
    );
};

export default MyQuoteHeader;