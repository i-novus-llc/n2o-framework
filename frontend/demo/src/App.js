import React, { Component } from 'react';
import N2O from 'n2o-framework/lib/N2o';
import createFactoryConfig from "n2o-framework/lib/core/factory/createFactoryConfig";
import functions from "n2o-framework/lib/utils/functions";
import { handleApi, defaultApiProvider, FETCH_APP_CONFIG } from 'n2o-framework/lib/core/api';
import Route from 'n2o-framework/lib/components/core/Route';
import Page from 'n2o-framework/lib/components/core/Page';

import Template from './components/core/Template';
import DashboardV2 from './pages/DashboardV2';
import TableTree from './pages/TableTree';
import Select from './pages/Select';
import DataGrid from './components/widgets/DataGrid/DataGrid';
import AvatarCell from './components/cells/Avatar/AvatarCell';
import CollapsedCardFieldset from './components/fieldset/CollapsedCard/CollapsedCardFieldset';

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
      info: 0,
    }
  },
  evalContext: functions,
  apiProvider: handleApi({
    ...defaultApiProvider,
    [FETCH_APP_CONFIG]: (options) => {
      console.warn('--------------------- API PROVIDER START ---------------------');
      console.warn(FETCH_APP_CONFIG);
      console.warn(options);
      console.warn('--------------------- API PROVIDER END ---------------------');
      return new Promise((resolve, reject) => {
        setTimeout(() => {
          resolve({
            "messages": {},
            "menu": {
              "brand": "My project",
              "color": "inverse",
              "fixed": true,
              "collapsed": true,
              "search": false,
              "items": [
                {
                  "id": "proto",
                  "label": "Proto",
                  "href": "/proto",
                  "linkType": "inner",
                  "type": "link"
                },
                {
                  "id": "v1",
                  "label": "Custom V1",
                  "href": "/custom/v1",
                  "linkType": "inner",
                  "type": "link"
                },
                {
                  "id": "v2",
                  "label": "Custom V2",
                  "href": "/custom/v2",
                  "linkType": "inner",
                  "type": "link"
                },
                {
                  "id": "v3",
                  "label": "Custom V3",
                  "href": "/custom/v3",
                  "linkType": "inner",
                  "type": "link"
                }
              ],
              "extraItems": [
                {
                  "id": "goo",
                  "label": "Google",
                  "href": "http://google.com",
                  "linkType": "outer",
                  "type": "link"
                }
              ]
            },
            "user": {
              "username": null,
              "testProperty": "testProperty"
            }
          });
        }, 0);
      });
    }
  }),
  realTimeConfig: true,
  embeddedRouting: true,
};

class App extends Component {
  render() {
    return (
      <N2O {...createFactoryConfig(config)}>
        {/* 1 полный кастом */}
        <Route path="/custom/v1" exact component={DashboardV2} />
        {/* 2 обертка, без метаданных */}
        <Route path="/custom/v2" exact render={routeProps => {
          return (<Page {...routeProps} page={Select} rootPage />);
        }}  />
        {/* 3 обертка, метаданные */}
        <Route path="/custom/v3" exact render={routeProps => <Page {...routeProps} page={Select} needMetadata rootPage />}  />
        {/* 5 */}
        {/*<Route path="custom/:id" component={Page} render={Page} page={"DefaultPage" || "MyPage"} needMetadata={true || false} />*/}
      </N2O>
    );
  }
}

export default App;
