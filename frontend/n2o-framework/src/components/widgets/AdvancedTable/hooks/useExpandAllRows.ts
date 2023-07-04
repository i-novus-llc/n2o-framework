import { useEffect } from 'react'
import { useDispatch } from 'react-redux'

import { ChildrenToggleState } from '../../../Table'
import { Data } from '../../../Table/types/general'
import { getValueBySearchKey } from '../../../Table/utils'

export const useExpandAllRows = (
    callback: (value: string[]) => void,
    type: ChildrenToggleState,
    data: Data,
) => {
    const dispatch = useDispatch()

    useEffect(() => {
        if (type === ChildrenToggleState.Expand && data && data.length) {
            const allRowsId = getValueBySearchKey(data, {
                keyToExtract: 'id',
                keyToSearch: 'children',
                equalFunc: data => Boolean(data.children?.length),
            })

            callback(allRowsId)
        }
    }, [type, data, dispatch, callback])
}
