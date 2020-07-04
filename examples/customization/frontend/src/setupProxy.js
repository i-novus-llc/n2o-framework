const proxy = require('http-proxy-middleware');
module.exports = function(app) {
    app.use(proxy('/n2o',
        {
            target: "http://localhost:3001/",
            changeOrigin: true
        }
    ));
};
