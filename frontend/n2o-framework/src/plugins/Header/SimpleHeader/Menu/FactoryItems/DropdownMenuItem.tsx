import React from 'react'

import { Dropdown } from '../NavItems/Dropdown/Dropdown'
import { IDropdownContextItem } from '../Item'

export function DropdownMenuItem(props: IDropdownContextItem) {
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
