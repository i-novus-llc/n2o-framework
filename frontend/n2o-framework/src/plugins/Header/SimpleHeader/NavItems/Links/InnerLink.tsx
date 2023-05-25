import React, { ReactChildren } from 'react'
import { NavLink } from 'react-router-dom'
import { NavItem } from 'reactstrap'
import classNames from 'classnames'

export interface ILink {
    children: ReactChildren | JSX.Element
    className?: string,
    active?: boolean,
    href: string,
    target: string
}

export function InnerLink(props: ILink) {
    const { children, className, active, href, target } = props

    return (
        <NavItem>
            <NavLink
                className={classNames('nav-link', className, { active })}
                to={href}
                activeClassName="active"
                target={target}
                exact
            >
                {children}
            </NavLink>
        </NavItem>
    )
}
