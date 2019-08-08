import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, select, boolean, text } from '@storybook/addon-knobs/react';
import Placeholder from '../../Placeholder';
import meta from './Tree.meta';
import Tree from './Tree';

const stories = storiesOf('UI Компоненты/Placeholder/type=tree', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);
stories.addParameters({
  info: {
    propTables: [Tree],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      loading: boolean('loading', true),
      type: text('type', meta.type),
      rows: select('rows', [1, 2, 3, 4, 5], meta.rows),
      chevron: boolean('chevron', meta.chevron),
    };

    return <Placeholder {...props} />;
  })
  .add('chevron', () => {
    return <Placeholder chevron={true} rows={10} type="tree" loading={true} />;
  });
