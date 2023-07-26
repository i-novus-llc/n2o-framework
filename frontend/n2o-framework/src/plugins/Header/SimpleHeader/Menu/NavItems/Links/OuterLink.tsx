import React from 'react'
import { NavItem } from 'reactstrap'
import classNames from 'classnames'

import { ILink } from './InnerLink'

export function OuterLink(props: ILink) {
    const { children, className, href, target, style } = props

    return (
        <NavItem>
            <a className={classNames('nav-link', className)} href={href} target={target} style={style}>
                {children}
            </a>
        </NavItem>
    )
}
