import setWith from 'lodash/setWith'
import clone from 'lodash/clone'
import each from 'lodash/each'
import isObject from 'lodash/isObject'
import isArray from 'lodash/isArray'
import isString from 'lodash/isString'
import transform from 'lodash/transform'
import isEqual from 'lodash/isEqual'
import cloneDeepWith from 'lodash/cloneDeepWith'
import map from 'lodash/map'

/**
 * Не мутабельный set
 * @param object
 * @param path
 * @param value
 * @returns {Object}
 */
export const setIn = (object, path, value) => setWith(clone(object), path, value, clone)

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
                    $Res[Key ? [Key, key].join(delimiter) : key] =
            !needLinked($val) && isString($val) && !withoutEncode
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
            $Res[Key ? Key + delimiter + key : key] =
        !needLinked(val) && isString(val) && !withoutEncode
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

    if (res && res[2]) {
        return res[2]
    }

    return false
}

/**
 * Возвращается widgetId на основе page и container
 * @param pageId
 * @param cntId
 * @returns {string}
 */
export function getWidgetId(pageId, cntId) {
    return `${pageId}.${cntId}`
}

/**
 * Проверка является ли объект Promise
 * @param obj
 * @returns {boolean}
 */
export function isPromise(obj) {
    return (
        !!obj &&
    (typeof obj === 'object' || typeof obj === 'function') &&
    typeof obj.then === 'function'
    )
}

/**
 * Глубокое сравнение двух объектов
 * @param  {Object} object объект сравнания
 * @param  {Object} base   объект с чем сарвнивают
 * @return {Object}        возвращает новый объект с разницей
 */
export function difference(object, base) {
    const changes = (object, base) => transform(object, (result, value, key) => {
        if (!isEqual(value, base[key])) {
            result[key] =
          isObject(value) && isObject(base[key])
              ? changes(value, base[key])
              : value
        }
    })

    return changes(object, base)
}

/**
 * Глубокое удаление ключей
 * @param collection
 * @param excludeKeys
 * @returns {*}
 */
export function omitDeep(collection, excludeKeys) {
    function omitFn(value) {
        if (value && typeof value === 'object') {
            excludeKeys.forEach((key) => {
                delete value[key]
            })
        }
    }

    return cloneDeepWith(collection, omitFn)
}
