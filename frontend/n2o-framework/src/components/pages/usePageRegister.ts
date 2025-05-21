import { useLayoutEffect } from 'react'
import { useStore } from 'react-redux'
import isEmpty from 'lodash/isEmpty'
import { Dispatch } from 'redux'

import { dataSourceByIdSelector } from '../../ducks/datasource/selectors'
import { register, remove } from '../../ducks/datasource/store'
import { type DataSourceState } from '../../ducks/datasource/DataSource'
import { type Paging } from '../../ducks/datasource/Provider'

export const usePageRegister = (dispatch: Dispatch, datasources?: Record<string, DataSourceState>, pageId?: string) => {
    const { getState } = useStore()

    useLayoutEffect(() => {
        if (!datasources || isEmpty(datasources)) { return }
        const state = getState()

        Object.entries(datasources).forEach(([id, config]) => {
            // @INFO сохраненный paging (прим. local storage)
            const { paging: initialPaging = {} as Paging } = dataSourceByIdSelector(id)(state)
            const { paging: configPaging = {} as Paging, ...rest } = config

            dispatch(register(id, {
                pageId,
                // FIXME обход Автотеста
                paging: (isEmpty(initialPaging) || initialPaging.size < 5) ? configPaging : initialPaging,
                ...rest,
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
