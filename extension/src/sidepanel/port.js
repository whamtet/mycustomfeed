import * as htmx from "./htmx";
import { $ } from "../util";

let port = chrome.runtime.connect();
const tabId = location.href.split('=')[1];

port.onDisconnect.addListener(() => {
    port = chrome.runtime.connect();
})

port.onMessage.addListener(({type, msg}) => {
    if (type === 'to_sidePanel') {
        if (msg === 'heartbeat') {
            if (document.getElementById('return-to-linkedin')) {
                $('#refresh-extension').click();
            }
        }
        if (msg === 'disconnected') {
            if (!document.getElementById('return-to-linkedin')) {
                $('#show-warning').click();
            }
        }
    }
});

port.postMessage({type: "connect_tab", tabId});
const toTab = msg => port.postMessage({type: "to_tab", tabId, msg});

setInterval(() => toTab('heartbeat'), 500);
