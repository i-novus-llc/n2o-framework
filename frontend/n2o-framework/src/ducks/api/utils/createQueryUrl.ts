import isEmpty from 'lodash/isEmpty'

import { SortDirection } from '../../../core/datasource/const'

const ESCAPED_SYMBOL = '%26'

export function createQueryUrl(url: string, sorting: Partial<Record<string, SortDirection>> = {}) {
    const escapedUrl = url.replace(/'&'/g, ESCAPED_SYMBOL)

    if (isEmpty(sorting)) {
        return escapedUrl
    }

    let urlWithSorting = escapedUrl
    const sortingKeys = Object.keys(sorting)

    sortingKeys.forEach((sortKey, index) => {
        const params = `${sortKey}=${sorting[sortKey]}`

        if (index === 0) {
            urlWithSorting += `${ESCAPED_SYMBOL}sorting.${params}`
        } else {
            urlWithSorting += `${ESCAPED_SYMBOL}${params}`
        }
    })

    return urlWithSorting
}
