module.exports = {
  rootPath: './',
  watchDirs: ['src'],
  containerQuerySelector: '#root',
  globalImports: ['n2o/dist/n2o.css', '@fortawesome/fontawesome-free/css/all.css', 'src/cosmos.css'],
  webpackConfigPath: 'react-scripts/config/webpack.config',
  publicPath: 'public',
  // Optional: Add this when you start using proxies
  proxiesPath: 'src/cosmos.proxies',
};
