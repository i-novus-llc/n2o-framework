import {
    REGISTER,
    DATA_REQUEST,
    DATA_SUCCESS,
    DATA_FAIL,
    RESOLVE,
    REMOVE,
    SHOW,
    HIDE,
    ENABLE,
    DISABLE,
    LOADING,
    UNLOADING,
    ALERT_ADD,
    ALERT_REMOVE,
    SORT_BY,
    CHANGE_COUNT,
    CHANGE_SIZE,
    CHANGE_PAGE,
    CHANGE_FILTERS_VISIBILITY,
    TOGGLE_FILTERS_VISIBILITY,
    RESET_STATE,
    SET_WIDGET_METADATA,
    SET_TABLE_SELECTED_ID,
    SET_ACTIVE,
    DISABLE_ON_FETCH,
} from '../constants/widgets'

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
} from './widgets'

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
        it('Генирирует правильное событие', () => {
            const action = registerWidget(widgetId, initProps)
            expect(action.type).toEqual(REGISTER)
        })
        it('Возвращает правильный payload', () => {
            const action = registerWidget(widgetId, initProps)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.initProps).toEqual(initProps)
        })
    })

    describe('Проверка экшена dataRequestWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = dataRequestWidget(widgetId)
            expect(action.type).toEqual(DATA_REQUEST)
        })
        it('Возвращает правильный payload', () => {
            const action = dataRequestWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена dataSuccessWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = dataSuccessWidget(widgetId, json)
            expect(action.type).toEqual(DATA_SUCCESS)
        })
        it('Возвращает правильный payload', () => {
            const action = dataSuccessWidget(widgetId, json)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.query).toEqual(json)
        })
    })

    describe('Проверка экшена dataFailWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = dataFailWidget(widgetId, err, errResponse)
            expect(action.type).toEqual(DATA_FAIL)
        })
        it('Возвращает правильный payload', () => {
            const action = dataFailWidget(widgetId, err, errResponse)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.err).toEqual(err)
        })
    })

    describe('Проверка экшена resolveWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = resolveWidget(widgetId, resolveModel)
            expect(action.type).toEqual(RESOLVE)
        })
        it('Возвращает правильный payload', () => {
            const action = resolveWidget(widgetId, resolveModel)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.model).toEqual(resolveModel)
        })
    })

    describe('Проверка экшена removeWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = removeWidget(widgetId)
            expect(action.type).toEqual(REMOVE)
        })
        it('Возвращает правильный payload', () => {
            const action = removeWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена showWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = showWidget(widgetId)
            expect(action.type).toEqual(SHOW)
        })
        it('Возвращает правильный payload', () => {
            const action = showWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена hideWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = hideWidget(widgetId)
            expect(action.type).toEqual(HIDE)
        })
        it('Возвращает правильный payload', () => {
            const action = hideWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена enableWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = enableWidget(widgetId)
            expect(action.type).toEqual(ENABLE)
        })
        it('Возвращает правильный payload', () => {
            const action = enableWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена disableWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = disableWidget(widgetId)
            expect(action.type).toEqual(DISABLE)
        })
        it('Возвращает правильный payload', () => {
            const action = disableWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена loadingWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = loadingWidget(widgetId)
            expect(action.type).toEqual(LOADING)
        })
        it('Возвращает правильный payload', () => {
            const action = loadingWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена unloadingWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = unloadingWidget(widgetId)
            expect(action.type).toEqual(UNLOADING)
        })
        it('Возвращает правильный payload', () => {
            const action = unloadingWidget(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена alertAddWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = alertAddWidget(widgetId, alertKey)
            expect(action.type).toEqual(ALERT_ADD)
        })
        it('Возвращает правильный payload', () => {
            const action = alertAddWidget(widgetId, alertKey)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.alertKey).toEqual(alertKey)
        })
    })

    describe('Проверка экшена alertRemoveWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = alertRemoveWidget(widgetId, alertKey)
            expect(action.type).toEqual(ALERT_REMOVE)
        })
        it('Возвращает правильный payload', () => {
            const action = alertRemoveWidget(widgetId, alertKey)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.alertKey).toEqual(alertKey)
        })
    })

    describe('Проверка экшена sortByWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = sortByWidget(widgetId, fieldKey, sortDirection)
            expect(action.type).toEqual(SORT_BY)
        })
        it('Возвращает правильный payload', () => {
            const action = sortByWidget(widgetId, fieldKey, sortDirection)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.fieldKey).toEqual(fieldKey)
            expect(action.payload.sortDirection).toEqual(sortDirection)
        })
    })

    describe('Проверка экшена changePageWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = changePageWidget(widgetId, page)
            expect(action.type).toEqual(CHANGE_PAGE)
        })
        it('Возвращает правильный payload', () => {
            const action = changePageWidget(widgetId, page)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.page).toEqual(page)
        })
    })

    describe('Проверка экшена changeCountWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = changeCountWidget(widgetId, count)
            expect(action.type).toEqual(CHANGE_COUNT)
        })
        it('Возвращает правильный payload', () => {
            const action = changeCountWidget(widgetId, count)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.count).toEqual(count)
        })
    })

    describe('Проверка экшена changeSizeWidget', () => {
        it('Генирирует правильное событие', () => {
            const action = changeSizeWidget(widgetId, size)
            expect(action.type).toEqual(CHANGE_SIZE)
        })
        it('Возвращает правильный payload', () => {
            const action = changeSizeWidget(widgetId, size)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.size).toEqual(size)
        })
    })

    describe('Проверка экшена showWidgetFilters', () => {
        it('Генирирует правильное событие', () => {
            const action = showWidgetFilters(widgetId)
            expect(action.type).toEqual(CHANGE_FILTERS_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = showWidgetFilters(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.isFilterVisible).toEqual(true)
        })
    })

    describe('Проверка экшена hideWidgetFilters', () => {
        it('Генирирует правильное событие', () => {
            const action = hideWidgetFilters(widgetId)
            expect(action.type).toEqual(CHANGE_FILTERS_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = hideWidgetFilters(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.isFilterVisible).toEqual(false)
        })
    })

    describe('Проверка экшена changeFilterVisibility', () => {
        it('Генирирует правильное событие', () => {
            const action = changeFiltersVisibility(widgetId, false)
            expect(action.type).toEqual(CHANGE_FILTERS_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = changeFiltersVisibility(widgetId, false)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.isFilterVisible).toEqual(false)
        })
    })

    describe('Проверка экшена toggleWidgetFilters', () => {
        it('Генирирует правильное событие', () => {
            const action = toggleWidgetFilters(widgetId)
            expect(action.type).toEqual(TOGGLE_FILTERS_VISIBILITY)
        })
        it('Возвращает правильный payload', () => {
            const action = toggleWidgetFilters(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена resetWidgetState', () => {
        it('Генирирует правильное событие', () => {
            const action = resetWidgetState(widgetId)
            expect(action.type).toEqual(RESET_STATE)
        })
        it('Возвращает правильный payload', () => {
            const action = resetWidgetState(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка экшена setWidgetMetadata', () => {
        it('Генирирует правильное событие', () => {
            const action = setWidgetMetadata(pageId, widgetId, metadata)
            expect(action.type).toEqual(SET_WIDGET_METADATA)
        })
        it('Возвращает правильный payload', () => {
            const action = setWidgetMetadata(pageId, widgetId, metadata)
            expect(action.payload.pageId).toEqual(pageId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.metadata).toEqual(metadata)
        })
    })

    describe('Проверка экшена setTableSelectedId', () => {
        it('Генирирует правильное событие', () => {
            const action = setTableSelectedId(widgetId, selectedId)
            expect(action.type).toEqual(SET_TABLE_SELECTED_ID)
        })
        it('Возвращает правильный payload', () => {
            const action = setTableSelectedId(widgetId, selectedId)
            expect(action.payload.widgetId).toEqual(widgetId)
            expect(action.payload.value).toEqual(selectedId)
        })
    })

    describe('Проверка экшена setActive', () => {
        it('Генирирует правильное событие', () => {
            const action = setActive(widgetId)
            expect(action.type).toEqual(SET_ACTIVE)
        })
        it('Возвращает правильный payload', () => {
            const action = setActive(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })

    describe('Проверка disabledWidgetOnFetch', () => {
        it('Генирирует правильное событие', () => {
            const action = disableWidgetOnFetch(widgetId)
            expect(action.type).toEqual(DISABLE_ON_FETCH)
        })
        it('Генирирует правильный payload', () => {
            const action = disableWidgetOnFetch(widgetId)
            expect(action.payload.widgetId).toEqual(widgetId)
        })
    })
})
