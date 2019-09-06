import React from 'react';
import { storiesOf } from '@storybook/react';
import withForm from 'N2oStorybook/decorators/withForm';
import AutoComplete from './AutoComplete';
import fetchMock from 'fetch-mock';

const stories = storiesOf('Контролы/AutoComplete', module);

const form = withForm({ src: 'AutoComplete' });

const options = [
  {
    id: 1,
    name: 'test1',
  },
  {
    id: 2,
    name: 'test2',
  },
  {
    id: 3,
    name: 'test3',
  },
];

stories
  .add(
    'Компонент',
    () => {
      return (
        <AutoComplete
          valueFieldId="name"
          labelFieldId="name"
          options={options}
        />
      );
    },
    {
      info: {
        text: `
      Компонент AutoComplete
      ~~~js
      import AutoComplete from 'n2o/lib/components/controls/AutoComplete/AutoComplete';
      
      <AutoComplete
            valueFieldId="name"
            options={[
                {
                    id: 1,
                    name: 'a'
                },
                {
                    id: 2,
                    name: 'ab'
                },
                {
                    id: 3,
                    name: 'abc'
                }
            ]}
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Метаданные',
    form(() => {
      fetchMock.restore().get('*', url => {
        return {
          count: 3,
          list: [
            {
              id: 1,
              name: 'a',
              icon: 'fa fa-plus',
            },
            {
              id: 2,
              name: 'ab',
            },
            {
              id: 3,
              name: 'abc',
            },
          ],
          page: 1,
          size: 10,
        };
      });

      const props = {
        valueFieldId: 'name',
        iconFieldId: 'icon',
        dataProvider: {
          url: '/n2o/test/autocomplete',
          queryMapping: {},
        },
      };

      return props;
    })
  );
