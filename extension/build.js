const { build } = require('esbuild');
require('dotenv').config();
var watcher = require('node-watch');

const args = process.argv.slice(2);
const watch = args.includes('--watch');

const define = {};

for (const k in process.env) {
  define[`process.env.${k}`] = JSON.stringify(process.env[k]);
}

const buildEntry = (entrypoint, outfile) => build({
  entryPoints: ['extension/' + entrypoint],
  outfile: 'extension/' + outfile,
  bundle: true,
  minify: !watch,
  define
});

const buildOnce = () => {
  buildEntry('src/main.js', 'dist/content-script.js');
  buildEntry('src/sidepanel.js', 'dist/sidepanel.js');
}

buildOnce();

if (watch) {
  watcher('extension/src', { recursive: true }, buildOnce);
}
