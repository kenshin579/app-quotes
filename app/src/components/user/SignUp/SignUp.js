import React, {Component} from 'react';
import styles from './SignUp.scss';
import classNames from 'classnames/bind';
import {Link} from 'react-router-dom';
import {Button, Form, Input} from 'antd';

const cx = classNames.bind(styles);

class SignUp extends Component {
    render() {
        return (
            <div className={cx('signup-container')}>
                <h1 className={cx('page-title')}>회원가입</h1>
                <div className={cx('signup-content')}>
                    <Form className={cx('signup-form')}>
                        <Form.Item label="사용자 아이디" hasFeedback>
                            <Input
                                size="large"
                                name="username"
                                autoComplete="off"
                                placeholder="사용자 아이디를 입력하세요"/>
                        </Form.Item>
                        <Form.Item label="이름">
                            <Input
                                size="large"
                                name="name"
                                autoComplete="off"
                                placeholder="이름을 입력하세요"/>
                        </Form.Item>
                        <Form.Item
                            label="이메일"
                            hasFeedback>
                            <Input
                                size="large"
                                name="email"
                                type="email"
                                autoComplete="off"
                                placeholder="이메일 주소를 입력하세요"/>
                        </Form.Item>
                        <Form.Item label="암호">
                            <Input
                                size="large"
                                name="password"
                                type="password"
                                autoComplete="off"
                                placeholder="암호를 입력하세요"/>
                        </Form.Item>
                        <Form.Item>
                            <Button type="primary"
                                    htmlType="submit"
                                    size="large"
                                    className={cx('signup-form-button')}>회원가입</Button>
                            이미 계정이 있습니까?<Link to="/login">로그인으로 이동!</Link>
                        </Form.Item>
                    </Form>
                </div>
            </div>
        );
    }
}

export default SignUp;