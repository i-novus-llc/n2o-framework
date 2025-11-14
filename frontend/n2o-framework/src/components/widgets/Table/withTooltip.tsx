import React, { ComponentType } from 'react'
import get from 'lodash/get'

import { Tooltip, type TooltipHocProps } from '../../snippets/Tooltip/TooltipHOC'

export interface Props extends TooltipHocProps {
    model: Record<string, string | boolean | number | Array<Record<string, unknown>>>
    tooltipFieldId?: string
}

/**
 * HOC, оборачивает Cell добавляя Tooltip,
 * дает компоненту forwardedRef для установки tooltip trigger
 */
export function withTooltip<P extends Props>(Component: ComponentType<P>) {
    function Wrapper(props: P) {
        const { model, placement = 'bottom', tooltipFieldId = '', container = 'body' } = props
        const hint = get(model, tooltipFieldId, null) as Props['hint']

        if (!hint) { return <Component {...props} /> }

        return (
            <Tooltip container={container} hint={hint} placement={placement} className="n2o-cell-tooltip">
                <Component {...props} />
            </Tooltip>
        )
    }

    Wrapper.displayName = `withTooltip(${Component?.displayName || 'UnknownComponent'})`

    return Wrapper
}

export default withTooltip
