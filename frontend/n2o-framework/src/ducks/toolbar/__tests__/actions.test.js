import {
    callActionImpl,
    toggleButtonVisibility,
    changeButtonDisabled,
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
    changeButtonVisibility,
} from '../store'

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
        it('Возвращает правильный payload', () => {
            const action = changeButtonVisibility(key, id, true)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.visible).toEqual(true)
        })
    })

    describe('Проверка экшена toggleButtonVisibility', () => {
        it('Возвращает правильный payload', () => {
            const action = toggleButtonVisibility(key, id)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
        })
    })

    describe('Проверка экшена changeButtonDisabled', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonDisabled(key, id, false)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.disabled).toEqual(false)
        })
    })

    describe('Проверка экшена toggleButtonDisabled', () => {
        it('Возвращает правильный payload', () => {
            const action = toggleButtonDisabled(key, id)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
        })
    })

    describe('Проверка экшена changeButtonTitle', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonTitle(key, id, title)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.title).toEqual(title)
        })
    })

    describe('Проверка экшена changeButtonSize', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonSize(key, id, size)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.size).toEqual(size)
        })
    })

    describe('Проверка экшена changeButtonColor', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonColor(key, id, color)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.color).toEqual(color)
        })
    })

    describe('Проверка экшена changeButtonCount', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonCount(key, id, count)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.count).toEqual(count)
        })
    })

    describe('Проверка экшена changeButtonHint', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonHint(key, id, hint)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.hint).toEqual(hint)
        })
    })

    describe('Проверка экшена changeButtonIcon', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonIcon(key, id, icon)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.icon).toEqual(icon)
        })
    })

    describe('Проверка экшена changeButtonStyle', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonStyle(key, id, style)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.style).toEqual(style)
        })
    })

    describe('Проверка экшена changeButtonClass', () => {
        it('Возвращает правильный payload', () => {
            const action = changeButtonClass(key, id, btnClass)
            expect(action.payload.key).toEqual(key)
            expect(action.payload.buttonId).toEqual(id)
            expect(action.payload.className).toEqual(btnClass)
        })
    })

    describe('Проверка экшена', () => {
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
