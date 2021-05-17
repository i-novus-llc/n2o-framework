import React from 'react'
import { storiesOf } from '@storybook/react'

import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'

import FormWidgetData from './FormWidget.meta.json'

const stories = storiesOf('Виджеты/Форма', module)

stories.add('Расположение полей', () => {
    const fieldSets = [
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
                                    label: 'Имя',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                        {
                            size: 6,
                            fields: [
                                {
                                    id: 'surname',
                                    src: 'StandardField',
                                    label: 'Фамилия',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                    ],
                },
                {
                    cols: [
                        {
                            size: 8,
                            fields: [
                                {
                                    id: 'nam',
                                    src: 'StandardField',
                                    label: 'Отчество',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                        {
                            size: 4,
                            fields: [
                                {
                                    id: 'sur',
                                    src: 'StandardField',
                                    label: 'Должность',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                    ],
                },
                {
                    cols: [
                        {
                            size: 3,
                            fields: [
                                {
                                    id: 'age',
                                    src: 'StandardField',
                                    label: 'Возраст',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                        {
                            size: 3,
                            fields: [
                                {
                                    id: 'weight',
                                    src: 'StandardField',
                                    label: 'Вес',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                        {
                            size: 3,
                            fields: [
                                {
                                    id: 'size',
                                    src: 'StandardField',
                                    label: 'Рост',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                        {
                            size: 3,
                            fields: [
                                {
                                    id: 'home',
                                    src: 'StandardField',
                                    label: 'Адрес',
                                    control: {
                                        src: 'Input',
                                        readOnly: false,
                                    },
                                },
                            ],
                        },
                    ],
                },
            ],
        },
    ]

    const form = {
        fieldsets: fieldSets,
    }

    return (
        <div>
            <Factory
                level={WIDGETS}
                {...FormWidgetData.Page_Form}
                id="Page_Form"
                form={form}
                toolbar={{}}
            />
        </div>
    )
})
