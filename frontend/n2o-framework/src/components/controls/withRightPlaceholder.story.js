import React from 'react';
import { storiesOf } from '@storybook/react';
import InputMask from './InputMask/InputMask';
import InputNumber from './InputNumber/InputNumber';
import InputText from './InputText/InputText';
import TextArea from './TextArea/TextArea';
import InputMoney from './InputMoney/InputMoney';
import withRightPlaceholder from './withRightPlaceholder';

const stories = storiesOf('Контролы/RightPlaceholder', module);

stories.addParameters({
  info: {
    propTables: [withRightPlaceholder],
    propTablesExclude: [
      InputMask,
      InputNumber,
      InputText,
      TextArea,
      InputMoney,
    ],
  },
});

stories.add(
  'Компонент',
  () => {
    const maskProps = {
      mask: '9999',
      className: '',
      preset: 'none',
      placeholder: 'Введите что-нибудь...',
      placeholderChar: '_',
      value: 1234,
      guide: false,
      keepCharPosition: true,
      resetOnNotValid: true,
      measure: 'км',
    };
    const numberProps = {
      value: [1, 1],
      visible: true,
      step: '0.1',
      min: -0.5,
      max: 1.5,
      showButtons: true,
      disabled: false,
      measure: 'см',
    };
    const areaProps = {
      placeholder: 'Введите значение',
      disabled: false,
      rows: 5,
      maxRows: 10,
      measure: 'много текста',
    };
    const moneyProps = {
      className: 'n2o',
      preset: 'money',
      dictionary: {},
      guide: true,
      keepCharPositions: false,
      resetOnNotValid: true,
      prefix: '',
      suffix: ' руб.',
      thousandsSeparatorSymbol: ' ',
      decimalSymbol: ',',
      integerLimit: null,
      measure: 'руб',
    };

    return (
      <div className="d-flex flex-column">
        <div className="m-2">
          InputMask
          <InputMask {...maskProps} />
        </div>
        <div className="m-2">
          InputNumber
          <InputNumber {...numberProps} />
        </div>
        <div className="m-2">
          InputText
          <InputText value="text" measure="текст" />
        </div>
        <div className="m-2">
          TextArea
          <TextArea {...areaProps} />
        </div>
        <div className="m-2">
          InputMoney
          <InputMoney {...moneyProps} />
        </div>
      </div>
    );
  },
  {
    info: {
      text: `
    Компонент-обертка 'withRightPlaceholder'
    ~~~js
    
    <Input
        {...props}
        measure:="руб"
     />
    ~~~
    `,
    },
  }
);
