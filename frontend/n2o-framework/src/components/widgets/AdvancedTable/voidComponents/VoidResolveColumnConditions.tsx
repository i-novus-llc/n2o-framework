import { useSelector, useStore } from 'react-redux'
import { useLayoutEffect, useRef, VFC } from 'react'

import { modelsSelector } from '../../../../ducks/models/selectors'
import { ChangeColumnParam, ColumnState } from '../hooks/useColumnsState'
import { resolveConditions } from '../../../../sagas/conditions'

type Props = {
    columnsState: ColumnState
    changeColumnParam: ChangeColumnParam
    widgetId: string
}

export const VoidResolveColumnConditions: VFC<Props> = ({ columnsState, changeColumnParam, widgetId }) => {
    const { getState } = useStore()
    const refState = useRef(columnsState)
    const models = useSelector(modelsSelector)

    refState.current = columnsState

    useLayoutEffect(() => {
        const state = getState()

        refState.current.forEach(({ conditions, visible, columnId }) => {
            if (!conditions?.visible) {
                return
            }

            const resolvedVisibility = resolveConditions(state, conditions.visible).resolve
            const needChangeVisibility = resolvedVisibility !== visible

            if (needChangeVisibility) {
                changeColumnParam(widgetId, columnId, 'visible', resolvedVisibility)
            }
        })
    }, [changeColumnParam, getState, models])

    return null
}
