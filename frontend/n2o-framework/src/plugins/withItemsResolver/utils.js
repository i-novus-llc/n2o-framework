import { isEmpty } from 'lodash'

import propsResolver from '../../utils/propsResolver'
import { libAsterisk } from '../Menu/helpers'

export const EXPRESSION_SYMBOL = ':'

const resolveItem = (item, datasourceModels, datasource) => {
    const { datasource: childrenDataSource } = item

    let datasourceModel = null

    if (childrenDataSource) {
        datasourceModel = datasourceModels[childrenDataSource]?.[0]
    } else {
        datasourceModel = datasourceModels[datasource]?.[0]
    }

    if (isEmpty(datasourceModel)) { return item }

    let newHref = item.href
    let newTitle = item.title

    if (newHref) {
        const pathVariables = Object.entries(item.pathMapping)

        pathVariables.forEach(([key, valueObj]) => {
            if (key in datasourceModel) {
                newHref = newHref.replaceAll(valueObj.value, datasourceModel[key])
            }
        })

        let hasQuery = false
        const queryVariables = Object.entries(item.queryMapping)

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

    let newItems = []

    if (item.items) {
        newItems = item.items.map(i => resolveItem(i, datasourceModels, childrenDataSource || datasource))
    }

    return { ...item,
        ...(item.items && { items: newItems }),
        ...(item.href && { href: newHref }),
        ...(item.title && { title: newTitle }),
    }
}

export const resolveItems = (items, datasourceModels, datasource) => items
    .map(item => resolveItem(item, datasourceModels, datasource))

export const resolveExpression = (location, path) => {
    if (!location || !path) { return {} }
    let key

    const expressionPosition = path.split('/').findIndex((e) => {
        key = e.substring(1).replaceAll(libAsterisk, '')

        return e[0] === EXPRESSION_SYMBOL
    })
    const value = location.pathname.split('/')[expressionPosition]

    return { key, value }
}
