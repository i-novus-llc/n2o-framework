import React, { Component } from "react";
import N2o from "n2o-framework";
import createFactoryConfigLight from "n2o-framework/lib/core/factory/createFactoryConfigLight";
import AppTemplate from "./components/AppTemplate";
import widgets from "./components/widgets";
import regions from "./components/regions";
import controls from "./components/controls";
import cells from "./components/cells";
import fieldsets from "./components/fieldsets";
import fields from "./components/fields";
import PrintPage from './pages/PrintPage';

const config = {
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
    return <N2o {...createFactoryConfigLight(config)} />;
  }
}

export default App;
