import React from 'react'
import sinon from 'sinon'

import SimpleBtn from './Simple'
import mappingProps from './mappingProps'

const setup = (props) => {
    const wrapper = mount(<SimpleBtn {...props} />)
    return {
        wrapper,
        btn: wrapper.find('Button'),
        icon: wrapper.find('Icon'),
        badge: wrapper.find('Badge'),
    }
}

describe('<Simple />', () => {
    it('label', () => {
        const { btn } = setup({ label: 'test' })
        expect(btn.length).toBe(1)
        expect(btn.childAt(0).text()).toBe('test')
    })
    it('icon', () => {
        const { icon } = setup({ icon: 'fa fa-test' })
        expect(icon.length).toBe(1)
        expect(icon.props().name).toBe('fa fa-test')
    })
    it('size', () => {
        const { btn } = setup({ size: 'sm' })
        expect(btn.props().size).toBe('sm')
    })
    it('color', () => {
        const { btn } = setup({ color: 'primary' })
        expect(btn.props().color).toBe('primary')
    })
    it('outline', () => {
        const { btn, wrapper } = setup({ outline: true, color: 'secondary' })
        expect(btn.props().outline).toBe(true)
        expect(wrapper.find('.btn-outline-secondary').exists()).toBeTruthy()
    })
    it('visible', () => {
        const { btn, wrapper } = setup({ visible: false })
        expect(btn.exists()).toBe(false)
        wrapper.setProps({ visible: true })
        wrapper.update()
        expect(wrapper.find('Button').exists()).toBe(true)
    })
    it('disabled', () => {
        const { btn, wrapper } = setup({ disabled: true })
        expect(btn.props().disabled).toBe(true)
        expect(wrapper.find('button').props().disabled).toBe(true)
    })
    it('count', () => {
        const { badge } = setup({ count: 6 })
        expect(badge.exists()).toBeTruthy()
        expect(badge.childAt(0).text()).toBe('6')
    })
    it('children', () => {
        const { wrapper } = setup({ children: <div className="test" /> })
        expect(wrapper.find('.test').exists()).toBeTruthy()
    })
    it('tag', () => {
        const { wrapper } = setup({ tag: 'a', className: 'link' })
        expect(wrapper.find('a.link').exists()).toBeTruthy()
    })
    it('onClick', () => {
        const onClick = sinon.spy()
        const { btn } = setup({ onClick })
        btn.simulate('click')
        expect(onClick.calledOnce).toBe(true)
    })
})

describe('mappingProps', () => {
    it('со всеми параметрами', () => {
        const testData = {
            id: 'id',
            tag: 'tag',
            label: 'label',
            icon: 'icon',
            size: 'size',
            color: 'color',
            outline: 'outline',
            visible: 'visible',
            disabled: 'disabled',
            count: 'count',
            onClick: () => null,
        }
        expect(mappingProps(testData)).toEqual(testData)
    })
})
