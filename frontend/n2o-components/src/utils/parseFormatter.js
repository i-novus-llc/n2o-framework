import dayjs from 'dayjs'
import numeral from 'numeral'
import split from 'lodash/split'
import trim from 'lodash/trim'
import join from 'lodash/join'
import slice from 'lodash/slice'
import map from 'lodash/map'
import isNil from 'lodash/isNil'
import isString from 'lodash/isString'
import toString from 'lodash/toString'

const typesFunctions = {
    date: ({ data, format }) => dayjs(data).format(format),
    password: ({ data }) => join(map(data, () => '*'), ''),
    number: ({ data, format }) => {
        const number = isString(data) ? Number(data) : data

        return numeral(number).format(format)
    },
    dateFromNow: ({ format }) => dayjs().format(format),
    time: ({ data, format }) => dayjs(data, 'HH:mm:ss').format(format),
    snils: ({ data }) => snils(data),
}

function snils(value, formatString = '000-000-000 00') {
    const str = value?.toString().split('') || []

    if (str.length !== 11) { return value }

    let formated = ''

    for (const char of formatString) {
        formated += char === '0' ? str.shift() : char
    }

    return formated
}

/**
 * Преобразует строку по формату
 * Примеры форматов:
 * format="date DD.MM.YYYY HH:mm"
 * format="date DD.MM.YYYY"
 * format="dateFromNow"
 * format="password"
 * format="number 0,0.00"
 * format="number 0,0.00[000]"
 * format="number +7 (123) 456-78-90"
 * format="number 123-456-789 10"
 * @param data - исходная строка
 * @param typeAndformat - строка с типом данных и форматом
 * @returns {*}
 */
export function parseFormatter(data, typeAndformat = '') {
    if (numeral.formats.phone === undefined) {
        numeral.register('format', 'phone', {
            regexps: {
                format: /\+?0?[ .-]?\(?000\)?[ .-]?000[ .-]?00[ .-]?00/,
            },
            format(value, formatString) {
                if (!value || !formatString) {
                    return ''
                }

                function normalize(phoneNumber) {
                    return phoneNumber.toString().replace(
                        /^[\d\s+,{}-]*\(?(\d{3})\)?[ .-](\d{3})[ .-](\d{2})[ .-](\d{2})$/,
                        '$1$2$3$4',
                    )
                }

                function format(phoneNumber, formatString) {
                    phoneNumber = normalize(phoneNumber)
                    for (let i = 0, l = phoneNumber.length; i < l; i++) {
                        formatString = formatString.replace('0', phoneNumber[i])
                    }

                    return formatString
                }

                return format(value, formatString)
            },
        })
    }

    if (numeral.formats.snils === undefined) {
        numeral.register('format', 'snils', {
            regexps: {
                format: /(0{3}[ .-]?){3}0{2}/,
            },
            format: snils,
        })
    }

    const str = toString(data)
    const typeAndFormat = split(trim(typeAndformat), ' ')

    const type = typeAndFormat[0]

    if (
        isNil(data) ||
        !typeAndformat ||
        (str === '' && typeAndFormat[0] !== 'dateFromNow')
    ) { return data }

    if (!typeAndFormat.length) { return null }

    const format = join(slice(typeAndFormat, 1), ' ')

    return typesFunctions[type]({ data: str, format })
}
