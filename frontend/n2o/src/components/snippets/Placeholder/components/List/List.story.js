import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import { withKnobs, select, boolean, text } from '@storybook/addon-knobs/react';
import Placeholder from '../../Placeholder';
import meta from './List.meta';

const stories = storiesOf('UI Компоненты/Placeholder/type=list', module);

stories.addDecorator(withKnobs);
stories.addDecorator(jsxDecorator);

stories
  .add('Компонент', () => {
    const props = {
      loading: boolean('loading', true),
      type: text('type', meta.type),
      rows: select('rows', [1, 2, 3, 4, 5], meta.rows),
      paragraphs: select('paragraphs', [1, 2, 3, 4, 5], meta.paragraphs),
      avatar: boolean('avatar', meta.avatar),
    };

    return <Placeholder {...props} />;
  })
  .add('Аватар', () => {
    const props = {
      loading: true,
      avatar: true,
      row: 1,
      paragraph: 6,
    };

    return <Placeholder {...props} />;
  });
