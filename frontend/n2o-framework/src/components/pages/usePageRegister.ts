import { useLayoutEffect } from 'react'
import { useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import { Dispatch } from 'redux'

import { register, remove } from '../../ducks/datasource/store'
import { type DataSourceConfig } from '../../ducks/datasource/DataSource'
import { type Paging } from '../../ducks/datasource/Provider'
import { getData } from '../../core/widget/useData'

export const usePageRegister = (dispatch: Dispatch, datasources?: Record<string, DataSourceConfig>, pageId?: string) => {
    const { getState } = useStore()

    useLayoutEffect(() => {
        if (!datasources || isEmpty(datasources)) { return }
        Object.entries(datasources).forEach(([id, config]) => {
            const { paging: configPaging = {} as Paging, ...rest } = config

            // @INFO сохраненный paging (прим. local storage)
            const data = getData<{ datasourceFeatures: { paging: Paging } }>(id)
            const initialPaging = data?.datasourceFeatures?.paging || {} as Paging

            const paging = (isEmpty(initialPaging) || initialPaging.size < 5) ? configPaging : initialPaging

            dispatch(register(id, {
                pageId,
                paging,
                // @INFO изначальные настройки полученные от сервера
                defaultDatasourceProps: {
                    paging: configPaging,
                    sorting: rest.sorting,
                },
                ...rest,
            }))
        })

        // eslint-disable-next-line consistent-return
        return () => {
            Object.keys(datasources).forEach((id) => {
                dispatch(remove(id))
            })
        }
    }, [datasources, dispatch, getState, pageId])
}
