import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import Placeholder from '../../Placeholder';
import meta from './Table.meta';

const stories = storiesOf('UI Компоненты/Placeholder/Table', module);

stories.addDecorator(withKnobs);

stories.add('type=table', () => {
  const props = {
    loading: boolean('loading', meta.loading),
    type: text('type', meta.type),
    rows: select('rows', [1, 2, 3, 4, 5], meta.rows),
    cols: select('cols', [1, 2, 3, 4, 5], meta.cols),
  };

  return <Placeholder {...props} />;
});
