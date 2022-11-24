import { isNil } from 'lodash'

import { HAVE_NOT_PRIMARY_KEY } from '../const'

export function checkPrimaryKey<TItem extends object>(
    item: TItem,
    primaryKey: keyof TItem,
) {
    if (isNil(item[primaryKey])) {
        throw new Error(HAVE_NOT_PRIMARY_KEY)
    }
}
