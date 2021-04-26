import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import InputMask from './InputMask'
import InputMaskJson from './InputMask.meta.json'

const stories = storiesOf('Контролы/Маскированный ввод', module)

const form = withForm({ src: 'InputMask' })

stories.addParameters({
    info: {
        propTables: [InputMask],
        propTablesExclude: [Factory],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                mask: '9999',
                className: '',
                preset: 'none',
                placeholder: 'Введите что-нибудь...',
                placeholderChar: '_',
                value: 1234,
                guide: false,
                keepCharPosition: true,
                clearOnBlur: true,
            }

            return <InputMask {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Масктрованный ввод'
      ~~~js
      import InputMask from 'n2o-framework/lib/components/controls/InputMask/InputMask';
      
      <InputMask
          mask="9999"
          preset="none"
          placeholder="Введите что-нибудь..."
          placeholderChar="_"
          value={1234}
          guide={false}
          keepCharPosition={true}
          clearOnBlur={true}
       />
      ~~~
      `,
            },
        },
    )

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
                clearOnBlur: InputMaskJson.clearOnBlur,
            }

            return props
        }),
    )

    .add(
        'Пресеты',
        () => (
            <>
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
            </>
        ),
        {
            info: {
                text: `
      Компонент 'Масктрованный ввод'
      ~~~js
      import InputMask from 'n2o-framework/lib/components/controls/InputMask/InputMask';
      
      <InputMask preset="phone" placeholder="Телефон" />
      <InputMask preset="post-code" placeholder="Индекс" />
      <InputMask preset="date" placeholder="Дата" />
      <InputMask preset="money" placeholder="Деньги" />
      <InputMask preset="percentage" placeholder="Проценты" />
      <InputMask preset="card" placeholder="Номер карты" />
      ~~~
      `,
            },
        },
    )

    .add(
        'Комбинации',
        () => (
            <>
                <InputMask
                    preset="card"
                    guide
                    keepCharPositions={false}
                    clearOnBlur={false}
                    placeholder="C шаблоном значения"
                />
                <br />
                <InputMask
                    preset="card"
                    guide={false}
                    keepCharPositions
                    clearOnBlur={false}
                    placeholder="Сохранять положение символа"
                />
                <br />
                <InputMask
                    preset="card"
                    guide={false}
                    keepCharPositions={false}
                    clearOnBlur
                    placeholder="Сброс при невалидных"
                />
            </>
        ),
        {
            info: {
                text: `
      Компонент 'Масктрованный ввод'
      ~~~js
      import InputMask from 'n2o-framework/lib/components/controls/InputMask/InputMask';
      
        <InputMask
          preset={'card'}
          guide={true}
          keepCharPositions={false}
          clearOnBlur={false}
          placeholder="C шаблоном значения"
        />
        <InputMask
          preset={'card'}
          guide={false}
          keepCharPositions={true}
          clearOnBlur={false}
          placeholder="Сохранять положение символа"
        />
        <InputMask
          preset={'card'}
          guide={false}
          keepCharPositions={false}
          clearOnBlur={true}
          placeholder="Сброс при невалидных"
        />
      ~~~
      `,
            },
        },
    )
