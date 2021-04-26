import React, { useMemo } from 'react'
import get from 'lodash/get'
import omit from 'lodash/omit'

import withCell from '../../withCell'
import withTooltip from '../../withTooltip'
import StandardButton from '../../../../buttons/StandardButton/StandardButton'

import { LinkCellType } from './linkCellTypes'

function LinkCell(props) {
    const {
        widgetId,
        dispatch,
        columnId,
        model,
        className,
        fieldKey,
        id,
        resolveWidget,
        icon,
        type,
        url,
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
        <StandardButton
            id={id}
            className={className}
            color="link"
            model={model}
            entityKey={widgetId}
            {...submitType}
            {...omit(rest, ['icon', 'label'])}
            url={url}
            href={url}
        />
    )
}

export { LinkCell }
export default withCell(withTooltip(LinkCell))
