import React from 'react'
import sinon from 'sinon'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import * as hocs from './TableContainer'

const NullComponent = () => null
const TableContainerTest = hocs.default

/*
  Setup functions
 */
function setup(props, hocName) {
    const TestComponent = hocs[hocName](NullComponent)
    return mount(<TestComponent {...props} />)
}

function setupToProvider(props, hocName, overrideStore) {
    const TestComponent = hocs[hocName](NullComponent)
    const mockStore = configureMockStore()
    const store = mockStore({
        models: { resolve: {} },
        ...overrideStore,
    })
    return mount(
        <Provider store={store}>
            <TestComponent {...props} />
        </Provider>,
    )
}

function setupToProviderToDefault(props, overrideStore = {}) {
    const mockStore = configureMockStore()
    const store = mockStore({
        models: { resolve: {} },
        ...overrideStore,
    })
    return mount(
        <Provider store={store}>
            <TableContainerTest {...props} />
        </Provider>,
    )
}

describe('TableContainer', () => {
    describe('Проверка прокидвания пропсов withWidgetContainer', () => {
        it('Проверка создания элемента', () => {
            const wrapper = setupToProvider({}, 'withWidgetContainer')
            expect(wrapper.find(NullComponent).exists()).toBeTruthy()
        })

        it('Проверка прокидывания props', () => {
            const testPropsData = {
                widgetId: 'widgetId',
                modelId: 'widgetId',
                pageId: 'pageId',
                headers: [{ id: 'headers' }],
                cells: [{ id: 'cells' }],
                isAnyTableFocused: false,
                hasFocus: true,
                hasSelect: true,
                autoFocus: true,
                rowColor: 'red',
                size: 10,
                actions: { anyActions: {} },
                redux: true,
                rowClick: { src: 'dummy' },
            }

            const stateData = {
                widgets: {
                    widgetId: {
                        isActive: true,
                        selectedId: 1,
                        sorting: 'DESC',
                    },
                },
                models: {
                    datasource: {
                        widgetId: [{ id: 'datasource' }],
                    },
                },
            }
            const wrapper = setupToProvider(
                testPropsData,
                'withWidgetContainer',
                stateData,
            )

            expect(wrapper.find(NullComponent).props()).toEqual({
                ...testPropsData,
                ...stateData.widgets.widgetId,
                datasource: stateData.models.datasource.widgetId,
                onFocus: expect.any(Function),
                onResolve: expect.any(Function),
                onSort: expect.any(Function),
                onActionImpl: expect.any(Function),
            })
        })
    })

    describe('Проверка withContainerLiveCycle', () => {
        it('Создание компонента', () => {
            const wrapper = setup({}, 'withContainerLiveCycle')
            expect(wrapper.find(NullComponent).exists()).toBeTruthy()
        })
        it('Вызов onResolve если есть hasSelected, поменялись данные и нет выбранной записи', () => {
            const onResolve = sinon.spy()
            const wrapper = setup(
                {
                    hasSelect: true,
                    datasource: ['datasource'],
                    onResolve,
                },
                'withContainerLiveCycle',
            )

            wrapper.setProps({ datasource: ['newdatasouce'] }).update()
            expect(onResolve.calledOnce).toBe(true)
            expect(onResolve.calledWith('newdatasouce')).toBe(true)
        })
    })

    it('Вызов onResolve если есть hasSelected, поменялись данные и поменялась выбранная запись', () => {
        const onResolve = sinon.spy()
        const wrapper = setup(
            {
                hasSelect: true,
                datasource: ['datasource'],
                onResolve,
                selectedId: 1,
            },
            'withContainerLiveCycle',
        )

        wrapper.setProps({ datasource: ['newdatasouce'], selectedId: 2 }).update()
        expect(onResolve.calledOnce).toBe(true)
        expect(onResolve.calledWith('newdatasouce')).toBe(true)
    })

    it('Вызов onResolve если есть hasSelected, поменялись данные и выбранная запись указывает на разные значения предыдущих и новых данных', () => {
        const onResolve = sinon.spy()
        const wrapper = setup(
            {
                hasSelect: true,
                datasource: [{ id: 1, test: 'datasource' }],
                onResolve,
                selectedId: 1,
            },
            'withContainerLiveCycle',
        )

        wrapper
            .setProps({ datasource: [{ id: 1, test: 'newdatasource' }] })
            .update()
        expect(onResolve.calledOnce).toBe(true)
        expect(onResolve.calledWith({ id: 1, test: 'newdatasource' })).toBe(true)
    })

    it('Проверка compose', () => {
        const wrapper = setupToProviderToDefault()

        expect(
            wrapper
                .find('WidgetContainer')
                .find('lifecycle(withHandlers(withI18nextTranslation(Table)))')
                .exists(),
        ).toBe(true)
    })

    it('Проверка withWidgetHandlers', () => {
        const onActionImpl = sinon.spy()
        const wrapper = setup(
            {
                onActionImpl,
                rowClick: { src: 'dummy' },
            },
            'withWidgetHandlers',
        )
        wrapper
            .find(NullComponent)
            .props()
            .onRowClickAction()
        expect(onActionImpl.calledOnce).toBe(true)
        expect(onActionImpl.calledWith({ src: 'dummy' })).toBe(true)
    })
})
