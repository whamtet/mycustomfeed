import './service-worker/sidepanel-opener';
import * as requestListener from './service-worker/request-listener';
import { nth } from './util';

// on message from content-script.js
chrome.runtime.onMessage.addListener((message, sender) => {
    // The callback for runtime.onMessage must return falsy if we're not sending a response
    (async () => {
        // closing the sidePanel breaks the extension icon - don't do it!
        // you can reopen the sidePanel
        // however getOptions will always return enabled: true regardless of whether the tab is open
        // AGGGHHHH!!!
        if (message.type === 'open_side_panel') {
            // This will open a tab-specific side panel only on the current tab.
            await chrome.sidePanel.open({ tabId: sender.tab.id });
        }

        if (message.type === 'notify_load') {
            const {url, documentId} = sender;
            if (url.startsWith('https://www.linkedin.com/in/')) {
                const index = url.endsWith('/') ? -2 : -1;
                const username = nth(url.split('/'), index);
                requestListener.addUser(username, documentId);
            }
        }
    })();
});


