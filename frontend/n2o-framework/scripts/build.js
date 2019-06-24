const fs = require('fs');
const rimraf = require('rimraf');
const ejs = require('ejs');
const webpack = require('webpack');
const _ = require('lodash');
const task = require('./task');
const config = require('./config');

// Генерация index файлов для компонентов
const indexes = task('indexes', () => {
  rimraf.sync(__dirname+'/../src/components/core/AppIndex.js', { nosort: true, dot: true });
  let appT = fs.readFileSync(__dirname+'/templates/application.index.ejs', 'utf8');
  let appR = ejs.compile(appT, { filename: __dirname+'templates/application.index.ejs' });
  let appO = appR({path: config.application});
  fs.writeFileSync(__dirname+'/../src/components/core/AppIndex.js', appO, 'utf8');
  _.forEach(config.components, function(options, cType) {
    rimraf.sync(options.basePath+'/index.js', { nosort: true, dot: true });
    const template = fs.readFileSync(__dirname+'/templates/components.index.ejs', 'utf8');
    const render = ejs.compile(template, { filename: __dirname+'templates/components.index.ejs' });
    const output = render(options);
    fs.writeFileSync(options.basePath+'/index.js', output, 'utf8');
  });
});

// Copy ./index.html into the /public folder
const html = task('html', () => {
  const webpackConfig = require('../config/webpack.config.old');
  const assets = JSON.parse(fs.readFileSync('./public/dist/assets.json', 'utf8'));
  const template = fs.readFileSync('./public/index.ejs', 'utf8');
  const render = ejs.compile(template, { filename: './public/index.ejs' });
  const output = render({ debug: webpackConfig.debug, bundle: assets.main.js, config });
  fs.writeFileSync('./public/index.html', output, 'utf8');
});

// Bundle JavaScript, CSS and image files with Webpack
const bundle = task('bundle', () => {
  const webpackConfig = require('../config/webpack.config.old');
  return new Promise((resolve, reject) => {
    webpack(webpackConfig).run((err, stats) => {
      if (err) {
        reject(err);
      } else {
        console.log(stats.toString(webpackConfig.stats));
        resolve();
      }
    });
  });
});

//
// Build website into a distributable format
// -----------------------------------------------------------------------------
module.exports = task('build', () => {
  global.DEBUG = process.argv.includes('--debug') || false;
  rimraf.sync('public/dist/*', { nosort: true, dot: true });
  return Promise.resolve()
    .then(indexes)
    .then(bundle)
    .then(html);
});
