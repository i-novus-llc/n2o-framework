import {
    registerColumn,
    changeColumnVisibility,
    changeColumnDisabled,
    toggleColumnVisibility,
    setColumnVisible,
    setColumnHidden
}  from '../store'

const widgetId = 'widgetId'
const columnId = 'columnId'
const label = 'label'
const visible = 'label'
const disabled = 'label'

describe('Тесты для экшенов columns', () => {
    describe('Проверка экшена changeColumnVisibility', () => {
        it('Проверяет правильность payload', () => {
            const action = changeColumnVisibility(widgetId, columnId, true)

            expect(action.payload.key).toEqual(widgetId)
            expect(action.payload.columnId).toEqual(columnId)
            expect(action.payload.visible).toEqual(true)
        })
    })

    describe('Проверка экшена setColumnsVisible', () => {
        it('Проверяет правильность visible', () => {
            const action = setColumnVisible(widgetId, columnId)

            expect(action.payload.visible).toEqual(true)
        })
    })

    describe('Проверка экшена setColumnHidden', () => {
        it('Проверяет правильность visible', () => {
            const action = setColumnHidden(widgetId, columnId)

            expect(action.payload.visible).toEqual(false)
        })
    })

    describe('Проверка экшена toggleColumnVisibility', () => {
        it('Проверяет правильность payload', () => {
            const action = toggleColumnVisibility(widgetId, columnId)

            expect(action.payload.key).toEqual(widgetId)
            expect(action.payload.columnId).toEqual(columnId)
        })
    })

    describe('Проверка экшена changeColumnDisabled', () => {
        it('Проверяет правильность payload', () => {
            const action = changeColumnDisabled(widgetId, columnId, true)

            expect(action.payload.key).toEqual(widgetId)
            expect(action.payload.columnId).toEqual(columnId)
            expect(action.payload.disabled).toEqual(true)
        })
    })

    describe('Проверка экшена registerColumn', () => {
        it('Проверяет правильность payload', () => {
            const action = registerColumn(
                widgetId,
                columnId,
                label,
                visible,
                disabled,
            )

            expect(action.payload.label).toEqual(label)
            expect(action.payload.visible).toEqual(visible)
            expect(action.payload.disabled).toEqual(disabled)
            expect(action.payload.columnId).toEqual(columnId)
            expect(action.payload.key).toEqual(widgetId)
        })
    })
})
