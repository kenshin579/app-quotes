import React from 'react';
import styles from './FolderCreateModal.scss';
import classNames from 'classnames/bind';
import {Form, Input, Modal} from "antd";

const cx = classNames.bind(styles);

const FolderCreateModal = ({visible, onCreate, onCancel}) => {
    const [form] = Form.useForm();

    return (
        <Modal
            visible={visible}
            title="폴더 추가"
            okText="추가"
            cancelText="취소"
            onCancel={onCancel}
            onOk={() => {
                form.validateFields()
                    .then(values => {
                        form.resetFields();
                        onCreate(values);
                    })
                    .catch(info => {
                        console.log('Validate Failed:', info);
                    });
            }}>

            <Form
                form={form}
                layout="vertical"
                name="form_in_modal"
                initialValues={{
                    useYn: 'Y',
                }}>
                <Form.Item name="folderName"
                           label="폴더 이름"
                           rules={[
                               {
                                   required: true,
                                   message: '폴더 이름을 입력해주세요',
                               },
                           ]}>
                    <Input placeholder="폴더 이름을 입력하세요"/>
                </Form.Item>
            </Form>

        </Modal>
    );
};

export default FolderCreateModal;
