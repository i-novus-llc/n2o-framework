import React, { useCallback } from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import { isEmpty } from 'lodash'
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
        onInvoke:
                (widgetId, dataProvider, data, modelLink, meta) => startInvoke(
                    widgetId, dataProvider, data, modelLink, meta,
                ),
        onUpdateModel: (prefix, key, field, values) => updateModel(prefix, key, field, values),
        onResolveWidget: (widgetId, model) => setModel(PREFIXES.resolve, widgetId, model),
        // TODO костыль для быстрого решения, по хорошему вынести в таблицу и ячейка ничего не должна знать о моделях
        updateDatasource: (widgetId, datasource, currentModel, newModel) => {
            const newDatasource = datasource.map((model) => {
                if (model.id === currentModel.id) {
                    return newModel
                }

                return model
            })

            return setModel(PREFIXES.datasource, widgetId, newDatasource)
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
        index,
        fieldKey,
        dataProvider,
        dispatch,
        ...rest
    }) {
        const resolveWidget = useCallback(data => onResolveWidget(widgetId, data), [onResolveWidget, widgetId])
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
            onInvoke(widgetId, customProvider || dataProvider, data, null, meta)
        }, [onInvoke, widgetId, dataProvider])

        const updateFieldInModel = useCallback((value, prefix = 'datasource') => {
            onUpdateModel(prefix, widgetId, `[${index}].${fieldKey}`, value)
        }, [onUpdateModel, widgetId, index, fieldKey])

        const callAction = useCallback((data) => {
            resolveWidget(data)

            if (isEmpty(defaultAction)) {
                updateDatasource(widgetId, datasourceModel, defaultModel, data)
            } else {
                dispatch(defaultAction)
            }
        }, [defaultAction, dispatch, resolveWidget, updateDatasource, datasourceModel, defaultModel])

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
