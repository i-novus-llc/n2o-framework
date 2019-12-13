import React from 'react';
import { storiesOf } from '@storybook/react';

import RadioGroup from './RadioGroup';
import Radio from '../Radio/Radio';

const stories = storiesOf('Контролы/Радио', module);

stories.add(
  'Радио группа',
  () => {
    const props = {
      value: '2',
      disabled: false,
      visible: true,
      className: '',
      inline: true,
    };

    return (
      <RadioGroup name="numbers" onChange={() => {}} {...props}>
        <Radio value="1" label="Первый" />
        <Radio value="2" label="Второй" />
        <Radio value="3" label="Третий" />
      </RadioGroup>
    );
  },
  {
    info: {
      text: `
    Компонент 'RadioN2O'
    ~~~js
    import RadioGroup from 'n2o-framework/lib/components/controls/RadioGroup/RadioGroup';
    import Radio from 'n2o-framework/lib/components/controls/Radio/Radio';
    
    <RadioGroup 
        name="numbers" 
        onChange={onChange}
        value="2"
        inline={true}
      >
      <Radio value="1" label="Первый" />
      <Radio value="2" label="Второй" />
      <Radio value="3" label="Третий" />
    </RadioGroup>
    ~~~
    `,
    },
  }
);
