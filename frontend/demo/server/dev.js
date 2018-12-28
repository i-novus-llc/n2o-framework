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
};

const exampleProxy = proxy(options);

app.get('/n2o/page/proto', (req, res) => {
  const json = require('./json/proto.json');
  res.setHeader('Content-Type', 'application/json');
  res.send(JSON.stringify(json));
});

app.get('/n2o/page/proto/patients/create', (req, res) => {
  const json = require('./json/proto_patients_create.json');
  res.setHeader('Content-Type', 'application/json');
  res.send(JSON.stringify(json));
});

app.get('/n2o/page/proto/patients/:patientId/update', (req, res) => {
  const json = require('./json/proto_patients_update.json');
  res.setHeader('Content-Type', 'application/json');
  res.send(JSON.stringify(json));
});

app.get('/n2o/page/proto/create2', (req, res) => {
  const json = require('./json/proto_patients_create2.json');
  res.setHeader('Content-Type', 'application/json');
  res.send(JSON.stringify(json));
});

app.get('/n2o/page/proto/patients/:patientId/update2', (req, res) => {
  const json = require('./json/proto_patients_update2.json');
  res.setHeader('Content-Type', 'application/json');
  res.send(JSON.stringify(json));
});

app.use('/n2o', exampleProxy);

app.listen(9000, () => {
  console.log('Example app listening on port 9000!');
});
