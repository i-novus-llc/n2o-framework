import widgets, {
    registerWidget,
    dataRequestWidget,
    resolveWidget,
    showWidget,
    hideWidget,
    enableWidget,
    disableWidget,
    disableWidgetOnFetch,
    setActive,
    changeFiltersVisibility,
    toggleWidgetFilters,
    resetWidgetState,
    removeWidget,
} from '../store'

describe('Тесты widget reducer', () => {
    it('Проверка REGISTER', () => {
        expect(
            widgets(
                {
                    'Page.Widget': {},
                },
                {
                    type: registerWidget.type,
                    payload: {
                        widgetId: 'Page.Widget',
                        initProps: {
                            datasource: 'Page.Widget',
                            containerId: 'containerId',
                            fetch: "always",
                            count: 1,
                            dataProvider: {
                                url: 'n2o/data',
                            },
                            filter: {
                                key: 'name',
                                type: 'includes',
                            },
                            isActive: true,
                            isEnabled: true,
                            isFilterVisible: false,
                            page: 2,
                            pageId: 'page-id-2',
                            size: 20,
                            sorting: {
                                name: 'ASC',
                            },
                            type: 'table',
                        },
                    },
                },
            ),
        ).toEqual({
            'Page.Widget': {
                datasource: 'Page.Widget',
                containerId: 'containerId',
                fetch: "always",
                count: 1,
                dataProvider: {
                    url: 'n2o/data',
                },
                filter: {
                    key: 'name',
                    type: 'includes',
                },
                isActive: true,
                isEnabled: true,
                isFilterVisible: false,
                isInit: true,
                isLoading: false,
                isResolved: false,
                isVisible: true,
                page: 2,
                pageId: 'page-id-2',
                size: 20,
                sorting: {
                    name: 'ASC',
                },
                type: 'table',
                validation: {},
                error: null,
                visible: true,
            },
        })
    })

    it('Проверка DATA_REQUEST', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: dataRequestWidget.type,
                    payload: {
                        widgetId: 'widget',
                    },
                },
            ),
        ).toEqual({
            widget: {
                isLoading: true,
            },
        })
    })

    it('Проверка RESOLVE', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: resolveWidget.type,
                    payload: {
                        widgetId: 'widget',
                    },
                },
            ),
        ).toEqual({
            widget: {
                isResolved: true,
            },
        })
    })

    it('Проверка SHOW', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: showWidget.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isVisible: true,
            },
        })
    })

    it('Проверка HIDE', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: hideWidget.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isVisible: false,
            },
        })
    })

    it('Проверка ENABLE', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: enableWidget.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isEnabled: true,
            },
        })
    })

    it('Проверка DISABLE', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: disableWidget.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isEnabled: false,
            },
        })
    })

    it('Проверка DISABLE_ON_FETCH', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: disableWidgetOnFetch.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isEnabled: false,
            },
        })
    })

    it('Проверка CHANGE_FILTERS_VISIBILITY', () => {
        expect(
            widgets(
                {
                    widget: {
                        isFilterVisible: false,
                    },
                },
                {
                    type: changeFiltersVisibility.type,
                    payload: {
                        widgetId: 'widget',
                        isFilterVisible: true,
                    },
                },
            ),
        ).toEqual({
            widget: {
                isFilterVisible: true,
            },
        })
    })

    it('Проверка TOGGLE_FILTERS_VISIBILITY', () => {
        expect(
            widgets(
                {
                    widget: {
                        isFilterVisible: true,
                    },
                },
                {
                    type: toggleWidgetFilters.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isFilterVisible: false,
            },
        })
    })

    it('Проверка RESET_STATE', () => {
        expect(
            widgets(
                {
                    widget: {},
                },
                {
                    type: resetWidgetState.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isInit: false,
            },
        })
    })

    it('Проверка SET_ACTIVE', () => {
        expect(
            widgets(
                {
                    widget: {
                        isActive: false,
                    },
                },
                {
                    type: setActive.type,
                    payload: {
                        widgetId: 'widget'
                    },
                },
            ),
        ).toEqual({
            widget: {
                isActive: true,
            },
        })
    })

    it('Проверка REMOVE', () => {
        expect(
            widgets(
                {
                    widget: {
                        isActive: true,
                        containerId: 'id',
                    },
                },
                {
                    type: removeWidget.type,
                    payload: 'widget',
                },
            ),
        ).toEqual({})
    })
})
