import React, { useCallback, useMemo } from 'react'
import get from 'lodash/get'
import omit from 'lodash/omit'
import flowRight from 'lodash/flowRight'
import classNames from 'classnames'

import { WithCell } from '../../withCell'
import { withTooltip } from '../../withTooltip'
import StandardButton from '../../../../buttons/StandardButton/StandardButton'
import { DefaultCell } from '../DefaultCell'
import { EMPTY_OBJECT } from '../../../../../utils/emptyTypes'

import { CELL_TYPE, type Props } from './types'

function LinkCellBody({
    widgetId,
    className,
    fieldKey,
    id,
    icon,
    type,
    url: propsUrl,
    disabled,
    callAction,
    action,
    resolveWidget,
    model = EMPTY_OBJECT,
    ...rest
}: Props) {
    const modelUrl = get(model, 'url', '') as string

    const onResolve = useCallback((_e?: MouseEvent) => {
        if (!model) { return }

        if (callAction) { callAction(model) }

        if (resolveWidget) { resolveWidget(model) }
    }, [callAction, model, resolveWidget])

    const createUrl = () => {
        if (propsUrl) { return propsUrl }

        /* ready url from the model */
        if (modelUrl?.startsWith('http')) { return modelUrl }

        return propsUrl
    }

    const url = createUrl()

    const submitType = useMemo(() => {
        if (type === CELL_TYPE.TEXT) {
            return { label: get(model, fieldKey || id, '') }
        }

        if (type === CELL_TYPE.ICON) {
            return { icon }
        }

        return { icon, label: get(model, fieldKey || id, '') }
    }, [type, model, fieldKey, id, icon])

    const onClick = (e?: MouseEvent) => {
        e?.stopPropagation()

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

const LinkCell = flowRight(WithCell, withTooltip)(LinkCellBody)

LinkCell.displayName = 'LinkCell'

export { LinkCell }
export default LinkCell
