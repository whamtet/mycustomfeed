
// on message from content-script.js
chrome.runtime.onMessage.addListener((message, sender) => {
    // The callback for runtime.onMessage must return falsy if we're not sending a response
    (async () => {
        if (message.type === 'open_side_panel') {
            // This will open a tab-specific side panel only on the current tab.
            await chrome.sidePanel.open({ tabId: sender.tab.id });
        }
    })();
});

const HOST = 'www.google.com';

chrome.tabs.onUpdated.addListener(async (tabId, info, tab) => {
    const url = new URL(tab.url);
    // Enables the side panel on google.com
    if (url.host === HOST) {
        await chrome.sidePanel.setOptions({
            tabId,
            path: 'sidepanel.html'
        });
    }
});
