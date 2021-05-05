import RequestError from './RequestError'

describe('Проверка RequestError', () => {
    it('сохраняет данные об ошибке', () => {
        const error = new RequestError('test error', 400, {}, {})

        expect(error.name).toEqual('RequestError')
        expect(error.message).toEqual('test error')
        expect(error.status).toEqual(400)
        expect(error.body).toEqual({})
        expect(error.json).toEqual({})
    })

    it.skip('возвращает мету из json', () => {
        const error = new RequestError(
            'error',
            404,
            {},
            {
                meta: {
                    value: 'json meta',
                },
            },
        )

        expect(error.getMeta()).toEqual({
            value: 'json meta',
        })
    })
})
