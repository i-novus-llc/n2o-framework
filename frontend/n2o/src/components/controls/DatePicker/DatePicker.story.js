import React from 'react';
import moment from 'moment';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, select } from '@storybook/addon-knobs/react';
import { action } from '@storybook/addon-actions';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import DatePicker from './DatePicker';
import DatePickerJson from './DatePicker.meta.json';

const form = withForm({ src: 'DatePicker' });
const stories = storiesOf('Контролы/Выбор дат', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('DateTimeControl'));

stories
  .add('Компонент', () => {
    const props = {
      min: text('min', DatePickerJson.min),
      max: text('max', DatePickerJson.max),
      disabled: boolean('disabled', DatePickerJson.disabled),
      timeFormat: text('timeFormat', DatePickerJson.timeFormat),
      dateDivider: text('dateDivider', ' '),
      dateFormat: text('dateFormat', DatePickerJson.dateFormat),
      locale: select('locale', ['ru', 'en'], DatePickerJson.locale)
    };
    return <DatePicker {...props} onChange={action('onChange')} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        dateFormat: text('dateFormat', 'DD/MM/YYYY'),
        timeFormat: text('timeFormat', 'HH:mm'),
        defaultTime: text('defaultTime', '12:00'),
        min: text('min', '5/12/2012'),
        max: text('max', '15/12/2021'),
        disabled: boolean('disabled', false),
        locale: select('locale', ['ru', 'en'], 'ru')
      };
      return props;
    })
  )

  .add('Форматы дат', () => {
    return (
      <React.Fragment>
        <DatePicker
          dateFormat="DD/MM/YYYY"
          timeFormat="HH:mm"
          placeholder="dateFormat=DD/MM/YYYY timeFormat=HH:mm"
        />
        <br />
        <DatePicker dateFormat="DD.MM.YYYY" placeholder="DD.MM.YYYY" />
        <br />
        <DatePicker dateFormat="YYYY-MM" placeholder="YYYY-MM" />
      </React.Fragment>
    );
  })

  .add('Текущая неделя', () => {
    return (
      <React.Fragment>
        <DatePicker />
      </React.Fragment>
    );
  })

  .add('Время по умолчанию', () => {
    return (
      <React.Fragment>
        <DatePicker
          dateFormat="DD/MM/YYYY"
          timeFormat="HH:mm"
          defaultTime="13:00"
          placeholder="dateFormat=DD/MM/YYYY timeFormat=HH:mm defaultTime=13:00"
        />
      </React.Fragment>
    );
  })

  .add('Расположение', () => {
    return (
      <div style={{ marginTop: '100px' }}>
        <DatePicker popupPlacement="top" />
      </div>
    );
  });
