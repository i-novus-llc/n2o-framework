import React from 'react';
import { storiesOf } from '@storybook/react';

import Placeholder from '../../Placeholder';
import meta from './Table.meta';
import Table from './Table';

const stories = storiesOf('UI Компоненты/Placeholder/type=table', module);

stories.addParameters({
  info: {
    propTables: [Table],
  },
});

stories.add('Компонент', () => {
  const props = {
    loading: true,
    type: meta.type,
    rows: meta.rows,
    cols: meta.cols,
  };

  return <Placeholder {...props} />;
});
