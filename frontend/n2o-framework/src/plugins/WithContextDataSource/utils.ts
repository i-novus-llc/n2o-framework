import isEmpty from 'lodash/isEmpty'

// @ts-ignore ignore import error from js file
import { EXPRESSION_SYMBOL } from '../withItemsResolver/utils'

type valueToReplaceType = string

interface IMapping {
    value?: string | number
}
interface IQueryMapping {
    [key: string]: IMapping
}

export function hasExpression(value: string): boolean {
    return value.startsWith(EXPRESSION_SYMBOL)
}

function mappingResolver(mapping: IMapping, valueToReplace: valueToReplaceType): IMapping {
    const { value } = mapping

    if (typeof value !== 'string') {
        return mapping
    }

    if (hasExpression(value)) {
        return { ...mapping, value: valueToReplace }
    }

    return mapping
}

/* replaces values in queryMapping */
export function queryMappingResolver(
    queryMapping: IQueryMapping,
    valueToReplace: valueToReplaceType,
    extraMapping: IQueryMapping | undefined,
): IQueryMapping | null {
    if (isEmpty(queryMapping)) {
        return null
    }
    const resolvedQueryMapping: IQueryMapping = {}
    const keys = Object.keys(queryMapping)

    keys.forEach((key: string): void => {
        const currentQueryMapping = queryMapping[key]

        resolvedQueryMapping[key] = mappingResolver(currentQueryMapping, valueToReplace)
    })

    if (extraMapping !== undefined) {
        return { ...resolvedQueryMapping, ...extraMapping }
    }

    return resolvedQueryMapping
}
