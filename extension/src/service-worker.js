import './service-worker/sidepanel-opener';
import { urlToObj } from './util.js'

chrome.webRequest.onBeforeRequest.addListener(
    function(request) {
        if (request.url.includes('ACoAAAJhTPcBcJT9e59yV6WnTZ_ewyddT1D2Jvc')) {
            console.log('params', urlToObj(request.url), request); //todo
        }
        return {cancel: false};
    },
    {urls: ["<all_urls>"]}
);
