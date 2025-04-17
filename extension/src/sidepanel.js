import * as htmx from "./htmx.js";

const BASE_URL = process.env.BASE_URL;

htmx.config.defaultSwapStyle = 'outerHTML';

// const frame = document.createElement('iframe');
// frame.src = BASE_URL + '/';
// document.body.appendChild(frame);

htmx.defineExtension('zession', {
    encodeParameters : function(xhr, parameters, elt) {
        const kookie = localStorage.getItem('kookie');
        if (kookie) {
            xhr.setRequestHeader('kookie', kookie);
        }
    },
    transformResponse : function(text, xhr, elt) {
        const kookie = xhr.getResponseHeader('kookie');
        if (kookie) {
            localStorage.setItem('kookie', kookie);
        }

        return text;
    },
});
//

const container = document.getElementById("container");
container.setAttribute('hx-post', BASE_URL + '/extension/login');
htmx.process(container.parentElement);
htmx.trigger("#container", "click", {});

const link = document.createElement('link');
link.setAttribute('rel', 'stylesheet');
link.setAttribute('href', BASE_URL + '/output.css');

document.head.appendChild(link);
