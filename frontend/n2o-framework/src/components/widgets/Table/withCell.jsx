import React, { useCallback, useContext } from 'react'
import { useDispatch, useSelector } from 'react-redux'
import { isEmpty, isEqual } from 'lodash'
import PropTypes from 'prop-types'

import { setSourceModel } from '../../../ducks/datasource/store'
import { dataSourceModelsSelector } from '../../../ducks/datasource/selectors'
import { DataSourceContext } from '../../../core/widget/context'

/**
 * HOC для оборачивания Cell
 * Переопределяет свойство onClick в компонентах для посылки экшена
 * @param WrappedComponent
 * @returns {*}
 */
export default function WithCell(WrappedComponent) {
    function WithCellComponent({
        action: defaultAction,
        model,
        datasource,
        ...rest
    }) {
        const dispatch = useDispatch()
        const { setResolve } = useContext(DataSourceContext)
        const list = useSelector(dataSourceModelsSelector(datasource)).datasource
        const updateDatasource = useCallback((newModel) => {
            // TODO костыль для быстрого решения, по хорошему вынести в таблицу и ячейка ничего не должна знать о моделях
            const newList = list.map((model) => {
                if (model.id === newModel.id) {
                    return newModel
                }

                return model
            })

            return setSourceModel(datasource, newList)
        }, [datasource, list])
        const callAction = useCallback((newModel) => {
            setResolve(newModel)

            if (isEmpty(defaultAction)) {
                if (!isEqual(model, newModel)) {
                    updateDatasource(newModel)
                }
            } else {
                dispatch(defaultAction)
            }
        }, [setResolve, defaultAction, model, updateDatasource, dispatch])

        return (
            <WrappedComponent
                resolveWidget={setResolve}
                callAction={callAction}
                action={defaultAction}
                model={model}
                {...rest}
            />
        )
    }

    WithCellComponent.propTypes = {
        action: PropTypes.object,
        model: PropTypes.object,
        datasource: PropTypes.string.isRequired,
        dispatch: PropTypes.func,
    }

    return WithCellComponent
}
