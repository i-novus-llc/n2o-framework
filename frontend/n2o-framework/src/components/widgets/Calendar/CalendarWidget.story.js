import React from 'react';

import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';

import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import metadata from './CalendarWidget.meta.json';

const stories = storiesOf('Виджеты/Виджет Календарь', module);
const urlPattern = '*';

stories
  .add('Компоненты', () => {
    fetchMock.restore().get(urlPattern, url => {
      return {
        meta: {},
        page: 0,
        size: 10,
        list: [
          {
            id: 1,
            begin: '2020-06-10T10:00:00',
            end: '2020-06-10T15:00:00',
            name: 'Some title',
            tooltip: 'tooltip text',
            color: 'red',
            disabled: true,
          },
        ],
      };
    });

    return (
      <Factory
        level={WIDGETS}
        {...metadata['Page_Calendar']}
        id="Page_Calendar"
      />
    );
  })
  .add('События', () => {
    fetchMock.restore().get(urlPattern, url => {
      return {
        meta: {},
        page: 0,
        size: 10,
        list: [
          {
            id: 1,
            begin: '2020-06-10T10:00:00',
            end: '2020-06-10T15:00:00',
            name: 'Some title',
            tooltip: 'tooltip text',
            color: 'red',
            disabled: true,
          },
        ],
      };
    });

    return (
      <Factory
        level={WIDGETS}
        {...metadata['Page_Calendar']}
        id="Page_Calendar"
      />
    );
  })
  .add('Планирование ресурсов', () => {
    fetchMock.restore().get(urlPattern, url => {
      return {
        meta: {},
        page: 0,
        size: 10,
        list: [
          {
            id: 1,
            begin: '2020-06-10T10:00:00',
            end: '2020-06-10T15:00:00',
            name: 'Some title',
            tooltip: 'tooltip text',
            color: 'red',
            disabled: true,
          },
        ],
      };
    });

    const resources = {
      calendar: {
        resources: [
          {
            id: 1,
            title: 'Конференц зал',
          },
          {
            id: 2,
            title: 'Вторая Эрпэльная',
          },
        ],
      },
    };

    const withResources = { ...metadata['Page_Calendar'], ...resources };

    return <Factory level={WIDGETS} {...withResources} id="Page_Calendar" />;
  });
