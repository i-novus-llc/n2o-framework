import React from 'react'
import { mount } from 'enzyme'
import configureMockStore from 'redux-mock-store'
import { Provider } from 'react-redux'

import ListTextCell from './ListTextCell'

const defaultProps = {
    label: 'объекта',
    fieldKey: 'test',
    model: {
        test: ['первый', 'второй', 'третий'],
    },
}

const setupImageCell = (propsOverride) => {
    const props = { ...defaultProps, ...propsOverride }

    const wrapper = mount(
        <Provider store={configureMockStore()({ models: { datasource: {}, resolve: {}, multi: {}, filter: {}, edit: {} } })}>
            <ListTextCell {...props} />
        </Provider>,
    )

    return { wrapper, props }
}

describe('<ListTextCell />', () => {
    it('Проверяет создание ListTextCell', () => {
        const { wrapper } = setupImageCell()

        expect(wrapper.find('.list-text-cell').exists()).toBeTruthy()
    })
    it('Проверяет верный label, подсчет объектов в tooltip', () => {
        const { wrapper } = setupImageCell({
            fieldKey: 'test',
            fewLabel: '{size} объекта',
            model: { test: ['1', '2', '3'] },
        })

        expect(wrapper.text()).toBe('3 объекта')
    })
    it('Проверяет верный label, replace placeholder', () => {
        const { wrapper } = setupImageCell({ label: 'объектов {size}' })

        expect(wrapper.text()).toBe('объектов 3')
    })
    it('Отрисовываются верные тэги, классы и контент', () => {
        const { wrapper } = setupImageCell({ label: 'объектов {size}' })

        expect(wrapper.html()).toEqual(
            '<div class="default-cell list-text-cell"><span class="list-text-cell__trigger">объектов 3</span></div>',
        )
    })
    it('Отрисовываются labelDashed', () => {
        const { wrapper } = setupImageCell({
            fieldKey: 'test',
            fewLabel: '{size} объекта',
            model: { test: ['1', '2', '3'] },
            labelDashed: true,
        })

        expect(wrapper.html()).toEqual(
            '<div class="default-cell list-text-cell"><span class="list-text-cell__trigger_dashed">3 объекта</span></div>',
        )
    })

    it('Правильно склоняются label', () => {
        const { wrapper: wrapperOneLabel } = setupImageCell({
            label: 'Объектов {size} шт',
            oneLabel: '{size} объект',
            fieldKey: 'test',
            model: { test: ['1'] },
        })

        expect(wrapperOneLabel.text()).toBe('1')

        const { wrapper: wrapperFewLabel } = setupImageCell({
            label: 'Объектов {size} шт',
            fewLabel: '{size} объекта',
            fieldKey: 'test',
            model: { test: ['1', '2'] },
        })

        expect(wrapperFewLabel.text()).toBe('2 объекта')

        const { wrapper: wrapperManyLabel } = setupImageCell({
            label: 'Объектов {size} шт',
            manyLabel: '{size} объектов',
            fieldKey: 'test',
            model: { test: ['1', '2', '3', '4', '5'] },
        })

        expect(wrapperManyLabel.text()).toBe('5 объектов')
    })
})
