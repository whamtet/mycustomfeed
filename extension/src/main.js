import { GET } from "./client.js";

(async () => {
    const response = await GET('/api/button');
    const html = await response.text();

    const div = document.createElement('div');
    div.innerHTML = html;
    document.body.append(div);
})()
