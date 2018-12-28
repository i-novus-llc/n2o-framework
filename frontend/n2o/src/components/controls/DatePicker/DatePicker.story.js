import React from 'react';
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
  .addWithJSX('Компонент', () => {
    const props = {
      value: text('value', DatePickerJson.value),
      dateFormat: text('dateFormat', DatePickerJson.dateFormat),
      min: text('min', DatePickerJson.min),
      max: text('max', DatePickerJson.max),
      disabled: boolean('disabled', DatePickerJson.disabled),
      timeFormat: text('timeFormat', 'h:mm:ss'),
      dateDivider: text('dateDivider', ' '),
      defaultTime: text('defaultTime', '11:11:11'),
      locale: select('locale', ['ru', 'en'], DatePickerJson.locale)
    };
    return <DatePicker {...props} onChange={action('onChange')} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        value: text('value', '12/12/2012'),
        dateFormat: text('dateFormat', 'DD/MM/YYYY HH:mm'),
        defaultTime: text('defaultTime', '12:00'),
        min: text('min', '5/12/2012'),
        max: text('max', '15/12/2021'),
        disabled: boolean('disabled', false),
        locale: select('locale', ['ru', 'en'], 'ru')
      };
      return props;
    })
  )

  .addWithJSX('Форматы дат', () => {
    return (
      <React.Fragment>
        <DatePicker dateFormat="DD/MM/YYYY HH:mm" placeholder="DD/MM/YYYY HH:mm" />
        <br />
        <DatePicker dateFormat="DD.MM.YYYY" placeholder="DD.MM.YYYY" />
        <br />
        <DatePicker dateFormat="YYYY-MM" placeholder="YYYY-MM" />
      </React.Fragment>
    );
  })

  .addWithJSX('Текущая неделя', () => {
    return (
      <React.Fragment>
        <DatePicker min="09/04/2018" max="15/04/2018" />
      </React.Fragment>
    );
  })

  .addWithJSX('Время по умолчанию', () => {
    return (
      <React.Fragment>
        <DatePicker dateFormat="DD/MM/YYYY HH:mm" placeholder="DD/MM/YYYY 00:00" />
      </React.Fragment>
    );
  })

  .addWithJSX('Расположение', () => {
    return (
      <div style={{ marginTop: '100px' }}>
        <DatePicker popupPlacement="top" />
      </div>
    );
  });
