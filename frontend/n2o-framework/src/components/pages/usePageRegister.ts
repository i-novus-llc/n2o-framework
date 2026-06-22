import { useLayoutEffect } from 'react'
import { useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import { Dispatch } from 'redux'
import set from 'lodash/set'
import get from 'lodash/get'
import has from 'lodash/has'

import { register, remove } from '../../ducks/datasource/store'
import { type DataSourceCache, type DataSourceConfig } from '../../ducks/datasource/DataSource'
import { type Paging } from '../../ducks/datasource/Provider'
import { getData } from '../../core/widget/useData'
import { dataSourceSortingSelector } from '../../ducks/datasource/selectors'
import { dataSourceSaveSettings } from '../../ducks/datasource/sagas/saveSettings'

export const usePageRegister = (dispatch: Dispatch, datasources?: Record<string, DataSourceConfig>, pageId?: string) => {
    const { getState } = useStore()
    const state = getState()

    useLayoutEffect(() => {
        if (!datasources || isEmpty(datasources)) { return }
        Object.entries(datasources).forEach(([id, config]) => {
            const { paging = {} as Paging, saveSettings, ...rest } = config
            const reduxSorting = dataSourceSortingSelector(id)(state) || {}

            const cached = {} as DataSourceCache

            if (saveSettings) {
                const currentCache = getData<DataSourceCache>(id) || {}

                for (const key of saveSettings) {
                    const mapping = dataSourceSaveSettings[key]

                    const value = get(currentCache, mapping.path)

                    if (value === undefined) { continue }

                    set(cached, mapping.path, value)
                }
            }

            dispatch(register(id, {
                pageId,
                // @INFO изначальные настройки полученные от сервера
                defaultDatasourceProps: { paging, sorting: rest.sorting },
                ...rest,
                paging: { ...paging, ...cached?.paging || {} },
                // @INFO null указывает на пользовательский (save-settings) сброс сортировки
                sorting: has(cached, 'sorting') ? cached.sorting : { ...rest.sorting, ...reduxSorting },
                saveSettings,
            }))
        })

        // eslint-disable-next-line consistent-return
        return () => {
            Object.keys(datasources).forEach((id) => {
                dispatch(remove(id))
            })
        }
    }, [datasources, dispatch, pageId])
}
