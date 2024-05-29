import React, { useCallback, useContext, useRef } from 'react'
import { isEmpty, isEqual } from 'lodash'
import PropTypes from 'prop-types'

import { useDispatch } from '../../../core/Redux/useDispatch'
import { DataSourceContext } from '../../../core/widget/context'
import { useTableActions } from '../../Table'

/**
 * HOC для оборачивания Cell
 * Переопределяет свойство onClick в компонентах для посылки экшена
 * @param WrappedComponent
 * @returns {*}
 */
export default function WithCell(WrappedComponent) {
    function WithCellComponent({
        action: defaultAction,
        datasource,
        rowIndex,
        model,
        ...rest
    }) {
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

        const callAction = useCallback((actionModel) => {
            setResolve(actionModel)

            if (isEmpty(defaultAction) && !isEqual(actionModel, modelRef.current)) {
                onUpdateModel(actionModel, rowIndex)
            } else {
                dispatch(defaultAction)
            }
        }, [setResolve, defaultAction, onUpdateModel, rowIndex, dispatch])

        return (
            <WrappedComponent
                resolveWidget={setResolve}
                callAction={callAction}
                action={defaultAction}
                dispatch={dispatch}
                model={model}
                {...rest}
            />
        )
    }

    WithCellComponent.displayName = `withCellComponent(${WrappedComponent?.displayName || 'UnknownCellComponent'})`

    WithCellComponent.propTypes = {
        action: PropTypes.object,
        model: PropTypes.object,
        datasource: PropTypes.string.isRequired,
        dispatch: PropTypes.func,
        url: PropTypes.string,
        target: PropTypes.string,
    }

    return WithCellComponent
}
