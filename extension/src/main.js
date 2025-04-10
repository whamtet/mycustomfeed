import { GET } from "./client.js";

const clickButton = () => {
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
