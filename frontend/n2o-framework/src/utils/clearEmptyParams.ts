import pickBy from 'lodash/pickBy'
import isObject from 'lodash/isObject'
import isEmpty from 'lodash/isEmpty'
import isNil from 'lodash/isNil'

/**
 * Удаляет все пустые значения в параметрах запроса
 * @param obj
 * @returns {*}
 */
export function clearEmptyParams<T extends object>(obj: T): Partial<T> {
    return pickBy(obj, (value) => {
        if (isNil(value)) { return false }
        if (isObject(value)) { return !isEmpty(value) }

        return true
    })
}
