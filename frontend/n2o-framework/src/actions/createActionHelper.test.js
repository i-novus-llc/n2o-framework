import { USER_CHECK } from '../ducks/user/constants'

import createActionHelper from './createActionHelper'

describe('Тест для createActionHelper', () => {
    it('Генирирует правильные события', () => {
        const action = createActionHelper(USER_CHECK)()
        expect(action.type).toEqual(USER_CHECK)
    })
    it('Возвращает правильный payload', () => {
        const action = createActionHelper(USER_CHECK)({ data: 'test', visible: true })
        expect(action.payload.data).toEqual('test')
        expect(action.payload.visible).toEqual(true)
    })
    it('Возвращает правильную meta', () => {
        const action = createActionHelper(USER_CHECK)(
            {},
            {
                alert: {
                    severity: 'danger',
                    label: 'test',
                },
            },
        )
        expect(action.meta.alert.severity).toEqual('danger')
        expect(action.meta.alert.label).toEqual('test')
    })
})
