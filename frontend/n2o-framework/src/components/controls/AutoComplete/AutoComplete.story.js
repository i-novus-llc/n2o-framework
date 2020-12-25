import React from 'react';
import { storiesOf } from '@storybook/react';
import withForm from 'N2oStorybook/decorators/withForm';
import AutoComplete, {
  AutoComplete as AutoCompleteComponent,
} from './AutoComplete';
import fetchMock from 'fetch-mock';

const stories = storiesOf('Контролы/AutoComplete', module);

const form = withForm({ src: 'AutoComplete' });

const options = [
  {
    id: 1,
    name: 'ab',
  },
  {
    id: 2,
    name: 'abc',
  },
  {
    id: 3,
    name: 'abcd',
  },
];

stories.addParameters({
  info: {
    propTables: [AutoCompleteComponent],
  },
});

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
      import AutoComplete from 'n2o-framework/lib/components/controls/AutoComplete/AutoComplete';
      
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
    'Предустановленное значение',
    () => {
      return (
        <AutoComplete
          valueFieldId="name"
          labelFieldId="name"
          options={options}
          value="a"
        />
      );
    },
    {
      info: {
        text: `
      Компонент AutoComplete
      ~~~js
      import AutoComplete from 'n2o-framework/lib/components/controls/AutoComplete/AutoComplete';
      
      <AutoComplete
            valueFieldId="name"
            value='a'
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
    'Мод tags',
    () => {
      return (
        <>
          <AutoComplete
            tags={true}
            valueFieldId="name"
            labelFieldId="name"
            options={options}
            style={{ marginBottom: '25px' }}
          />
          <span>maxTagTextLength = 2</span>
          <AutoComplete
            tags={true}
            valueFieldId="name"
            labelFieldId="name"
            options={options}
            maxTagTextLength={2}
          />
        </>
      );
    },
    {
      info: {
        text: `
      Компонент AutoComplete
      ~~~js
      import AutoComplete from 'n2o-framework/lib/components/controls/AutoComplete/AutoComplete';
      
      <AutoComplete
            valueFieldId="name"
            tags={true}
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
    'Мод tags - предустановленные значения',
    () => {
      return (
        <AutoComplete
          tags={true}
          valueFieldId="name"
          labelFieldId="name"
          options={options}
          value={['a', 'ab']}
        />
      );
    },
    {
      info: {
        text: `
      Компонент AutoComplete
      ~~~js
      import AutoComplete from 'n2o-framework/lib/components/controls/AutoComplete/AutoComplete';
      
      <AutoComplete
            valueFieldId="name"
            tags={true}
            value={['a', 'ab']}
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
