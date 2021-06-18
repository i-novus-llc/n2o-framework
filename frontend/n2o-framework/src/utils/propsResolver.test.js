/**
 * Created by emamoshin on 04.10.2017.
 */
import Enzyme, { shallow, render, mount } from 'enzyme'

import propsResolver from './propsResolver'

const props = {
    static: 'label-success',
    localLink: '`name`',
    jsExp: '`name == \'test\'`',
    combine: '`\'label-\'+color`',
    combineExp: '`\'label-\'+(name == \'test\' ? \'success\' : \'info\')`',
    deep: {
        test: '`color`',
    },
    withFunctions: '`_.isString(name)`',
}

const model = {
    name: 'test',
    color: 'warning',
}

let newProps

describe('propsResolver', () => {
    beforeAll(() => {
        newProps = propsResolver(props, model)
    })
    it('проверяет статичный резолв', () => {
        expect(newProps.static).toBe('label-success')
    })
    it('проверяет проверяет ссылку в модели', () => {
        expect(newProps.localLink).toBe('test')
    })
    it('проверяет js выражение', () => {
        expect(newProps.jsExp).toBe(true)
    })
    it('проверяет комбинацию js и статики', () => {
        expect(newProps.combine).toBe('label-warning')
    })
    it('проверяет комбинацию js, статики и выражения', () => {
        expect(newProps.combineExp).toBe('label-success')
    })
    it('проверяет глубокую итерацию', () => {
        expect(newProps.deep.test).toBe('warning')
    })
    it('проверяет работоспособность внешних функций', () => {
        expect(newProps.withFunctions).toBe(true)
    })
})
