import React from 'react';
import { storiesOf } from '@storybook/react';

import withForm from 'N2oStorybook/decorators/withForm';
import InputMoney from './InputMoney';
import InputMoneyJson from './InputMoney.meta';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Контролы/Ввод денег', module);

const form = withForm({ src: 'InputMoney' });

stories.addParameters({
  info: {
    propTables: [InputMoney],
    propTablesExclude: [Factory],
  },
});

stories
  .add(
    'Компонент',
    () => {
      return <InputMoney {...InputMoneyJson} />;
    },
    {
      info: {
        text: `
      Компонент 'Ввод денег'
      ~~~js
      import InputMoney from 'n2o-framework/lib/components/controls/InputMoney/InputMoney';
      
      <InputMoney 
          className="n2o"
          preset="money"
          guide={true}
          keepCharPosition={false}
          resetOnNotValid={true}
          prefix=""
          suffix=" руб."
          thousandsSeparatorSymbol=" "
          decimalSymbol=","
      />
      ~~~
      `,
      },
    }
  )
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
    () => {
      return <InputMoney {...InputMoneyJson} allowDecimal={true} />;
    },
    {
      info: {
        text: `
      Компонент 'Ввод денег'
      ~~~js
      import InputMoney from 'n2o-framework/lib/components/controls/InputMoney/InputMoney';
      
      <InputMoney 
          {...props}
          allowDecimal={true}
      />
      ~~~
      `,
      },
    }
  )
  .add(
    'Раделитель тысяч',
    () => {
      const props = {
        ...InputMoneyJson,
        thousandsSeparatorSymbol: '$',
      };

      return <InputMoney {...props} />;
    },
    {
      info: {
        text: `
      Компонент 'Ввод денег'
      ~~~js
      import InputMoney from 'n2o-framework/lib/components/controls/InputMoney/InputMoney';
      
      <InputMoney 
          {...props}
          thousandsSeparatorSymbol="$"
      />
      ~~~
      `,
      },
    }
  );
