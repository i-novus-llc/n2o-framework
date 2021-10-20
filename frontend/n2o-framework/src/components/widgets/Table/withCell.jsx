import React, { useCallback } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { isEmpty, isEqual } from 'lodash'
import PropTypes from 'prop-types'
import { createStructuredSelector } from 'reselect'

import { PREFIXES } from '../../../ducks/models/constants'
import { callActionImpl } from '../../../ducks/toolbar/store'
import { startInvoke } from '../../../actions/actionImpl'
import { updateModel, setModel } from '../../../ducks/models/store'
import { makeGetModelByPrefixSelector } from '../../../ducks/models/selectors'

const mapDispatchToProps = dispatch => bindActionCreators(
    {
        onActionImpl: ({ src, component, options }) => callActionImpl(src || component, { ...options, dispatch }),
        onInvoke: (widgetId, dataProvider, data, modelLink, meta, modelId) => startInvoke(
            widgetId, dataProvider, data, modelLink, meta, modelId,
        ),
        onUpdateModel: (prefix, key, field, values) => updateModel(prefix, key, field, values),
        onResolveWidget: (modelId, model) => setModel(PREFIXES.resolve, modelId, model),
        // TODO костыль для быстрого решения, по хорошему вынести в таблицу и ячейка ничего не должна знать о моделях
        updateDatasource: (modelId, datasource, currentModel, newModel) => {
            const newDatasource = datasource.map((model) => {
                if (model.id === currentModel.id) {
                    return newModel
                }

                return model
            })

            return setModel(PREFIXES.datasource, modelId, newDatasource)
        },
    },
    dispatch,
)

const mapStateToProps = createStructuredSelector({
    datasourceModel: (state, { widgetId }) => makeGetModelByPrefixSelector(PREFIXES.datasource, widgetId)(state) || {},
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
        onActionImpl,
        onInvoke,
        onUpdateModel,
        onResolveWidget,
        updateDatasource,
        datasourceModel,
        action: defaultAction,
        model: defaultModel,
        widgetId,
        modelId,
        index,
        fieldKey,
        dataProvider,
        dispatch,
        ...rest
    }) {
        const resolveWidget = useCallback(data => onResolveWidget(modelId, data), [onResolveWidget, modelId])
        /**
         * @deprecated
         */
        const callActionImpl = useCallback((e, { action, model }) => {
            const currentModel = model || defaultModel
            const currentAction = action || defaultAction

            // eslint-disable-next-line no-unused-expressions
            currentModel && resolveWidget(currentModel)
            // eslint-disable-next-line no-unused-expressions
            currentAction && onActionImpl(currentAction)
        }, [defaultModel, defaultAction, resolveWidget, onActionImpl])

        /**
         * @deprecated
         */
        const callInvoke = useCallback((data, customProvider = null, meta) => {
            onInvoke(widgetId, customProvider || dataProvider, data, null, meta, modelId)
        }, [onInvoke, widgetId, dataProvider, modelId])

        const updateFieldInModel = useCallback((value, prefix = 'datasource') => {
            onUpdateModel(prefix, modelId, `[${index}].${fieldKey}`, value)
        }, [onUpdateModel, modelId, index, fieldKey])

        const callAction = useCallback((newModel) => {
            resolveWidget(newModel)

            if (isEmpty(defaultAction)) {
                if (!isEqual(defaultModel, newModel)) {
                    updateDatasource(widgetId, datasourceModel, defaultModel, newModel)
                }
            } else {
                dispatch(defaultAction)
            }
        }, [defaultAction, dispatch, resolveWidget, updateDatasource, datasourceModel, defaultModel, modelId])

        return (
            <WrappedComponent
                updateFieldInModel={updateFieldInModel}
                resolveWidget={resolveWidget}
                callActionImpl={callActionImpl}
                callInvoke={callInvoke}
                callAction={callAction}
                action={defaultAction}
                model={defaultModel}
                fieldKey={fieldKey}
                widgetId={widgetId}
                modelId={modelId}
                {...rest}
            />
        )
    }

    WithCellComponent.propTypes = {
        onActionImpl: PropTypes.func,
        onInvoke: PropTypes.func,
        onUpdateModel: PropTypes.func,
        onResolveWidget: PropTypes.func,
        action: PropTypes.object,
        model: PropTypes.object,
        widgetId: PropTypes.string,
        modelId: PropTypes.string,
        index: PropTypes.number,
        fieldKey: PropTypes.string,
        dataProvider: PropTypes.object,
        dispatch: PropTypes.func,
        datasourceModel: PropTypes.any,
        updateDatasource: PropTypes.func,
    }

    return connect(
        mapStateToProps,
        mapDispatchToProps,
    )(WithCellComponent)
}
