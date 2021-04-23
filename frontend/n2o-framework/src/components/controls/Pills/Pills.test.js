import React from 'react'
import sinon from 'sinon'
import { mount } from 'enzyme'

import Pills from './Pills'

describe('<Pills />', () => {
    it('создание', () => {
        expect(mount(<Pills />)).toBeTruthy()
    })

    it('проброс свойств', () => {
        const wrapper = mount(
            <Pills
                data={[
                    { id: 1, label: 'test1' },
                    { id: 1, label: 'test2', active: true },
                ]}
            />,
        )

        expect(wrapper.find('NavLink').length).toBe(2)
        expect(
            wrapper
                .find('NavLink')
                .at(1)
                .props().active,
        ).toBe(true)
        expect(
            wrapper
                .find('NavLink')
                .at(0)
                .text(),
        ).toBe('test1')
    })

    it('onClick', () => {
        const onClick = sinon.spy()
        const wrapper = mount(
            <Pills data={[{ id: 22, label: 'test1' }]} onClick={onClick} />,
        )

        wrapper.find('NavLink').simulate('click')
        expect(onClick.calledOnce).toBe(true)
        expect(onClick.getCalls()[0].lastArg).toBe(22)
    })
})
