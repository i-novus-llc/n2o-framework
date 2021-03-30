import "core-js/es6/symbol";
import "core-js/es6/object";
import "core-js/es6/function";
import "core-js/es6/parse-int";
import "core-js/es6/parse-float";
import "core-js/es6/number";
import "core-js/es6/math";
import "core-js/es6/string";
import "core-js/es6/date";
import "core-js/es6/array";
import "core-js/es6/regexp";
import "core-js/es6/map";
import "core-js/es6/set";
import "core-js/es7/array";

import "react-app-polyfill/ie11";
import "react-app-polyfill/stable";

import React from "react";
import ReactDOM from "react-dom";
import App from "./App";

import "n2o-framework/dist/n2o.css";
import "./demo.css";

ReactDOM.render(<App />, document.getElementById("n2o"));

if (navigator && navigator.serviceWorker) {
    navigator.serviceWorker.register(`${process.env.PUBLIC_URL}/serviceWorker.js`).then(() => {
        console.info('ServiceWorker installing success');
    }, (error) => {
        console.warn('ServiceWorker installing error: ', error);
    });
} else {
    console.warn('ServiceWorker installing error: not supported');
}
