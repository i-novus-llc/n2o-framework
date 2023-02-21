import React, { useMemo } from 'react'
import get from 'lodash/get'
import omit from 'lodash/omit'
import { isEmpty } from 'lodash'
import { compose, withHandlers } from 'recompose'
import PropTypes from 'prop-types'
import classNames from 'classnames'

import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import StandardButton from '../../../../buttons/StandardButton/StandardButton'
import DefaultCell from '../DefaultCell'

import { LinkCellType } from './linkCellTypes'

function LinkCell(props) {
    const {
        widgetId,
        model = {},
        className,
        fieldKey,
        id,
        icon,
        type,
        url: propsUrl,
        disabled,
        onResolve,
        ...rest
    } = props

    const { url: modelUrl = '' } = model

    const createUrl = () => {
        if (propsUrl) {
            return propsUrl
        }

        /* ready url from the model */
        if (modelUrl && modelUrl.startsWith('http')) {
            return modelUrl
        }

        return propsUrl
    }

    const url = createUrl()

    const submitType = useMemo(() => {
        let content = {
            icon,
            label: get(model, fieldKey || id, ''),
        }

        if (type === LinkCellType.TEXT) {
            content = {
                label: get(model, fieldKey || id, ''),
            }
        } else if (type === LinkCellType.ICON) {
            content = { icon }
        }

        return content
    }, [type, model, fieldKey, id, icon])

    const onClick = (e) => {
        e.stopPropagation()

        onResolve(e)
    }

    return (
        <DefaultCell
            tag="span"
            disabled={disabled}
            onClick={onClick}
        >
            <StandardButton
                id={id}
                className={classNames(className, 'n2o-link-cell')}
                color="link"
                model={model}
                entityKey={widgetId}
                {...submitType}
                {...omit(rest, ['icon', 'label', 'resolveWidget', 'columnId', 'dispatch'])}
                url={url}
                href={url}
            />
        </DefaultCell>
    )
}

LinkCell.propTypes = {
    widgetId: PropTypes.string,
    dispatch: PropTypes.func,
    columnId: PropTypes.string,
    model: PropTypes.object,
    className: PropTypes.string,
    fieldKey: PropTypes.string,
    id: PropTypes.string,
    resolveWidget: PropTypes.func,
    icon: PropTypes.string,
    type: PropTypes.any,
    url: PropTypes.string,
    disabled: PropTypes.bool,
    onResolve: PropTypes.func,
}

export { LinkCell }
export default compose(
    withCell,
    withTooltip,
    withHandlers({
        onResolve: ({ callAction, model, action, resolveWidget }) => () => {
            if (!model) {
                return
            }

            if (callAction && isEmpty(action)) {
                callAction(model)
            }

            if (resolveWidget) {
                resolveWidget(model)
            }
        },
    }),
)(LinkCell)
