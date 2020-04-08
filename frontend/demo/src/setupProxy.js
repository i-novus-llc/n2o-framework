const proxy = require("http-proxy-middleware");

module.exports = function(app) {
  app.use(
    proxy("/n2o", {
      target: "http://docker.one:30896/sandbox/view/4PUQ7/",
      changeOrigin: true
    })
  );
};
