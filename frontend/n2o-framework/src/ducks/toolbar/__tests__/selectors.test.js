import {
    toolbarSelector,
    isVisibleSelector,
    sizeSelector,
    colorSelector,
    isDisabledSelector,
    titleSelector,
    countSelector,
    hintSelector,
    iconSelector,
    classSelector,
    styleSelector,
    isInitSelector,
    isLoading,
    errorSelector,
    getContainerButtons,
} from '../selectors'

const state = {
    toolbar: {
        widgetId: {
            create: {
                isInit: true,
                visible: true,
                disabled: true,
                size: 'lg',
                color: 'success',
                count: 1,
                title: 'title',
                hint: 'hint',
                icon: 'fa fa-plus',
                style: {},
                className: 'test 1',
                loading: false,
                error: false,
            },
            delete: {
                isInit: false,
                visible: false,
                disabled: false,
                size: 'sm',
                color: 'danger',
                count: 2,
                title: 'title 2',
                hint: 'hint 2',
                icon: 'fa fa-upload',
                style: {},
                className: 'test 2',
                loading: true,
                error: true,
            },
        },
    },
}

describe('Проверка селекторов toolbar', () => {
    it('toolbarSelector должен вернуть state toolbar', () => {
        expect(toolbarSelector(state)).toEqual(state.toolbar)
    })
    it('isVisibleSelector должен вернуть visible кнопки', () => {
        expect(isVisibleSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.visible,
        )
    })
    it('sizeSelector должен вернуть size кнопки', () => {
        expect(sizeSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.size,
        )
    })
    it('colorSelector должен вернуть color кнопки', () => {
        expect(colorSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.color,
        )
    })
    it('isDisabledSelector должен вернуть disabled кнопки', () => {
        expect(isDisabledSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.disabled,
        )
    })
    it('titleSelector должен вернуть title кнопки', () => {
        expect(titleSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.title,
        )
    })
    it('countSelector должен вернуть count кнопки', () => {
        expect(countSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.count,
        )
    })
    it('hintSelector должен вернуть hint кнопки', () => {
        expect(hintSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.hint,
        )
    })
    it('iconSelector должен вернуть icon кнопки', () => {
        expect(iconSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.icon,
        )
    })
    it('classSelector должен вернуть class кнопки', () => {
        expect(classSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.className,
        )
    })
    it('styleSelector должен вернуть style кнопки', () => {
        expect(styleSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.style,
        )
    })
    it('isInitSelector должен вернуть isInit кнопки', () => {
        expect(isInitSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.isInit,
        )
    })
    it('isLoading должен вернуть loading кнопки', () => {
        expect(isLoading('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.loading,
        )
    })
    it('errorSelector должен вернуть error кнопки', () => {
        expect(errorSelector('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId.create.error,
        )
    })
    it('getContainerButtons должен вернуть кнопки', () => {
        expect(getContainerButtons('widgetId', 'create')(state)).toEqual(
            state.toolbar.widgetId,
        )
    })
})
