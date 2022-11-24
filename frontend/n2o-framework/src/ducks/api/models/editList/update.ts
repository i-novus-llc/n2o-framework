import { isEqual } from 'lodash'

import { FOUND_MANY_MESSAGE, NOT_FOUND_MESSAGE } from '../const'

import { checkPrimaryKey } from './checkPrimaryKey'

export function update<
    TItem extends object,
    TPrimaryKey extends keyof TItem
>(list: TItem[], item: TItem, primaryKey: TPrimaryKey): TItem[] {
    checkPrimaryKey(item, primaryKey)

    let found = false
    const newList = list.map((element) => {
        if (isEqual(element[primaryKey], item[primaryKey])) {
            if (found) {
                throw new Error(FOUND_MANY_MESSAGE)
            }

            found = true

            return item
        }

        return element
    })

    if (!found) {
        throw new Error(NOT_FOUND_MESSAGE)
    }

    return newList
}
