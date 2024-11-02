import { useLayoutEffect } from 'react'
import isEmpty from 'lodash/isEmpty'
import { Dispatch } from 'redux'

import { register, remove } from '../../ducks/datasource/store'
import type { DataSourceState } from '../../ducks/datasource/DataSource'

export const usePageRegister = (dispatch: Dispatch, datasources?: Record<string, DataSourceState>, pageId?: string) => {
    useLayoutEffect(() => {
        if (!datasources || isEmpty(datasources)) { return }

        Object.entries(datasources).forEach(([id, config]) => {
            // @ts-ignore FIXME разобраться TS2554: Expected 1 arguments, but got 2
            dispatch(register(id, { pageId, ...config }))
        })

        // eslint-disable-next-line consistent-return
        return () => {
            Object.keys(datasources).forEach((id) => {
                dispatch(remove(id))
            })
        }
    }, [datasources, dispatch, pageId])
}
