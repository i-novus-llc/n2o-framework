import React from 'react'
import PropTypes from 'prop-types'
import classNames from 'classnames'
import NavbarBrand from 'reactstrap/lib/NavbarBrand'

import { NavbarBrandContent } from './NavbarBrandContent'

export function Logo({ title, className, style, href, src, showContent, isMiniView }) {
    return (
        <section
            className={classNames(
                'n2o-header-logo d-flex flex-wrap justify-content-center',
                className,
                {
                    visible: showContent,
                    mini: isMiniView,
                },
            )}
            style={style}
        >
            {src && (
                <NavbarBrand className="n2o-brand" href={href}>
                    <NavbarBrandContent brandImage={src} />
                </NavbarBrand>
            )}
            {title && (
                <a href={href} className="navbar-brand">
                    {title}
                </a>
            )}
        </section>
    )
}

Logo.propTypes = {
    title: PropTypes.string,
    className: PropTypes.string,
    style: PropTypes.object,
    href: PropTypes.string,
    src: PropTypes.string,
    showContent: PropTypes.bool,
    isMiniView: PropTypes.bool,
}

Logo.defaultProps = {
    href: '/',
    showContent: true,
}
