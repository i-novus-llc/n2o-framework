import React from 'react'
import { connect } from 'react-redux'
import { bindActionCreators } from 'redux'
import set from 'lodash/set'
import unset from 'lodash/unset'
import { isEmpty } from 'lodash'
import PropTypes from 'prop-types'

import { PREFIXES } from '../../../ducks/models/constants'
import { callActionImpl } from '../../../ducks/toolbar/store'
import { startInvoke } from '../../../actions/actionImpl'
import { updateModel, setModel } from '../../../ducks/models/store'

const mapDispatchToProps = dispatch => bindActionCreators(
    {
        onActionImpl: ({ src, component, options }) => callActionImpl(src || component, { ...options, dispatch }),
        onInvoke:
                (widgetId, dataProvider, data, modelLink, meta) => startInvoke(
                    widgetId, dataProvider, data, modelLink, meta,
                ),
        onUpdateModel: (prefix, key, field, values) => updateModel(prefix, key, field, values),
        onResolveWidget: (widgetId, model) => setModel(PREFIXES.resolve, widgetId, model),
    },
    dispatch,
)

/**
 * HOC для оборачивания Cell
 * Переопределяет свойство onClick в компонентах для посылки экшена
 * @param WrappedComponent
 * @returns {*}
 */

// eslint-disable-next-line func-names
export default function (WrappedComponent) {
    function ReturnedComponent({
        onActionImpl,
        onInvoke,
        onUpdateModel,
        onResolveWidget,
        action: defaultAction,
        model: defaultModel,
        widgetId,
        index,
        fieldKey,
        dataProvider,
        dispatch,
        ...rest
    }) {
    /**
     * @deprecated
     */
        const callActionImpl = (e, { action, model }) => {
            const currentModel = model || defaultModel
            const currentAction = action || defaultAction

            // eslint-disable-next-line no-unused-expressions
            currentModel && resolveWidget(currentModel)
            // eslint-disable-next-line no-unused-expressions
            currentAction && onActionImpl(currentAction)
        }

        /**
     * @deprecated
     */
        const callInvoke = (data, customProvider = null, meta) => {
            onInvoke(widgetId, customProvider || dataProvider, data, null, meta)
        }

        const updateFieldInModel = (value, prefix = 'datasource') => {
            onUpdateModel(prefix, widgetId, `[${index}].${fieldKey}`, value)
        }

        const resolveWidget = data => onResolveWidget(widgetId, data)

        const callAction = (data) => {
            const resolvedAction = resolveWidget(data)
            const action = isEmpty(defaultAction) ? resolvedAction : ({

                ...defaultAction,
            })

            set(action, 'payload.data', data)
            unset(action, 'payload.modelLink')

            dispatch(action)
        }

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

    ReturnedComponent.propTypes = {
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
    }

    return connect(
        null,
        mapDispatchToProps,
    )(ReturnedComponent)
}
