import {
    registerWidget,
    dataRequestWidget,
    dataSuccessWidget,
    dataFailWidget,
    resolveWidget,
    removeWidget,
    showWidget,
    hideWidget,
    enableWidget,
    disableWidget,
    loadingWidget,
    unloadingWidget,
    alertAddWidget,
    alertRemoveWidget,
    sortByWidget,
    changePageWidget,
    changeCountWidget,
    changeSizeWidget,
    showWidgetFilters,
    hideWidgetFilters,
    changeFiltersVisibility,
    toggleWidgetFilters,
    resetWidgetState,
    setWidgetMetadata,
    setTableSelectedId,
    setActive,
    disableWidgetOnFetch,
} from '../store'

const widgetId = ' widgetId'
const initProps = {
    disabled: false,
    visible: true,
}
const json = JSON.stringify({
    data: {
        a: 1,
        b: 2,
    },
})
const err = {
    text: 'Not found',
    status: 404,
}
const errResponse = JSON.stringify(err)
const resolveModel = {
    data: {
        a: 1,
        b: 2,
    },
}
const alertKey = 'alertKey'
const fieldKey = 'name'
const sortDirection = 'ASC'
const page = 'Page_Table'
const count = 5
const size = 'lg'
const pageId = 'Page_id'
const selectedId = 'selectedId'
const metadata = {
    toolbar: {},
    actions: {},
}

describe('Тесты экшенов widgets', () => {
    describe('Проверка экшена registerWidget ', () => {
        it('Возвращает правильный payload', () => {
            const action = registerWidget(widgetId, initProps)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.initProps).toEqual(initProps)
        })
    })

    describe('Проверка экшена dataRequestWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = dataRequestWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена dataSuccessWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = dataSuccessWidget(widgetId, json)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.query).toEqual(json)
        })
    })

    describe('Проверка экшена dataFailWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = dataFailWidget(widgetId, err, errResponse)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.err).toEqual(err)
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

    describe('Проверка экшена loadingWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = loadingWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена unloadingWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = unloadingWidget(widgetId)
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

    describe('Проверка экшена sortByWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = sortByWidget(widgetId, fieldKey, sortDirection)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.fieldKey).toEqual(fieldKey)
            expect(action.payload.sortDirection).toEqual(sortDirection)
        })
    })

    describe('Проверка экшена changePageWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = changePageWidget(widgetId, page)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.page).toEqual(page)
        })
    })

    describe('Проверка экшена changeCountWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = changeCountWidget(widgetId, count)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.count).toEqual(count)
        })
    })

    describe('Проверка экшена changeSizeWidget', () => {
        it('Возвращает правильный payload', () => {
            const action = changeSizeWidget(widgetId, size)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.size).toEqual(size)
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

    describe('Проверка экшена setTableSelectedId', () => {
        it('Возвращает правильный payload', () => {
            const action = setTableSelectedId(widgetId, selectedId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.value).toEqual(selectedId)
        })
    })

    describe('Проверка экшена setActive', () => {
        it('Возвращает правильный payload', () => {
            const action = setActive(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка disabledWidgetOnFetch', () => {
        it('Генирирует правильный payload', () => {
            const action = disableWidgetOnFetch(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })
})
