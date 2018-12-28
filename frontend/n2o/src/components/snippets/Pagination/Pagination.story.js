import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';

import Pagination from './Pagination';

const stories = storiesOf('UI Компоненты/Пагинация', module);

stories.addDecorator(withKnobs);
// todo: баг в jest addon
stories.addDecorator(withTests('ReplaceOnPagination'));

stories.addWithJSX('Компонент', () => {
  const props = {
    prev: boolean('prev', false),
    next: boolean('next', false),
    first: boolean('first', false),
    last: boolean('last', false),
    lazy: boolean('lazy', false),
    showCountRecords: boolean('showCountRecords', true),
    hideSinglePage: boolean('hideSinglePage', true),
    activePage: number('activePage', 1),
    count: number('count', 100),
    size: number('size', 10),
    maxButtons: number('maxButtons', 4),
    stepIncrement: number('stepIncrement', 10)
  };

  return <Pagination onSelect={action('pagination-item-click')} {...props} />;
});
