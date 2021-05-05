import { ADD } from '../constants/alerts'
import { USER_CHECK } from '../constants/auth'

import createActionHelper from './createActionHelper'

describe('Тест для createActionHelper', () => {
    it('Генирирует правильные события', () => {
        const action1 = createActionHelper(ADD)()

        expect(action1.type).toEqual(ADD)
        const action2 = createActionHelper(USER_CHECK)()

        expect(action2.type).toEqual(USER_CHECK)
    })
    it('Возвращает правильный payload', () => {
        const action = createActionHelper(ADD)({ data: 'test', visible: true })

        expect(action.payload.data).toEqual('test')
        expect(action.payload.visible).toEqual(true)
    })
    it('Возвращает правильную meta', () => {
        const action = createActionHelper(ADD)(
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
