import React from 'react';
import {Modal} from "antd";

const DeleteModal = ({visible, deleteModalType, onDelete, onCancel}) => {
    return (
        <Modal
            visible={visible}
            title={`${deleteModalType} 삭제`}
            okText="삭제"
            cancelText="취소"
            onCancel={onCancel}
            onOk={onDelete}>
            <p>선택한 항목을 삭제할까요?</p>
        </Modal>
    );
};

export default DeleteModal;
