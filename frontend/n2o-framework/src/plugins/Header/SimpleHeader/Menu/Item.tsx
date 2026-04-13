import React from 'react'

import NavItemContainer from '../NavItemContainer'
import { Item as ItemProps } from '../../../CommonMenuTypes'

export function Item(props: ItemProps) {
    const { href, pathname } = props
    const active = href ? pathname.includes(href) : false

    return (
        <NavItemContainer
            itemProps={props}
            active={active}
        />
    )
}
