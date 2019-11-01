const env = process.env.NODE_ENV;

module.exports = {
  presets: [
    ['@babel/preset-es2015', env === 'build-es' ? { modules: false } : {}],
  ]
};