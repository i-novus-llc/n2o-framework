import isObject from 'lodash/isObject'
import map from 'lodash/map'

export function findDeep(collection, key, value) {
    if (!isObject(collection)) {
        return null
    }

    const finded = collection[key] === value ? collection : null
    const findedDeep = map(collection, nestCollection => findDeep(nestCollection, key, value)).flat()

    return [finded, ...findedDeep].filter(Boolean)
}
