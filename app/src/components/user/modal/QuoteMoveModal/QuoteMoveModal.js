import React from 'react';
import styles from './QuoteMoveModal.scss';
import classNames from 'classnames/bind';
import {Form, Modal, Select} from "antd";
import _ from "lodash";

const cx = classNames.bind(styles);

const {Option} = Select;

const QuoteMoveModal = ({visible, folders, onMove, onCancel}) => {
    const [form] = Form.useForm();
    let selectOptionListView = [];

    //todo: 현재 폴더는 제외하고 보여줘야 함
    _(folders).forEach(folder => {
        selectOptionListView.push(
            <Option value={`${folder.folderId}`}>{folder.folderName}</Option>
        )
    });

    return (
        <Modal
            visible={visible}
            title="명언 이동"
            okText="이동"
            cancelText="취소"
            onCancel={onCancel}
            onOk={() => {
                form.validateFields()
                    .then(values => {
                        form.resetFields();
                        onMove(values);
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
                <Form.Item name="folderId">
                    <Select
                        showSearch
                        style={{width: 300}}
                        placeholder="이동하려는 폴더를 선택하세요"
                        optionFilterProp="children"
                        filterOption={(input, option) =>
                            option.children.toLowerCase().indexOf(input.toLowerCase()) >= 0
                        }>
                        {selectOptionListView}
                    </Select>
                </Form.Item>
            </Form>

        </Modal>
    );
};

export default QuoteMoveModal;
