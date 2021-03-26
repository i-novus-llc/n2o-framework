import React, { Component } from "react";
import N2O from "n2o-framework/lib/N2o";
import { authProvider } from "n2o-auth";
import createFactoryConfig from "n2o-framework/lib/core/factory/createFactoryConfig";
import functions from "n2o-framework/lib/utils/functions";
import Route from "n2o-framework/lib/components/core/Route";
import Page from "n2o-framework/lib/components/core/Page";
import { EcpButton } from "n2o-ecp-plugin";

import DashboardV2 from "./pages/DashboardV2";
import Select from "./pages/Select";
import DataGrid from "./components/widgets/DataGrid/DataGrid";
import AvatarCell from "./components/cells/Avatar/AvatarCell";
import CollapsedCardFieldset from "./components/fieldset/CollapsedCard/CollapsedCardFieldset";

const config = {
  buttons: {
    EcpButton
  },
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
      success: 0,
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
  embeddedRouting: true,
  locales: {
    ru: {
      // тут могут быть переводы, которые добавятся к базовым
    },
    en: {
      // there may be translations that will be added to the basic ones
    },
    fr: {
      // il peut y avoir des traductions qui seront ajoutées aux traductions de base
    }
  }
};

class App extends Component {
  render() {
    return (
      <N2O {...createFactoryConfig(config)}>
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
