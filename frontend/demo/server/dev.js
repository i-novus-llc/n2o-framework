/**
 * Created by emamoshin on 01.06.2017.
 */
const express = require("express");
const proxy = require("http-proxy-middleware");
const bodyParser = require("body-parser");

const app = express();

const options = {
  target: "https://n2o.i-novus.ru/dev/",
  changeOrigin: true,
  ws: true
};

const exampleProxy = proxy(options);

app.use(bodyParser());

// app.get('/config', (req, res) => {
//   res.setHeader('Content-Type', 'application/json');
//   res.send({"messages":{},"menu":{"brand":"N2O","color":"inverse","fixed":false,"collapsed":true,"search":false,"items":[{"id":"menuItem0","label":"Контакты","href":"/proto","linkType":"inner","type":"link"}],"extraItems":[]},"user":{"username":null,"testProperty":"testProperty"}})
// });

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
  // res.send({
  //   docPackId: 6303,
  //   docTypeId: 3,
  //   hash: "9eff17fa13a36e67cb64464020141425fb331e964642057409ab4759e2c4a9e5",
  //   id: 15642,
  //   roleId: 4,
  //   userId: 65
  // });
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


app.use("/n2o", exampleProxy);


// app.get('/n2o/data/proto/5607677/card?', (req, res) => {
//   const json = `{
//   "meta": {},
//   "list": [
//     {
//       "id": 5607677,
//       "surname": "Плюхина",
//       "name": "Лада",
//       "patrName": "Всеволодовна",
//       "fullName": "Плюхина Лада Всеволодовна",
//       "birthday": "1927-01-01T00:00:00",
//       "age": 93,
//       "gender": {
//         "name": "Женский",
//         "id": 2
//       },
//       members: [
//        {
//         surname: 'test',
//         name: 'test'
//        }
//       ],
//       "genderName": "Женский",
//       "vip": false,
//       "hasCitizenship": false,
//       "ethnicGroupName": "русские",
//       "socialGroupName": "Студент ВУЗа",
//       "nationality": {
//         "name": "Буряты",
//         "id": 17
//       }
//     }
//   ],
//   "count": 1,
//   "size": 1,
//   "page": 1
// }`;
//
//   res.setHeader('Content-Type', 'application/json');
//   res.send(json);
// });

app.listen(9000, () => {
  console.log("Example app listening on port 9000!");
});
