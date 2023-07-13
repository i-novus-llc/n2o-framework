import React from 'react'
import isEmpty from 'lodash/isEmpty'

import { metaPropsType } from '../../../utils'

import { Item, IItem } from './Item'

interface IProps {
    items: IItem[]
    datasources?: metaPropsType[]
    pathname: string
}

export function Menu(props: IProps) {
    const { pathname, datasources = [], items = [] } = props

    if (isEmpty(items)) {
        return null
    }

    return items.map(item => (
        <Item
            {...item}
            pathname={pathname}
            datasources={datasources}
        />
    ))
}
