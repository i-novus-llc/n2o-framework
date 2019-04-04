import React from 'react';
import { storiesOf } from '@storybook/react';
import {
  withKnobs,
  text,
  boolean,
  array,
  select,
  object,
} from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import { action } from '@storybook/addon-actions';
import withForm from 'N2oStorybook/decorators/withForm';
import DateIntervalJSON from './DateInterval.meta';

import DateInterval from './DateInterval';

const stories = storiesOf('Контролы/Интервал дат', module);

const form = withForm({ src: 'DateInterval' });

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('DateTimeControl'));

stories
  .add('Компонент', () => {
    const props = {
      dateFormat: text('dateFormat', 'DD/MM/YYYY'),
      timeFormat: text('timeFormat', 'HH:mm:ss'),
      defaultTime: text('defaultTime', '13:00:00'),
      min: text('min', '5/12/2012'),
      max: text('max', '15/12/2021'),
      disabled: boolean('disabled', false),
      locale: select('locale', ['ru', 'en'], 'ru'),
      outputFormat: text('outputFormat', 'DD/MM/YYYY'),
    };
    return <DateInterval onChange={action('onChange')} {...props} />;
  })
  .add(
    'Метаданные',
    form(() => {
      return DateIntervalJSON;
    })
  );
