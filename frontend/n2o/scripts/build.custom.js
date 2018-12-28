/**
 * N2O сборка проекта
 */

const fs = require('fs');
const rimraf = require('rimraf');
const ejs = require('ejs');
const webpack = require('webpack');
const _ = require('lodash');
const fse = require('fs-extra');
const merge = require('deepmerge');
const path = require('path');
const task = require('./task');
const config = require('./config');
let customConfig = customOptions = {};

function configPathNormalize(cfg, rootPath) {
  let afp = path.resolve(rootPath, cfg.application);
  cfg.application = path.relative(rootPath, afp).replace(/\\/g, '/');
  _.forEach(cfg.components, function(c, cType) {
    let basePath = config.components[cType].basePath;
    _.forEach(c.list, function(l, k) {
      let filePath = path.resolve(rootPath, l.path);
      l.path = path.relative(basePath, filePath).replace(/\\/g, '/');
    });
  });
  return config;
}

// Генерация index файлов для компонентов
const indexes = task('indexes', () => {
  let cfg = merge(config, customConfig, {clone: true});
  _.forEach(cfg.components, function(options, cType) {
    rimraf.sync(options.basePath+'/index.js', { nosort: true, dot: true });
    const template = fs.readFileSync(__dirname+'/components.index.ejs', 'utf8');
    const render = ejs.compile(template, { filename: __dirname+'components.index.ejs' });
    const output = render(options);
    fs.writeFileSync(options.basePath+'/index.js', output, 'utf8');
  });
});

// Copy ./index.html into the /public folder
const html = task('html', () => {
  const webpackConfig = require('./webpack.config');
  const assets = JSON.parse(fs.readFileSync(__dirname+'/../public/dist/assets.json', 'utf8'));
  const template = fs.readFileSync(__dirname+'/../public/index.ejs', 'utf8');
  const render = ejs.compile(template, { filename: __dirname+'/../public/index.ejs' });
  const output = render({ debug: webpackConfig.debug, bundle: assets.main.js, config });
  fs.writeFileSync(__dirname+'/../public/index.html', output, 'utf8');
});

// Bundle JavaScript, CSS and image files with Webpack
const bundle = task('bundle', () => {
  const webpackConfig = require('./webpack.config');
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

// Копирование в нужный каталог
const copy = task('copy', () => {
  let output = customOptions.output;
  const rootPath = customOptions.rootPath;
  output = path.relative(rootPath, output);
  rimraf.sync(output, { nosort: true, dot: true });
  fse.copySync(path.resolve(__dirname, '../public'), output);
});

//
// Build website into a distributable format
// -----------------------------------------------------------------------------
module.exports = function (config, options) {
  customOptions = options;
  const rootPath = customOptions.rootPath;
  customConfig = configPathNormalize(config, rootPath);
  task('build.custom', () => {
    global.DEBUG = process.argv.includes('--debug') || false;
    rimraf.sync(path.resolve(__dirname, '../public/dist/*'), { nosort: true, dot: true });
    return Promise.resolve()
      .then(indexes)
      .then(bundle)
      .then(html)
      .then(copy);
  })();
}
