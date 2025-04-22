const { build } = require('esbuild');
require('dotenv').config();
const watcher = require('node-watch');
const fs = require('fs');
const md5 = require('md5');

const args = process.argv.slice(2);
const watch = args.includes('--watch');

const define = {
  'BASE_URL': watch ? "'http://localhost:3002'" : "'https://app.mycustomfeed.com'",
  'DEV': String(watch),
};

const buildEntry = (entrypoint, outfile, md5) => build({
  entryPoints: ['extension/' + entrypoint],
  outfile: 'extension/' + outfile,
  bundle: true,
  minify: !watch,
  define: {
    ...define,
    'OUTPUT_MD5': `'${md5}'`
  }
});

const calcMD5 = () => {
  const output = new String(fs.readFileSync('./resources/public/output.css'));
  return md5(output);
}

const buildOnce = () => {
  buildEntry('src/service-worker.js', 'dist/service-worker.js', '');
  buildEntry('src/sidepanel.js', 'dist/sidepanel.js', calcMD5());
}

buildOnce();

if (watch) {
  watcher('extension/src', { recursive: true }, buildOnce);
}
