import React from 'react'
import renderer from 'react-test-renderer'
import { StaticRouter } from 'react-router'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import BreadcrumbContainer from './BreadcrumbContainer'
import DefaultBreadcrumb from './DefaultBreadcrumb'

const setupContainer = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }
    const initialState = {
        models: {
            test: {
                test: 'test',
            },
        },
    }
    const mockStore = configureMockStore()
    const store = mockStore(initialState)
    const component = (
        <StaticRouter location="test" context={{}}>
            <Provider store={store}>
                <BreadcrumbContainer
                    defaultBreadcrumb={props => <DefaultBreadcrumb {...props} />}
                    {...props}
                />
            </Provider>
        </StaticRouter>
    )

    return {
        props,
        component,
    }
}

describe('<Breadcrumb />', () => {
    it('тестирует снэпшот контейнера брэдкрамба', () => {
        const { component } = setupContainer({
            items: [
                {
                    label: '`test ? test : \'dummy\'`',
                    modelLink: 'models.test',
                    path: '/redirectTest',
                },
                {
                    label: '`test == \'fake\' ? test : \'Test\'`',
                    modelLink: 'models.test',
                },
            ],
        })
        const tree = renderer.create(component).toJSON()

        expect(tree).toMatchSnapshot()
    })
})
