import React from 'react';
import styles from './Login.scss';
import classNames from 'classnames/bind';
import {Button, Form, Input} from "antd";
import {Link} from "react-router-dom";
import {UserOutlined} from '@ant-design/icons';

const cx = classNames.bind(styles);

const Login = ({onFinish}) => {
    const [form] = Form.useForm();

    const onFinishFailed = ({ errorFields }) => {
        form.scrollToField(errorFields[0].name);
    };

    return (
        <div className={cx('login-container')}>
            <h1 className={cx('page-title')}>로그인</h1>
            <div className={'login-content'}>
                <Form className={cx("login-form")} form={form} onFinish={onFinish} onFinishFailed={onFinishFailed}>
                    <Form.Item
                        label="아이디"
                        name="username"
                        rules={[{required: true, message: 'Please input your username or email!'}]}>
                        <Input
                            prefix={<UserOutlined />}
                            size="large"
                            name="username"
                            placeholder="Username or Email"/>
                    </Form.Item>
                    <Form.Item
                        label="암호"
                        name="password"
                        rules={[{required: true, message: 'Please input your Password!'}]}>
                        <Input.Password
                            size="large"
                            name="password"
                            type="password"
                            placeholder="Password"/>
                    </Form.Item>
                    <Form.Item>
                        <Button type="primary" htmlType="submit" size="large"
                                className={cx('login-form-button')}>로그인</Button>
                        <Link to="/signup"> 회원 가입!</Link>
                    </Form.Item>
                </Form>
            </div>
        </div>
    );
};

export default Login;