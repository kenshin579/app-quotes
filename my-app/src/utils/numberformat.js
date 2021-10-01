
const objectToQueryString = (obj) => {
    return Object.keys(obj).map(key => key + '=' + obj[key]).join('&');
};

export const convertNumberWithComma = (value) => {
    return value.toLocaleString(navigator.language, { minimumFractionDigits: 0 });
};
