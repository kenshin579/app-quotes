import {ACCESS_TOKEN} from "../constants";

const request = (options) => {
    const headers = new Headers({
        'Content-Type': 'application/json',
    });

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
export const login = (loginData) => {
    console.log('loginData', loginData);

    return request({
        url: '/api/auth/login',
        method: 'POST',
        body: JSON.stringify(loginData)
    });
};
export const signup = () => console.log('signup');
export const logout = () => console.log('logout');
