import { isEmpty } from 'lodash'

import propsResolver from '../../utils/propsResolver'

const EXPRESSION_SYMBOL = ':'

const resolveItem = (item, models) => {
    if (isEmpty(models.datasource)) { return item }

    let newHref = item.href
    const modelObj = models.datasource[0]
    const pathVariables = Object.entries(item.pathMapping)

    pathVariables.forEach(([key, valueObj]) => {
        if (key in modelObj) {
            newHref = newHref.replaceAll(valueObj.value, modelObj[key])
        }
    })

    let hasQuery = false
    const queryVariables = Object.entries(item.queryMapping)

    queryVariables.forEach(([queryKey, queryValueObj]) => {
        const queryObj = propsResolver({ [queryKey]: queryValueObj.value }, modelObj)

        Object.entries(queryObj).forEach(([key, value]) => {
            newHref += `${hasQuery ? '&' : '?'}${key}=${value}`
            if (!hasQuery) { hasQuery = true }
        })
    })

    let newItems

    if (item.items) { newItems = item.items.map(i => resolveItem(i, models)) }

    return { ...item, items: newItems, href: newHref }
}

export const resolveItems = (items, models) => items.map(item => resolveItem(item, models))

export const resolveExpression = (location, path) => {
    if (!location || !path) { return {} }
    let key

    const expressionPosition = path.split('/').findIndex((e) => {
        key = e.substring(1)

        return e[0] === EXPRESSION_SYMBOL
    })
    const value = location.pathname.split('/')[expressionPosition]

    return { key, value }
}
