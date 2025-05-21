import isEmpty from 'lodash/isEmpty'

import { EXPRESSION_SYMBOL } from '../withItemsResolver/utils'

type valueToReplaceType = string

interface Mapping {
    value?: string | number
}
interface QueryMapping {
    [key: string]: Mapping
}

export function hasExpression(value: string): boolean {
    return value.startsWith(EXPRESSION_SYMBOL)
}

function mappingResolver(mapping: Mapping, valueToReplace: valueToReplaceType): Mapping {
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
    queryMapping: QueryMapping,
    valueToReplace: valueToReplaceType,
    extraMapping: QueryMapping | undefined,
): QueryMapping | null {
    if (isEmpty(queryMapping)) {
        return null
    }
    const resolvedQueryMapping: QueryMapping = {}
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
