export const urlToObj = s => {
    const paramsRaw = s.split('?')[1];
    const params = new URLSearchParams(paramsRaw);
    const out = {};
    for (const [key, value] of params) {
        out[key] = value;
    }
    return out;
}
