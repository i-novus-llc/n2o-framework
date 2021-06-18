import React from 'react'
import { mount } from 'enzyme'

import { stringConverter, parseToFloat, parseToInt } from './utils'

const NullComponent = () => null

const setup = (data, props = {}) => {
    const Wrapper = stringConverter(data)(NullComponent)
    return mount(<Wrapper {...props} />)
}

describe('slider utils', () => {
    describe('parseToFloat', () => {
        it('string format', () => {
            expect(parseToFloat('4.0')).toBe(4.0)
            expect(parseToFloat('4.999999999')).toBe(4.999999999)
        })
        it('float format', () => {
            expect(parseToFloat(4.9)).toBe(4.9)
            expect(parseToFloat(4.999999999)).toBe(4.999999999)
        })
        it('negative', () => {
            expect(parseToFloat('sdvsdv')).toBe(undefined)
        })
    })
    describe('parseToInt', () => {
        it('string format', () => {
            expect(parseToInt('4')).toBe(4)
            expect(parseToInt('4.5')).toBe(4)
        })
        it('number format', () => {
            expect(parseToInt(4)).toBe(4)
        })
        it('negative', () => {
            expect(parseToInt('sdvsdv')).toBe(undefined)
        })
    })
    describe('stringConverter', () => {
        it('Проброс параметров', () => {
            const wrapper = setup([], { test: 'test' })
            expect(wrapper.find(NullComponent).props()).toEqual({ test: 'test' })
        })
        it('Конвертация параметров string', () => {
            const wrapper = setup(['test'], { test: '5.6', stringMode: true })
            expect(wrapper.find(NullComponent).props()).toEqual({ test: 5.6 })
        })
        it('Конвертация параметров array', () => {
            const wrapper = setup(['test'], {
                test: ['56', '5.6'],
                stringMode: true,
            })
            expect(wrapper.find(NullComponent).props()).toEqual({ test: [56, 5.6] })
        })
        it('Конвертация параметров number', () => {
            const wrapper = setup(['test'], { test: 5, stringMode: true })
            expect(wrapper.find(NullComponent).props()).toEqual({ test: 5 })
        })
    })
})
