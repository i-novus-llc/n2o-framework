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
        model,
        className,
        fieldKey,
        id,
        icon,
        type,
        url,
        disabled,
        onResolve,
        ...rest
    } = props

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

    return (
        <DefaultCell
            tag="span"
            disabled={disabled}
            onClick={onResolve}
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
        onResolve: ({ callAction, model, action }) => () => {
            if (callAction && model && isEmpty(action)) {
                callAction(model)
            }
        },
    }),
)(LinkCell)
