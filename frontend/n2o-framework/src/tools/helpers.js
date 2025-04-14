import each from 'lodash/each'
import isObject from 'lodash/isObject'
import isArray from 'lodash/isArray'
import isString from 'lodash/isString'
import map from 'lodash/map'
import isNaN from 'lodash/isNaN'
import isEmpty from 'lodash/isEmpty'

function buildHTTPQuery(formData, numericPrefix, argSeparator) {
    let i = 0
    const tmp = []

    if (!argSeparator) {
        argSeparator = '&'
    }

    // eslint-disable-next-line no-restricted-syntax
    for (const key in formData) {
        // eslint-disable-next-line no-prototype-builtins
        if (!formData.hasOwnProperty(key)) {
            // eslint-disable-next-line no-continue
            continue
        }
        let property = key
        const value = String(formData[key]).replace(/%20/g, '+')

        // eslint-disable-next-line no-restricted-globals
        if (numericPrefix && !isNaN(key)) {
            property = numericPrefix + i
        }
        tmp[i] = `${property}=${value}`
        // eslint-disable-next-line no-plusplus
        i++
    }

    return tmp.join(argSeparator) && `?${tmp.join(argSeparator)}`
}

/**
 * Make "flat" query string from object Json
 *  and extends params
 * @param objectAim
 * @param [Key] - key id start
 * @param [$Res] - extender result object
 * @param [delimiter] - extender result object
 * @returns {String}
 */
export function generateFlatQuery(objectAim, Key, $Res, delimiter) {
    $Res = $generateFlatQuery(objectAim, Key, $Res, delimiter)

    return buildHTTPQuery($Res)
}

function $generateFlatQuery(objectAim, Key, $Res, delimiter, options) {
    delimiter = delimiter || '.'
    $Res = $Res || {}
    if (isObject(Key)) {
        options = Key || {}
        Key = ''
    } else {
        Key = Key || ''
        options = options || {}
    }
    const ignoreNull = options.ignoreNull || false
    const withoutEncode = options.withoutEncode || false

    each(objectAim, (val, key) => {
        if (isArray(val)) {
            each(val, ($val) => {
                if (isObject($val)) {
                    // eslint-disable-next-line sonarjs/no-extra-arguments
                    generateFlatQuery(
                        $val,
                        Key ? [Key, key].join(delimiter) : key,
                        $Res,
                        delimiter,
                        options,
                    )
                } else if ($val !== null && $val !== undefined) {
                    $Res[Key ? [Key, key].join(delimiter) : key] = !needLinked($val) && isString($val) && !withoutEncode
                        ? map(val, v => encodeURIComponent(v)).join(`&${key}=`)
                        : val.join(`&${key}=`)
                }
            })

            return
        }

        if (isObject(val)) {
            // eslint-disable-next-line sonarjs/no-extra-arguments
            generateFlatQuery(
                val,
                Key ? Key + delimiter + key : key,
                $Res,
                delimiter,
                options,
            )
        } else if ((val !== null || ignoreNull) && val !== undefined) {
            $Res[Key ? Key + delimiter + key : key] = !needLinked(val) && isString(val) && !withoutEncode
                ? encodeURIComponent(val)
                : val
        }
    })

    return $Res
}

/**
 * @ignore
 */
function needLinked(query) {
    query = String(query)
    // noinspection JSValidateTypes
    const res = query.match('^(\\$|\\@)?{([^}^{]*)}$') // => {Page.container.name}

    if (res?.[2]) {
        return res[2]
    }

    return false
}

/**
 Бэкенд всегда присылает значения с единицами измерения
 прим. xml width = '200' будет получено как string '200px'
 Функция получения численных значений.
 Используется в компонентах в которых необходимы доп вычисления,
 либо работающими с integer only
 **/
export function mapToNumeric(params) {
    if (isEmpty(params)) {
        return {}
    }

    const newParams = { ...params }

    const keys = Object.keys(newParams)

    keys.forEach((key) => {
        const value = newParams[key]
        const parsed = parseFloat(value)

        if (!isNaN(parsed)) {
            newParams[key] = parsed
        }
    })

    return newParams
}
