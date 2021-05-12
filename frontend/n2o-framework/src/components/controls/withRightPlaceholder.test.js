import React from 'react'

import { InputMoney } from './InputMoney/InputMoney'
import InputMask from './InputMask/InputMask'
import InputNumber from './InputNumber/InputNumber'
import InputText from './InputText/InputText'
import TextArea from './TextArea/TextArea'

const setupMoney = propsOverride => mount(<InputMoney {...propsOverride} />)
const setupNumber = propsOverride => mount(<InputNumber {...propsOverride} />)
const setupMask = propsOverride => mount(<InputMask {...propsOverride} />)
const setupText = propsOverride => mount(<InputText {...propsOverride} />)
const setupArea = propsOverride => mount(<TextArea {...propsOverride} />)
describe('Проверка компонента-обертки withRightPlaceholder', () => {
    it('InputMoney', () => {
        const wrapper = setupMoney({ measure: 'test' })
        expect(wrapper.find('.n2o-control-container-placeholder').exists()).toEqual(
            true,
        )
    })
    it('InputMask', () => {
        const wrapper = setupMask({ measure: 'test' })
        expect(wrapper.find('.n2o-control-container-placeholder').exists()).toEqual(
            true,
        )
    })
    it('InputNumber', () => {
        const wrapper = setupNumber({ measure: 'test' })
        expect(wrapper.find('.n2o-control-container-placeholder').exists()).toEqual(
            true,
        )
    })
    it('InputText', () => {
        const wrapper = setupText({ measure: 'test' })
        expect(wrapper.find('.n2o-control-container-placeholder').exists()).toEqual(
            true,
        )
    })
    it('setupArea', () => {
        const wrapper = setupArea({ measure: 'test' })
        expect(wrapper.find('.n2o-control-container-placeholder').exists()).toEqual(
            true,
        )
    })
})
