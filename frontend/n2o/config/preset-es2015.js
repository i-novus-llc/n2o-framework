const env = process.env.NODE_ENV;

module.exports = {
  presets: [
    ['es2015', env === 'build-es' ? { modules: false } : {}],
  ]
};