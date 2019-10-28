const proxy = require('http-proxy-middleware');

module.exports = function(app) {
    app.use(proxy('/n2o',
        {
            target: 'https://n2o.i-novus.ru/dev/',
            changeOrigin: true
        }
    ));
};
