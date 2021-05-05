import React from 'react'
import sinon from 'sinon'

import Base from './Base'

describe('<Base />', () => {
    it('проверяет рендер по умолчанию', () => {
        const wrapper = mount(<Base tag="div" />)

        expect(wrapper.exists()).toBeTruthy()
    })

    describe('transform', () => {
        it('code', () => {
            const wrapper = mount(<Base tag="div" code />)

            expect(wrapper.find('code').exists()).toBeTruthy()
        })
        it('del', () => {
            const wrapper = mount(<Base tag="div" del />)

            expect(wrapper.find('del').exists()).toBeTruthy()
        })
        it('mark', () => {
            const wrapper = mount(<Base tag="div" mark />)

            expect(wrapper.find('mark').exists()).toBeTruthy()
        })
        it('strong', () => {
            const wrapper = mount(<Base tag="div" strong />)

            expect(wrapper.find('strong').exists()).toBeTruthy()
        })
        it('underline', () => {
            const wrapper = mount(<Base tag="div" underline />)

            expect(wrapper.find('u').exists()).toBeTruthy()
        })
        it('small', () => {
            const wrapper = mount(<Base tag="div" small />)

            expect(wrapper.find('small').exists()).toBeTruthy()
        })
    })

    describe('color', () => {
        it('primary', () => {
            const wrapper = mount(<Base tag="div" color="primary" />)

            expect(wrapper.find('div.text-primary').exists()).toBeTruthy()
        })
        it('secondary', () => {
            const wrapper = mount(<Base tag="div" color="secondary" />)

            expect(wrapper.find('div.text-secondary').exists()).toBeTruthy()
        })
    })

    describe('functional', () => {
        it('editable', () => {
            const event = {
                preventDefault() {},
                currentTarget: {
                    textContent: 'testotest',
                },
            }
            const onChange = sinon.spy()
            const wrapper = mount(
                <Base tag="p" editable onChange={onChange} />,
            )

            expect(wrapper.find('i.fa-pencil').exists()).toBeTruthy()
            wrapper.find('i.fa-pencil').simulate('click')
            expect(wrapper.find('i.fa-pencil').exists()).toBe(false)
            expect(wrapper.find('div.editable').exists()).toBeTruthy()
            wrapper
                .find('div.editable')
                .props()
                .onInput(event)
            expect(onChange.calledOnce).toBe(true)
            expect(onChange.getCall(0).args[0]).toBe('testotest')
        })

        it('copyable', () => {
            const wrapper = mount(<Base tag="p" copyable />)

            expect(wrapper.find('i.fa-files-o').exists()).toBeTruthy()
        })
    })
})
