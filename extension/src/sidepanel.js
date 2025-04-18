import * as htmx from "./htmx.js";
import { POST } from "./client.js";

htmx.config.defaultSwapStyle = 'outerHTML';
// withCredentials requires more specific cors
// however the extension doesn't specify an origin
// so we have to use * cors, which rules out withCredentials
// htmx.config.withCredentials = true;

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

const container = document.getElementById("container");
container.setAttribute('hx-post', BASE_URL + '/extension/login');
htmx.process(container.parentElement);
htmx.trigger("#container", "click", {});

const link = document.createElement('link');
link.setAttribute('rel', 'stylesheet');
const hash = DEV ? Math.random() : OUTPUT_MD5;
link.setAttribute('href', BASE_URL + '/output.css?hash=' + hash);
document.head.appendChild(link);

if (DEV) {
    (async () => {
        const response = await POST('/api/session');
        console.log(await response.text());
    })();
}
