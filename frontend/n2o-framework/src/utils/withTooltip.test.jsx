import React from 'react'
import UncontrolledTooltip from 'reactstrap/lib/UncontrolledTooltip'

import withTooltip from './withTooltip'

const setup = propsOverride => mount(
    withTooltip(<div className="test">test</div>, 'test hint', 123),
)

describe('Проверка withTooltip', () => {
    it('вернет компонент с тултипом', () => {
    //  const wrapper = setup();
    // expect(wrapper.find(UncontrolledTooltip).exists()).toEqual(true);
    })
})
