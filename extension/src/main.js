import { GET } from "./client.js";

const clickButton = () => {
    // the sidePanel API is too broken to do anything other than open.
    // in theory we could ping sidepanel.html, but let's keep things simple for now
    chrome.runtime.sendMessage({ type: 'open_side_panel' })
}

(async () => {
    const response = await GET('/api/button');
    const html = await response.text();

    const div = document.createElement('div');
    div.innerHTML = html;
    document.body.append(div);

    div.addEventListener('click', clickButton);
})()
