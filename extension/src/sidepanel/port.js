import * as htmx from "./htmx";
import { $ } from "../util";

const port = chrome.runtime.connect();
const tabId = location.href.split('=')[1];

const onDisconnect = () => {
    if (!document.getElementById('return-to-linkedin')) {
        $('#show-warning').click();
    }
};
const onHeartbeat = () => {
    if (document.getElementById('return-to-linkedin')) {
        $('#refresh-extension').click();
    }
}

port.onMessage.addListener(({type, msg}) => {
    if (type === 'to_sidePanel') {
        if (msg === 'heartbeat') {
            onHeartbeat();
        }
        if (msg === 'disconnected') {
            onDisconnect();
        }
    }
});

port.postMessage({type: "connect_tab", tabId});
const toTab = msg => port.postMessage({type: "to_tab", tabId, msg});

setInterval(() => toTab('heartbeat'), 500);
