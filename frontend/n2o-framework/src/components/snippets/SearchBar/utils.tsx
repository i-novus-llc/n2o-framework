import React from 'react'
import classNames from 'classnames'
import { NavLink, NavItem } from 'reactstrap'
import { BrowserRouter } from 'react-router-dom'

import { type SearchBarLinkProps } from './types'

export const itemInSearchBarClassName = (directionIconsInPopUp: SearchBarLinkProps['directionIconsInPopUp']) => classNames({
    'n2o-search-bar__popup_item-right': directionIconsInPopUp === 'right',
    'n2o-search-bar__popup_item-left': directionIconsInPopUp === 'left',
})

const Icon = ({ icon, directionIconsInPopUp }: SearchBarLinkProps) => {
    if (!icon) { return null }

    if (typeof icon === 'string') {
        return (
            <i className={classNames(icon, {
                'n2o-search-bar__popup_icon-left': directionIconsInPopUp === 'left',
                'n2o-search-bar__popup_icon-right': directionIconsInPopUp === 'right',
            })}
            />
        )
    }

    return icon
}

export const Description = ({ description, disabled }: SearchBarLinkProps) => {
    if (!description) { return null }

    return (
        <div className={classNames('dropdown-header', { 'n2o-search-bar__popup_desc-disabled': disabled })}>
            {description}
        </div>
    )
}

export const RenderLink = ({ label, description, icon, href, linkType, disabled, directionIconsInPopUp }: SearchBarLinkProps) => {
    if (linkType === 'inner') {
        return (
            <div className="n2o-search-bar__link-container">
                <Icon icon={icon} directionIconsInPopUp={directionIconsInPopUp} />
                <div>
                    <BrowserRouter>
                        <NavItem>
                            <NavLink
                                exact
                                className="nav-link"
                                to={href}
                                title={description}
                                activeClassName="active"
                                disabled={disabled}
                            >
                                {label}
                                <Description description={description} disabled={disabled} />
                            </NavLink>
                        </NavItem>
                    </BrowserRouter>
                </div>
            </div>
        )
    }

    return (
        <div className="n2o-search-bar__link-container">
            <Icon icon={icon} directionIconsInPopUp={directionIconsInPopUp} />
            <div>
                <BrowserRouter>
                    <NavLink href={href} title={description} disabled={disabled}>
                        {label}
                        <Description description={description} disabled={disabled} />
                    </NavLink>
                </BrowserRouter>
            </div>
        </div>
    )
}
