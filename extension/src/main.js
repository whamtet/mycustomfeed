import { GET } from "./client.js";

const clickSidePanelButton = () => {
    // the sidePanel API is too broken to do anything other than open.
    // in theory we could ping sidepanel.html, but let's keep things simple for now
    chrome.runtime.sendMessage({ type: 'open_side_panel' });
}

const addSidePanelButton = async () => {
    const response = await GET('/api/button');
    const html = await response.text();

    const div = document.createElement('div');
    div.innerHTML = html;
    document.body.append(div);

    div.addEventListener('click', clickSidePanelButton);
}
addSidePanelButton();

const notifyProfilePage = () => {
    // document.body.innerHTML.match(/urn:li:fsd_profile:([a-zA-Z0-9]+)/g);
}