import sinon from 'sinon'

import link from './link'

describe('link', () => {
    it('должен вызывать событие перхода внутри приложения', () => {
        const dispatch = sinon.spy()

        link({
            target: 'application',
            dispatch,
            path: '/test/:id',
            pathMapping: {
                id: {
                    link: 'test.id',
                },
            },
            state: {
                test: {
                    id: 123,
                },
            },
        })

        expect(dispatch.calledOnce).toBeTruthy()
        expect(dispatch.getCall(0).args[0].payload.args[0]).toBe('/test/123')
    })
})
