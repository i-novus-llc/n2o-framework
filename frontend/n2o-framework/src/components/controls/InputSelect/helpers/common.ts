import isEmpty from 'lodash/isEmpty'

import type { Option, Options } from '../types'
import propsResolver from '../../../../utils/propsResolver'

export function getField<T = unknown>(data: Option, key?: string): T | string {
    return key && data[key] !== undefined ? data[key] as T : ''
}
export const getOptionById = (id: string | number | null, options: Options) => options.find(option => option.id === id)
export const getOptionByFieldName = (fieldName: string, value: string, options: Options) => options.find(option => option[fieldName] === value)

// TODO нужно для popup работает только c array
export const getSelectedOptions = (selected?: Options | Option, multiSelect?: boolean): Options => {
    if (multiSelect) { return selected as Options || [] }

    return isEmpty(selected) ? [] : [selected] as Options
}

export const computeFormattedOptions = (
    options: Options,
    format: string | undefined,
): Array<Option & { formattedTitle?: string }> => {
    if (!format) { return [...options] }

    return options.map(option => ({ ...option, formattedTitle: propsResolver(format, option) }))
}

// Из за особенностей н2о в multi может придти type Option
export const normalizeMultiSelected = (selected?: Option | Options) => {
    if (!selected) { return [] }

    if (Array.isArray(selected)) { return selected }

    return [selected]
}

export interface getDynamicListHeightProps {
    count: number
    page: number
    size: number
}

const DEFAULT_ELEMENT_HEIGHT = 35
const MAX_HEIGHT = 250

function getHeight(height: number | 'auto', max: number) {
    if (height === 'auto' || height < max) { return height }

    return MAX_HEIGHT
}

export function getDynamicListHeight({ count, page, size }: getDynamicListHeightProps) {
    const fullSize = size * page
    const listHeight = fullSize * DEFAULT_ELEMENT_HEIGHT

    const computedHeight = (count !== 0 && count > fullSize) ? listHeight : 'auto'

    const height = getHeight(computedHeight, MAX_HEIGHT)

    return { height, maxHeight: height }
}

export const TOLERANCE = 1

export const isScrollEnd = (element: HTMLElement): boolean => {
    const { scrollTop, clientHeight, scrollHeight } = element

    return scrollTop + clientHeight >= scrollHeight - TOLERANCE
}
