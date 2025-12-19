import { useCallback } from 'react'

import { useDispatch } from '../../../core/Redux/useDispatch'
import evalExpression from '../../../utils/evalExpression'

type ActionFunc = (model: never) => void

export type ClickAction = Record<string, never>

export const useOnActionMethod = <Action extends ClickAction>(config?: Action) => {
    const dispatch = useDispatch()

    return (
        useCallback<ActionFunc>((model) => {
            if (!config) {
                console.warn('Config - second argument, not passed')

                return
            }

            const {
                enablingCondition,
                action,
            } = config

            const allowRowClick = (
                enablingCondition
                    ? evalExpression(enablingCondition, model)
                    : true
            )

            if (action && allowRowClick) {
                dispatch(action)
            }
        }, [config, dispatch])
    )
}
