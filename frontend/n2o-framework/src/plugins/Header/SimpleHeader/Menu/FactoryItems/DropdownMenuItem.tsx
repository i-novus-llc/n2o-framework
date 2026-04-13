import React from 'react'

import { Dropdown, DropdownProps } from '../NavItems/Dropdown/Dropdown'
import { ContextItemCommon } from '../../../../CommonMenuTypes'

export interface DropdownContextItem extends ContextItemCommon {
    item: DropdownProps
}

export function DropdownMenuItem({ item, ...props }: DropdownContextItem) {
    return (
        <Dropdown
            {...item}
            {...props}
        />
    )
}
