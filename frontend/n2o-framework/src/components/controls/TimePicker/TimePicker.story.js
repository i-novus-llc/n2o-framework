import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import TimePickerMeta from './Timepicker.meta.json';
import TimePicker from './TimePicker';

const stories = storiesOf('Контролы/Таймпикер', module);

stories
  .add(
    'Таймпикер',
    () => {
      return <TimePicker />;
    },
    {
      // info: {
      //   text: `
      //   Компонент 'CheckboxButton'
      //   ~~~js
      //   import CheckboxButton from 'n2o-framework/lib/components/controls/Checkbox/CheckboxButton';
      //   <CheckboxButton
      //     label="Label"
      //     checked={checked}
      //     onChange={onChange}
      //   />
      //   ~~~
      //   `,
      // },
    }
  )
  .add('Метаданные', () => {
    return <TimePicker {...TimePickerMeta} />;
  });
