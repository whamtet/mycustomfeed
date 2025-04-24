export const addLink = () => {
    const oldLink = document.head.querySelector('link[rel="stylesheet"]');

    const link = document.createElement('link');
    link.setAttribute('rel', 'stylesheet');
    const hash = DEV ? Math.random() : OUTPUT_MD5;
    link.setAttribute('href', BASE_URL + '/output.css?hash=' + hash);

    if (oldLink) {
        console.log('replacing');
        oldLink.replaceWith(link);
    } else {
        document.head.appendChild(link);
    }
}
