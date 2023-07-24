import findIndex from 'lodash/findIndex'
import isEmpty from 'lodash/isEmpty'
import isString from 'lodash/isString'
import get from 'lodash/get'
import reduce from 'lodash/reduce'
import has from 'lodash/has'

import { TOLERANCE, UNKNOWN_GROUP_FIELD_ID } from './constants'
import { TOption } from './types'

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const inArray = (array: any[] = [], item: TOption | string = '') => array.some(arrayItem => (isString(item)
    ? arrayItem === item
    : arrayItem.id && item.id && arrayItem.id === item.id))

export const groupData = (data: TOption[], groupFieldId: string) => reduce(
    data,
    // eslint-disable-next-line @typescript-eslint/no-explicit-any
    (acc: any, item) => {
        const key = get(item, groupFieldId) || UNKNOWN_GROUP_FIELD_ID

        if (has(acc, key)) {
            acc[key].push(item)
        } else {
            acc[key] = [item]
        }

        return acc
    },
    {},
)

export const isDisabled = (
    item: TOption,
    selected: TOption[],
    disabled: Array<string | number>,
) => inArray(disabled, item) || inArray(selected, item)

const getNextNotDisabledId = (
    data: TOption[],
    selected: TOption[],
    disabled: Array<string | number>,
    initialId: number,
    distance: number,
    valueFieldId: string,
) => {
    let index = findIndex(data, item => item[valueFieldId as keyof TOption] === initialId)

    while (
        data[index + distance] &&
        isDisabled(data[index + distance], selected, disabled)
    ) {
        index += distance
    }
    if (!data[index + distance]) { return initialId }

    return data[index + distance][valueFieldId as keyof TOption]
}

const getIdByDistance = (
    data: TOption[],
    currentId: string,
    distance: number,
    valueFieldId: string,
    selected: TOption[],
    disabled: Array<string | number>,
) => {
    if (isEmpty(data)) { return }
    const id = currentId || data[0][valueFieldId as keyof TOption]

    // eslint-disable-next-line consistent-return
    return getNextNotDisabledId(
        data,
        selected,
        disabled,
        id,
        distance,
        valueFieldId,
    )
}

export const getFirstNotDisabledId = (
    data: TOption[],
    selected: TOption[],
    disabled: Array<string | number>,
    valueFieldId: string,
) => {
    if (isEmpty(data)) { return }
    // eslint-disable-next-line consistent-return
    if (!isDisabled(data[0], selected, disabled)) { return data[0][valueFieldId as keyof TOption] }

    // eslint-disable-next-line consistent-return
    return getNextNotDisabledId(
        data,
        selected,
        disabled,
        data[0][valueFieldId as keyof TOption],
        1,
        valueFieldId,
    )
}

export const getNextId = (
    data: TOption[],
    currentId: string,
    valueFieldId: string,
    selected: TOption[],
    disabled: Array<string | number>,
) => getIdByDistance(data, currentId, 1, valueFieldId, selected, disabled)

export const getPrevId = (
    data: TOption[],
    currentId: string,
    valueFieldId: string,
    selected: TOption[],
    disabled: Array<string | number>,
) => getIdByDistance(data, currentId, -1, valueFieldId, selected, disabled)

export const isBottom = ({
    scrollHeight,
    scrollTop,
    clientHeight,
}: Element) => {
    const difference = Math.floor(scrollHeight - scrollTop)

    if (difference === clientHeight) {
        return true
    }

    const inaccuracyLess = clientHeight - TOLERANCE
    const inaccuracyLarge = clientHeight + TOLERANCE

    return difference === inaccuracyLess || difference === inaccuracyLarge
}

export const getValueArray = <TValue>(value: TValue | TValue[]): TValue[] => {
    if (Array.isArray(value)) { return value }
    if (value) { return [value] }

    return []
}
