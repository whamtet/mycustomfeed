const BASE_URL = process.env.BASE_URL;

htmx.config.defaultSwapStyle = 'outerHTML';

htmx.ajax('GET', BASE_URL + '/extension/login?count=0', '#container')
