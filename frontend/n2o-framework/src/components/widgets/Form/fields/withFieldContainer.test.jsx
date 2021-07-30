import React from 'react'
import sinon from 'sinon'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'
import { reduxForm } from 'redux-form'

import { registerFieldExtra } from '../../../../actions/formPlugin'

import withFieldContainer from './withFieldContainer'

const NullComponent = () => null

function setupToProvider(props, overrideStore) {
    const TestComponent = reduxForm({ form: 'formName' })(
        withFieldContainer(NullComponent),
    )

    const mockStore = configureMockStore()
    const store = mockStore({
        models: { resolve: {} },
        ...overrideStore,
    })

    const wrapper = mount(
        <Provider store={store}>
            <TestComponent {...props} />
        </Provider>,
    )

    return {
        wrapper,
        store,
    }
}

describe('withFieldContainer', () => {
    it('Проверка регистрации свойств компонента', () => {
        const testPropsData = {
            isInit: false,
            visible: true,
            disabled: true,
            dependency: 'test',
            required: true,
            meta: {
                form: 'formName',
            },
            input: {
                name: 'testName',
            },
        }

        const { store } = setupToProvider(testPropsData)

        expect(store.getActions()[0]).toEqual(
            registerFieldExtra('formName', 'testName', {
                visible: true,
                disabled: true,
                dependency: 'test',
                required: true,
                visible_field: true,
                disabled_field: true,
            }),
        )
    })
})
