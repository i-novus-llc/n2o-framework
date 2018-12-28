/**
 * Created by emamoshin on 01.06.2017.
 */
const express = require('express');
const proxy = require('http-proxy-middleware');

const app = express();

const options = {
  target: 'https://n2o.i-novus.ru/react/',
  changeOrigin: true,
  ws: true,
  // pathRewrite: {
  //   '^/n2o' : '',
  // },
};

const exampleProxy = proxy(options);

app.get('/n2o/page/ProtoPage', (req, res) => {
  const json = require('../../server/metadata.json');
  res.setHeader('Content-Type', 'application/json');
  res.send(JSON.stringify(json));
});

app.use('/n2o', exampleProxy);

app.listen(8080, () => {
  console.log('Example app listening on port 8080!');
});
