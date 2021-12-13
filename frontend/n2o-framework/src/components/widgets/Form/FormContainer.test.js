import React from 'react'
import { BrowserRouter as Router, Switch, Link, Route } from 'react-router-dom'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'
import { createStore } from 'redux'

import FormWithPrompt from './tests/FormWithPrompt.json'
import Factory from '../../../core/factory/Factory'
import { WIDGETS } from '../../../core/factory/factoryLevels'
import FactoryProvider from '../../../core/factory/FactoryProvider'
import createFactoryConfig from '../../../core/factory/createFactoryConfig'
import rootReducer from '../../../reducers'
import history from '../../../history'

import * as hocs from './FormContainer'

const NullComponent = () => null

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

const setupPromptForm = store => mount(
    <Provider store={store}>
        <FactoryProvider config={createFactoryConfig({})}>
            <Router>
                <div>
                    <div className="row">
                        <div className="col-6">
                            <h5>Меню</h5>
                            <div className="nav flex-column">
                                <Link id="form-link" className="nav-link" to="/">
                    Форма
                                </Link>
                                <Link className="nav-link" to="/another">
                    Другая страница
                                </Link>
                            </div>
                        </div>
                        <div className="col-6">
                            {renderForm(FormWithPrompt)}
                            <Switch>
                                <Route path="/another" component={() => <div>test</div>} />
                            </Switch>
                        </div>
                    </div>
                </div>
            </Router>
        </FactoryProvider>
    </Provider>,
)

const renderForm = json => (
    <Factory level={WIDGETS} {...json.Page_Form} id="Page_Form" />
)

describe('FormContainer', () => {
    // FIXME проверить что не так и расскоментировать

    // describe('Проверка прокидвания пропсов withWidgetContainer', () => {
    //     it('Проверка создания', () => {
    //         const wrapper = setupToProvider({}, 'withWidgetContainer')
    //         expect(wrapper.find(NullComponent).exists()).toBeTruthy()
    //     })

    //     it('Проверка прокидывания props', () => {
    //         const testPropsData = {
    //             widgetId: 'widgetId',
    //             modelId: 'widgetId',
    //             pageId: 'pageId',
    //             autoFocus: true,
    //             fieldsets: [{ id: 1, fieldset: 'any' }],
    //             modelPrefix: 'modelPrefix',
    //             validation: true,
    //         }

    //         const stateData = {
    //             widgets: {
    //                 widgetId: {
    //                     isEnabled: true,
    //                 },
    //             },
    //             models: {
    //                 datasource: {
    //                     widgetId: [{ id: 'datasource' }],
    //                 },
    //                 modelPrefix: {
    //                     widgetId: {
    //                         any: 'any',
    //                     },
    //                 },
    //             },
    //         }

    //         const wrapper = setupToProvider(
    //             testPropsData,
    //             'withWidgetContainer',
    //             stateData,
    //         )

    //         expect(wrapper.find(NullComponent).props()).toEqual({
    //             ...testPropsData,
    //             ...stateData.widgets.widgetId,
    //             datasource: stateData.models.datasource.widgetId[0],
    //             activeModel: stateData.models.modelPrefix.widgetId,
    //             resolveModel: {},
    //             placeholder: false,
    //             onSetModel: expect.any(Function),
    //             setActive: expect.any(Function),
    //         })
    //     })
    // })

    it('Проверка prompt', () => {
        function configureStore() {
            return createStore(rootReducer(history))
        }
        const store = configureStore()
        const form = setupPromptForm(store)
        form
            .find('Link')
            .at(0)
            .simulate('click')
        expect(form.find('Prompt').props().when).toBe(false)
        form.find('input').simulate('change', {
            target: {
                value: 'test',
            },
        })
        form
            .find('Link')
            .at(1)
            .simulate('click')
        expect(form.find('Prompt').props().when).toBe(true)
    })
})
