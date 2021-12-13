import React, { useCallback } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { isEmpty, isEqual } from 'lodash'
import PropTypes from 'prop-types'
import { createStructuredSelector } from 'reselect'

import { setActiveModel, setSourceModel } from '../../../ducks/datasource/store'
import { dataSourceModelsSelector } from '../../../ducks/datasource/selectors'

const mapDispatchToProps = (dispatch, { datasource, allModels }) => bindActionCreators(
    {
        // TODO костыль для быстрого решения, по хорошему вынести в таблицу и ячейка ничего не должна знать о моделях
        updateDatasource: (newModel) => {
            const list = allModels.map((model) => {
                if (model.id === newModel.id) {
                    return newModel
                }

                return model
            })

            return setSourceModel(datasource, list)
        },
        setResolve: model => dispatch(setActiveModel(datasource, model)),
    },
    dispatch,
)

const mapStateToProps = createStructuredSelector({
    allModels: (state, { datasource }) => dataSourceModelsSelector(datasource)(state).datasource,
})

/**
 * HOC для оборачивания Cell
 * Переопределяет свойство onClick в компонентах для посылки экшена
 * @param WrappedComponent
 * @returns {*}
 */

// eslint-disable-next-line func-names
export default function (WrappedComponent) {
    function WithCellComponent({
        setResolve,
        updateDatasource,
        action: defaultAction,
        model,
        datasource,
        dispatch,
        ...rest
    }) {
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
        setResolve: PropTypes.func,
        action: PropTypes.object,
        model: PropTypes.object,
        datasource: PropTypes.string,
        dispatch: PropTypes.func,
        updateDatasource: PropTypes.func,
    }

    return connect(
        mapStateToProps,
        mapDispatchToProps,
    )(WithCellComponent)
}
