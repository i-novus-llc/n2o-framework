import React from 'react';
import { storiesOf, forceReRender } from '@storybook/react';
import TimePickerMeta from './Timepicker.meta.json';
import TimePicker from './TimePicker';

const stories = storiesOf('Контролы/Таймпикер', module);

stories
  .add('Таймпикер', () => {
    return <TimePicker />;
  })
  .add('Метаданные', () => {
    return <TimePicker {...TimePickerMeta} />;
  });
