/* eslint-disable @typescript-eslint/no-explicit-any */
import { useCallback } from 'react'
import { useStore } from 'react-redux'

import { dataSourceModelByPrefixSelector, dataSourceModelsSelector } from '../../../../ducks/datasource/selectors'
import { getHashMapFromData } from '../../../Table/utils'
import { setModel } from '../../../../ducks/models/store'
import { ModelPrefix } from '../../../../core/datasource/const'

export const useTableActionReactions = (datasourceId: string) => {
    const store = useStore()
    const setMultiModelAfterChangeSelectedRows = useCallback((selectedRows: string[]) => {
        const state = store.getState()
        const { multi, datasource } = dataSourceModelsSelector(datasourceId)(state)
        const datasourceAsMap = getHashMapFromData(datasource as any[], { keyAsHash: 'id', keyToIterate: 'children' })
        const selectedData: any[] = []

        selectedRows.forEach((selectedId) => {
            const foundData = datasourceAsMap.get(selectedId)

            selectedData.push(foundData)
        })

        const newMulti = [...multi, ...selectedData]

        store.dispatch(setModel(ModelPrefix.selected, datasourceId, newMulti))
    }, [datasourceId, store])

    const unsetMultiModelAfterChangeSelectedRows = useCallback((rowValues: any[]) => {
        const state = store.getState()
        const multi = (dataSourceModelByPrefixSelector(datasourceId, ModelPrefix.selected)(state) as any[]) || []

        const newMulti = multi.filter((multiItem) => {
            const isMatchId = rowValues.some(deselectId => multiItem.id === deselectId)

            return !isMatchId
        })

        store.dispatch(setModel(ModelPrefix.selected, datasourceId, newMulti))
    }, [datasourceId, store])

    const setActiveModelAfterChangeFocusedRow = useCallback((model) => {
        if (model) {
            store.dispatch(setModel(ModelPrefix.active, datasourceId, model))
        }
    }, [datasourceId, store])

    return ({
        setMultiModel: setMultiModelAfterChangeSelectedRows,
        unsetMultiModel: unsetMultiModelAfterChangeSelectedRows,
        setActiveModel: setActiveModelAfterChangeFocusedRow,
    })
}
