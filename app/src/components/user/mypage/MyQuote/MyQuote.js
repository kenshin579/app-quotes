import React from 'react';
import styles from './MyQuote.scss';
import classNames from 'classnames/bind';
import {Button, Input, Layout, Table} from "antd";
import {PlusOutlined, DeleteOutlined} from '@ant-design/icons';
import _ from 'lodash';

const cx = classNames.bind(styles);

const MyQuote = ({quotes, rowSelection, pagination, onTableChange, onCreateModal, onDeleteModal}) => {
    const {Header, Content} = Layout;
    const Search = Input.Search;

    const quoteWithKey = _.map(quotes, x => {
        _.assign(x, {'key': x['quoteId']});
        return x;
    });

    const columns = [
        {
            title: '명언',
            dataIndex: 'quoteText',
            key: 'quoteText',
            ellipsis: true
        },
        {
            title: '저자',
            dataIndex: 'authorName',
            key: 'authorName',
            width: 100
        },
        {
            title: '공개여부',
            dataIndex: 'useYn',
            key: 'useYn',
            width: 100
        }
    ];

    console.log('rowSelection', rowSelection);
    const hasSelected = rowSelection.selectedRowKeys.length > 0;

    return (
        <Layout className="layout-background">
            <Header className="toolbar">
                <Button type="link" icon={<PlusOutlined/>} onClick={onCreateModal}>새 명언 추가</Button>
                {hasSelected ? <Button type="link" icon={<DeleteOutlined />} onClick={onDeleteModal}>명언 삭제</Button> : null}
            </Header>
            <Content style={{margin: 20, background: '#fff', padding: 24}}>
                {/*<div className={cx('content-title')}>내 명언</div>*/}
                <header className={cx('header')}>
                    <h1 className={cx('title')}>내 명언</h1>
                    {/*<Search className={cx('action')} placeholder="Enter Title"*/}
                    {/*        style={{width: 200}}/>*/}
                </header>
                <Table style={{marginTop: 20}}
                       columns={columns}
                       rowSelection={rowSelection}
                       dataSource={quoteWithKey}
                       pagination={pagination}
                       onChange={onTableChange}
                />
            </Content>
        </Layout>
    );
};

export default MyQuote;