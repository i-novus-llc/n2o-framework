import {
    widgetsSelector,
    makeWidgetByIdSelector,
    makeWidgetIsInitSelector,
    makeWidgetVisibleSelector,
    makeWidgetDisabledSelector,
    makeWidgetSizeSelector,
    makeWidgetCountSelector,
    makeWidgetSortingSelector,
    makeWidgetFilterVisibilitySelector,
    makeIsActiveSelector,
    makeWidgetPageSelector,
    makeWidgetPageIdSelector,
} from '../selectors'

const state = {
    datasource: {
        ds1: {
            loading: true,
            paging: {
                size: 10,
                count: 100,
                page: 1,
            },
            sorting: {
                name: 'DESC',
            },
            validation: {
                some: 'value',
            },
            provider: {
                url: 'test',
                pathMapping: {},
                queryMapping: {},
            },
        }
    },
    widgets: {
        widget1: {
            datasource: 'ds1',
            isInit: true,
            visible: true,
            disabled: false,
            pageId: '_',
            isFilterVisible: true,
            isActive: true,
            type: 'table',
        },
        widget2: {},
    },
}

describe('Проверка селекторов widgets', () => {
    it('widgetsSelector должен вернуть widgets', () => {
        expect(widgetsSelector(state)).toEqual(state.widgets)
    })
    it('widgetsSelector должен вернуть widget по id', () => {
        expect(makeWidgetByIdSelector('widget1')(state)).toEqual(
            state.widgets.widget1,
        )
    })
    it('makeWidgetIsInitSelector должен вернуть isInit по id', () => {
        expect(makeWidgetIsInitSelector('widget1')(state)).toEqual(
            state.widgets.widget1.isInit,
        )
    })
    it('makeWidgetVisibleSelector должен вернуть visible по id', () => {
        expect(makeWidgetVisibleSelector('widget1')(state)).toEqual(
            state.widgets.widget1.visible,
        )
    })
    it('makeWidgetDisabledSelector должен вернуть enabled по id', () => {
        expect(makeWidgetDisabledSelector('widget1')(state)).toEqual(
            state.widgets.widget1.disabled,
        )
    })
    it('makeWidgetSizeSelector должен вернуть size по id', () => {
        expect(makeWidgetSizeSelector('widget1')(state)).toEqual(
            state.datasource.ds1.paging.size,
        )
    })
    it('makeWidgetCountSelector должен вернуть count по id', () => {
        expect(makeWidgetCountSelector('widget1')(state)).toEqual(
            state.datasource.ds1.paging.count,
        )
    })
    it('makeWidgetSortingSelector должен вернуть count по id', () => {
        expect(makeWidgetSortingSelector('widget1')(state)).toEqual(
            state.datasource.ds1.sorting,
        )
    })
    it('makeWidgetFilterVisibilitySelector должен вернуть isFilterVisible по id', () => {
        expect(makeWidgetFilterVisibilitySelector('widget1')(state)).toEqual(
            state.widgets.widget1.isFilterVisible,
        )
    })
    it('makeIsActiveSelector должен вернуть isActive по id', () => {
        expect(makeIsActiveSelector('widget1')(state)).toEqual(
            state.widgets.widget1.isActive,
        )
    })
    it('makeWidgetPageSelector должен вернуть page по id', () => {
        expect(makeWidgetPageSelector('widget1')(state)).toEqual(
            state.datasource.ds1.paging.page,
        )
    })
    it('makeWidgetPageIdSelector должен вернуть pageId по id', () => {
        expect(makeWidgetPageIdSelector('widget1')(state)).toEqual(
            state.widgets.widget1.pageId,
        )
    })
})
