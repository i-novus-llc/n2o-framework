import {
    CHANGE_BUTTON_VISIBILITY,
    CHANGE_BUTTON_TITLE,
    CHANGE_BUTTON_COUNT,
    CHANGE_BUTTON_SIZE,
    CHANGE_BUTTON_COLOR,
    CHANGE_BUTTON_DISABLED,
    TOGGLE_BUTTON_DISABLED,
    TOGGLE_BUTTON_VISIBILITY,
    REGISTER_BUTTON,
    CHANGE_BUTTON_HINT,
    CHANGE_BUTTON_ICON,
    CHANGE_BUTTON_CLASS,
    CHANGE_BUTTON_STYLE,
} from '../constants/toolbar'
import { RESET_STATE } from '../constants/widgets'

import toolbar from './toolbar'

describe('Проверка toolbar reducer', () => {
    it('Проверка REGISTER_BUTTON', () => {
        expect(
            toolbar(
                {
                    buttonKey: {},
                },
                {
                    type: REGISTER_BUTTON,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        color: 'red',
                        icon: 'fa fa-plus',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    color: 'red',
                    conditions: null,
                    count: 0,
                    error: null,
                    hint: null,
                    icon: 'fa fa-plus',
                    buttonId: 'buttonId',
                    disabled: false,
                    isInit: true,
                    visible: true,
                    key: 'buttonKey',
                    loading: false,
                    message: null,
                    size: null,
                    title: null,
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_VISIBILITY', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            visible: false,
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_VISIBILITY,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        visible: true,
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    visible: true,
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_TITLE', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            title: 'title',
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_TITLE,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        title: 'new title',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    title: 'new title',
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_COUNT', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            count: 20,
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_COUNT,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        count: 30,
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    count: 30,
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_SIZE', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            size: 'sm',
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_SIZE,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        size: 'lg',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    size: 'lg',
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_COLOR', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            color: 'red',
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_COLOR,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        color: 'blue',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    color: 'blue',
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_DISABLED', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            disabled: true,
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_DISABLED,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        disabled: false,
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    disabled: false,
                },
            },
        })
    })

    it('Проверка TOGGLE_BUTTON_DISABLED', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            disabled: true,
                        },
                    },
                },
                {
                    type: TOGGLE_BUTTON_DISABLED,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    disabled: false,
                },
            },
        })
    })

    it('Проверка TOGGLE_BUTTON_VISIBILITY', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            visible: true,
                        },
                    },
                },
                {
                    type: TOGGLE_BUTTON_VISIBILITY,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    visible: false,
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_HINT', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            hint: 'hint',
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_HINT,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        hint: 'new hint',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    hint: 'new hint',
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_STYLE', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            style: {
                                color: 'red',
                                fontSize: 18,
                            },
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_STYLE,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        style: {
                            position: 'relative',
                            left: '-4px',
                        },
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    style: {
                        position: 'relative',
                        left: '-4px',
                    },
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_ICON', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            icon: 'fa fa-minus',
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_ICON,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        icon: 'fa fa-plus',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    icon: 'fa fa-plus',
                },
            },
        })
    })

    it('Проверка CHANGE_BUTTON_CLASS', () => {
        expect(
            toolbar(
                {
                    buttonKey: {
                        buttonId: {
                            className: 'old-class',
                        },
                    },
                },
                {
                    type: CHANGE_BUTTON_CLASS,
                    payload: {
                        key: 'buttonKey',
                        buttonId: 'buttonId',
                        className: 'new-class',
                    },
                },
            ),
        ).toEqual({
            buttonKey: {
                buttonId: {
                    className: 'new-class',
                },
            },
        })
    })

    it('Проверка RESET_STATE', () => {
        expect(
            toolbar(
                {
                    WidgetId: {
                        buttonKey: {
                            buttonId: {
                                color: 'red',
                            },
                        },
                    },
                },
                {
                    type: RESET_STATE,
                    payload: {
                        widgetId: 'WidgetId',
                        buttonbuttonId: 'buttonId',
                        buttonKey: 'buttonKey',
                    },
                },
            ),
        ).toEqual({
            WidgetId: {
                buttonKey: {
                    buttonId: {
                        color: 'red',
                    },
                    isInit: false,
                },
            },
        })
    })
})
