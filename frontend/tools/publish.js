const path = require('path');
const fs = require('fs');
const RegClient = require('npm-registry-client');
const commandLineArgs = require('command-line-args');
const lernaConfig = require('../lerna.json');

const client = new RegClient({});
const optionDefinitions = [
  { name: 'registry', alias: 'r', type: String },
  { name: 'username', alias: 'u', type: String },
  { name: 'password', alias: 'p', type: String },
  { name: 'email', alias: 'e', type: String }
];
const cmdArgs = commandLineArgs(optionDefinitions);
const uri = cmdArgs.registry;
const auth = {
  username: cmdArgs.username,
  password: cmdArgs.password,
  email: cmdArgs.email,
  alwaysAuth: true
};

function runPublish() {
  lernaConfig.packages.map(scope => {
    const packagePath = path.resolve(__dirname, `../${scope}/package.json`);
    const pkg = require(packagePath);
    if (!pkg.private) {
      const bodyPath = require.resolve(packagePath);
      const tarball = fs.createReadStream(bodyPath);
      const params = {
        metadata: pkg,
        access: 'public',
        body: tarball,
        auth: auth
      };
      client.publish(uri, params, function (error) {
        if (error) {
          throw error;
        }
        console.info(`Пакет ${pkg.name} успешно опубликован. Новаря версия: ${pkg.version}`);
      });
    }
  })
}

runPublish();