import React from 'react'
import { storiesOf } from '@storybook/react'

import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'

import FormWidget from './FormWidget.meta.json'

const stories = storiesOf('Виджеты/Форма', module)

stories.add('Форма с toolbar', () => {
    const form = {
        fieldsets: [
            {
                src: 'StandardFieldset',
                rows: [
                    {
                        cols: [
                            {
                                size: 12,
                                fields: [
                                    {
                                        id: 'name',
                                        src: 'StandardField',
                                        label: 'Поле 1',
                                        control: {
                                            src: 'InputText',
                                            readOnly: false,
                                        },
                                        toolbar: [
                                            {
                                                buttons: [
                                                    {
                                                        src: 'StandardButton',
                                                        id: 'update1',
                                                        label: 'Кнопка',
                                                        icon: 'fa fa-trash',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'primary',
                                                        hint: 'some hint',
                                                    },
                                                    {
                                                        src: 'DropdownButton',
                                                        id: 'update3',
                                                        label: 'Кнопка',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'primary',
                                                        subMenu: [
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'Проверка длинного лейбла',
                                                                icon: 'fa fa-paper-plane',
                                                                hint: 'Подписать запись',
                                                                count: '4',
                                                                color: 'primary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'Проверка длинного лейбла №2',
                                                                icon: 'fa fa-fax',
                                                                hint: 'Подписать запись',
                                                                count: '15',
                                                                color: 'primary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                        ],
                                                    },
                                                    {
                                                        src: 'DropdownButton',
                                                        id: 'update4',
                                                        label: 'Проверка цвета на кнопках',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'secondary',
                                                        subMenu: [
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'Проверка success',
                                                                icon: 'fa fa-paper-plane',
                                                                hint: 'Подписать запись',
                                                                count: '4',
                                                                color: 'success',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'Проверка primary',
                                                                icon: 'fa fa-paper-plane',
                                                                hint: 'Подписать запись',
                                                                count: '4',
                                                                color: 'primary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'Проверка danger',
                                                                icon: 'fa fa-paper-plane',
                                                                hint: 'Подписать запись',
                                                                count: '4',
                                                                color: 'danger',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                        ],
                                                    },
                                                    {
                                                        src: 'DropdownButton',
                                                        id: 'update4',
                                                        label: 'для теста очень длинного label на кнопке',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'secondary',
                                                        subMenu: [
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'label',
                                                                icon: 'fa fa-paper-plane',
                                                                count: '4',
                                                                color: 'secondary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                        ],
                                                    },
                                                    {
                                                        src: 'DropdownButton',
                                                        id: 'update4',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'secondary',
                                                        hint: 'some hint',
                                                        subMenu: [
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'label',
                                                                icon: 'fa fa-paper-plane',
                                                                hint: 'Подписать запись',
                                                                count: '4',
                                                                color: 'secondary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                        ],
                                                    },
                                                    {
                                                        src: 'DropdownButton',
                                                        id: 'update4',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'secondary',
                                                        subMenu: [
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label:
                                  'Длинный лэйбл для теста popper должен перевернуться',
                                                                icon: 'fa fa-paper-plane',
                                                                count: '4',
                                                                color: 'secondary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label:
                                  'Второй длинный лэйбл для теста popper должен перевернуться',
                                                                icon: 'fa fa-paper-plane',
                                                                hint: 'Подписать запись',
                                                                count: '4',
                                                                color: 'secondary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                        ],
                                                    },
                                                ],
                                            },
                                        ],
                                    },
                                ],
                            },
                        ],
                    },
                ],
            },
        ],
    }

    const form2 = {
        fieldsets: [
            {
                src: 'StandardFieldset',
                rows: [
                    {
                        cols: [
                            {
                                size: 12,
                                fields: [
                                    {
                                        id: 'uniqId',
                                        src: 'StandardField',
                                        label: 'Поле 2',
                                        control: {
                                            src: 'InputText',
                                            readOnly: false,
                                        },
                                        toolbar: [
                                            {
                                                buttons: [
                                                    {
                                                        src: 'StandardButton',
                                                        id: 'update123',
                                                        label: 'Кнопка md',
                                                        icon: 'fa fa-trash',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'secondary',
                                                        hint: 'some hint',
                                                        size: 'md',
                                                    },
                                                    {
                                                        src: 'DropdownButton',
                                                        id: 'update3334',
                                                        label: 'Кнопка md',
                                                        actionId: 'update',
                                                        validate: true,
                                                        validatedWidgetId: 'create2_main',
                                                        color: 'secondary',
                                                        size: 'md',
                                                        hint: 'длинный hint кнопка md',
                                                        subMenu: [
                                                            {
                                                                id: 'testBtn221',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'Проверка длинного лейбла',
                                                                icon: 'fa fa-paper-plane',
                                                                hint: 'Подписать запись',
                                                                count: '4',
                                                                size: 'md',
                                                                color: 'secondary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                            {
                                                                id: 'testBtn226',
                                                                actionId: 'dummy',
                                                                src: 'StandardButton',
                                                                label: 'Проверка длинного лейбла №2',
                                                                icon: 'fa fa-fax',
                                                                count: '15',
                                                                size: 'md',
                                                                color: 'secondary',
                                                                visible: true,
                                                                disabled: false,
                                                            },
                                                        ],
                                                    },
                                                ],
                                            },
                                        ],
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
            <div className="d-flex flex-nowrap w-100">
                <Factory
                    level={WIDGETS}
                    {...FormWidget.Page_Form}
                    id="Page_Form"
                    form={form2}
                    toolbar={{}}
                />
                <Factory
                    level={WIDGETS}
                    {...FormWidget.Page_Form}
                    id="Page_Form"
                    form={form2}
                    toolbar={{}}
                />
            </div>
        </div>
    )
})
