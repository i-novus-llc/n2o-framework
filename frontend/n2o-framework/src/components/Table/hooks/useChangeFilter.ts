import { useStore } from 'react-redux'
import { useCallback } from 'react'
import set from 'lodash/set'
import cloneDeep from 'lodash/cloneDeep'

import { dataSourceModelByPrefixSelector } from '../../../ducks/datasource/selectors'
import { ModelPrefix } from '../../../core/datasource/const'
// @ts-ignore - отсутствует типизация
import { useDataSourceMethodsContext } from '../../../core/widget/context'

// eslint-disable-next-line @typescript-eslint/no-explicit-any
export const useChangeFilter = (onUpdateFilter: (data: any) => void, id: string) => {
    const { fetchData } = useDataSourceMethodsContext()
    const store = useStore()

    return useCallback((filterData) => {
        const state = store.getState()
        const filterModel = dataSourceModelByPrefixSelector(id, ModelPrefix.filter)(state) || {}
        const newFilter = cloneDeep(filterModel)

        set(newFilter, filterData.id, filterData.value)

        onUpdateFilter(newFilter)
        fetchData()
    }, [fetchData, id, store, onUpdateFilter])
}
