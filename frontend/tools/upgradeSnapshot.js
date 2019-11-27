const execa = require('execa');
const lernaJson = require('../lerna');

(async function() {
  const currentVersion = lernaJson.version;
  const newVersion = `${currentVersion}-SNAPSHOT.${+new Date()}`;

  execa('./node_modules/.bin/lerna', ['version', newVersion, '--no-git-tag-version', '--no-push', '--yes'])
    .catch(err => {
      throw new Error(err);
    });
})();
