/**
 * Created by emamoshin on 01.06.2017.
 */
const express = require("express");
const proxy = require("http-proxy-middleware");
const bodyParser = require("body-parser");

const app = express();

const options = {
  target: "https://n2o.i-novus.ru/next/demo/",
  changeOrigin: true,
  ws: true
};

const exampleProxy = proxy(options);

app.use(bodyParser.json());

app.get("/n2o/config", (req, res) => {
  const config = require("./json/config");
  res.setHeader("Content-Type", "application/json");
  res.send(config);
});

app.get("/n2o/page/proto", (req, res) => {
  const json = require("./json/proto.json");
  res.setHeader("Content-Type", "application/json");
  res.send(JSON.stringify(json));
});

app.get("/n2o/page/proto/patients/create", (req, res) => {
  const json = require("./json/proto_patients_create.json");
  res.setHeader("Content-Type", "application/json");
  res.send(JSON.stringify(json));
});

app.get("/n2o/page/proto/patients/:patientId/update", (req, res) => {
  const json = require("./json/proto_patients_update.json");
  res.setHeader("Content-Type", "application/json");
  res.send(JSON.stringify(json));
});

app.get("/n2o/page/proto/create2", (req, res) => {
  const json = require("./json/proto_patients_create2.json");
  res.setHeader("Content-Type", "application/json");
  res.send(JSON.stringify(json));
});

app.get("/n2o/page/proto/patients/:patientId/update2", (req, res) => {
  const json = require("./json/proto_patients_update2.json");
  res.setHeader("Content-Type", "application/json");
  res.send(JSON.stringify(json));
});

app.all("/sign/get", (req, res) => {
  res.setHeader("Content-Type", "application/json");
  res.setHeader("Access-Control-Allow-Origin", "*");

  res.send(
    JSON.stringify([
      {
        docPackId: 6303,
        docTypeId: 3,
        hash:
          "9eff17fa13a36e67cb64464020141425fb331e964642057409ab4759e2c4a9e5",
        id: 15642,
        roleId: 4,
        userId: 65
      },
      {
        docPackId: 6303,
        docTypeId: 2,
        hash:
          "4dd2cbb9e1ee89914f4fd63045d00a38e7023189e7df3db7ec90c585eafd6f42",
        id: 15623,
        roleId: 4,
        userId: 65
      }
    ])
  );
});

app.all("/sign/set", (req, res) => {
  res.setHeader("Access-Control-Allow-Origin", "*");
  console.log(req.body);
  res.send(200);
});

app.get("/n2o/data/tiles", (req, res) => {
    res.send({
        meta: {},
        page: 0,
        size: 10,
        list: [
            {
                tile1: {
                    title: 'Ячейка с картинкой',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2017 17:32:23'
                },
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2018 17:32:23'
                }
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2019 17:32:23'
                }
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2020 17:32:23'
                },
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2021 17:32:23'
                }
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2022 17:32:23'
                }
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2023 17:32:23'
                },
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2024 17:32:23'
                }
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2025 17:32:23'
                }
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2026 17:32:23'
                },
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2027 17:32:23'
                }
            },
            {
                tile1: {
                    title: 'Ячейка с картинкой 2',
                    url: 'https://www.google.com/images/branding/googlelogo/1x/googlelogo_color_272x92dp.png',
                },
                tile2: {
                    text: '11-23-2028 17:32:23'
                }
            }
        ]
    });
});


app.use("/n2o", exampleProxy);
app.use("/n2o/data", exampleProxy);
app.use("/n2o/config", exampleProxy);

app.listen(9000, () => {
  console.log("Example app listening on port 9000!");
});
