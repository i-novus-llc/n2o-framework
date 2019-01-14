import React from 'react';
import { storiesOf } from '@storybook/react';
import { withState } from '@dump247/storybook-state';
import Pagination from './Pagination';

const stories = storiesOf('UI Компоненты/Пагинация', module);
stories.add(
  'Интерактивное использование',
  withState({ page: 1 })(({ store }) => (
    <Pagination
      onSelect={page => {
        store.set({ page });
      }}
      activePage={store.state.page}
      size={10}
      count={100}
      maxButtons={4}
      stepIncrement={10}
    />
  ))
);
