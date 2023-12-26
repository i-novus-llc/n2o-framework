import React, { ComponentType } from 'react'

import { Tooltip, TooltipHocProps } from '../snippets/Tooltip/TooltipHOC'

interface Props extends TooltipHocProps {
    Component?: ComponentType<TooltipHocProps>
    hint: string
    placement: string
}

export function ActionButton(props: Props) {
    const { Component, hint, placement } = props

    if (!Component) { return null }

    return (
        <Tooltip placement={placement} hint={hint}>
            <Component {...props} />
        </Tooltip>
    )
}
