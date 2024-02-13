import React from 'react'

import { Dropdown } from '../NavItems/Dropdown/Dropdown'
import { DropdownContextItem } from '../Item'

export function DropdownMenuItem(props: DropdownContextItem) {
    const { item, active, from, className, direction } = props

    return (
        <Dropdown
            {...item}
            active={active}
            from={from}
            className={className}
            direction={direction}
        />
    )
}
