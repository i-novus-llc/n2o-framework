/*
 * Launches the Docusaurus production build for the Maven `docusaurus build`
 * execution in pom.xml.
 *
 * Why this exists instead of just `yarn run build`: under Maven's non-TTY exec
 * the yarn wrapper was killed with exit 129 (SIGHUP). Invoking Docusaurus
 * directly through node avoids that.
 *
 * Why it reports the signal explicitly: when the build dies on a signal, Maven
 * only ever reports "Exit value: 1", which hides the actual cause.
 */
const { spawn } = require('child_process');
const path = require('path');

const docusaurusBin = path.join(__dirname, 'node_modules', '@docusaurus', 'core', 'bin', 'docusaurus.mjs');
const args = [docusaurusBin, 'build', ...process.argv.slice(2)];

const child = spawn(process.execPath, args, { stdio: 'inherit' });

child.on('error', (err) => {
    console.error('[ci-build] failed to spawn docusaurus:', err.message);
    process.exit(1);
});

child.on('close', (code, signal) => {
    if (signal) {
        console.error('[ci-build] docusaurus was killed by ' + signal +
            ' (no exit code). This is a crash, not a build error.');
        process.exit(1);
    }
    process.exit(code == null ? 1 : code);
});
