import React from 'react';
import { storiesOf } from '@storybook/react';
import { jsxDecorator } from 'storybook-addon-jsx';
import {
  withKnobs,
  text,
  boolean,
  number,
  array,
  select,
} from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import InputMoney from './InputMoney';
import InputMoneyJson from './InputMoney.meta';

const stories = storiesOf('Контролы/Ввод денег', module);

const form = withForm({ src: 'InputMoney' });

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('InputMoney'));
stories.addDecorator(jsxDecorator);

stories
  .add('Компонент', () => {
    return <InputMoney {...InputMoneyJson} />;
  })
  .add(
    'Метаданные',
    form(() => {
      const props = {
        ...InputMoneyJson,
      };

      return props;
    })
  )
  .add(
    'Включенные копейки',
    form(() => {
      const props = {
        ...InputMoneyJson,
        allowDecimal: true,
      };

      return props;
    })
  )
  .add('Раделитель тысяч', () => {
    const props = {
      ...InputMoneyJson,
      thousandsSeparatorSymbol: '$',
    };

    return <InputMoney {...props} />;
  });
