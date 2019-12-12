import React from 'react';
import { storiesOf } from '@storybook/react';

import CheckboxGroup from './CheckboxGroup';
import CheckboxButton from '../Checkbox/CheckboxButton';

const stories = storiesOf('Контролы/Группа чекбоксов', module);

stories.add(
  'Группа в виде кнопок',
  () => {
    const props = {
      value: ['1', '2'],
      disabled: false,
      visible: true,
      className: '',
      inline: false,
    };

    return (
      <CheckboxGroup name="numbers" isBtnGroup={true} {...props}>
        <CheckboxButton value="1" label="Первый" />
        <CheckboxButton value="2" label="Второй" />
        <CheckboxButton value="3" label="Третий" />
      </CheckboxGroup>
    );
  },
  {
    info: {
      text: `
    Компонент 'CheckboxGroupButton'
    ~~~js
    import CheckboxGroup from 'n2o-framework/lib/components/controls/CheckboxGroup/CheckboxGroup';
    import CheckboxButton from 'n2o-framework/lib/components/controls/CheckboxButton/CheckboxButton';
    
    <CheckboxGroup 
        name="numbers" 
        isBtnGroup={true}
        value={['1', '2']}
        visible={true}
    >
      <CheckboxButton value="1" label="Первый" />
      <CheckboxButton value="2" label="Второй" />
      <CheckboxButton value="3" label="Третий" />
    </CheckboxGroup>
    ~~~
    `,
    },
  }
);
