import React, { Component } from "react";
import N2O from "n2o-framework/lib/N2o";
import { authProvider } from "n2o-auth";
import createFactoryConfigLight from "n2o-framework/lib/core/factory/createFactoryConfigLight";
import functions from "n2o-framework/lib/utils/functions";
import {
  handleApi,
  defaultApiProvider,
  FETCH_APP_CONFIG
} from "n2o-framework/lib/core/api";
import Route from "n2o-framework/lib/components/core/Route";
import Page from "n2o-framework/lib/components/core/Page";

import Template from "./components/core/Template";
import DashboardV2 from "./pages/DashboardV2";
import TableTree from "./pages/TableTree";
import Select from "./pages/Select";
import DataGrid from "./components/widgets/DataGrid/DataGrid";
import AvatarCell from "./components/cells/Avatar/AvatarCell";
import CollapsedCardFieldset from "./components/fieldset/CollapsedCard/CollapsedCardFieldset";

const config = {
  widgets: {
    DataGrid: DataGrid
  },
  cells: {
    AvatarCell: AvatarCell
  },
  fieldsets: {
    CollapsedCardFieldset: CollapsedCardFieldset
  },
  messages: {
    timeout: {
      error: 0,
      success: 5000,
      warning: 0,
      info: 0
    }
  },
  security: {
    authProvider,
    externalLoginUrl: "/"
  },
  evalContext: functions,
  realTimeConfig: true,
  embeddedRouting: true
};

class App extends Component {
  render() {
    return (
      <N2O {...createFactoryConfigLight(config)}>
        {/* 1 полный кастом */}
        <Route path="/custom/v1" exact component={DashboardV2} />
        {/* 2 обертка, без метаданных */}
        <Route
          path="/custom/v2"
          exact
          render={routeProps => {
            return <Page {...routeProps} page={Select} rootPage />;
          }}
        />
        {/* 3 обертка, метаданные */}
        <Route
          path="/custom/v3"
          exact
          render={routeProps => (
            <Page {...routeProps} page={Select} needMetadata rootPage />
          )}
        />
        {/* 5 */}
        {/*<Route path="custom/:id" component={Page} render={Page} page={"DefaultPage" || "MyPage"} needMetadata={true || false} />*/}
      </N2O>
    );
  }
}

export default App;
