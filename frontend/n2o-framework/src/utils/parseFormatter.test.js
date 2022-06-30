import moment from 'moment'

import '../numeralSettings'

import parseFormatter from './parseFormatter'

describe('Проверка parseFormatter', () => {
    it('преобразует дату', () => {
        expect(parseFormatter('2019-02-01T00:00:00', 'date DD:MM:YYYY')).toEqual(
            '01:02:2019',
        )
    })

    it('преобразует пароль', () => {
        expect(parseFormatter('password123', 'password')).toEqual('***********')
    })

    it('преобразует число', () => {
        expect(parseFormatter(12.12, 'number 0,0')).toEqual('12')
        expect(parseFormatter(12345678910, 'number +0 (000) 000-00-00')).toEqual('+1 (234) 567-89-10')
    })

    it('преобразует дату текущего момента', () => {
        expect(parseFormatter({}, 'dateFromNow D')).toEqual(moment().format('D'))
    })

    it('вернет data при невалидном значении data или typeAndFormat', () => {
        expect(parseFormatter(null)).toEqual(null)
        expect(parseFormatter(undefined)).toEqual(undefined)
        expect(parseFormatter({})).toEqual({})
        expect(parseFormatter(0)).toEqual(0)
    })
})
