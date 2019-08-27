import React from "react";
import { render } from "react-dom";
import N2O from "n2o-framework/lib/N2o";
import "n2o-framework/dist/n2o.css";
import "./correct.css";
import "antd/dist/antd.css";

import conf from "../../src";

const config = {
  ...conf,
  messages: {
    timeout: {
      error: 0,
      success: 5000,
      warning: 0,
      info: 0
    }
  }
};

render(<N2O {...config} />, document.querySelector("#demo"));
