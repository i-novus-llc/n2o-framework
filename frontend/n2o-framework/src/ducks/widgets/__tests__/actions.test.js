import {
    registerWidget,
    resolveWidget,
    removeWidget,
    showWidget,
    hideWidget,
    enableWidget,
    disableWidget,
    alertAddWidget,
    alertRemoveWidget,
    showWidgetFilters,
    hideWidgetFilters,
    changeFiltersVisibility,
    toggleWidgetFilters,
    resetWidgetState,
    setWidgetMetadata,
    setActive,
} from '../store'

const widgetId = ' widgetId'
const initProps = {
    disabled: false,
    visible: true,
}
const err = {
    text: 'Not found',
    status: 404,
}
const resolveModel = {
    data: {
        a: 1,
        b: 2,
    },
}
const alertKey = 'alertKey'
const pageId = 'Page_id'
const metadata = {
    toolbar: {},
}

describe('Тесты экшенов widgets', () => {
    describe('Проверка экшена registerWidget ', () => {
        it('Возвращает правильный payload', () => {
            const action = registerWidget(widgetId, initProps)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.initProps).toEqual(initProps)
        })
    })

    describe('Проверка экшена resolveWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = resolveWidget(widgetId, resolveModel)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.model).toEqual(resolveModel)
        })
    })

    describe('Проверка экшена removeWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = removeWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена showWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = showWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена hideWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = hideWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена enableWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = enableWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена disableWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = disableWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена alertAddWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = alertAddWidget(widgetId, alertKey)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.alertKey).toEqual(alertKey)
        })
    })

    describe('Проверка экшена alertRemoveWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = alertRemoveWidget(widgetId, alertKey)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.alertKey).toEqual(alertKey)
        })
    })

    describe('Проверка экшена showWidgetFilters', () => {
        it('Возвращает правильный payload', () => {
            const action = showWidgetFilters(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.isFilterVisible).toEqual(true)
        })
    })

    describe('Проверка экшена hideWidgetFilters', () => {
        it('Возвращает правильный payload', () => {
            const action = hideWidgetFilters(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.isFilterVisible).toEqual(false)
        })
    })

    describe('Проверка экшена changeFilterVisibility', () => {
        it('Возвращает правильный payload', () => {
            const action = changeFiltersVisibility(widgetId, false)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.isFilterVisible).toEqual(false)
        })
    })

    describe('Проверка экшена toggleWidgetFilters', () => {
        it('Возвращает правильный payload', () => {
            const action = toggleWidgetFilters(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена resetWidgetState', () => {
        it('Возвращает правильный payload', () => {
            const action = resetWidgetState(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена setWidgetMetadata', () => {
        it('Возвращает правильный payload', () => {
            const action = setWidgetMetadata(pageId, widgetId, metadata)
            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.metadata).toEqual(metadata)
        })
    })

    describe('Проверка экшена setActive', () => {
        it('Возвращает правильный payload', () => {
            const action = setActive(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })
})
