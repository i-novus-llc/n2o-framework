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

function http_build_query(formdata, numeric_prefix, arg_separator) {
    let key
    let use_val
    let use_key
    let i = 0
    const tmp_arr = []

    if (!arg_separator) {
        arg_separator = '&'
    }

    for (key in formdata) {
        if (!formdata.hasOwnProperty(key)) {
            continue
        }
        use_key = key
        use_val = String(formdata[key])
        use_val = use_val.replace(/%20/g, '+')

        if (numeric_prefix && !isNaN(key)) {
            use_key = numeric_prefix + i
        }
        tmp_arr[i] = `${use_key}=${use_val}`
        i++
    }

    return tmp_arr.join(arg_separator) && `?${tmp_arr.join(arg_separator)}`
}

/**
 * new method conversion in url for query
 * see {NNO-266}
 * @param data
 * @returns {String}
 */
export function http_params_query(data) {
    const params = []

    each(data, (val, key) => {
        if (data.hasOwnProperty(key)) {
            params.push({ name: key, value: val })
        }
    })

    return params
}

/**
 * Make "flat" query string from object Json
 *  and extends params
 *  with new paramize method
 * @param objectAim
 * @param [Key] - key id start
 * @param [$Res] - extender result object
 * @param [delimiter] - extender result object
 * @returns {String}
 */
export function generateParamQuery(objectAim, Key, $Res, delimiter) {
    $Res = $generateFlatQuery(objectAim, Key, $Res, delimiter)

    return http_params_query($Res)
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

    return http_build_query($Res)
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
            each(val, ($val, $key) => {
                if (isObject($val)) {
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
 * Create object form string with dot notation
 *
 * @example
 * "Page.sdf.test"  =>  {
 *     Page: {
 *         sdf: {
 *             test: 'default value'
 *         }
 *     }
 * }
 *
 * @param {String} string
 * @param [defaultValue]
 * @param [delimiter]
 * @returns {Object}
 * @param {Object} [$resultObject]
 */
export function createObjectFromDotString(
    string,
    defaultValue,
    delimiter,
    $resultObject,
) {
    // defaultValue = defaultValue || {};
    delimiter = delimiter || '.'
    const keys = string.split(delimiter)
    const res = $resultObject || {}
    let $next = res

    each(keys, (item, i) => {
    // if key array's
        let resRegExp

        if ((resRegExp = item && item.match(/\[(\d)]/))) {
            const index = +resRegExp[1]
            const $key = item.replace(/\[(\d)]/, '')

            $next[$key] = $next[$key] || []
            // about array[0][0] ???

            $next[$key][index] = $next[$key][index] || {}
            if (keys.length === i + 1) {
                $next[$key][index] = defaultValue
            }

            $next = $next[$key][index]

            return $next
        }
        $next[item] = $next[item] || {}

        $next[item] = $next[item] || {}
        if (keys.length === i + 1) {
            $next[item] = defaultValue
        }
        $next = $next[item]
    })

    return res
}

/**
 * Create object dy dot notation string and set value from context.
 * @param key string with dot notation
 * @param obj
 * @param [delimiter]
 * @returns {{}}
 */
export function createObjectByDotNotationKey(key, obj, delimiter) {
    delimiter = delimiter || '.'
    const keys = key.split(delimiter)
    const res = {}
    let $next = res
    const $val = obj

    const deep = []

    if (keys.length === 1) {
        res[key] = obj[key]

        return res
    }

    each(keys, (item, i) => {
        deep.push(item)
        $next[item] = $next[item] || {}
        $val[item] = $val[item]
        if (keys.length === i + 1) {
            $next[item] = getPathValue($val, deep.join(delimiter))
        }
        $next = $next[item]
    })

    return res
}

/**
 * get value from sub-path object
 * @param object
 * @param path
 */
export function getPathValue(object, path) {
    const flatParams = $generateFlatQuery(object, { withoutEncode: true })

    return flatParams[path]
}

/**
 * get func from preFilter
 * @param value
 */
export function preFilterIsFunction(value) {
    const regExp = `${value}`.match(/^`(.*)`$/)

    return regExp && regExp[1]
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
