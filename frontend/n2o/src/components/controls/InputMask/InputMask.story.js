import React from 'react';
import { storiesOf } from '@storybook/react';
import { withKnobs, text, boolean, number, array, select } from '@storybook/addon-knobs/react';
import withTests from 'N2oStorybook/withTests';
import withForm from 'N2oStorybook/decorators/withForm';
import InputMask from './InputMask';
import InputMaskJson from './InputMask.meta.json';

const stories = storiesOf('Контролы/Маскированный ввод', module);

const form = withForm({ src: 'InputMask' });

stories.addDecorator(withKnobs);
stories.addDecorator(withTests('InputMask'));

stories
  .add('Компонент', () => {
    const props = {
      mask: text('mask', '9999'),
      className: text('className', ''),
      preset: select(
        'preset',
        ['none', 'phone', 'post-code', 'date', 'money', 'percentage', 'card'],
        'none'
      ),
      placeholder: text('placeholder', 'Введите что-нибудь...'),
      placeholderChar: text('placeholderChar', '_'),
      value: text('value', 1234),
      guide: boolean('guide', false),
      keepCharPosition: boolean('keepCharPosition', true),
      resetOnNotValid: boolean('resetOnNotValid', true)
    };

    return <InputMask {...props} />;
  })

  .add(
    'Метаданные',
    form(() => {
      const props = {
        mask: text('mask', InputMaskJson.mask),
        preset: select(
          'preset',
          ['none', 'phone', 'post-code', 'date', 'money', 'percentage', 'card'],
          InputMaskJson.preset
        ),
        placeholder: text('placeholder', InputMaskJson.placeholder),
        placeholderChar: text('placeholderChar', InputMaskJson.placeholderChar),
        guide: boolean('guide', InputMaskJson.guide),
        keepCharPosition: boolean('keepCharPosition', InputMaskJson.keepCharPositions),
        resetOnNotValid: boolean('resetOnNotValid', InputMaskJson.resetOnNotValid)
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
