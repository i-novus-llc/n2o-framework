import { isEqual, isNil } from 'lodash'

import { FOUND_MANY_MESSAGE, HAVE_NOT_PRIMARY_KEY, NOT_ARRAY, NOT_FOUND_MESSAGE, Operations, UNKNOWN_OPERATION } from './const'

function checkPrimaryKey<TItem extends object>(
    item: TItem,
    primaryKey: keyof TItem,
) {
    if (isNil(item[primaryKey])) {
        throw new Error(HAVE_NOT_PRIMARY_KEY)
    }
}

function create<TItem extends object>(list: TItem[], item: TItem | TItem[]): TItem[] {
    const tail = Array.isArray(item) ? item : [item]

    return [
        ...list,
        ...tail,
    ]
}

function update<
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

function deleteItem<TItem extends object>(list: TItem[], item: TItem, primaryKey: keyof TItem): TItem[] {
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

function deleteMany<
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

function updateList<TItem extends object>(
    list: TItem[],
    item: TItem | TItem[],
    primaryKey: keyof TItem,
    operation: Operations,
): TItem[] {
    switch (operation) {
        case Operations.createMany:
        case Operations.create: { return create(list, item) }
        case Operations.update: { return update(list, item as TItem, primaryKey) }
        case Operations.delete: { return deleteItem(list, item as TItem, primaryKey) }
        case Operations.deleteMany: { return deleteMany(list, item as TItem[], primaryKey) }
        default: {
            throw new Error(UNKNOWN_OPERATION)
        }
    }
}

export { updateList }
