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
    })();
});

const HOST = 'www.linkedin.com';

chrome.tabs.onUpdated.addListener(async (tabId, info, tab) => {
    const url = new URL(tab.url);
    if (url.host === HOST) {
        await chrome.sidePanel.setOptions({
            tabId,
            path: 'sidepanel.html'
        });
    }
});
