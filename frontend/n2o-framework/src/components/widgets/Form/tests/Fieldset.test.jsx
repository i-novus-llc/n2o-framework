import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import Fieldset from '../Fieldset'
import StandardFieldset from '../fieldsets/BlankFieldset'
import {
    ENABLE_FIELDS,
    DISABLE_FIELDS,
    SHOW_FIELDS,
    HIDE_FIELDS,
} from '../../../../constants/formPlugin'

const mockStore = configureMockStore()

const defaultStateObj = {
    models: {
        resolve: {},
    },
}

const setup = (storeObj, propOverrides = {}) => {
    const props = {
        form: '__form',
        component: StandardFieldset,
        visible: '`id == 2`',
        enabled: '`id == 3`',
        rows: [],
        ...propOverrides,
    }

    const store = mockStore(storeObj || defaultStateObj)

    const wrapper = mount(
        <Provider store={store}>
            <Fieldset {...props} />
        </Provider>,
    )

    return {
        props,
        wrapper,
        store,
    }
}

describe('<FieldSelts />', () => {
    it('проверяет создание элемента', () => {
        const { wrapper } = setup()

        expect(wrapper.find(StandardFieldset).exists()).toBeTruthy()
    })
})
