import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import DateIntervalJSON from './DateInterval.meta.json'
import DateInterval from './DateInterval'

const stories = storiesOf('Контролы/Интервал дат', module)

const form = withForm({ src: 'DateInterval' })

stories.addParameters({
    info: {
        propTables: [DateInterval],
        propTablesExclude: [Factory],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                dateFormat: 'DD/MM/YYYY',
                timeFormat: 'HH:mm:ss',
                defaultTime: '13:00:00',
                min: '5/12/2012',
                max: '15/12/2021',
                disabled: false,
                locale: 'ru',
                outputFormat: 'DD/MM/YYYY',
            }
            return <DateInterval onChange={() => {}} {...props} />
        },
        {
            info: {
                text: `
      Компонент 'Интервал дат'
      ~~~js
      import DateInterval from 'n2o-framework/lib/components/controls/DatePicker/DateInterval';

      <DateInterval
          dateFormat="DD/MM/YYYY"
          timeFormat="HH:mm:ss"
          defaultTime="13:00:00"
          min="5/12/2012"
          max="15/12/2021"
          locale="ru"
          outputFormat="DD/MM/YYYY"
          onChange={onChange}
      />
      ~~~
      `,
            },
        },
    )
    .add(
        'Метаданные',
        form(() => DateIntervalJSON),
    )
    .add(
        'Min/Max',
        form(() => ({
            dateFormat: 'DD.MM.YYYY',
            min: '2020-03-05 00:00:00',
            max: '2020-03-25 00:00:00',
        })),
    )
    .add(
        'Min',
        form(() => ({
            dateFormat: 'DD.MM.YYYY',
            min: '2020-03-05 00:00:00',
        })),
    )
    .add(
        'Max',
        form(() => ({
            dateFormat: 'DD.MM.YYYY',
            max: '2020-03-25 00:00:00',
        })),
    )
