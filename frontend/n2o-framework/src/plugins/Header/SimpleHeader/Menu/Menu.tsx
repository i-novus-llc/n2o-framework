import React from 'react'
import isEmpty from 'lodash/isEmpty'

import { type Item as ItemProps } from '../../../CommonMenuTypes'

import { Item as ItemComponent } from './Item'

interface Props {
    items: ItemProps[]
    pathname: string
}

export function Menu({ pathname, items = [] }: Props) {
    if (isEmpty(items)) { return null }

    return (
        <>
            {items.map((item, index) => (
                <ItemComponent
                    key={item.id || index}
                    {...item}
                    pathname={pathname}
                />
            ))}
        </>
    )
}
