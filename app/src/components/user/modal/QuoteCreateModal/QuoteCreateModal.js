import React from 'react';
import styles from './QuoteCreateModal.scss';
import classNames from 'classnames/bind';
import {Form, Input, Modal, Radio} from "antd";

const cx = classNames.bind(styles);

const QuoteCreateModal = ({visible, onCreate, onCancel}) => {
    const [form] = Form.useForm();
    const {TextArea} = Input;

    return (
        <Modal
            visible={visible}
            title="명언 추가"
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
                <Form.Item name="quoteText"
                           label="명언"
                           rules={[
                               {
                                   required: true,
                                   message: '명언을 입력해주세요',
                               },
                           ]}>
                    <TextArea rows={4} placeholder='명언을 입력해주세요'/>
                </Form.Item>
                <Form.Item name="authorName"
                           label="저자 이름">
                    <Input placeholder="저자 이름을 입력하세요"/>
                </Form.Item>
                <Form.Item name="tags"
                           label="태그"
                           rules={[
                               {
                                   required: true,
                                   message: '태그는 최소 2개 이상은 입력해주세요',
                               },
                           ]}>
                    <Input placeholder="태그를 입력해주세요"/>
                </Form.Item>

                <Form.Item name="useYn">
                    <Radio.Group>
                        <Radio value="Y">공개</Radio>
                        <Radio value="N">비공개</Radio>
                    </Radio.Group>
                </Form.Item>
            </Form>

        </Modal>
    );
};

export default QuoteCreateModal;
