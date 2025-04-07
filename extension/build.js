const { build } = require('esbuild');
require('dotenv').config();
var watcher = require('node-watch');

const args = process.argv.slice(2);
const watch = args.includes('--watch');

const define = {};

for (const k in process.env) {
  define[`process.env.${k}`] = JSON.stringify(process.env[k]);
}

const options = {
  entryPoints: ['extension/src/main.js'],
  outfile: 'extension/content-script.js',
  bundle: true,
  minify: !watch,
  define,
};

const buildOnce = () => build(options);

buildOnce();

if (watch) {
  watcher('extension/src', { recursive: true }, buildOnce);
}
