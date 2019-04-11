import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, boolean, number } from '@storybook/addon-knobs/react';
import { withState } from '@dump247/storybook-state';
import Pagination from './Pagination';

const stories = storiesOf('UI Компоненты/Пагинация', module);

stories.addDecorator(withKnobs);
stories.add(
  'Компонент',
  withState({ page: 1 })(({ store }) => {
    const props = {
      prev: boolean('prev', false),
      next: boolean('next', false),
      first: boolean('first', false),
      last: boolean('last', false),
      lazy: boolean('lazy', false),
      showCountRecords: boolean('showCountRecords', true),
      hideSinglePage: boolean('hideSinglePage', true),
      activePage: number('activePage', store.state.page),
      count: number('count', 100),
      size: number('size', 10),
      maxButtons: number('maxButtons', 4),
      stepIncrement: number('stepIncrement', 10),
    };

    return (
      <Pagination
        onSelect={page => {
          action('pagination-item-click')(page);
          store.set({ page });
        }}
        activePage={store.state.page}
        {...props}
      />
    );
  })
);
