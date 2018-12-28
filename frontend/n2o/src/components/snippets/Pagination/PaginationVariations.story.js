import React from 'react';
import { storiesOf } from '@storybook/react';

import Pagination from './Pagination';

const stories = storiesOf('UI Компоненты/Пагинация', module);

stories.add('Вариации', () => {
  return (
    <React.Fragment>
      <div className="row">
        <Pagination size={10} count={100} maxButtons={4} stepIncrement={10} />
      </div>
      <div className="row">
        <Pagination
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
      <div className="row">
        <Pagination
          prev={true}
          next={true}
          first={true}
          last={true}
          size={1}
          count={2}
          maxButtons={0}
          stepIncrement={0}
          showCountRecords={false}
          hideSinglePage={true}
        />
      </div>
    </React.Fragment>
  );
});
