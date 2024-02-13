import React from 'react'
import { NavItem } from 'reactstrap'
import classNames from 'classnames'

import { Link } from './InnerLink'

export function OuterLink(props: Link) {
    const { children, className, href, target, style } = props

    return (
        <NavItem>
            <a className={classNames('nav-link', className)} href={href} target={target} style={style}>
                {children}
            </a>
        </NavItem>
    )
}
