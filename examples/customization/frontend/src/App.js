import React, { Component } from "react";
import N2o from "n2o";
import AppTemplate from "./components/AppTemplate";
import layouts from "./components/layouts";
import widgets from "./components/widgets";
import regions from "./components/regions";
import controls from "./components/controls";
import cells from "./components/cells";
import fieldsets from "./components/fieldsets";
import fields from "./components/fields";
import PrintPage from './pages/PrintPage';

const config = {
  layouts,
  regions,
  widgets,
  controls,
  cells,
  fieldsets,
  fields,
  defaultTemplate: AppTemplate,
  routes: [
    {
      path: '/print',
      component: PrintPage,
    }
  ],
};

class App extends Component {
  render() {
    return <N2o {...config} />;
  }
}

export default App;
