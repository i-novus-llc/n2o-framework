import React, { ComponentType } from 'react'
import get from 'lodash/get'

import { Tooltip, TooltipHocProps } from '../../snippets/Tooltip/TooltipHOC'

export interface Props extends TooltipHocProps {
    model: Record<string, string>
    tooltipFieldId: string
}

export function withTooltip<P extends Props>(Component: ComponentType<P>) {
    /**
     * HOC, оборачивает Cell добавляя Tooltip,
     * дает компоненту forwardedRef для установки tooltip trigger
     * @param props
     */
    function Wrapper(props: P) {
        const { model, placement, tooltipFieldId } = props
        const hint = get(model, tooltipFieldId, null) as Props['hint']

        if (!hint) { return <Component {...props} /> }

        return (
            <Tooltip hint={hint} placement={placement || 'bottom'}>
                <Component {...props} />
            </Tooltip>
        )
    }

    Wrapper.displayName = `withTooltip(${Component?.displayName || 'UnknownComponent'})`

    return Wrapper
}

export default withTooltip
