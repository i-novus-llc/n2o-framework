import React, { Component } from 'react';
import N2O from 'n2o-framework';

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
  }
};

class App extends Component {
  render() {
    return (
      <N2O {...config}  />
    );
  }
}

export default App;
