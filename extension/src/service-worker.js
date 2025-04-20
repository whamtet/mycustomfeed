import './service-worker/sidepanel-opener';

const urlToObj = s => {
    const paramsRaw = s.split('?')[1];
    const params = new URLSearchParams(paramsRaw);
    const out = {};
    for (const [key, value] of params) {
        out[key] = value;
    }
    return out;
}

chrome.webRequest.onBeforeRequest.addListener(
    function(request) {
        if (request.url.includes('ACoAAAJhTPcBcJT9e59yV6WnTZ_ewyddT1D2Jvc')) {
            console.log('params', urlToObj(request.url), request); //todo
        }
        return {cancel: false};
    },
    {urls: ["<all_urls>"]}
);
