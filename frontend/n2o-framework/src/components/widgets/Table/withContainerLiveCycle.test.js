import React from 'react'
import sinon from 'sinon'

import {withContainerLiveCycle} from './withContainerLiveCycle'

const NullComponent = () => null

/*
  Setup functions
 */
function setup(props) {
    const TestComponent = withContainerLiveCycle(NullComponent)
    return mount(<TestComponent {...props} />)
}

describe('TableContainer', () => {
    describe('Проверка withContainerLiveCycle', () => {
        it('Создание компонента', () => {
            const wrapper = setup({})
            expect(wrapper.find(NullComponent).exists()).toBeTruthy()
        })
        it('Вызов onResolve если есть hasSelected, поменялись данные и нет выбранной записи', () => {
            const onResolve = sinon.spy()
            const wrapper = setup({
                hasSelect: true,
                datasource: ['datasource'],
                onResolve,
            })

            wrapper.setProps({ datasource: ['newdatasouce'] }).update()
            expect(onResolve.calledOnce).toBe(true)
            expect(onResolve.calledWith('newdatasouce')).toBe(true)
        })
    })
})
