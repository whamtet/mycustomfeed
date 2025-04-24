export const urlToObj = s => {
    const paramsRaw = s.split('?')[1];
    const params = new URLSearchParams(paramsRaw);
    const out = {};
    for (const [key, value] of params) {
        out[key] = value;
    }
    return out;
}

export const nth = (l, i) => l[i < 0 ? i + l.length : i];

export const $ = x => document.body.querySelector(x);
export const $$ = x => document.body.querySelectorAll(x);

export const enqueueKey = (m, k, v) => {
    if (!m[k]) {
        m[k] = [];
    }
    m[k].push(v);
}

export const afterId = (id, f) => {
    if (document.getElementById(id)) {
        f();
    } else {
        setTimeout(() => afterId(id, f), 100);
    }
};
