import React from 'react'
import { storiesOf } from '@storybook/react'

import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'

import FormWidget from './FormWidget.meta.json'

const stories = storiesOf('Виджеты/Форма', module)

stories.add('Биндинги', () => {
    const form = {
        fieldsets: [
            {
                src: 'StandardFieldset',
                rows: [
                    {
                        cols: [
                            {
                                size: 6,
                                fields: [
                                    {
                                        id: 'name',
                                        src: 'StandardField',
                                        label: 'Поле 1',
                                        control: {
                                            src: 'InputText',
                                            readOnly: false,
                                        },
                                    },
                                ],
                            },
                            {
                                size: 6,
                                fields: [
                                    {
                                        id: 'name',
                                        src: 'StandardField',
                                        label: 'Поле 2',
                                        control: {
                                            src: 'InputText',
                                            readOnly: false,
                                        },
                                    },
                                ],
                            },
                        ],
                    },
                ],
            },
        ],
    }

    return (
        <div>
            <Factory
                level={WIDGETS}
                {...FormWidget.Page_Form}
                id="Page_Form"
                form={form}
                toolbar={{}}
            />
        </div>
    )
})
