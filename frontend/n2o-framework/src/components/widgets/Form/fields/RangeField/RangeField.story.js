import React from 'react'
import set from 'lodash/set'
import { storiesOf } from '@storybook/react'
import withPage from 'N2oStorybook/decorators/withPage'
import RangeFieldJson from 'N2oStorybook/json/RangeField'

import Factory from '../../../../../core/factory/Factory'
import { WIDGETS } from '../../../../../core/factory/factoryLevels'

const stories = storiesOf('Виджеты/Форма/Fields/RangeField')
const renderForm = json => (
    <Factory level={WIDGETS} {...json.Page_Form} id="Page_Form" />
)
stories
    .add('Метаданные', () => withPage(RangeFieldJson)(() => renderForm(RangeFieldJson)))
    .add('Разделитель', () => {
        const metadata = { ...RangeFieldJson }
        set(
            metadata,
            'Page_Form.form.fieldsets[0].rows[0].cols[0].fields[0].divider',
            '$$',
        )
        return withPage(metadata)(() => renderForm(metadata))
    })
    .add('Постфикс', () => {
        const metadata = { ...RangeFieldJson }
        set(
            metadata,
            'Page_Form.form.fieldsets[0].rows[0].cols[0].fields[0].measure',
            'руб.',
        )
        set(
            metadata,
            'Page_Form.form.fieldsets[0].rows[0].cols[0].fields[0].divider',
            undefined,
        )
        return withPage(metadata)(() => renderForm(metadata))
    })
    .add('Лоадер', () => {
        const metadata = { ...RangeFieldJson }
        set(
            metadata,
            'Page_Form.form.fieldsets[0].rows[0].cols[0].fields[0].loading',
            true,
        )
        return withPage(metadata)(() => renderForm(metadata))
    })
