import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import InputNumber from './InputNumber'
import InputNumberJson from './InputNumber.meta.json'

const stories = storiesOf('Контролы/Ввод чисел', module)

stories.addParameters({
    info: {
        propTables: [InputNumber],
        propTablesExclude: [Factory],
    },
})

const form = withForm({ src: 'InputNumber' })

stories
    .add(
        'Компонент',
        () => {
            const props = {
                visible: true,
                step: '0.1',
                showButtons: true,
                min: 5,
                max: 100,
                value: 10,
            }

            return <InputNumber {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Ввод чисел'
      ~~~js
      import InputNumber from 'n2o-framework/lib/components/controls/InputNumber/InputNumber';
      
      <InputNumber
          visible={true}
          ste="0.1"
          showButtons={true}
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
                visible: InputNumberJson.visible,
                step: InputNumberJson.step,
                showButtons: InputNumberJson.showButtons,
                disabled: InputNumberJson.disabled,
                min: InputNumberJson.min,
                max: InputNumberJson.max,
            }
            return props
        }),
    )

    .add(
        'Min/max',
        () => {
            const props = {
                value: 1,
                min: -150,
                max: 150,
            }

            return <InputNumber {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Ввод чисел'
      ~~~js
      import InputNumber from 'n2o-framework/lib/components/controls/InputNumber/InputNumber';
      
      <InputNumber
          value={1}
          min={-150}
          max={150}
      />
      ~~~
      `,
            },
        },
    )

    .add(
        'Шаги',
        () => {
            const props = {
                value: 1,
                min: -150,
                max: 150,
            }

            return (
                <>
                    <div className="row" style={{ marginBottom: '10px' }}>
                        <InputNumber {...props} step="10" />
                    </div>
                    <div className="row">
                        <InputNumber {...props} step="0.05" />
                    </div>
                </>
            )
        },
        {
            info: {
                text: `
      Компонент 'Ввод чисел'
      ~~~js
      import InputNumber from 'n2o-framework/lib/components/controls/InputNumber/InputNumber';
      
        <InputNumber {...props} step="10" />
        <InputNumber {...props} step="0.05" />
      ~~~
      `,
            },
        },
    )

    .add(
        'Свойство precision',
        () => {
            const props = {
                value: 1,
                step: 1,
                min: -150,
                max: 150,
                showButtons: false,
            }

            return (
                <>
                    <div className="row mb-1 flex-column">
                        <div className="row">Без дробной части</div>
                        <div className="row">
                            <InputNumber {...props} precision={0} />
                        </div>
                    </div>
                    <div className="row mb-1 flex-column">
                        <div className="row">Только 2 символа после запятой</div>
                        <div className="row">
                            <InputNumber {...props} precision={2} />
                        </div>
                    </div>
                </>
            )
        },
        {
            info: {
                text: `
      Компонент 'Ввод чисел'
      ~~~js
      import InputNumber from 'n2o-framework/lib/components/controls/InputNumber/InputNumber';
      
        <InputNumber {...props} precision={0} />
        <InputNumber {...props} precision={2} />
      ~~~
      `,
            },
        },
    )

    .add(
        'Без кнопок',
        () => {
            const props = {
                value: 1,
                min: -150,
                max: 150,
                showButtons: false,
            }

            return <InputNumber {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Ввод чисел'
      ~~~js
      import InputNumber from 'n2o-framework/lib/components/controls/InputNumber/InputNumber';
      
        <InputNumber 
            value={1}
            min={-150}
            max={150}
            showButtons={false}
         />
      ~~~
      `,
            },
        },
    )
