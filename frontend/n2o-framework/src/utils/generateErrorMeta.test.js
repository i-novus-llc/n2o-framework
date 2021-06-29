import { initialState } from '../ducks/alerts/store'

import { generateErrorMeta } from './generateErrorMeta'

describe('проверка generateErrorMeta', () => {
    it('генерирует объект meta error из ошибки', () => {
        expect(
            generateErrorMeta({
                value: 'any message',
            }),
        ).toEqual({
            alert: {
                messages: [
                    {
                        ...initialState,
                        ...{ value: 'any message' },
                    },
                ],
            },
        })
    })

    it('генерирует default meta error, если нет присланной ошибки', () => {
        expect(generateErrorMeta()).toEqual({
            alert: {
                messages: [
                    {
                        ...initialState,
                    },
                ],
            },
        })
    })
})
