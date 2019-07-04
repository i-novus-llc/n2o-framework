import React, { Component } from 'react';
import N2O from 'n2o/lib/N2o';
import { handleApi, defaultApiProvider, FETCH_APP_CONFIG } from 'n2o/lib/core/api';

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
  routes: [
    {
      path: '/dashboard/v2',
      component: DashboardV2,
    },
    {
      path: '/table-tree/',
      component: TableTree

    },
    {
      path: '/select/',
      component: Select
    }
  ],
  messages: {
    timeout: {
      error: 0,
      success: 5000,
      warning: 0,
      info: 0,
    }
  },
  apiProvider: handleApi({
    ...defaultApiProvider,
    [FETCH_APP_CONFIG]: (options) => {
      console.warn('--------------------- API PROVIDER START ---------------------');
      console.warn(FETCH_APP_CONFIG);
      console.warn(options);
      console.warn('--------------------- API PROVIDER END ---------------------');
      return Promise.resolve({
        "messages": {},
        "menu": {
          "brand": "My project",
          "color": "default",
          "fixed": true,
          "collapsed": true,
          "search": false,
          "items": [
            {
              "id": "ya",
              "label": "Yandex",
              "href": "http://yandex.ru",
              "linkType": "outer",
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
    }
  }),
  realTimeConfig: true
};

class App extends Component {
  render() {
    return (
      <N2O {...config}  />
    );
  }
}

export default App;
