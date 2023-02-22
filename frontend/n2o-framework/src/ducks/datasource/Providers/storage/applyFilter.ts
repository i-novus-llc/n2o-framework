import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import get from 'lodash/get'

import evalExpression, { parseExpression } from '../../../../utils/evalExpression'
import { getModelFieldByPath } from '../../../models/selectors'
import { State } from '../../../State'
import { IFilter, FilterType } from '../../Provider'

function equal(valueFromItem: unknown, valueFromFilter: unknown) {
    return isEqual(valueFromItem, valueFromFilter)
}

const FilterFn: Record<FilterType, typeof equal> = {
    [FilterType.Equal]: equal,
}

export function resolveValueByFilter({ value, link }: IFilter, state: State): unknown {
    if (!link) {
        return value
    }

    const expression = parseExpression(value)
    const modelByLink: object = getModelFieldByPath(link)(state)

    return expression ? evalExpression(expression, modelByLink) : modelByLink
}

export function applyFilter<TData>(state: State, list: TData[], filters?: IFilter[]) {
    if (isEmpty(filters)) {
        return list
    }

    return list
        .filter(item => (filters as IFilter[])
            .every((filter) => {
                const { type, fieldId } = filter

                const valueFromItem = get(item, fieldId)
                const valueFromFilter = resolveValueByFilter(filter, state)

                return FilterFn[type](valueFromItem, valueFromFilter)
            }))
}
