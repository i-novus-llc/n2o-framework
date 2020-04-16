import React from 'react';
import { storiesOf } from '@storybook/react';

import StatusText from './StatusText';

const stories = storiesOf('UI Компоненты/StatusText', module);

stories
  .add('Компонент', () => {
    return (
      <StatusText
        text={'Test text'}
        className={'test-class'}
        color={'success'}
      />
    );
  })
  .add('Позиционирование', () => {
    return (
      <>
        <StatusText
          text={'Test text'}
          className={'test-class'}
          color={'success'}
          textPosition={'left'}
        />
        <StatusText
          text={'Test text'}
          className={'test-class'}
          color={'info'}
          textPosition={'right'}
        />
      </>
    );
  });
