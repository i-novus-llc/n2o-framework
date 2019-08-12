import React from 'react';
import { storiesOf } from '@storybook/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import InputMask from './InputMask';
import InputMaskJson from './InputMask.meta.json';
import Factory from '../../../core/factory/Factory';

const stories = storiesOf('Контролы/Маскированный ввод', module);

const form = withForm({ src: 'InputMask' });

stories.addDecorator(withTests('InputMask'));

stories.addParameters({
  info: {
    propTables: [InputMask],
    propTablesExclude: [Factory],
  },
});

stories
  .add('Компонент', () => {
    const props = {
      mask: '9999',
      className: '',
      preset: 'none',
      placeholder: 'Введите что-нибудь...',
      placeholderChar: '_',
      value: 1234,
      guide: false,
      keepCharPosition: true,
      resetOnNotValid: true,
    };

    return <InputMask {...props} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        mask: InputMaskJson.mask,
        preset: InputMaskJson.preset,
        placeholder: InputMaskJson.placeholder,
        placeholderChar: InputMaskJson.placeholderChar,
        guide: InputMaskJson.guide,
        keepCharPosition: InputMaskJson.keepCharPositions,
        resetOnNotValid: InputMaskJson.resetOnNotValid,
      };

      return props;
    })
  )

  .add('Пресеты', () => {
    return (
      <React.Fragment>
        <InputMask preset="phone" placeholder="Телефон" />
        <br />
        <InputMask preset="post-code" placeholder="Индекс" />
        <br />
        <InputMask preset="date" placeholder="Дата" />
        <br />
        <InputMask preset="money" placeholder="Деньги" />
        <br />
        <InputMask preset="percentage" placeholder="Проценты" />
        <br />
        <InputMask preset="card" placeholder="Номер карты" />
        <br />
      </React.Fragment>
    );
  })

  .add('Комбинации', () => {
    return (
      <React.Fragment>
        <InputMask
          preset={'card'}
          guide={true}
          keepCharPositions={false}
          resetOnNotValid={false}
          placeholder="C шаблоном значения"
        />
        <br />
        <InputMask
          preset={'card'}
          guide={false}
          keepCharPositions={true}
          resetOnNotValid={false}
          placeholder="Сохранять положение символа"
        />
        <br />
        <InputMask
          preset={'card'}
          guide={false}
          keepCharPositions={false}
          resetOnNotValid={true}
          placeholder="Сброс при невалидных"
        />
      </React.Fragment>
    );
  });
