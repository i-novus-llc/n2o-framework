import React from 'react'
import classNames from 'classnames'

import { IContextItem } from '../../../../CommonMenuTypes'
import { LinkBody } from '../NavItems/Links/LinkBody'

export function StaticMenuItem(props: IContextItem) {
    const { item, className, style } = props

    return (
        <li className={classNames(className, 'static-menu-item')} style={style}>
            <LinkBody {...item} />
        </li>
    )
}
