import React from 'react';
import { forceReRender, storiesOf } from '@storybook/react';

import SearchBar, { SearchBar as SearchBarComponent } from './SearchBar';
import { StateDecorator, Store } from '@sambego/storybook-state';

const store = new Store({
  value: '',
});

store.subscribe(forceReRender);

const stories = storiesOf('UI Компоненты/SearchBar', module);

stories.addParameters({
  info: {
    propTables: [SearchBarComponent],
    propTablesExclude: [SearchBar],
  },
});
stories.addDecorator(StateDecorator(store));

stories
  .add(
    'Компонент',
    () => {
      return (
        <div>
          <div>Значение: {store.get('value1')}</div>
          <SearchBar
            value={store.get('value1')}
            onSearch={value1 => store.set({ value1 })}
          />
        </div>
      );
    },
    {
      info: {
        text: `
      ~~~js
      import SearchBar from 'n2o/lib/components/snippets/SearchBar/SearchBar';
      
      <SearchBar
        value={value}
        onSearch={onSearch}
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Автоматический поиск',
    () => {
      return (
        <div>
          <h3>Поиск запуститься через 400ms после ввода</h3>
          <div>Значение: {store.get('value2')}</div>
          <SearchBar onSearch={value2 => store.set({ value2 })} />
        </div>
      );
    },
    {
      info: {
        text: `
      ~~~js
      import SearchBar from 'n2o/lib/components/snippets/SearchBar/SearchBar';
      
      <SearchBar
        onSearch={onSearch}
        trigger="CHANGE"
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Поиск по нажатию ENTER',
    () => {
      return (
        <div>
          <h3>Поиск запуститься при нажатии ENTER</h3>
          <div>Значение: {store.get('value3')}</div>
          <SearchBar
            trigger="ENTER"
            onSearch={value3 => store.set({ value3 })}
          />
        </div>
      );
    },
    {
      info: {
        text: `
      ~~~js
      import SearchBar from 'n2o/lib/components/snippets/SearchBar/SearchBar';
      
      <SearchBar
        onSearch={onSearch}
        trigger="ENTER"
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Поиск с кнопкой',
    () => {
      return (
        <div>
          <div>Значение: {store.get('value4')}</div>
          <SearchBar
            trigger="BUTTON"
            button={{ label: 'Искать', color: 'primary' }}
            onSearch={value4 => store.set({ value4 })}
          />
        </div>
      );
    },
    {
      info: {
        text: `
      ~~~js
      import SearchBar from 'n2o/lib/components/snippets/SearchBar/SearchBar';
      
      <SearchBar
        trigger="BUTTON"
        button={{ label: 'Искать', color: 'primary' }}}
        onSearch={onSearch}
      />
      ~~~
      `,
      },
    }
  );
