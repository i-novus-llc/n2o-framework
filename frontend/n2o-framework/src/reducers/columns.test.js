import {
    CHANGE_COLUMN_DISABLED,
    CHANGE_COLUMN_VISIBILITY,
    REGISTER_COLUMN,
    TOGGLE_COLUMN_VISIBILITY,
} from '../constants/columns'
import { RESET_STATE } from '../constants/widgets'

import columns from './columns'

describe('Тесты columns reducer', () => {
    it('Проверка REGISTER_COLUMN', () => {
        expect(
            columns(
                {
                    testKey: {
                        name: 'testColumns',
                    },
                },
                {
                    type: REGISTER_COLUMN,
                    payload: {
                        key: 'testKey',
                        columnId: 'columnId',
                    },
                },
            ),
        ).toEqual({
            testKey: {
                columnId: {
                    key: 'testKey',
                    columnId: 'columnId',
                    disabled: false,
                    isInit: true,
                    visible: true,
                    frozen: false,
                },
                name: 'testColumns',
            },
        })
    })

    it('Проверка CHANGE_COLUMN_VISIBILITY', () => {
        expect(
            columns(
                {
                    testKey: {
                        name: 'testName',
                    },
                },
                {
                    type: CHANGE_COLUMN_VISIBILITY,
                    payload: {
                        key: 'testKey',
                        columnId: 'columnId',
                        visible: true,
                    },
                },
            ),
        ).toEqual({
            testKey: {
                columnId: {
                    disabled: false,
                    isInit: true,
                    visible: true,
                    frozen: false,
                },
                name: 'testName',
            },
        })
    })

    it('Проверка CHANGE_COLUMN_DISABLED', () => {
        expect(
            columns(
                {
                    testKey: {
                        name: 'testName',
                    },
                },
                {
                    type: CHANGE_COLUMN_DISABLED,
                    payload: {
                        key: 'testKey',
                        columnId: 'columnId',
                        disabled: true,
                    },
                },
            ),
        ).toEqual({
            testKey: {
                columnId: {
                    disabled: true,
                    isInit: true,
                    visible: true,
                    frozen: false,
                },
                name: 'testName',
            },
        })
    })

    it('Проверка TOGGLE_COLUMN_VISIBILITY', () => {
        expect(
            columns(
                {
                    testKey: {
                        name: 'testName',
                    },
                },
                {
                    type: TOGGLE_COLUMN_VISIBILITY,
                    payload: {
                        key: 'testKey',
                        columnId: 'columnId',
                    },
                },
            ),
        ).toEqual({
            testKey: {
                columnId: {
                    disabled: false,
                    isInit: true,
                    visible: false,
                    frozen: false,
                },
                name: 'testName',
            },
        })
    })

    it('Проверка RESET_STATE', () => {
        expect(
            columns(
                {
                    widgetId: {
                        columnId: {
                            isInit: true,
                        },
                    },
                },
                {
                    type: RESET_STATE,
                    payload: {
                        widgetId: 'widgetId',
                        columnId: 'columnId',
                    },
                },
            ),
        ).toEqual({
            widgetId: {
                columnId: {
                    isInit: false,
                },
            },
        })
    })
})
