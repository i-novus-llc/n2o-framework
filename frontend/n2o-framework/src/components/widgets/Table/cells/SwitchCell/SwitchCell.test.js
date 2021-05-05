import React from 'react'

import SwitchCell from './SwitchCell'
import { model, model2, modelFromDefaultView } from './switchCellStoryProps'

const setupOptions = props => shallow(<SwitchCell {...props} />)

describe('SwitchCell tests <SwitchCell>', () => {
    it('Компонент возвращает верный cell - TextCell', () => {
        const wrapper = setupOptions(model)
        expect(wrapper.props().src).toBe('TextCell')
        expect(wrapper.props().text).toBe('TextCell text')
    })

    it('Компонент возвращает верный cell - LinkCell', () => {
        const wrapper = setupOptions(model2)
        expect(wrapper.props().src).toBe('LinkCell')
    })

    it('Компонент возвращает верный default cell - ImageCell', () => {
        const wrapper = setupOptions(modelFromDefaultView)
        expect(wrapper.props().src).toBe('ImageCell')
    })
})
