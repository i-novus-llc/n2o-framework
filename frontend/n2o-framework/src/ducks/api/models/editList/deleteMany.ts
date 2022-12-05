import { isEqual } from 'lodash'

import { NOT_ARRAY, NOT_FOUND_MESSAGE } from '../const'

export function deleteMany<
    TItem extends object,
    TPrimaryKey extends keyof TItem
>(list: TItem[], items: TItem[], primaryKey: TPrimaryKey): TItem[] {
    if (!Array.isArray(items)) {
        throw new Error(NOT_ARRAY)
    }

    let found = false
    const newList = list.filter((element) => {
        const include = items.some(item => (isEqual(element[primaryKey], item[primaryKey])))

        found = found || include

        return !include
    })

    if (!found) {
        throw new Error(NOT_FOUND_MESSAGE)
    }

    return newList
}
