import React, {Component} from 'react';
import N2o from 'n2o';
import layouts from './components/layouts';
import widgets from './components/widgets';
import regions from './components/regions';
import controls from './components/controls';
import cells from './components/cells';
import fieldsets from './components/fieldsets';
import headers from './components/headers';
import fields from './components/fields';

const config = {
    layouts,
    regions,
    widgets,
    controls,
    cells,
    fieldsets,
    headers,
    fields
};

class App extends Component {
  constructor(props) {
    super(props);
  }

  render() {
    return (
      <N2o {...config} />
    );
  }
}

export default App;