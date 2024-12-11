import React, { useCallback, useContext, useRef, ComponentType } from 'react'
import isEmpty from 'lodash/isEmpty'
import isEqual from 'lodash/isEqual'
import { Action } from 'redux'

import { useDispatch } from '../../../core/Redux/useDispatch'
import { DataSourceContext } from '../../../core/widget/context'
import { useTableActions } from '../../Table'
import { N2OAction } from '../../../ducks/Action'

export interface Props {
    action?: Action
    datasource?: string
    rowIndex: number
    model?: Record<string, unknown>
}

export interface WrappedComponentProps {
    resolveWidget?(model: Record<string, unknown>): void
    callAction?(actionModel: Record<string, unknown>): void
    action?: Action
    dispatch?(action: never): N2OAction
    model?: Record<string, unknown>
}

/**
 * HOC для Cell
 * Переопределяет свойство onClick в компонентах для посылки экшена
 */
export function WithCell<P extends WrappedComponentProps>(
    WrappedComponent: ComponentType<P & WrappedComponentProps>,
) {
    const WithCellComponent: ComponentType<P & WrappedComponentProps & Props> = ({
        action,
        datasource,
        rowIndex,
        model,
        ...rest
    }) => {
        const dispatch = useDispatch()
        const { onUpdateModel } = useTableActions()
        const { setResolve } = useContext(DataSourceContext)

        /**
         * FIXME:
         *  ref и проверка на равенство моделей является костылем т.к. Событие callAction стрелят например на клик
         *  по кнопки тулбара. Вероятно это ошибка и требуется разобраться с тем, когда должен стрелять метод callAction
         */
        const modelRef = useRef(model)

        modelRef.current = model

        const callAction = useCallback(
            (actionModel: Record<string, unknown>) => {
                setResolve(actionModel)

                if (isEmpty(action) && !isEqual(actionModel, modelRef.current)) {
                    onUpdateModel(actionModel, rowIndex)
                } else {
                    dispatch(action)
                }
            },
            [setResolve, action, onUpdateModel, rowIndex, dispatch],
        )

        return (
            <WrappedComponent
                action={action}
                dispatch={dispatch}
                model={model}
                resolveWidget={setResolve}
                callAction={callAction}
                {...rest as unknown as P}
            />
        )
    }

    WithCellComponent.displayName = `WithCellComponent(${
        WrappedComponent.displayName ?? 'UnknownCellComponent'
    })`

    return WithCellComponent
}

export default WithCell
