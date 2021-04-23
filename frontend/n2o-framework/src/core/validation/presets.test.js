import {
    email,
    required,
    condition,
    constraint,
    integer,
    minLength,
    maxLength,
    match,
} from './presets'

describe('Проверка presets', () => {
    it('email валиден', () => {
        expect(email('email', { email: 'test@gmail.com' })).toEqual(true)
    })

    it('email не валиден', () => {
        expect(email('email', { email: 'test@@gmail.com' })).toEqual(false)
        expect(email('email', { email: 'test@gmail' })).toEqual(false)
        expect(email('email', { email: '@gmail.ru' })).toEqual(false)
    })

    it('required вернет true для required', () => {
        expect(required('test', { test: 'value' })).toEqual(true)
        expect(required('test', { test: { value: 'value' } })).toEqual(true)
        expect(required('test', { test: 23 })).toEqual(true)
        expect(required('test', { test: ['test'] })).toEqual(true)
        expect(
            required(
                'test',
                { test: 'test' },
                {
                    expression: '`test === \'test\'`',
                },
            ),
        ).toEqual(true)
    })

    it('required вернет false для не required', () => {
        expect(required('test', { test: '' })).toEqual(false)
        expect(required('test', { test: {} })).toEqual(false)
        expect(required('test', { test: [] })).toEqual(false)
        expect(required('test', { test: undefined })).toEqual(false)
        expect(required('test', { test: null })).toEqual(false)
        expect(required('test', { test: NaN })).toEqual(false)
    })

    it('integer вернет true', () => {
        expect(integer('value', { value: 1 })).toEqual(true)
        expect(integer('value', { value: -2 })).toEqual(true)
    })

    it('integer вернет значение если значение не валидно', () => {
        expect(integer('value', { value: undefined })).toEqual(undefined)
        expect(integer('value', { value: null })).toEqual(null)
        expect(integer('value', { value: NaN })).toEqual(NaN)
        expect(integer('value', { value: '' })).toEqual('')
    })

    it('integer вернет false если не int', () => {
        expect(integer('value', { value: {} })).toEqual(false)
    })
})
