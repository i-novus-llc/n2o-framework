import { isEqual } from 'lodash'

import { FOUND_MANY_MESSAGE, NOT_FOUND_MESSAGE } from '../const'

import { checkPrimaryKey } from './checkPrimaryKey'

export function deleteItem<TItem extends object>(list: TItem[], item: TItem, primaryKey: keyof TItem): TItem[] {
    checkPrimaryKey(item, primaryKey)

    let found = false

    const newList = list.filter((element) => {
        if (isEqual(element[primaryKey], item[primaryKey])) {
            if (found) {
                throw new Error(FOUND_MANY_MESSAGE)
            }

            found = true

            return false
        }

        return true
    })

    if (!found) {
        throw new Error(NOT_FOUND_MESSAGE)
    }

    return newList
}
