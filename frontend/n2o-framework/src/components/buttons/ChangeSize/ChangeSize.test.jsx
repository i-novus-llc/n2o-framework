import React from 'react'
import { Provider } from 'react-redux'
import sinon from 'sinon'

import configureStore from '../../../store'
import history from '../../../history'

import { ChangeSize } from './ChangeSize'

const store = configureStore(
    {
        datasource: {
            ds1: {
                size: 1,
            }
        },
        widgets: {
            someWidget: {
                datasource: 'ds1',
            },
        },
    },
    history,
    {},
)

const setup = (propsOverride) => {
    const props = {}

    return mount(
        <Provider store={store}>
            <ChangeSize entityKey="someWidget" {...props} {...propsOverride} />
        </Provider>,
    )
}

describe('<ChangeSize/>', () => {
    it('компонент должен отрисоваться', () => {
        const wrapper = setup()

        expect(wrapper.find('DropdownItem').exists()).toBeTruthy()
        expect(wrapper.find('DropdownItem').length).toBe(4)
    })

    // раскоментить после реализации хока withDatadource, чтобы прокинуть внутрь теста setSize метод и нормально проверять
    // it('должен вызвать resize', () => {
    //     const dispatch = sinon.spy()
    //     const wrapper = setup({
    //         dispatch,
    //     })

    //     wrapper
    //         .find('DropdownItem')
    //         .last()
    //         .simulate('click')

    //     expect(store.getState().datasource.ds1.size).toBe(50)
    // })
})
