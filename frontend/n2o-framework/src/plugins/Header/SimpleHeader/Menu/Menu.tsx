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

export function Menu(props: Props) {
    const { pathname, datasources = [], items = [] } = props

    if (isEmpty(items)) {
        return null
    }

    return items.map(item => (
        <ItemComponent
            {...item}
            pathname={pathname}
            datasources={datasources}
        />
    ))
}
