import React from 'react'
import onClickOutsideHOC from 'react-onclickoutside'

import { triggerClassName } from './utils'
import { type ListTextCellTriggerProps as Props } from './types'

function Component({ label, labelDashed, forwardedRef }: Omit<Props, 'tooltipClose'>) {
    return <span ref={forwardedRef} className={triggerClassName(labelDashed)}>{label}</span>
}

const Extended = onClickOutsideHOC(Component)

export function ListTextCellTrigger({ label, labelDashed, forwardedRef, tooltipClose }: Props) {
    return (
        <Extended
            label={label}
            labelDashed={labelDashed}
            forwardedRef={forwardedRef}
            handleClickOutside={tooltipClose}
        />
    )
}
