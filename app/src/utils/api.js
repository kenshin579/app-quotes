import {ACCESS_TOKEN} from "../constants";

const request = (options) => {
    let headers = new Headers({
        'Content-Type': 'application/json',
    });

    if (options.body instanceof FormData) {
        headers = new Headers();
    }

    let token = localStorage.getItem(ACCESS_TOKEN);
    if (token) {
        headers.append('Authorization', `Bearer ${token}`)
    }
    const defaults = {headers: headers};
    options = Object.assign({}, defaults, options);
    console.log('options', options);

    return fetch(options.url, options)
        .then(response =>
            response.json().then(json => {
                if (!response.ok) {
                    return Promise.reject(json);
                }
                return json;
            })
        );
};

const objectToQueryString = (obj) => {
    return Object.keys(obj).map(key => key + '=' + obj[key]).join('&');
};


export const login = (loginRequest) => {
    console.log('loginRequest', loginRequest);

    return request({
        url: '/api/auth/login',
        method: 'POST',
        body: JSON.stringify(loginRequest)
    });
};

export const getCurrentUser = () => {
    if (!localStorage.getItem(ACCESS_TOKEN)) {
        return Promise.reject("No access token set.");
    }

    return request({
        url: '/api/user/me',
        method: 'GET'
    });
};

export const signup = (signUpRequest) => {
    console.log('signUpRequest', signUpRequest);
    return request({
        url: "/api/auth/signup",
        method: 'POST',
        body: JSON.stringify(signUpRequest)
    });
};

export const logout = () => {
    console.log('logout');

    return request({
        url: '/api/auth/logout',
        method: 'POST'
    });
};

export const getQuoteList = (id, pagination) => {
    console.log('getRandomList :: id', id, 'pagination', pagination);

    const queryStr = objectToQueryString({
        pageIndex: pagination['current'],
        pageSize: pagination['pageSize']
    });

    let url = `/api/quotes/folders/${id}?${queryStr}`;
    console.log('url', url);

    return request({
        url: url,
        method: 'GET'
    });
};

export const getFolderList = () => {
    return request({
        url: '/api/folders',
        method: 'GET'
    });
};

export const createFolder = (folderName) => {
    console.log('folderName', folderName);

    return request({
        url: '/api/folders?folderName=' + folderName,
        method: 'POST'
    });
};

export const renameFolder = (folderId, folderName) => {
    console.log('folderId', folderId);
    console.log('folderName', folderName);

    return request({
        url: `/api/folders/${folderId}/rename?folderName=${folderName}`,
        method: 'PUT'
    });
};

export const deleteFolder = (folderId) => {
    return request({
        url: '/api/folders?folderIds=' + folderId,
        method: 'DELETE'
    });
};

export const createQuote = (folderId, quoteData) => {
    const formData = new FormData();
    for (const name in quoteData) {
        formData.append(name, quoteData[name]);
    }

    return request({
        url: '/api/quotes/folders/' + folderId,
        method: 'POST',
        body: formData
    });
};

export const deleteQuotes = (selectedRowKeys) => {
    console.log('selectedRowKeys', selectedRowKeys);
    const quoteIds = selectedRowKeys.join(',');

    return request({
        url: `/api/quotes?quoteIds=${quoteIds}`,
        method: 'DELETE',
    });
};

export const moveQuotes = (folderId, selectedRowKeys) => {
    const quoteIds = selectedRowKeys.join(',');

    return request({
        url: `/api/quotes/move/${folderId}?quoteIds=${quoteIds}`,
        method: 'PUT',
    });
};

export const getRandomList = (pagination) => {
    console.log('getRandomList :: pagination', pagination);

    const queryStr = objectToQueryString({
        pageIndex: pagination['current'],
        pageSize: pagination['pageSize']
    });

    return request({
        url: `/api/quotes/today?${queryStr}`,
        method: 'GET'
    });
};