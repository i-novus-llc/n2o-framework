import React from 'react';
import { storiesOf } from '@storybook/react';
import Pagination from './Pagination';
import { withState } from '@dump247/storybook-state';

const stories = storiesOf('UI Компоненты/Пагинация', module);

const initPages = {
  simple: 1,
  prevNext: 1,
  firstLast: 1,
  combination: 1,
};

stories.add(
  'Вариации',
  withState({ pages: initPages })(({ store }) => (
    <React.Fragment>
      <div className="row">
        <Pagination
          activePage={store.state.simple}
          onSelect={page => store.set({ simple: page })}
          size={10}
          count={100}
          maxButtons={4}
          stepIncrement={10}
        />
      </div>
      <div className="row">
        <Pagination
          activePage={store.state.prevNext}
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
          activePage={store.state.firstLast}
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
          activePage={store.state.combination}
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
  ))
);
