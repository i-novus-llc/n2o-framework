import fetchMock from 'fetch-mock'
import pick from 'lodash/pick'

import formValidJson from '../../../.storybook/json/FormValidations.json'

import { validateField } from './createValidator'
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

const validConfig = {
    condition: {
        type: 'condition',
        expression: 'test == "test"',
        severity: 'danger',
        text: 'Ошибка',
    },
    constraint: {
        type: 'constraint',
        validationKey: 'testValid',
    },
}

describe('Проверка пресетов', () => {
    describe('Проверка email пресета', () => {
        it('валидное значение', () => {
            expect(email('test', { test: 'test@test.ru' })).toBe(true)
        })
        it('не валидное значение', () => {
            expect(email('test', { test: 'test' })).toBe(false)
        })
        it('не валидное значение (ошибка в email)', () => {
            expect(email('test', { test: 'test@test' })).toBe(false)
        })
    })
    describe('Проверка required пресета', () => {
        it('валидное значение', () => {
            expect(required('test', { test: 'test' })).toBe(true)
        })
        it('не валидное значение', () => {
            expect(required('test', { test2: 'test' })).toBe(false)
        })
        it('Проверка dot нотации в пути во вложенных обьектах', () => {
            expect(required('test2.test', { test2: { test: 'test' } })).toBe(true)
        })
    })
    describe('Проверка condition пресета', () => {
        it('валидное значение', () => {
            expect(condition('test', { test: 'test' }, validConfig.condition)).toBe(
                true,
            )
        })
        it('не валидное значение', () => {
            expect(condition('test', { test: 'test1' }, validConfig.condition)).toBe(
                false,
            )
        })
        it('валидное - expression в виде функции', () => {
            expect(
                condition(
                    'test',
                    { test: 'test', test2: 'test2' },
                    {
                        expression:
              '(function(values){ return test2 == "test2" && values.test == "test"; })(this)',
                    },
                ),
            ).toBe(true)
        })
    })
    describe('Проверка constraint пресета', () => {
        const dispatched = []
        it('ответ с сообщениями', async () => {
            fetchMock.restore().get('begin:n2o/validation', {
                status: 200,
                sendAsJson: true,
                body: {
                    message: [
                        {
                            severity: 'danger',
                            text: 'Тест',
                        },
                    ],
                },
            })
            expect(
                await constraint(
                    'test',
                    { test: 'test' },
                    validConfig.constraint,
                    action => dispatched.push(action),
                ),
            ).toEqual({
                message: [
                    {
                        severity: 'danger',
                        text: 'Тест',
                    },
                ],
            })
        })
    })
    describe('Проверка integer пресета', () => {
        it('валидное значение', () => {
            expect(integer('test', { test: 1 })).toBe(true)
        })
        it('не валидное значение', () => {
            expect(integer('test', { test: 'test' })).toBe(false)
        })
    })
    describe('Проверка minLength пресета', () => {
        it('валидное значение', () => {
            expect(minLength('test', { test: '12345' }, { min: 3 })).toBe(true)
        })
        it('не валидное значение', () => {
            expect(minLength('test', { test: '12' }, { min: 3 })).toBe(false)
        })
    })
    describe('Проверка maxLength пресета', () => {
        it('валидное значение', () => {
            expect(maxLength('test', { test: '12' }, { max: 3 })).toBe(true)
        })
        it('не валидное значение', () => {
            expect(maxLength('test', { test: '12345' }, { max: 3 })).toBe(false)
        })
    })
    describe('Проверка match пресета', () => {
        it('валидное значение', () => {
            expect(match('test', { test: 'test' }, { field: 'test' })).toBe(true)
        })
        it('не валидное значение', () => {
            expect(match('test', { test: 'test1' }, { field: 'test' })).toBe(false)
        })
    })
})

describe('Проверка валидатора', () => {
    const validator = validateField(
        pick(formValidJson.Page_Form.form.validation, [
            'field2',
            'field4',
            'field8',
        ]),
        'testForm',
    )
    let dispatched = []
    const dispatch = (action) => {
        dispatched.push(action)
    }
    beforeEach(() => {
        dispatched = []
    })
    it('есть ошибки', async () => {
        expect.assertions(2)
        const res = await validator(
            { field2: 'test1', field4: null, field8: 'qwe' },
            dispatch,
        )
        expect(res).toBe(true)
        expect(dispatched.length).toBe(1)
    })
    it('есть ошибки, когда форма пустая', async () => {
        expect.assertions(2)
        const res = await validator({}, dispatch)
        expect(res).toBe(true)
        expect(dispatched.length).toBe(1)
    })
})
