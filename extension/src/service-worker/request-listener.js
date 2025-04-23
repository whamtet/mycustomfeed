
const userProfileSubstrings = [
    'voyagerIdentityDashOpenToCards',
    'voyagerTrustDashVerificationEntryPointPages'
];

const urlRegex = /fsd_profile%3A([a-zA-Z0-9_-]+)/;

const documentToUrn = {};

chrome.webRequest.onBeforeRequest.addListener(
    function(request) {
        userProfileSubstrings.forEach(profileSubstring => {
            if (request.url.includes(profileSubstring)) {
                // console.log('request.url', request.url);
                const regexMatch = request.url.match(urlRegex);
                if (regexMatch) {
                    const profile = regexMatch[1];
                    documentToUrn[request.documentId] = profile;
                } else {
                    console.error('unmatch', request.url);
                }
            }
        });
        return {cancel: false};
    },
    {urls: ["<all_urls>"]}
);

export const getUrn = documentId => documentToUrn[documentId];
