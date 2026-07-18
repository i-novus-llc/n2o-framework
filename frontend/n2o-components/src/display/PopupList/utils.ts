import get from 'lodash/get'
import groupBy from 'lodash/groupBy'

import { UNKNOWN_GROUP_FIELD_ID } from './constants'
import { type Option, type Options } from './types'

export const contains = (options: Options, option: Option) => {
    return options.some(
        item => item?.id && option?.id && String(item.id) === String(option.id),
    )
}

export const groupData = (data: Options, groupFieldId: string) => groupBy(data, item => get(item, groupFieldId) || UNKNOWN_GROUP_FIELD_ID)
