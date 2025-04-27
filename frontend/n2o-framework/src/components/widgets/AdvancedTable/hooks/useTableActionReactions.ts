/* eslint-disable @typescript-eslint/no-explicit-any */
import { useCallback } from 'react'
import { useStore } from 'react-redux'

import { dataSourceModelByPrefixSelector, dataSourceModelsSelector } from '../../../../ducks/datasource/selectors'
import { getHashMapFromData } from '../../../Table/utils'
import { setModel } from '../../../../ducks/models/store'
import { ModelPrefix } from '../../../../core/datasource/const'
import { reorderColumn as reorderColumnAction } from '../../../../ducks/table/store'

export const useTableActionReactions = (datasourceId: string, widgetId: string) => {
    const store = useStore()
    const setMultiModelAfterChangeSelectedRows = useCallback((selectedRows: string[] | string) => {
        const state = store.getState()
        const { multi, datasource } = dataSourceModelsSelector(datasourceId)(state)
        const datasourceAsMap = getHashMapFromData(datasource as any[], { keyAsHash: 'id', keyToIterate: 'children' })

        if (Array.isArray(selectedRows)) {
            const selectedData: any[] = []

            selectedRows.forEach((selectedId) => {
                const foundData = datasourceAsMap.get(selectedId)

                selectedData.push(foundData)
            })

            const newMulti = [...multi, ...selectedData]

            store.dispatch(setModel(ModelPrefix.selected, datasourceId, newMulti))
        } else {
            const newMulti = [datasourceAsMap.get(selectedRows)]

            store.dispatch(setModel(ModelPrefix.selected, datasourceId, newMulti))
        }
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

    const setActiveModel = useCallback((model) => {
        if (model) {
            store.dispatch(setModel(ModelPrefix.active, datasourceId, model))
        }
    }, [datasourceId, store])

    const updateDatasource = useCallback((model: Record<string, any>, rowIndex: number) => {
        const state = store.getState()
        const datasource = (dataSourceModelByPrefixSelector(datasourceId, ModelPrefix.source)(state) as any[]) || []
        const newDatasource = datasource?.map((data, index) => (index === rowIndex ? model : data)) || []

        setActiveModel(model)
        store.dispatch(setModel(ModelPrefix.source, datasourceId, newDatasource))
    }, [datasourceId, store, setActiveModel])

    const reorderColumn = useCallback((id: string, draggingId: string, targetId: string) => {
        store.dispatch(reorderColumnAction(widgetId, id, draggingId, targetId))
    }, [store, widgetId])

    return ({
        setMultiModel: setMultiModelAfterChangeSelectedRows,
        unsetMultiModel: unsetMultiModelAfterChangeSelectedRows,
        setActiveModel,
        updateDatasource,
        reorderColumn,
    })
}
