const express = require('express');
const app = express();
const config = require('./json/config.json');
const page = require('./json/proto.json');
const splitPaneLayout = require('./json/splitPaneLayout');
const regionAccordion = require('./json/regionAccordion');
const jumbortonExample = require('./json/jumbortonWidget');
const imageControll = require('./json/imageControll');
const inputCell = require('./json/cellInput');
const backgroundImageFieldset = require('./json/backgroundImageFieldset');
const iconHeader = require('./json/iconHeader');
const backgroundImageField = require('./json/backgroundImageField');

app.get('/n2o/config', (req, res) => {
    res.send(config);
});

app.get('/n2o/page', (req, res) => {
    res.send(page);
});

/**
 * Custom
 */

app.get('/n2o/page/split-pane-layout', (req, res) => {
   res.send(splitPaneLayout);
});

app.get('/n2o/page/region-accordion', (req, res) => {
    res.send(regionAccordion);
});

app.get('/n2o/page/widgets-jumborton', (req, res) => {
    res.send(jumbortonExample);
});

app.get('/n2o/page/controls-image', (req, res) => {
   res.send(imageControll);
});

app.get('/n2o/page/cells-input', (req, res) => {
   res.send(inputCell);
});

app.get('/n2o/page/fieldsets-background', (req, res) => {
   res.send(backgroundImageFieldset);
});

app.get('/n2o/page/headers-icon', (req, res) => {
   res.send(iconHeader);
});

app.get('/n2o/page/field-background', (req, res) => {
   res.send(backgroundImageField);
});

/**  */


app.listen(3001, () => {
    console.log('Server is running on port 3001')
});