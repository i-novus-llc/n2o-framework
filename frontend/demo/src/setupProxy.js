const proxy = require("http-proxy-middleware");

module.exports = function(app) {
  app.use(
    proxy("/n2o", {
      target: "http://docker.one:32439/sandbox/view/Ra1BB/",
      changeOrigin: true
    })
  );
};
