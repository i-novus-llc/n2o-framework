import React from 'react'

import { Dropdown } from '../NavItems/Dropdown/Dropdown'
import { DropdownContextItem } from '../Item'

export function DropdownMenuItem({ item, active, from, className, direction }: DropdownContextItem) {
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
