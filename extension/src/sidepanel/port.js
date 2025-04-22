const port = chrome.runtime.connect();
const tabId = location.href.split('=')[1];

port.onMessage.addListener(({type, msg}) => {
    if (type === 'to_sidePanel') {
        console.log('sidePanel got', msg);
    }
});

port.postMessage({type: "connect_tab", tabId});
const toTab = msg => port.postMessage({type: "to_tab", tabId, msg});
toTab({greeting: "hi"});
