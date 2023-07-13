import React from 'react'
import classNames from 'classnames'

import { IContextItem } from '../Item'
import { LinkBody } from '../NavItems/Links/LinkBody'

export function StaticMenuItem(props: IContextItem) {
    const { item, className } = props

    return <li className={classNames(className, 'static-menu-item')}><LinkBody {...item} /></li>
}
