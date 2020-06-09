import React from 'react';

import { storiesOf } from '@storybook/react';
import fetchMock from 'fetch-mock';

import Factory from '../../../core/factory/Factory';
import { WIDGETS } from '../../../core/factory/factoryLevels';
import metadata from './CalendarWidget.meta.json';

const stories = storiesOf('Виджеты/Виджет Календарь', module);
const urlPattern = '*';

stories.add('Метаданные', () => {
  fetchMock.restore().get(urlPattern, url => {
    return {
      page: 1,
      size: 10,
      meta: {},
      list: [
        {
          id: 1,
          begin: '2020-06-010T10:00:00',
          end: '2020-06-010T15:00:00',
          name: 'Some title',
          tooltip: 'tooltip text',
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
});
