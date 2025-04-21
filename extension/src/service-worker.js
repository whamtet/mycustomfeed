import './service-worker/sidepanel-opener';
import * as requestListener from './service-worker/request-listener';
import { nth } from './util';

const toTab = {};
const toSidePanel = {};

chrome.runtime.onConnect.addListener((port) => port.onMessage.addListener(message => {
    // The callback for runtime.onMessage must return falsy if we're not sending a response

    // closing the sidePanel breaks the extension icon - don't do it!
    // you can reopen the sidePanel
    // however getOptions will always return enabled: true regardless of whether the tab is open
    // AGGGHHHH!!!
    if (message.type === 'open_side_panel') {
        // This will open a tab-specific side panel only on the current tab.
        chrome.sidePanel.open({ tabId: port.sender.tab.id });
        toTab[port.sender.tab.id] = msg => port.postMessage(msg);
    }

    if (message.type === 'notify_load') {
        const {url, documentId} = port.sender;
        if (url.startsWith('https://www.linkedin.com/in/')) {
            const index = url.endsWith('/') ? -2 : -1;
            const username = nth(url.split('/'), index);
            requestListener.addUser(username, documentId);
        }
    }

    if (message.type === 'connect_tab') {
        toSidePanel[message.tabId] = msg => port.postMessage(msg);
    }

    if (message.type === 'to_tab') {
        toTab[message.tabId]({msg: message.msg, type: message.type});
    }
    if (message.type === 'to_sidePanel') {
        toSidePanel[port.sender.tab.id]({msg: message.msg, type: message.type});
    }
}));

