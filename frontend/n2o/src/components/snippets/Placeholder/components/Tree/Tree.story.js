import React from 'react';
import { storiesOf } from '@storybook/react';

import Placeholder from '../../Placeholder';
import meta from './Tree.meta';
import Tree from './Tree';

const stories = storiesOf('UI Компоненты/Placeholder/type=tree', module);

stories.addParameters({
  info: {
    propTables: [Tree],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      loading: true,
      type: meta.type,
      rows: meta.rows,
      chevron: meta.chevron,
    };

    return <Placeholder {...props} />;
  })
  .add('chevron', () => {
    return <Placeholder chevron={true} rows={10} type="tree" loading={true} />;
  });
