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
        const store = createStore(reducers(history), {
            form: {
                testForm: {
                    registeredFields: {
                        test: {}
                    }
                }
            }
        })

        setup(store)

        expect(store.getState().form.testForm.registeredFields.test).toMatchObject({
            dependency: [
                {
                    type: 'reRender',
                    on: ['name'],
                },
            ],
        })
    })
})
