import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import Input from './InputText';
import InputJson from './InputText.meta.json';

const form = withForm({ src: 'InputText' });

const stories = storiesOf('Контролы/Ввод текста', module);

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('Input'));

stories.add(
  'Метаданные',
  form(() => {
    const props = {
      placeholder: text('placeholder', InputJson.placeholder),
      disabled: boolean('disabled', InputJson.disabled),
      length: number('length', InputJson.length),
    };

    return props;
  })
);
