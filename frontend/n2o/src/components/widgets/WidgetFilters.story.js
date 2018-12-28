import React from 'react';
import { storiesOf } from '@storybook/react';
import { getStubData } from 'N2oStorybook/fetchMock';
import fetchMock from 'fetch-mock';

import TableData from './Table/TableWidget.meta.json';
import filter from './filter.meta.json';
import Factory from '../../core/factory/Factory';
import { WIDGETS } from '../../core/factory/factoryLevels';
import withPage from '../../../.storybook/decorators/withPage';

const stories = storiesOf('Виджеты/Таблица', module);

stories.addDecorator(withPage(TableData)).add('Выборочное сбрасывание фильтра', () => {
  fetchMock.restore().getOnce('begin:n2o/data', getStubData);
  const filter = {
    filterFieldsets: [
      {
        src: 'StandardFieldset',
        rows: [
          {
            cols: [
              {
                fields: [
                  {
                    id: 'name',
                    label: 'Имя',
                    description: 'Это поле не сбросится',
                    control: {
                      src: 'Input'
                    }
                  }
                ]
              },
              {
                fields: [
                  {
                    id: 'surname',
                    label: 'Фамилия',
                    control: {
                      src: 'Input'
                    }
                  }
                ]
              }
            ]
          }
        ]
      }
    ],
    filterButtonId: 'filter',
    blackResetList: ['name']
  };
  return (
    <div>
      <Factory level={WIDGETS} {...TableData['Page_Table']} id="Page_Table" filter={filter} />
    </div>
  );
});
