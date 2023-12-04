import React, { ComponentType } from 'react'

import { TooltipHOC, TooltipHocProps } from '../snippets/Tooltip/TooltipHOC'

interface Props extends TooltipHocProps {
    Component?: ComponentType
    componentProps: Record<string, unknown>
}

function ActionButtonBody(props: Props) {
    const { Component, componentProps } = props

    if (!Component) { return null }

    return <Component {...componentProps} />
}

export const ActionButton = TooltipHOC(ActionButtonBody)
