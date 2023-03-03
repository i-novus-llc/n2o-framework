import React from 'react'

import FilterButtonsField from './FilterButtonsField'
import { WidgetFilterContext } from '../../../WidgetFilters'

const setup = propsOverride => mount(
    <WidgetFilterContext.Provider value={{}}>
        <FilterButtonsField {...propsOverride} />
    </WidgetFilterContext.Provider>
)

describe('Проверка FilterButtonsFiled', () => {
    it('visible = false', () => {
        const wrapper = setup({ visible: false })
        expect(wrapper.children().children().exists()).toEqual(false)
    })
})
