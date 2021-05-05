import {
    CHANGE_BUTTON_COLOR,
    CHANGE_BUTTON_COUNT,
    CHANGE_BUTTON_DISABLED,
    CHANGE_BUTTON_TITLE,
    CHANGE_BUTTON_SIZE,
    CHANGE_BUTTON_VISIBILITY,
    CHANGE_BUTTON_HINT,
    CHANGE_BUTTON_CLASS,
    CHANGE_BUTTON_STYLE,
    REGISTER_BUTTON,
    CALL_ACTION_IMPL,
    TOGGLE_BUTTON_VISIBILITY,
    TOGGLE_BUTTON_DISABLED,
    CHANGE_BUTTON_ICON,
} from '../constants/toolbar'

import {
    callActionImpl,
    changeButtonVisiblity,
    setButtonVisible,
    setButtonHidden,
    toggleButtonVisiblity,
    changeButtonDisabled,
    setButtonDisabled,
    setButtonEnabled,
    toggleButtonDisabled,
    changeButtonTitle,
    changeButtonSize,
    changeButtonColor,
    changeButtonCount,
    changeButtonHint,
    changeButtonIcon,
    changeButtonStyle,
    changeButtonClass,
    registerButton,
} from './toolbar'

const key = 'test-btn'
const id = 'unique-btn'
const title = 'new title'
const size = 'lg'
const color = 'success'
const count = 4
const hint = 'this is a hint'
const icon = 'fa fa-plus'
const style = {
    border: '1px solid red',
}
const btnClass = 'btn-class'

describe('Тесты экшенов toolbar', () => {
    describe('Проверка экшена callActionAmpl', () => {
        it('Генирирует правильное событие', () => {
            const action = callActionImpl('toggleFilter', {
                name: 'test',
            })

            expect(action.type).toEqual(CALL_ACTION_IMPL)
        })
        it('Возвращает правильный payload', () => {
            const action = callActionImpl('toggleFilter', {
                name: 'test',
            })

            expect(action.payload.actionSrc).toEqual('toggleFilter')
            expect(action.payload.options).toEqual({
                name: 'test',
            })
        })
    })

    describe('Проверка экшена changeButtonVisible', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonVisiblity(key, id, true)

            expect(action.type).toEqual(CHANGE_BUTTON_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonVisiblity(key, id, true)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.visible).toEqual(true)
        })
    })

    describe('Проверка экшена setButtonVisible', () => {
        it('Генирирует правильное событие', () => {
            const action = setButtonVisible(key, id)

            expect(action.type).toEqual(CHANGE_BUTTON_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = setButtonVisible(key, id)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.visible).toEqual(true)
        })
    })

    describe('Проверка экшена setButtonHidden', () => {
        it('Генирирует правильное событие', () => {
            const action = setButtonHidden(key, id)

            expect(action.type).toEqual(CHANGE_BUTTON_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = setButtonHidden(key, id)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.visible).toEqual(false)
        })
    })

    describe('Проверка экшена toggleButtonVisibility', () => {
        it('Генирирует правильное событие', () => {
            const action = toggleButtonVisiblity(key, id)

            expect(action.type).toEqual(TOGGLE_BUTTON_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = toggleButtonVisiblity(key, id)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
        })
    })

    describe('Проверка экшена changeButtonDisabled', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonDisabled(key, id, false)

            expect(action.type).toEqual(CHANGE_BUTTON_DISABLED)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonDisabled(key, id, false)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.disabled).toEqual(false)
        })
    })

    describe('Проверка экшена setButtonDisabled', () => {
        it('Генирирует правильное событие', () => {
            const action = setButtonDisabled(key, id)

            expect(action.type).toEqual(CHANGE_BUTTON_DISABLED)
        })
        it('Возвращает правильный payload', () => {
            const action = setButtonDisabled(key, id)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.disabled).toEqual(true)
        })
    })

    describe('Проверка экшена setButtonEnabled', () => {
        it('Генирирует правильное событие', () => {
            const action = setButtonEnabled(key, id)

            expect(action.type).toEqual(CHANGE_BUTTON_DISABLED)
        })
        it('Возвращает правильный payload', () => {
            const action = setButtonEnabled(key, id)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.disabled).toEqual(false)
        })
    })

    describe('Проверка экшена toggleButtonDisabled', () => {
        it('Генирирует правильное событие', () => {
            const action = toggleButtonDisabled(key, id)

            expect(action.type).toEqual(TOGGLE_BUTTON_DISABLED)
        })
        it('Возвращает правильный payload', () => {
            const action = toggleButtonDisabled(key, id)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
        })
    })

    describe('Проверка экшена changeButtonTitle', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonTitle(key, id, title)

            expect(action.type).toEqual(CHANGE_BUTTON_TITLE)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonTitle(key, id, title)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.title).toEqual(title)
        })
    })

    describe('Проверка экшена changeButtonSize', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonSize(key, id, size)

            expect(action.type).toEqual(CHANGE_BUTTON_SIZE)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonSize(key, id, size)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.size).toEqual(size)
        })
    })

    describe('Проверка экшена changeButtonColor', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonColor(key, id, color)

            expect(action.type).toEqual(CHANGE_BUTTON_COLOR)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonColor(key, id, color)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.color).toEqual(color)
        })
    })

    describe('Проверка экшена changeButtonCount', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonCount(key, id, count)

            expect(action.type).toEqual(CHANGE_BUTTON_COUNT)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonCount(key, id, count)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.count).toEqual(count)
        })
    })

    describe('Проверка экшена changeButtonHint', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonHint(key, id, hint)

            expect(action.type).toEqual(CHANGE_BUTTON_HINT)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonHint(key, id, hint)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.hint).toEqual(hint)
        })
    })

    describe('Проверка экшена changeButtonIcon', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonIcon(key, id, icon)

            expect(action.type).toEqual(CHANGE_BUTTON_ICON)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonIcon(key, id, icon)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.icon).toEqual(icon)
        })
    })

    describe('Проверка экшена changeButtonStyle', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonStyle(key, id, style)

            expect(action.type).toEqual(CHANGE_BUTTON_STYLE)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonStyle(key, id, style)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.style).toEqual(style)
        })
    })
    describe('Проверка экшена changeButtonClass', () => {
        it('Генирирует правильное событие', () => {
            const action = changeButtonClass(key, id, btnClass)

            expect(action.type).toEqual(CHANGE_BUTTON_CLASS)
        })
        it('Возвращает правильный payload', () => {
            const action = changeButtonClass(key, id, btnClass)

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.className).toEqual(btnClass)
        })
    })

    describe('Проверка экшена', () => {
        it('Генирирует правильное событие', () => {
            const action = registerButton(key, id, {
                id,
                size,
                count,
                icon,
                color,
                resolveEnabled: true,
                visible: true,
                disabled: false,
                hint,
                className: btnClass,
                style,
                containerKey: 'container',
                conditions: {},
            })

            expect(action.type).toEqual(REGISTER_BUTTON)
        })
        it('Возвращает правильный payload', () => {
            const action = registerButton(key, id, {
                key,
                count,
                visible: true,
                disabled: false,
                conditions: {},
            })

            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.visible).toEqual(true)
            expect(action.payload.disabled).toEqual(false)
            expect(action.payload.conditions).toEqual({})
            expect(action.payload.count).toEqual(4)
        })
    })
})
