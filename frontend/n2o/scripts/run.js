const fs = require('fs');
const ejs = require('ejs');
const rimraf = require('rimraf');
const webpack = require('webpack');
const Browsersync = require('browser-sync');
const _ = require('lodash');
const task = require('./task');
const config = require('./config');

global.HMR = !process.argv.includes('--no-hmr'); // Hot Module Replacement (HMR)

// Build the app and launch it in a browser for testing via Browsersync
module.exports = task('run', () => new Promise((resolve) => {
  rimraf.sync('public/dist/*', { nosort: true, dot: true });
  // rimraf.sync(__dirname+'/../src/components/core/AppIndex.js', { nosort: true, dot: true });
  // let appT = fs.readFileSync(__dirname+'/templates/application.index.ejs', 'utf8');
  // let appR = ejs.compile(appT, { filename: __dirname+'templates/application.index.ejs' });
  // let appO = appR({path: config.application});
  // fs.writeFileSync(__dirname+'/../src/components/core/AppIndex.js', appO, 'utf8');
  // _.forEach(config.components, function(options, cType) {
  //   rimraf.sync(options.basePath+'/index.js', { nosort: true, dot: true });
  //   const template = fs.readFileSync(__dirname+'/templates/components.index.ejs', 'utf8');
  //   const render = ejs.compile(template, { filename: __dirname+'templates/components.index.ejs' });
  //   const output = render(options);
  //   fs.writeFileSync(options.basePath+'/index.js', output, 'utf8');
  // });
  let count = 0;
  const bs = Browsersync.create();
  const webpackConfig = require('../config/webpack.config.dev');
  const compiler = webpack(webpackConfig);
  // Node.js middleware that compiles application in watch mode with HMR support
  // http://webpack.github.io/docs/webpack-dev-middleware.html
  const webpackDevMiddleware = require('webpack-dev-middleware')(compiler, {
    publicPath: webpackConfig.output.publicPath,
    stats: webpackConfig.stats,
  });

  compiler.plugin('done', (stats) => {
    // Generate index.html page
    const bundle = stats.compilation.chunks.find(x => x.name === 'main').files[0];
    const template = fs.readFileSync('./public/index.ejs', 'utf8');
    const render = ejs.compile(template, { filename: './public/index.ejs' });
    const output = render({ debug: true, bundle: `/dist/${bundle}`, config });
    fs.writeFileSync('./public/index.html', output, 'utf8');

    // Launch Browsersync after the initial bundling is complete
    // For more information visit https://browsersync.io/docs/options
    count += 1;
    if (count === 1) {
      bs.init({
        port: process.env.PORT || 3000,
        ui: { port: Number(process.env.PORT || 3000) + 1 },
        open: false,
        server: {
          baseDir: 'public',
          middleware: [
            webpackDevMiddleware,
            require('webpack-hot-middleware')(compiler),
            require('connect-history-api-fallback')(),
            require('http-proxy-middleware')(['/n2o'], {
              target: 'http://localhost:8080',
              changeOrigin: true,
            }),
          ],
        },
      }, resolve);
    }
  });
}));
