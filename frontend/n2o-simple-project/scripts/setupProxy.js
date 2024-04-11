const fs = require('fs');

// File destination.txt will be created or overwritten by default.
const INPUT = './setupProxy.example.ts'
const OUTPUT = './setupProxy.ts'

if (!fs.existsSync(OUTPUT)) {
    fs.copyFile(INPUT, OUTPUT, (err) => {
        if (err) { throw err }
    });
}
