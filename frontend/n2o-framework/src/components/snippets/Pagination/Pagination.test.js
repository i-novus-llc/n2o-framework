import React from 'react'

import Pagination from './Pagination'

const WrapperComp = props => (
    <Pagination {...props} />
)

const setup = (propOverrides, method) => {
    const props = {
    // use this to assign some default props

        ...propOverrides,
    }

    let wrapper

    if (method === 'mount') {
        wrapper = mount(<WrapperComp {...props} />)
    } else {
        wrapper = shallow(<WrapperComp {...props} />)
    }

    return {
        props,
        wrapper,
    }
}

const classicN2oProps = {
    layout: 'separated',
    prev: true,
    prevIcon: 'fa fa-angle-left',
    prevLabel: 'Prev',
    next: true,
    nextIcon: 'fa fa-angle-right',
    nextLabel: 'Next',
    first: true,
    firstIcon: 'fa fa-angle-double-left',
    firstLabel: 'First',
    last: true,
    lastIcon: 'fa fa-angle-double-right',
    lastLabel: 'Last',
    activePage: 1,
    count: 151,
    maxPages: 5,
}

describe('<Pagination />', () => {
    it('проверяет установку props', () => {
        const { wrapper } = setup({ ...classicN2oProps },
            'mount',
        )

        expect(wrapper.find('.separated').exists()).toBeTruthy()
        expect(wrapper.find('.fa-angle-double-left').exists()).toBeTruthy()
        expect(wrapper.find('.fa-angle-double-right').exists()).toBeTruthy()
        expect(wrapper.find('.fa-angle-left').exists()).toBeTruthy()
        expect(wrapper.find('.fa-angle-right').exists()).toBeTruthy()

        expect(wrapper.find('span').at(1).text()).toEqual('First')
        expect(wrapper.find('span').at(3).text()).toEqual('Prev')
        expect(wrapper.find('span').at(4).text()).toEqual('Next')
        expect(wrapper.find('span').at(6).text()).toEqual('Last')
    })

    it('проверяет работу вызова callback при смене страницы', () => {
        const value = 3
        const onSelect = jest.fn()
        const { wrapper } = setup(
            { ...classicN2oProps, onSelect },
            'mount',
        )

        wrapper
            .find('.page-item')
            .at(value - 1)
            .simulate('click')

        expect(onSelect).toHaveBeenCalledWith(1, expect.anything())
    })

    it('проверяет установку класса active для номера страницы', () => {
        const { wrapper } = setup({ ...classicN2oProps }, 'mount')
        expect(wrapper.find('.page-item.active > .page-link').text()).toEqual('1')
        wrapper.setProps({ activePage: 3 })
        wrapper.update()
        expect(wrapper.find('.page-item.active > .page-link').text()).toEqual('3')
    })
})
