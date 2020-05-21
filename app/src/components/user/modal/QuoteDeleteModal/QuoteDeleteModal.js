import React from 'react';
import styles from './QuoteDeleteModal.scss';
import classNames from 'classnames/bind';
import {Form, Input, Modal, Radio} from "antd";

const cx = classNames.bind(styles);

const QuoteDeleteModal = ({visible, onDelete, onCancel}) => {
    return (
        <Modal
            visible={visible}
            title="명언 삭제"
            okText="삭제"
            cancelText="취소"
            onCancel={onCancel}
            onOk={onDelete}>
            <p>선택한 항목을 삭제할까요?</p>
        </Modal>
    );
};

export default QuoteDeleteModal;
