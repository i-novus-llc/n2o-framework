import { useStore } from 'react-redux'
import { useCallback } from 'react'
import isEmpty from 'lodash/isEmpty'
import isNumber from 'lodash/isNumber'

import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
// @ts-ignore - отсутствует типизация
import { useDataSourceMethodsContext } from '../../../core/widget/context'

export const useChangeFilter = (id: string) => {
    const { setFilter, fetchData } = useDataSourceMethodsContext()
    const store = useStore()

    return useCallback((filterData) => {
        const state = store.getState()
        const filterModel = dataSourceModelByPrefixSelector(id, ModelPrefix.filter)(state)
        const newFilter = {
            ...filterModel,
            [filterData.id]: filterData.value,
        }

        if (isNumber(filterData.value) || !isEmpty(filterData.value)) {
            newFilter[filterData.id] = filterData.value
        }

        setFilter(newFilter)
        fetchData()
    }, [fetchData, id, setFilter, store])
}
