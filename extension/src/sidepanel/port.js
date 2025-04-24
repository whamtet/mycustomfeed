import * as htmx from "./htmx";
import { $, afterId } from "../util";
import { addLink } from "../dom";

let port;
const tabId = location.href.split('=')[1];

const newPort = () => {
    port = chrome.runtime.connect();
    port.onDisconnect.addListener(newPort);

    port.onMessage.addListener(({type, msg}) => {
        if (type === 'to_sidePanel') {
            if (msg === 'heartbeat') {
                if (document.getElementById('return-to-linkedin')) {
                    $('#refresh-extension').click();
                }
            } else if (msg === 'disconnected') {
                if (!document.getElementById('return-to-linkedin')) {
                    $('#show-warning').click();
                }
            } else {
                afterId(
                    'profile',
                    () => {
                        if (DEV) {
                            addLink();
                        }
                        htmx.ajax(
                            'POST',
                            BASE_URL + '/extension/profile',
                            {
                                target: '#profile',
                                values: {info: JSON.stringify(msg)},
                            }
                        )
                    });
            }
        }
    });
}
newPort();

const toTab = msg => port.postMessage({type: "to_tab", tabId, msg});

setInterval(() => toTab('heartbeat'), 500);
