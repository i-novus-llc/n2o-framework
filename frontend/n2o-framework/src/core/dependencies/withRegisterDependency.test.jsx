import React from 'react'
import { createStore } from 'redux'
import { Provider } from 'react-redux'

import reducers from '../../reducers'
import history from '../../history'

import withRegisterDependency from './withRegisterDependency'

const setup = (store, props = {}) => {
    const Component = withRegisterDependency(() => <div>test</div>)

    return mount(
        <Provider store={store}>
            <Component
                id="test"
                form="testForm"
                dependency={[
                    {
                        type: 'reRender',
                        on: ['name'],
                    },
                ]}
                {...props}
            />
        </Provider>,
    )
}

describe('Проверка хока withRegisterDependency', () => {
    it('регистрирует fields', () => {
        const store = createStore(reducers(history), {})

        expect(store.getState().form).toEqual({})
        const wrapper = setup(store)

        expect(store.getState().form.testForm.registeredFields.test).toEqual({
            isInit: true,
            visible: true,
            disabled: false,
            message: null,
            filter: [],
            dependency: [
                {
                    type: 'reRender',
                    on: ['name'],
                },
            ],
            loading: false,
            required: false,
        })
    })
})
