import toolbar, {
    registerButton,
    changeButtonTitle,
    changeButtonCount,
    changeButtonSize,
    changeButtonColor,
    changeButtonDisabled,
    toggleButtonDisabled,
    changeButtonHint,
    changeButtonStyle,
    changeButtonIcon,
    changeButtonClass,
    toggleButtonVisibility,
    changeButtonVisibility,
} from '../store'
import { RESET_STATE } from '../../widgets/constants'

describe('Проверка toolbar reducer', () => {
    it('Проверка REGISTER_BUTTON', () => {
        expect(
            toolbar(
                {
                    buttonKey: {},
                },
                {
                    type: registerButton.type,
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
                    type: changeButtonVisibility.type,
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
                    type: changeButtonTitle,
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
                    type: changeButtonCount,
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
                    type: changeButtonSize,
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
                    type: changeButtonColor,
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
                    type: changeButtonDisabled,
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
                    type: toggleButtonDisabled,
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
                    type: toggleButtonVisibility,
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
                    type: changeButtonHint,
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
                    type: changeButtonStyle,
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
                    type: changeButtonIcon,
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
                    type: changeButtonClass,
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
                        buttonId: 'buttonId',
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
