import React, { CSSProperties } from 'react'
import classNames from 'classnames'
import { NavbarBrand } from 'reactstrap'

import { needRender } from '../../SideBar/utils'

import { NavbarBrandContent } from './NavbarBrandContent'

interface Props {
    title?: string
    subtitle?: string
    className?: string
    style?: CSSProperties
    href?: string
    src?: string
    showContent?: boolean
    isMiniView?: boolean
}

export const Logo = ({
    title,
    subtitle,
    className,
    style,
    src,
    isMiniView,
    showContent = true,
    href = '/',
}: Props) => {
    return (
        <section
            className={classNames(
                'n2o-header-logo d-flex flex-wrap justify-content-center',
                className,
                { visible: showContent, mini: isMiniView },
            )}
            style={style}
        >
            {src && (
                <NavbarBrand className="n2o-brand" href={href}>
                    <NavbarBrandContent brandImage={src} />
                </NavbarBrand>
            )}
            {needRender(title) && <a href={href} className="navbar-brand logo-title">{title}</a>}
            {needRender(subtitle) && <small className="navbar-brand logo-subtitle">{subtitle}</small>}
        </section>
    )
}
