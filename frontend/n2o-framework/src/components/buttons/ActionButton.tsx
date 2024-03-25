import React, { ComponentType } from 'react'

import { Tooltip, TooltipHocProps } from '../snippets/Tooltip/TooltipHOC'
import { Tooltip as TooltipType } from '../../ducks/form/Actions'

interface Props extends TooltipHocProps {
    Component?: ComponentType<TooltipHocProps>
    hint?: string
    placement?: string
    tooltip?: TooltipType
}

export function ActionButton(props: Props) {
    const { Component, hint, placement, tooltip = null } = props

    if (!Component) { return null }

    return (
        <Tooltip placement={placement} hint={tooltip || hint}>
            <Component {...props} />
        </Tooltip>
    )
}
