import * as htmx from "./htmx.js";

const BASE_URL = process.env.BASE_URL;

htmx.config.defaultSwapStyle = 'outerHTML';

// const frame = document.createElement('iframe');
// frame.src = BASE_URL + '/';
// document.body.appendChild(frame);

htmx.defineExtension('zession', {
    encodeParameters : function(xhr, parameters, elt) {
        xhr.setRequestHeader('kookie', 'hi');
    }
});

htmx.ajax('GET', BASE_URL + '/extension/login?count=0', '#container')
