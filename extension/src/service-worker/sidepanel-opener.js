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
