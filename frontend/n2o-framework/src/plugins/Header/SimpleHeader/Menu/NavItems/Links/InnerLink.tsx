import React, { ReactChildren } from 'react'
import { NavLink } from 'react-router-dom'
import { NavItem } from 'reactstrap'
import classNames from 'classnames'

export interface Link {
    children: ReactChildren | JSX.Element
    className?: string,
    active?: boolean,
    href: string,
    target: string
    style?: React.CSSProperties
}

export function InnerLink({ children, className, active, href, target, style }: Link) {
    return (
        <NavItem>
            <NavLink
                className={classNames('nav-link', className, { active })}
                to={href}
                style={style}
                activeClassName="active"
                target={target}
                exact
            >
                {children}
            </NavLink>
        </NavItem>
    )
}
