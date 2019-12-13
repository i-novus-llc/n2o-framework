import React from 'react';
import { storiesOf } from '@storybook/react';

import { getStubData } from 'N2oStorybook/fetchMock';
import fetchMock from 'fetch-mock';
import set from 'lodash/set';
import TableData from './Table/TableWidget.meta.json';
import Factory from '../../core/factory/Factory';
import { WIDGETS } from '../../core/factory/factoryLevels';
import withPage from '../../../.storybook/decorators/withPage';

const stories = storiesOf('Виджеты/Таблица', module);

stories
  .addDecorator(withPage(TableData))
  .add('Выборочное сбрасывание фильтра', () => {
    fetchMock.restore().get('begin:n2o/data', getStubData);
    const filter = {
      filterPlace: 'top',
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
                        src: 'InputText',
                      },
                    },
                  ],
                },
                {
                  fields: [
                    {
                      id: 'surname',
                      label: 'Фамилия',
                      control: {
                        src: 'InputText',
                      },
                    },
                  ],
                },
              ],
            },
          ],
        },
      ],
      filterButtonId: 'filter',
      blackResetList: ['name'],
    };

    const newProps = { ...set(TableData['Page_Table'], 'filter', filter) };

    return (
      <div>
        <Factory level={WIDGETS} {...newProps} id="Page_Table" />
      </div>
    );
  })
  .add('Поиск при изменений поля', () => {
    fetchMock.restore().get('begin:n2o/data', getStubData);
    const filter = {
      filterPlace: 'top',
      filterFieldsets: [
        {
          src: 'StandardFieldset',
          rows: [
            {
              cols: [
                {
                  fields: [
                    {
                      id: 'surname',
                      label: 'Фамилия',
                      control: {
                        src: 'InputText',
                      },
                    },
                  ],
                },
              ],
            },
          ],
        },
      ],
      filterButtonId: 'filter',
      blackResetList: ['name'],
      searchOnChange: true,
      hideButtons: true,
    };

    const newProps = { ...set(TableData['Page_Table'], 'filter', filter) };

    return (
      <div>
        <Factory level={WIDGETS} {...newProps} id="Page_Table" />
      </div>
    );
  });
