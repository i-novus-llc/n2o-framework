import React from 'react';
import { storiesOf } from '@storybook/react';
import withTests from 'N2oStorybook/withTests';

import withForm from 'N2oStorybook/decorators/withForm';
import DateIntervalJSON from './DateInterval.meta';

import DateInterval from './DateInterval';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Контролы/Интервал дат', module);

const form = withForm({ src: 'DateInterval' });

stories.addDecorator(withTests('DateTimeControl'));

stories.addParameters({
  info: {
    propTables: [DateInterval],
    propTablesExclude: [Factory],
  },
});

stories
  .add(
    'Компонент',
    () => {
      const props = {
        dateFormat: 'DD/MM/YYYY',
        timeFormat: 'HH:mm:ss',
        defaultTime: '13:00:00',
        min: '5/12/2012',
        max: '15/12/2021',
        disabled: false,
        locale: 'ru',
        outputFormat: 'DD/MM/YYYY',
      };
      return <DateInterval onChange={() => {}} {...props} />;
    },
    {
      info: {
        text: `
      Компонент 'Интервал дат'
      ~~~js
      import DateInterval from 'n2o/lib/components/controls/DatePicker/DateInterval';
      
      <DateInterval
          dateFormat="DD/MM/YYYY"
          timeFormat="HH:mm:ss"
          defaultTime="13:00:00"
          min="5/12/2012"
          max="15/12/2021"
          locale="ru"
          outputFormat="DD/MM/YYYY"
          onChange={onChange}
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Метаданные',
    form(() => {
      return DateIntervalJSON;
    })
  )
  .add(
    'Min/Max',
    form(() => {
      return {
        min: '2019-06-28 00:00:00',
        max: '2019-06-30 00:00:00',
      };
    })
  );
