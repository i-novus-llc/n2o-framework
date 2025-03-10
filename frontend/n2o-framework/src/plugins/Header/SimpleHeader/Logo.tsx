import React, { CSSProperties } from 'react'
import classNames from 'classnames'
import { NavbarBrand } from 'reactstrap'
import { Text } from '@i-novus/n2o-components/lib/Typography/Text'

import { needRender } from '../../SideBar/utils'

import { NavbarBrandContent } from './NavbarBrandContent'

export interface LogoProps {
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
}: LogoProps) => {
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
            {needRender(title) && <a href={href} className="navbar-brand logo-title"><Text>{title}</Text></a>}
            {needRender(subtitle) && <small className="navbar-brand logo-subtitle"><Text>{subtitle}</Text></small>}
        </section>
    )
}
