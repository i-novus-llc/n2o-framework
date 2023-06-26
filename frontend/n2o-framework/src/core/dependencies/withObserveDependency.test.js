import React from 'react'
import sinon from 'sinon'
import isEmpty from 'lodash/isEmpty'
import { Provider } from 'react-redux'
import { createStore } from 'redux'

import reducers from '../../reducers'
import history from '../../history'
import { registerFieldDependency } from '../../ducks/form/store'
import { setModel } from '../../ducks/models/store'

import withObserveDependency from './withObserveDependency'
import { ModelPrefix } from '../datasource/const'

const setup = (store, props = {}, onChange) => {
    const Component = withObserveDependency({
        onChange,
    })(() => <div>test</div>)
    return mount(
        <Provider store={store}>
            <Component {...props} />
        </Provider>,
    )
}

const initialState = {
    form: {
        testForm: {
            fields: {
                testField: {}
            }
        }
    }
}

describe('Проверка хока withObserveDependency', () => {
    it('подписывается на события', () => {
        const store = createStore(reducers(history), initialState)
        store.dispatch(
            registerFieldDependency('testForm', 'testField', [
                {
                    type: 'reRender',
                    on: ['anotherTestField'],
                },
            ]),
        )
        const wrapper = setup(
            store,
            {
                id: 'testField',
                form: 'testForm',
                modelPrefix: ModelPrefix.active,
                dependency: [
                    {
                        type: 'reRender',
                        on: ['testField'],
                    },
                ],
            },
            () => {},
        )
        expect(
            isEmpty(wrapper.find('ReRenderComponent').instance().observers),
        ).toEqual(false)
        expect(
            typeof wrapper.find('ReRenderComponent').instance().observers[0],
        ).toEqual('function')
    })

    it('срабатывает onChange reRender', () => {
        const onChange = sinon.spy()
        const store = createStore(reducers(history), initialState)
        store.dispatch(
            registerFieldDependency('testForm', 'testField', [
                {
                    type: 'reRender',
                    on: ['anotherTestField'],
                },
            ]),
        )
        store.dispatch(
            setModel('resolve', 'widgetName', { anotherValue: 'value' }),
        )
        const wrapper = setup(
            store,
            {
                id: 'testField',
                form: 'testForm',
                modelPrefix: ModelPrefix.active,
                dependency: [
                    {
                        type: 'reRender',
                        on: ['testField'],
                    },
                ],
            },
            onChange,
        )

        wrapper
            .find('ReRenderComponent')
            .instance()
            .reRenderDependencyAction()
        expect(onChange.called).toEqual(true)
    })

    it('срабатывает onChange fetch', () => {
        const onChange = sinon.spy()
        const store = createStore(reducers(history), initialState)
        store.dispatch(
            registerFieldDependency('testForm', 'testField', [
                {
                    type: 'reRender',
                    on: ['anotherTestField'],
                },
            ]),
        )
        store.dispatch(
            setModel('resolve', 'widgetName', { anotherValue: 'value' }),
        )
        const wrapper = setup(
            store,
            {
                id: 'testField',
                form: 'testForm',
                modelPrefix: ModelPrefix.active,
                dependency: [
                    {
                        type: 'fetch',
                        on: ['testField'],
                    },
                ],
            },
            onChange,
        )

        wrapper
            .find('ReRenderComponent')
            .instance()
            .fetchDependencyAction()
        expect(onChange.called).toEqual(true)
    })
})
