import { useLayoutEffect } from 'react'
import isEmpty from 'lodash/isEmpty'

import { register, remove } from '../../ducks/datasource/store'

export const usePageRegister = (datasources, dispatch, pageId) => {
    useLayoutEffect(() => {
        if (!datasources || isEmpty(datasources)) { return }

        Object.entries(datasources).forEach(([id, config]) => {
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
