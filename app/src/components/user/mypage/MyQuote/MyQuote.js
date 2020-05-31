import React from 'react';
import styles from './MyQuote.scss';
import classNames from 'classnames/bind';
import {Button, Input, Layout, Table, Tag} from "antd";
import {PlusOutlined, DeleteOutlined} from '@ant-design/icons';
import _ from 'lodash';
import MyQuoteHeader from "../MyQuoteHeader";

const cx = classNames.bind(styles);

const MyQuote = ({quotes, rowSelection, pagination, onTableChange, onCreateModal, onEditModal, onDeleteModal, onMoveModal}) => {
    const {Header, Content} = Layout;
    const Search = Input.Search;

    const quoteWithKey = _.map(quotes, x => {
        _.assign(x, {'key': x['quoteId']});
        return x;
    });

    const columns = [
        {
            title: 'id',
            dataIndex: 'quoteId',
            key: 'quoteId',
            width: 70
        },
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
            title: '태그',
            dataIndex: 'tags',
            key: 'tags',
            render: tags => tags != null && (
                <span>
                    {tags.map(tag => {
                        return (
                            <Tag color={'geekblue'} key={tag}>
                                {tag.toUpperCase()}
                            </Tag>
                        );
                    })}
                </span>
            ),
        },
        {
            title: '공개여부',
            dataIndex: 'useYn',
            key: 'useYn',
            width: 100
        }
    ];

    console.log('rowSelection', rowSelection);
    const selectedSize = rowSelection.selectedRowKeys.length;

    return (
        <Layout className={cx('layout-background')}>
            <MyQuoteHeader selectedSize={selectedSize}
                           onCreateModal={onCreateModal}
                           onEditModal={onEditModal}
                           onDeleteModal={onDeleteModal}
                           onMoveModal={onMoveModal}/>
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