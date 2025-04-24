import './service-worker/sidepanel-opener';
import * as requestListener from './service-worker/request-listener';
import * as util from './util';

const toTab = {};
const toSidePanel = {};
const sidePanelQueue = {};

chrome.runtime.onConnect.addListener((port) => {

    const tabId = port.sender.tab?.id; // only defined when opening from content script

    port.onMessage.addListener((message) => {
        // The callback for runtime.onMessage must return falsy if we're not sending a response
        // now we are using runtime.onConnect

        // closing the sidePanel breaks the extension icon - don't do it!
        // you can reopen the sidePanel
        // however getOptions will always return enabled: true regardless of whether the tab is open
        // AGGGHHHH!!!
        if (message.type === 'open_side_panel') {
            // This will open a tab-specific side panel only on the current tab.
            chrome.sidePanel.open({ tabId });
        }

        if (message.type === 'open_tab') {
            toTab[tabId] = msg => port.postMessage(msg);
        }

        if (message.type === 'connect_sidePanel') {
            toSidePanel[message.tabId] = msg => port.postMessage(msg);

            const queue = sidePanelQueue[message.tabId];
            if (queue) {
                port.postMessage(queue[queue.length - 1]);
                delete sidePanelQueue[message.tabId];
            }
        }

        if (message.type === 'to_tab') {
            const f = toTab[message.tabId];
            if (f) {
                f({msg: message.msg, type: message.type});
            } else {
                port.postMessage({msg: 'disconnected', type: 'to_sidePanel'});
            }
        }
        if (message.type === 'to_sidePanel') {
            // annotate with urn if necessary
            const {msg, type} = message;
            if (msg.linkedin) {
                msg.urn = requestListener.getUrn(port.sender.documentId);
            }
            const toSend = {msg, type};

            const f = toSidePanel[tabId];
            if (f) {
                f(toSend);
            } else {
                // enqueue it
                util.enqueueKey(sidePanelQueue, tabId, toSend);
            }
        }
    });
    port.onDisconnect.addListener((port) => {
        if (tabId) {
            delete toTab[tabId];
            delete sidePanelQueue[tabId];
        } else {
            const tabIdSidePanel = port.sender.url.split('=')[1];
            delete toSidePanel[tabIdSidePanel];
        }
    })
});

