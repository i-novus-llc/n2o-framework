/* eslint-disable @typescript-eslint/no-explicit-any */
import { useCallback } from 'react'
import isEmpty from 'lodash/isEmpty'
import merge from 'deepmerge'
import { push } from 'connected-react-router'
import { useDispatch, useStore } from 'react-redux'

import { ModelPrefix } from '../../../core/datasource/const'
import evalExpression from '../../../utils/evalExpression'
// @ts-ignore - отсутствуют типы
import { dataProviderResolver } from '../../../core/dataProviderResolver'

type TAction = Record<string, any> | undefined

type TActionFunc = (model: any, action?: TAction) => void
type TFuncWithRequiredAction = (model: any, action: TAction) => void
type TResolvedFunc<T extends TAction> = T extends undefined ? TActionFunc : TFuncWithRequiredAction

export const useOnActionMethod = <Action extends Record<string, any>>(modelsId: string, action?: Action) => {
    const store = useStore()
    const dispatch = useDispatch()

    return (
        useCallback<TResolvedFunc<Action>>((model, providedClickAction = action) => {
            if (!providedClickAction) {
                return
            }

            const {
                enablingCondition,
                action,
                url,
                pathMapping,
                queryMapping,
                target,
            } = providedClickAction

            const allowRowClick = (
                enablingCondition
                    ? evalExpression(enablingCondition, model)
                    : true
            )

            if (action && allowRowClick) {
                dispatch(action)

                return
            }

            if (url) {
                const state = store.getState()
                const updatedState = !isEmpty(model) ? merge(state, {
                    models: {
                        [ModelPrefix.active]: {
                            [modelsId]: model,
                        },
                    },
                }) : state

                const { url: compiledUrl } = dataProviderResolver(updatedState, {
                    url,
                    pathMapping,
                    queryMapping,
                })

                if (target === 'application') {
                    dispatch(push(compiledUrl))
                } else if (target === '_blank') {
                    window.open(compiledUrl)
                } else {
                    window.location = compiledUrl
                }
            }
        }, [action, dispatch, modelsId, store])
    )
}
