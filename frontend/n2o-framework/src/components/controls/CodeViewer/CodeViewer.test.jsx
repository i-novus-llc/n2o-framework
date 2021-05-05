import React from 'react'

import { CodeViewer } from './CodeViewer'

const setup = (propOverrides) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    const wrapper = mount(<CodeViewer {...props} />)

    return {
        props,
        wrapper,
    }
}

describe('<CodeViewer />', () => {
    it('виден по visible', () => {
        let { wrapper } = setup({ visible: false })

        expect(wrapper.find('.n2o-code-viewer').exists()).toEqual(false)
        wrapper = setup({ visible: true }).wrapper
        expect(wrapper.find('.n2o-code-viewer').exists()).toEqual(true)
    })

    it('SyntaxHighlighter доступен по show', () => {
        const { wrapper } = setup({ show: true })

        expect(wrapper.find('SyntaxHighlighter').exists()).toEqual(true)
    })
})
