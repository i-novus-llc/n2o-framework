import React from 'react'
import isEmpty from 'lodash/isEmpty'

import { metaPropsType } from '../../../utils'
import { Item as ItemProps } from '../../../CommonMenuTypes'

import { Item as ItemComponent } from './Item'

interface Props {
    items: ItemProps[]
    datasources?: metaPropsType[]
    pathname: string
}

export function Menu({ pathname, datasources = [], items = [] }: Props) {
    if (isEmpty(items)) { return null }

    return (
        <>
            {items.map((item, index) => (
                <ItemComponent
                    key={item.id || index}
                    {...item}
                    pathname={pathname}
                    datasources={datasources}
                />
            ))}
        </>
    )
}
