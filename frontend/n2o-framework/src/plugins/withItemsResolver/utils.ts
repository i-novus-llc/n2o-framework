import isEmpty from 'lodash/isEmpty'

import propsResolver from '../../utils/propsResolver'
import { libAsterisk } from '../Menu/helpers'

export const EXPRESSION_SYMBOL = ':'

interface Item {
    href?: string
    title?: string
    pathMapping?: Record<string, { value: string }>
    queryMapping?: Record<string, { value: string }>
    items?: Item[]
    datasource?: string
}

interface DatasourceModels {
    [key: string]: Array<Record<string, unknown>>
}

interface ResolveExpressionResult {
    key: string | undefined;
    value: string | undefined;
}

const resolveItem = (item: Item, datasourceModels: DatasourceModels, datasource: string): Item => {
    const { datasource: childrenDataSource } = item

    let datasourceModel: Record<string, unknown> = {}

    if (childrenDataSource) {
        datasourceModel = datasourceModels[childrenDataSource]?.[0]
    } else {
        datasourceModel = datasourceModels[datasource]?.[0]
    }

    if (isEmpty(datasourceModel)) { return item }

    let newHref = item.href || ''
    let newTitle = item.title

    if (newHref) {
        const pathVariables = Object.entries(item.pathMapping || {})

        pathVariables.forEach(([key, valueObj]) => {
            if (key in datasourceModel) {
                newHref = newHref.replaceAll(valueObj.value, <string>datasourceModel[key])
            }
        })

        let hasQuery = false
        const queryVariables = Object.entries(item.queryMapping || {})

        queryVariables.forEach(([queryKey, queryValueObj]) => {
            const queryObj = propsResolver({ [queryKey]: queryValueObj.value }, datasourceModel)

            Object.entries(queryObj).forEach(([key, value]) => {
                newHref += `${hasQuery ? '&' : '?'}${key}=${value}`
                if (!hasQuery) { hasQuery = true }
            })
        })
    }

    if (newTitle) {
        const { title } = propsResolver({ title: newTitle }, datasourceModel)

        newTitle = title
    }

    let newItems: Item[] = []

    if (item.items) {
        newItems = item.items.map(i => resolveItem(i, datasourceModels, childrenDataSource || datasource))
    }

    return {
        ...item,
        ...(item.items && { items: newItems }),
        ...(item.href && { href: newHref }),
        ...(item.title && { title: newTitle }),
    }
}

export const resolveItems = (items: Item[], datasourceModels: DatasourceModels, datasource: string): Item[] => {
    return items.map(item => resolveItem(item, datasourceModels, datasource))
}

export const resolveExpression = (location: { pathname: string }, path: string): ResolveExpressionResult => {
    if (!location || !path) { return { key: undefined, value: undefined } }
    let key

    const expressionPosition = path.split('/').findIndex((e) => {
        key = e.substring(1).replaceAll(libAsterisk, '')

        return e.startsWith(EXPRESSION_SYMBOL)
    })
    const value = location.pathname.split('/')[expressionPosition]

    return { key, value }
}
