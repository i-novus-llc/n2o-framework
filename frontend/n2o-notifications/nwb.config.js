module.exports = {
  type: "react-component",
  npm: {
    esModules: false,
    umd: false
  },
  karma: {
    testContext: "tests.webpack.js"
  },
  devServer: {
    proxy: {
      "/wp/bank/n2o/data": {
        target: "http://localhost:8080/",
        changeOrigin: true
      },
      "/wp/bank/n2o": {
        target: "http://localhost:8080/",
        changeOrigin: true
      },
      "/ws": {
        target: "http://docker.one:30675/",
        changeOrigin: true,
        ws: true
      }
    }
  }
};
