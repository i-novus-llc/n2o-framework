import React from 'react'
import classNames from 'classnames'
import isString from 'lodash/isString'
import { NavLink, NavItem, DropdownItem } from 'reactstrap'
import { BrowserRouter } from 'react-router-dom'

const iconInPopUpClassName = (icon, directionIconsInPopUp) => classNames(icon, {
    'n2o-search-bar__popup_icon-left': directionIconsInPopUp === 'left',
    'n2o-search-bar__popup_icon-right': directionIconsInPopUp === 'right',
})

export const itemInSearchBarClassName = directionIconsInPopUp => classNames({
    'n2o-search-bar__popup_item-right': directionIconsInPopUp === 'right',
    'n2o-search-bar__popup_item-left': directionIconsInPopUp === 'left',
})

const renderIcon = (icon, directionIconsInPopUp) => (isString(icon) ? (
    <i className={iconInPopUpClassName(icon, directionIconsInPopUp)} />
) : (
    icon
))

// eslint-disable-next-line react/prop-types
export const RenderLink = ({ label, description, icon, href, ...props }) => {
    // eslint-disable-next-line react/prop-types
    const { linkType, disabled, directionIconsInPopUp } = props

    return linkType === 'inner' ? (
        <div className="n2o-search-bar__link-container">
            {renderIcon(icon, directionIconsInPopUp)}
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
                            {renderDescription(description, disabled)}
                        </NavLink>
                    </NavItem>
                </BrowserRouter>
            </div>
        </div>
    ) : (
        <div className="n2o-search-bar__link-container">
            {renderIcon(icon, directionIconsInPopUp)}
            <div>
                <BrowserRouter>
                    <NavLink href={href} title={description} disabled={disabled}>
                        {label}
                        {renderDescription(description)}
                    </NavLink>
                </BrowserRouter>
            </div>
        </div>
    )
}

export const renderDescription = (description, disabled) => (description && disabled ? (
    <div className="dropdown-header n2o-search-bar__popup_desc-disabled">
        {description}
    </div>
) : (
    description && (
        <div className="dropdown-header n2o-search-bar__popup_desc-disabled">
            {description}
        </div>
    )
))

export const renderDivider = (props) => {
    const { separateLink } = props

    return separateLink && separateLink === true && <DropdownItem divider />
}
