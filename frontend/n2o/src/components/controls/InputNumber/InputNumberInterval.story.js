import React from 'react';
import { storiesOf } from '@storybook/react';
import { action } from '@storybook/addon-actions';
import {
  withKnobs,
  text,
  boolean,
  number,
  array,
} from '@storybook/addon-knobs/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import withTests from 'N2oStorybook/withTests';
import InputNumberInterval from './InputNumberInterval';

const stories = storiesOf('Контролы/Интервал ввода чисел', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('InputNumber'));
stories.addDecorator(jsxDecorator);

stories.add('базовый функционал', () => {
  const props = {
    value: array('value', [1, 1]),
    visible: boolean('visible', true),
    step: text('step', '0.1'),
    min: number('min', -0.5),
    max: number('max', 1.5),
    showButtons: boolean('showButtons', true),
    disabled: boolean('disabled', false),
  };

  return <InputNumberInterval {...props} />;
});
