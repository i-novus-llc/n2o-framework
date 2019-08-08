import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import { withKnobs, boolean, number } from '@storybook/addon-knobs/react';
import { StateDecorator, Store } from '@sambego/storybook-state';
import Pagination from './Pagination';
import { withState } from '@dump247/storybook-state';

const initPages = {
  simple: 1,
  prevNext: 1,
  firstLast: 1,
  combination: 1,
};

const store = new Store({
  page: 1,
  ...initPages,
});

store.subscribe(forceReRender);

const stories = storiesOf('UI Компоненты/Пагинация', module);

stories.addDecorator(withKnobs);
stories.addDecorator(StateDecorator(store));
stories
  .addWithJSX(
    'Компонент',
    () => {
      const props = {
        prev: boolean('prev', false),
        next: boolean('next', false),
        first: boolean('first', false),
        last: boolean('last', false),
        lazy: boolean('lazy', false),
        showCountRecords: boolean('showCountRecords', true),
        hideSinglePage: boolean('hideSinglePage', true),
        activePage: number('activePage', store.get('page')),
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
          activePage={store.get('page')}
          {...props}
        />
      );
    },
    { jsx: { skip: 1 } }
  )
  .addWithJSX(
    'Интерактивное использование',
    () => (
      <Pagination
        onSelect={page => {
          store.set({ page });
        }}
        activePage={store.get('page')}
        size={10}
        count={100}
        maxButtons={4}
        stepIncrement={10}
      />
    ),
    { jsx: { skip: 1 } }
  )
  .addWithJSX(
    'Вариации',
    () => (
      <React.Fragment>
        <div className="row">
          <Pagination
            activePage={store.get('simple')}
            onSelect={page => store.set({ simple: page })}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
        </div>
        <div className="row">
          <Pagination
            activePage={store.get('prevNext')}
            onSelect={page => store.set({ prevNext: page })}
            prev={true}
            next={true}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
        </div>
        <div className="row">
          <Pagination
            activePage={store.get('firstLast')}
            onSelect={page => store.set({ firstLast: page })}
            first={true}
            last={true}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
        </div>
        <div className="row">
          <Pagination
            activePage={store.get('combination')}
            onSelect={page => store.set({ combination: page })}
            prev={true}
            next={true}
            first={true}
            last={true}
            size={10}
            count={100}
            maxButtons={4}
            stepIncrement={10}
          />
        </div>
      </React.Fragment>
    ),
    { jsx: { skip: 1 } }
  );
