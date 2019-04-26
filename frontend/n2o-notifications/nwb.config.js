module.exports = {
  type: "react-component",
  npm: {
    esModules: true,
    umd: false
  },
  devServer: {
    proxy: {
      "/n2o/data": {
        target: "https://n2o.i-novus.ru/dev/",
        changeOrigin: true
      },
      "/n2o": {
        target: "https://n2o.i-novus.ru/dev/",
        changeOrigin: true
      }
    }
  }
};
