import moment from 'moment'
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
    date: ({ data, format }) => moment(data).format(format),
    password: ({ data }) => join(map(data, () => '*'), ''),
    number: ({ data, format }) => {
        const number = isString(data) ? Number(data) : data

        return numeral(number).format(format)
    },
    dateFromNow: ({ format }) => moment().format(format),
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
 * @param data - исходная строка
 * @param typeAndformat - строка с типом данных и форматом
 * @returns {*}
 */
function parseFormatter(data, typeAndformat = false) {
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

export default parseFormatter
