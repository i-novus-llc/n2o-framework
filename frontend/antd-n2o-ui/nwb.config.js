module.exports = {
  type: "react-component",
  npm: {
    esModules: true,
    umd: false
  },
  devServer: {
    hot: true,
    proxy: {
      "/n2o": "http://localhost:9090"
    }
  }
};
