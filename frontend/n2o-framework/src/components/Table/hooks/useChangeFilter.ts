import { useStore } from 'react-redux'
import { useCallback } from 'react'
import set from 'lodash/set'
import cloneDeep from 'lodash/cloneDeep'

import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
import { useDataSourceMethodsContext } from '../../../core/widget/context'

export type DataType = Record<string, unknown> | Array<Record<string, unknown>>
export type onFilterType = (data: DataType) => void

export const useChangeFilter = (onUpdateFilter: onFilterType, id: string) => {
    const { fetchData } = useDataSourceMethodsContext()
    const store = useStore()

    return useCallback((filterData) => {
        const state = store.getState()
        const filterModel = dataSourceModelByPrefixSelector(id, ModelPrefix.filter)(state) as DataType || {}
        const newFilter = cloneDeep(filterModel)

        set(newFilter, filterData.id, filterData.value)

        onUpdateFilter(newFilter)
        fetchData()
    }, [fetchData, id, store, onUpdateFilter])
}
