import React from 'react'
import omit from 'lodash/omit'
import classNames from 'classnames'

import { ContextItem } from '../../../../CommonMenuTypes'
import { LinkBody } from '../NavItems/Links/LinkBody'

export function StaticMenuItem(props: ContextItem) {
    const { item, className } = props
    const { style } = item

    return (
        <li className={classNames(className, 'static-menu-item')} style={style}>
            <LinkBody {...omit(item, 'style')} />
        </li>
    )
}
