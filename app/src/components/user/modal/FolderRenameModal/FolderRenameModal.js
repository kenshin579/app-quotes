import React from 'react';
import {Form, Input, Modal} from "antd";

const FolderRenameModal = ({visible, onRename, onCancel}) => {
    const [form] = Form.useForm();

    return (
        <Modal
            visible={visible}
            title="폴더 이름 변경"
            okText="변경"
            cancelText="취소"
            onCancel={onCancel}
            onOk={() => {
                form.validateFields()
                    .then(values => {
                        form.resetFields();
                        onRename(values);
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

export default FolderRenameModal;
