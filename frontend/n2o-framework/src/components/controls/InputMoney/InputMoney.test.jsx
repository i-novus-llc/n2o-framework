import React from 'react'

import { InputMoney } from './InputMoney'

const setup = propsOverride => shallow(<InputMoney {...propsOverride} />)

describe('Проверка компонента InputMoney', () => {
    it('компонент отрисовывается', () => {
        const wrapper = setup()

        expect(wrapper.find('.n2o-input-money').exists()).toEqual(true)
    })
    it('правильно переводит из float в формат денег', () => {
        const wrapper = setup({ allowDecimal: true })

        expect(wrapper.instance().convertToMoney('123.33')).toEqual('123,33')
    })
    it('правильно переводит из формата денег во float', () => {
        const wrapper = setup({
            prefix: '# ',
            includeThousandsSeparator: true,
            thousandsSeparatorSymbol: ' ',
            decimalSymbol: ',',
        })

        expect(wrapper.instance().convertToFloat('# 1 234,55')).toEqual('1234.55')
    })
})
