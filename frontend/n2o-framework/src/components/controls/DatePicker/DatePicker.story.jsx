import React from 'react'
import { storiesOf } from '@storybook/react'
import withForm from 'N2oStorybook/decorators/withForm'

import Factory from '../../../core/factory/Factory'

import DatePicker from './DatePicker'
import DatePickerJson from './DatePicker.meta.json'

const form = withForm({ src: 'DatePicker' })
const stories = storiesOf('Контролы/Выбор дат', module)

stories.addParameters({
    info: {
        propTables: [DatePicker],
        propTablesExclude: [Factory],
    },
})

stories
    .add(
        'Компонент',
        () => {
            const props = {
                min: DatePickerJson.min,
                max: DatePickerJson.max,
                disabled: DatePickerJson.disabled,
                timeFormat: DatePickerJson.timeFormat,
                dateDivider: ' ',
                dateFormat: DatePickerJson.dateFormat,
                locale: DatePickerJson.locale,
            }
            return <DatePicker {...props} onChange={() => {}} />
        },
        {
            info: {
                text: `
      Компонент 'Выбор даты'
      ~~~js
      import DatePicker from 'n2o-framework/lib/components/controls/DatePicker/DatePicker';

      <DatePicker
          onChange={onChange}
          dateFormat="DD/MM/YYYY"
          timeFormat="HH:mm"
          placeholder="Введите дату"
          locale="ru"
          openOnFocus={false}
          utc={true}
       />
      ~~~
      `,
            },
        },
    )

    .add(
        'Метаданные',
        form(() => ({
            dateFormat: 'DD/MM/YYYY',
            min: '10/03/2020',
            max: '20/03/2020',
            disabled: false,
            locale: 'ru',
        })),
    )

    .add(
        'Форматы дат',
        () => (
            <>
                <DatePicker
                    dateFormat="DD/MM/YYYY"
                    timeFormat="HH:mm"
                    placeholder="dateFormat=DD/MM/YYYY timeFormat=HH:mm"
                />
                <br />
                <DatePicker dateFormat="DD.MM.YYYY" placeholder="DD.MM.YYYY" />
                <br />
                <DatePicker dateFormat="YYYY-MM" placeholder="YYYY-MM" />
            </>
        ),
        {
            info: {
                text: `
      Компонент 'Выбор даты'
      ~~~js
      import DatePicker from 'n2o-framework/lib/components/controls/DatePicker/DatePicker';

        <DatePicker
          dateFormat="DD/MM/YYYY"
          timeFormat="HH:mm"
          placeholder="dateFormat=DD/MM/YYYY timeFormat=HH:mm"
        />
        <DatePicker dateFormat="DD.MM.YYYY" placeholder="DD.MM.YYYY" />
        <DatePicker dateFormat="YYYY-MM" placeholder="YYYY-MM" />
      ~~~
      `,
            },
        },
    )

    .add(
        'Текущая неделя',
        () => (
            <>
                <DatePicker />
            </>
        ),
        {
            info: {
                text: `
      Компонент 'Выбор даты'
      ~~~js
      import DatePicker from 'n2o-framework/lib/components/controls/DatePicker/DatePicker';

      <DatePicker />
      ~~~
      `,
            },
        },
    )

    .add(
        'Время по умолчанию',
        () => (
            <>
                <DatePicker
                    dateFormat="DD/MM/YYYY"
                    timeFormat="HH:mm"
                    defaultTime="13:00"
                    placeholder="dateFormat=DD/MM/YYYY timeFormat=HH:mm defaultTime=13:00"
                />
            </>
        ),
        {
            info: {
                text: `
      Компонент 'Выбор даты'
      ~~~js
      import DatePicker from 'n2o-framework/lib/components/controls/DatePicker/DatePicker';

      <DatePicker
          dateFormat="DD/MM/YYYY"
          timeFormat="HH:mm"
          defaultTime="13:00"
          placeholder="dateFormat=DD/MM/YYYY timeFormat=HH:mm defaultTime=13:00"
        />
      ~~~
      `,
            },
        },
    )

    .add(
        'Расположение',
        () => (
            <div style={{ marginTop: '100px' }}>
                <DatePicker popupPlacement="top" />
            </div>
        ),
        {
            info: {
                text: `
      Компонент 'Выбор даты'
      ~~~js
      import DatePicker from 'n2o-framework/lib/components/controls/DatePicker/DatePicker';

      <DatePicker popupPlacement="top" />
      ~~~
      `,
            },
        },
    )
    .add(
        'Min/Max',
        () => (
            <div>
          Доступные даты с 28.06.2019 по 30.06.2019
                <DatePicker
                    popupPlacement="top"
                    dateFormat="YYYY-MM-DD"
                    dateDivider=" "
                    min="2019-06-28"
                    max="2019-06-30"
                />
            </div>
        ),
        {
            info: {
                text: `
      Компонент 'Выбор даты'
      ~~~js
      import DatePicker from 'n2o-framework/lib/components/controls/DatePicker/DatePicker';

      <DatePicker
          popupPlacement="top"
          dateFormat={'YYYY-MM-DD'}
          timeFormat={'hh:mm:ss'}
          dateDivider={' '}
          min="2019-06-28 00:00:00"
          max="2019-06-30 00:00:00"
        />
      ~~~
      `,
            },
        },
    )
